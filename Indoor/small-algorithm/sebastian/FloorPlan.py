#!/usr/bin/env python
# -----------------------------------------------------
# Author: Sebastian Hojas
# Date: 27-11-2015
# Comment: Displays the building
# -----------------------------------------------------

from Tkinter import *
from tkFileDialog import askopenfilename
from PIL import Image, ImageTk
from Path import *
from POILabel import *

import Config

class FloorPlan(object):

    def changeFloor(self, floor):

        if floor == self.floor:
            return

        self.floor = floor
        #adding the image
        orgImg = ImageTk.Image.open(Config.BUILDING_IMAGES + str(floor) + ".png")
        # lets cheat and scale down the image to fit to our screen
        orgImg = orgImg.resize(
            (int(orgImg.size[0]/Config.DISPLAY_SCALE_FACTOR),
             int(orgImg.size[1]/Config.DISPLAY_SCALE_FACTOR)), 
             ImageTk.Image.ANTIALIAS)
        img = ImageTk.PhotoImage(orgImg)

        self.imgContainer.config(image=img)
        self.imgContainer.image = img
        self.imgContainer.place(x=0, y=0)
        
        for label in self.labels:
            if label.loc.getFloor() == floor:
                label.display(self.root) 
            else:
                label.hide()


    def exit(self):
        self.root.destroy()

    def keyboardEvent(self,event):
        try:
            if int(event.char) < Config.FLOORS:
                # show Floor
                self.changeFloor(int(event.char))
        except ValueError:
            pass

        if event.char == "e":
                self.exit()


    def callbackClicked(self,event):
        return
        
    def addLabel(self, point, bg="red"):  
        
        p = POILabel(bg,str(len(self.labels)),point)
        if point.getFloor() == self.floor:
            p.display(self.root)
        self.labels.append(p)

        return p       

    def __init__(self, floor=0):
        self.root = Tk()
        self.floor = -1
        
        # make fullscreen
        # self.root.attributes('-fullscreen', True)
        self.root.geometry('800x1000')
        self.imgContainer = Label(self.root)

        self.labels = []
            
        self.changeFloor(floor)
        
        # mouseclick event
        self.imgContainer.bind("<Button 1>",self.callbackClicked)
        # keyboard event
        self.root.bind("<Key>",self.keyboardEvent)
    
    def show(self):
        self.root.mainloop()

if __name__ == "__main__":
    plan = FloorPlan()
    plan.show()

