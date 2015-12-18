

import Config
from Tkinter import *

class POILabel():

  def __init__(self,bg,index,loc):
    self.bg = bg
    self.index = index
    self.loc = loc
    self.label = None

  def display(self,tk):
    self.label = Label(tk, text=(str(self.index)), bg=self.bg, font=Config.FONT)
    cord = self.loc.getDisplayCoordinates()
    self.label.place(x=cord[0], y=cord[1])

  def hide(self):
    if self.label:
      self.label.destroy()
    self.label = None

  def changeColor(self,bg):
    self.bg = bg
    if self.label:
      self.label.config(bg=self.bg)
    





