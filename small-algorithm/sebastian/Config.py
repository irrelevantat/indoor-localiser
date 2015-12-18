# scale the resolution of the floor plan
DISPLAY_SCALE_FACTOR = 4.0
# scale the different floor values
FLOOR_MEASUREMENT_SCALE = 400

# DATABASE file
DATABASE_FILE = "scans/db.fingerprints"
# REFERENCE scan data in specified format
SCAN_FILE = "scans/scan.fingerprints"
# REFERENCE scan data in PLIST format
SCAN_FILE_PLIST = "scans/scan.plist"
# IMAGES of plan
BUILDING_IMAGES = "resources/exactum"

# how many best mesaurements should be averaged
AVERAGE_OPTIMISATION = 5

FONT = ("Helvetica", 8)

FLOORS = 4


# --------------------------
# Change to path directory
# --------------------------

import os
abspath = os.path.abspath(__file__)
dname = os.path.dirname(abspath)

DATABASE_FILE = dname + "/" + DATABASE_FILE
SCAN_FILE = dname + "/" + SCAN_FILE
SCAN_FILE_PLIST = dname + "/" + SCAN_FILE_PLIST
BUILDING_IMAGES = dname + "/" + BUILDING_IMAGES

# --------------------------
# Color Implementation by http://bsou.io/
# http://bsou.io/posts/color-gradients-with-python
# Copyright notice
# --------------------------

def hex_to_RGB(hex):
  ''' "#FFFFFF" -> [255,255,255] '''
  # Pass 16 to the integer function for change of base
  return [int(hex[i:i+2], 16) for i in range(1,6,2)]


def RGB_to_hex(RGB):
  ''' [255,255,255] -> "#FFFFFF" '''
  # Components need to be integers for hex to make sense
  RGB = [int(x) for x in RGB]
  return "#"+"".join(["0{0:x}".format(v) if v < 16 else
            "{0:x}".format(v) for v in RGB])


def linear_gradient(start_hex, finish_hex="#FFFFFF", n=10):
  ''' returns a gradient list of (n) colors between
    two hex colors. start_hex and finish_hex
    should be the full six-digit color string,
    inlcuding the number sign ("#FFFFFF") '''
  # Starting and ending colors in RGB form
  s = hex_to_RGB(start_hex)
  f = hex_to_RGB(finish_hex)
  # Initilize a list of the output colors with the starting color
  RGB_list = [s]
  # Calcuate a color at each evenly spaced value of t from 1 to n
  for t in range(1, n):
    # Interpolate RGB vector for color at the current value of t
    curr_vector = [
      int(s[j] + (float(t)/(n-1))*(f[j]-s[j]))
      for j in range(3)
    ]
    # Add it to our list of output colors
    RGB_list.append(curr_vector)

  return [RGB_to_hex(RGB) for RGB in RGB_list]

# --------------------------
# End of
# http://bsou.io/posts/color-gradients-with-python
# End of Copyright notice
# --------------------------

def YELLOW_SHADES(num=AVERAGE_OPTIMISATION):
  return linear_gradient("#FFFF00", "383802", num)
