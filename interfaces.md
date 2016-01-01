#-----------------
UI:
#-----------------

-

#-----------------
Controller.Callback:
#-----------------

locationUpdated(x,y,z)
locationFail(LocatingError)

LocatingError:
+ message

#-----------------
Controller:
#-----------------

registerForLocationUpdates(Controller.callback,interval)
unregisterForLocationUpdates()

#-----------------
Algorithm
#-----------------

getLocation(callback)




