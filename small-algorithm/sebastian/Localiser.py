#!/usr/bin/env python
# -----------------------------------------------------
# Author: Sebastian Hojas
# Date: 27-11-2015
# Comment: Calculates the position
# Input: scan.fingerprints (reference scan)
# Input: db.fingerprints (database with prints)
# -----------------------------------------------------

import Config
from Path import *
from random import randint
import sys

# Algorithm 1
def similarity_S1(one,other):

  sum_prop = 0.0
  keys_self = set(one.measurement.keys())
  keys_other = set(other.measurement.keys())
  
  intersection = keys_self & keys_other
  if len(intersection)==0:
    return 0
  for key in intersection:
    # divide larger number by smaller numbers
    # -40 / -80 = 0.5
    sum_prop += float(max(one.measurement[key],other.measurement[key])) / float(min(one.measurement[key],other.measurement[key]))
    # TODO: Should we push the fact that this is the most important thing (maybe )
  
  # the more matching measurements we have, the better
  sum_prop /= math.pow(len(intersection),-1.1)

  penalty = 0.0
  
  self_without_other = keys_self - keys_other
  for key in self_without_other:
    # the lower the db value, the higher the chance that it is not important
    penalty += 100.0 / float(abs(one.measurement[key]))
  
  others_without_self = keys_other-keys_self
  for key in others_without_self:
    penalty += 100.0 / float(abs(other.measurement[key]))

  return sum_prop-penalty


class Localiser:

  def __init__(self):
    # load database to structure
    self.db = []
    with open(Config.DATABASE_FILE, "r") as db_file:
      for line in db_file:
        m = Measurement.fromDataSet(line)
        self.db.append(m)

  def rawScan(self, rawScan):
    referenceScan = Scan(rawScan)
    return self.scan(referenceScan)



  def scan(self, referenceScan):
    # save the 5 most fitting results
    best_fits = []

    # print("refScan=%2d" % len(referenceScan.measurement))

    # algorithm for saving the 5 best fits
    def collectMax(array,delta,element,maxI):
      inserted = False
      for i in range(0,len(array)):
        (e_delta,e_element) = array[i]
        if e_delta < delta:
          array.insert(i,(delta,element))
          inserted = True
          break
      if len(array)<maxI and inserted == False:
        array.append((delta,element))
      elif len(array)>maxI:
        array.pop()

    for m in self.db:
      # compare to measurement
      # save the Config.AVERAGE_OPTIMISATION most overlapping scans
      # weighted average
      delta = similarity_S1(m,referenceScan)
      collectMax(best_fits,delta,m,Config.AVERAGE_OPTIMISATION*2)

    # Cluster results from Config.AVERAGE_OPTIMISATION * 2
    # Remove points furthest away
    avgPoint = Point(0,0,0)
    for (delta,element) in best_fits:
      avgPoint += element.point
    avgPoint = avgPoint.factor(1.0/len(best_fits))

    sanitised_fits = []
    for (delta,element) in best_fits:
      distance = element.point.distance(avgPoint)*-1
      collectMax(sanitised_fits,distance,(delta,element),Config.AVERAGE_OPTIMISATION)
    
    # reorder all of this
    ordered_sanitised_fits = []
    for (distance,(delta,element)) in sanitised_fits:
      distance = element.point.distance(avgPoint)
      collectMax(ordered_sanitised_fits,delta,element,Config.AVERAGE_OPTIMISATION)
  
    best_fits = ordered_sanitised_fits

    # combine those results
    localised = Point(0,0,0)
    sum_props = 0.0
    # weighted average
    counter = len(best_fits)
    for (delta,element) in best_fits:
      factor = delta
      counter -= 1
      sum_props += factor
      localised += element.point.factor(factor)
      # debug print
      # 
      # print("f=%3.0f, loc=%15s, len=%2d, delta=%2.8f" % (factor, str(element.point), len(element.measurement), delta))
    
    # avoid x/0 when no similar points exist
    if sum_props>0:
      localised = localised.factor(1.0/sum_props)
    else:
      print "Huom! No shared networks found."

    return (localised,best_fits)
    


if __name__ == "__main__":
  if len(sys.argv)<=1 or len(sys.argv)>4:
    print "Usage: python Localiser.py <input_file> [<database>]"
    print "       Input file is required, database optional."
  else:
    # use parameters
    Config.SCAN_FILE = sys.argv[1]
    if len(sys.argv)==3:
      Config.DATABASE_FILE = sys.argv[2]

    with open(Config.SCAN_FILE, 'r') as content_file:
      content = content_file.read()
    p=Localiser().rawScan(content)[0]
    print "x=",int(p.x)," y=",int(p.y)," z=",int(p.z)