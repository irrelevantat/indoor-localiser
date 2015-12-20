#!/usr/bin/env python
# -----------------------------------------------------
# Author: Sebastian Hojas
# Date: 27-11-2015
# Comment: Container for path
# -----------------------------------------------------

import Config
import math

class Point:

  def __init__(self,x_display,y_display,z_display):
    self.x = float(x_display) * Config.DISPLAY_SCALE_FACTOR
    self.y = float(y_display) * Config.DISPLAY_SCALE_FACTOR
    self.z = float(z_display) * Config.FLOOR_MEASUREMENT_SCALE

  def setRealCoordinates(self, x,y,z):
    self.x = float(x)
    self.y = float(y)
    self.z = float(z)

  def getDisplayCoordinates(self):
    return (int(self.x / Config.DISPLAY_SCALE_FACTOR),
            int(self.y / Config.DISPLAY_SCALE_FACTOR),
            int(self.z / Config.FLOOR_MEASUREMENT_SCALE))

  def getRealCoordinates(self):
    return (int(self.x),int(self.y),int(self.z))

  def __add__(self, other):
    result = Point(0,0,0)
    result.setRealCoordinates(self.x + other.x,
                              self.y + other.y,
                              self.z + other.z)
    return result

  def __sub__(self, other):
    result = Point(0,0,0)
    result.setRealCoordinates(self.x - other.x,
                              self.y - other.y,
                              self.z - other.z)
    return result  

  def factor(self, factor):
    result = Point(0,0,0)
    result.setRealCoordinates(self.x * factor,
                              self.y * factor,
                              self.z * factor)
    return result  

  def distance(self, other):
    distance = 0.0
    p = self-other
    return math.sqrt(
              math.pow(p.x,2)
            + math.pow(p.y,2)
            + math.pow(p.z,2))

  def __repr__(self):
    return str(self.x) + ";" + str(self.y) + ";" + str(self.z)

  def getFloor(self):
    return int(round(self.z / Config.FLOOR_MEASUREMENT_SCALE))

class Path:
  points = []

  def addStep(self, point):
    self.points.append(point)

  def removeLast(self):
    self.points.pop()

class Scan:
  def __init__(self,rawDataMeasurement):
    assert(rawDataMeasurement != None)
    self.measurement = Scan.parseToTupleArray(rawDataMeasurement)

  def __repr__(self):
    return ";".join([str(key)+";"+str(int(self.measurement[key])) for key in self.measurement])

  @staticmethod
  def parseToTupleArray(data):
    db = data.split(";")
    assert(len(db)%2==0)
    it = iter(db)
    scanValues = {}
    for x in it:
      # add as tuples
      db = float(next(it))
      assert(db < 0)
      scanValues[x] = db
    return scanValues

class Measurement(Scan):
  def __init__(self,point,rawDataMeasurement):
    Scan.__init__(self, rawDataMeasurement)
    assert(point != None)
    self.point = point
    
  def __repr__(self):
    return str(self.point) + ";" + Scan.__repr__(self)

  @staticmethod
  def fromDataSet(data):
    assert(data != None)
    p = Point(0,0,0)
    dataSplit = data.split(";")
    p.setRealCoordinates(dataSplit[0],dataSplit[1],dataSplit[2])
    m = ';'.join(str(x) for x in dataSplit[3:])
    return Measurement(p,m)

class WalkingMeasurement:
  
  def __init__(self,pStart,pEnd):
    self.pStart = pStart
    self.pEnd = pEnd
    self.rawDataMeasurement = []
    self.time = 0
    
  def addMeasurement(self,rawData,time):
    assert(rawData != None)
    assert(time != None)
    self.rawDataMeasurement.append((rawData,time))

  def compute(self):
    assert(len(self.rawDataMeasurement)>0)
    assert(self.time > 0)
    vector = self.pEnd - self.pStart
    oneStep = vector.factor(float(1/self.time)) 

    measurements = []

    for (data,time) in self.rawDataMeasurement:
      # interpolate over linear walking time
      # we assume the point has been shifted by delta (therefore we need an offset)
      point = oneStep.factor(time) + self.pStart
      measurements.append(Measurement(point,data))

    return measurements





