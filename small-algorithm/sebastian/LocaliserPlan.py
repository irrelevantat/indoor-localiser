#!/usr/bin/env python
# -----------------------------------------------------
# Author: Sebastian Hojas
# Date: 27-11-2015
# Comment: Show current tracking
# -----------------------------------------------------

from Tkinter import *
from PIL import Image, ImageTk
import tkMessageBox
import tkSimpleDialog
from datetime import datetime
import time

from FloorPlan import FloorPlan
from Path import *
from WIFIScanner import *
from Localiser import * 

class LocaliserPlan(FloorPlan):

  def __init__(self,floor=0):
    super(LocaliserPlan, self).__init__(floor)
    self.loc = Localiser()
    for data_point in self.loc.db:
      self.addLabel(data_point.point,bg="blue") 
    self.current_pos = None
   
  def reset(self):
    if self.current_pos != None:
      self.current_pos = None
      self.labels.pop().hide()
    for poi in self.labels:
      poi.changeColor("blue")
    self.root.update()

  def updateData(self, s):
    (localised,best_fits) = self.loc.scan(s)
    self.current_pos = self.addLabel(localised,bg="green")
    
    colors = list(Config.YELLOW_SHADES(Config.AVERAGE_OPTIMISATION))
    for (delta,element) in best_fits:
      # find according label in list
      found = 0
      for poi in self.labels:
        if poi.loc == element.point:
          poi.changeColor(colors.pop(0))
          found = 1
          break
      # make sure we found the according label
      assert(found==1)

  def recompute(self):
    self.reset()
    # use cache
    input = tkSimpleDialog.askinteger("Nearest Neighbor", "How many points to you want to include?")
    Config.AVERAGE_OPTIMISATION = int(input)
    data = WIFIScanner(None)._scan(True)
    assert(data!=None)
    s = Scan(data)
    self.updateData(s)
  
  def scan(self):
    self.reset()
    data = WIFIScanner(None)._scan()
    assert(data!=None)
    s = Scan(data)
    self.updateData(s)


  def keyboardEvent(self,event):
    super(LocaliserPlan, self).keyboardEvent(event)
    if event.char == "w":
      self.scan()
    if event.char == "q":
      self.recompute()




if __name__ == "__main__":
  LocaliserPlan(0).show()

