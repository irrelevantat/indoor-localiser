#!/usr/bin/env python
# -----------------------------------------------------
# Author: Sebastian Hojas
# Date: 27-11-2015
# Comment: Scans WIFI and parses it to required format
# -----------------------------------------------------

import plistlib
import sys
import commands
import threading
import traceback
import time

import Config

class WIFIScanner(threading.Thread):

  def __init__(self, callback):
    super(WIFIScanner, self).__init__()
    self.callback = callback
    self.stoprequest = threading.Event()

  def run(self):
    while not self.stoprequest.isSet():
      self.callback(self.scan())

  def scan(self,tryCounter=3):
    if tryCounter <= 0:
      print "Scanning failed fatally."
      return
    try:
      return self._scan(False)
    except:
      e = sys.exc_info()[0]
      print "Error when receiving scanning result:" + str(e)
      traceback.print_exc()
      return self.scan(tryCounter-1)

  def _scan(self,useCache=False):
    
    if useCache == False: 
      # deprecated since 2.6
      # disassociate from network
      commands.getstatusoutput('airport -z')
      data = commands.getstatusoutput('airport -s -x')

      # tuple consistence, check error code and format
      assert(len(data)==2 and data[0]==0)
      with open(Config.SCAN_FILE_PLIST, "w") as db:
          db.write((data[1]))
    else:
      # simulate waiting for just a bit
      time.sleep(0.2)

    scanning = plistlib.readPlist(Config.SCAN_FILE_PLIST)
    output = ""

    for wifi_signal in scanning:
      # OSX Bug. Ignore values with RSSI of 0 
      if wifi_signal['RSSI'] == 0:
        continue
      # if not first element add delimantor
      if output != "":
        output += ";"
      # osx plist formats 0c:85:25:de:ab:e1 to 0:85:25:de:ab:e1
      # We are handling strings. Make sure we don't miss first 0
      if len(wifi_signal['BSSID'].split(':')[0]) != 2:
        wifi_signal['BSSID'] = "0" + wifi_signal['BSSID']

      output += wifi_signal['BSSID'] + ";" + str(wifi_signal['RSSI'])

    return output

  # Allow other threads to abort and join thread
  def join(self, timeout=None):
    self.stoprequest.set()
    super(WIFIScanner, self).join(timeout)


# DEBUG Helper Method
def mePrint(arg):
  print arg

if __name__ == "__main__":
  WIFIScanner(mePrint).start()