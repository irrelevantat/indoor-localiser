#!/usr/bin/env python
# -----------------------------------------------------
# Author: Sebastian Hojas
# Date: 27-11-2015
# Comment: Allows to create a walk pase on top of a plan
# -----------------------------------------------------

from Tkinter import *
from tkFileDialog import askopenfilename
from PIL import Image, ImageTk

from FloorPlan import FloorPlan
from Path import *
from Walker import *
from Localiser import *

class WalkPlan(FloorPlan):

  path = Path()

  def __init__(self):
    super(WalkPlan, self).__init__()
    self.loc = Localiser()
    self.showLabelsInFloor(self.floor)


  def showLabelsInFloor(self, floor):
    for data_point in self.loc.db:
      self.addLabel(data_point.point,bg="green")



  def undo(self):
    self.path.removeLast()
    self.labels.pop().hide()

  def done(self):
    self.root.destroy()
    # Launch Walker
    Walker(self.path, self.floor).show()

  def keyboardEvent(self,event):
    # forward keyevents to super
    super(WalkPlan, self).keyboardEvent(event)
    if event.char == "z":
      self.undo()
    if event.char == "d":
      self.done()
    
  def callbackClicked(self,event):
    point = Point(event.x,event.y,self.floor)
    print "Added point to path: " + str(point)
    self.path.addStep(point)
    self.addLabel(point)
    

if __name__ == "__main__":
  plan = WalkPlan()
  plan.show()


