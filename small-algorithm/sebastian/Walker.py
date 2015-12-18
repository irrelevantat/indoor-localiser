#!/usr/bin/env python
# -----------------------------------------------------
# Author: Sebastian Hojas
# Date: 27-11-2015
# Comment: Allows to walk through a earlier created path
# -----------------------------------------------------

from Tkinter import *
from PIL import Image, ImageTk
import tkMessageBox
from datetime import datetime
import time


from FloorPlan import FloorPlan
from Path import *
from WIFIScanner import *

class Walker(FloorPlan):

  
  def __init__(self,myPath,floor):
    self.path = myPath
    self.current_index = 0
    self.unsavedWalkingMeasurement = []

    super(Walker, self).__init__(floor)

    for point in self.path.points:
      self.addLabel(point,bg="red")

    self.labels[self.current_index].changeColor("yellow")

   
  def undo(self):
    print "Undo"
    assert(self.current_index>0)
    self.unsavedWalkingMeasurement.pop()
    self.current_index-=1

    self.labels[self.current_index].changeColor("yellow")
    self.labels[self.current_index+1].changeColor("red")

  def save(self):
    print "Save"
    # TODO save to different files?
    measurements = []
    for measure in self.unsavedWalkingMeasurement:
      measurements.extend(measure.compute())
    for measure in measurements:
      with open(Config.DATABASE_FILE, "a") as db:
        db.write(str(measure) + "\n")
      self.root.update()
      time.sleep(0.1)
      self.addLabel(measure.point,bg="magenta")
    # empty list
    del self.unsavedWalkingMeasurement[:]


  def exit(self):
     self.root.destroy()


  def next(self):
    
    assert(self.current_index+1 < len(self.path.points))
    p1 = self.path.points[self.current_index]
    p2 = self.path.points[self.current_index+1]
   
    walk = WalkingMeasurement(p1,p2)    

    self.labels[self.current_index].changeColor("yellow")
    self.labels[self.current_index+1].changeColor("yellow")

    tkMessageBox.showinfo("Status", "Press OK when you are ready to start walking from " + str(self.current_index) + " to " + str(self.current_index+1))
    # Track starting time
    start = datetime.now()
    def wifiResult(data):
      assert(data!=None)
      delta = (datetime.now()-start).total_seconds()
      walk.addMeasurement(data,delta)
      print "Received WIFI Result: " + str(delta)

    # scan the hell out of the path in the background without blocking
    thread = WIFIScanner(wifiResult)
    thread.start()

    tkMessageBox.showinfo("Status", "Press OK when you have reached your goal " + str(self.current_index+1) + ".")
    thread.join()
   
    # meassure time and get dif
    walk.time = (datetime.now()-start).total_seconds()

    self.labels[self.current_index].changeColor("green")
    
    self.current_index+=1
    self.unsavedWalkingMeasurement.append(walk)


  def keyboardEvent(self,event):
    if event.char == "z":
      self.undo()
    if event.char == "d":
      self.save()
    if event.char == "e":
      self.exit()
    if event.char == "w":
      self.next()




if __name__ == "__main__":
  p = Path()
  p.addStep(Point(200,200,0))
  p.addStep(Point(500,300,0))
  p.addStep(Point(100,200,0))
  p.addStep(Point(200,500,0))
  Walker(p, 0).show()

