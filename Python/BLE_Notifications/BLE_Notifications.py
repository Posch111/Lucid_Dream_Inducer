from bluepy import btle
import binascii
import matplotlib.pyplot as plt
import time
import csv
from pathlib import Path
import traceback
import os
homedir = os.environ['HOME']

#
export_data = 1
export_data_array = []
csvfile = homedir+"/Documents/REM_Sleep_Cycle_Monitor/Python/BLE_Notifications/data.csv"
i = 0
datArray = []  # data coming in
hexString = []

def split_string(n, s):
    return [ s[i:i+n] for i in range(0, len(s), n) ]

def write_to_csv(array):
    with open(csvfile, "w") as output:
    	writer = csv.writer(output, lineterminator='\n')
    	for val in array:
            writer.writerow([val]) 
    print("data exported to csv")

#function when called opened a file and writes more data to it, or makes the file if it doesn't exist
def update_file_data(array):
    try:
	print "update file with new data"
	#check to see if the file already exists
	my_file = Path(csvfile)
	#if the file does already exist
	if my_file.is_file():
	    with open(csvfile, "a") as output:
    	        writer = csv.writer(output, lineterminator='\n')
    	        for val in array:
                    writer.writerow([val]) 
	#if the file doesn't exist, make one	
	else:
	    print "created new file"
	    with open(csvfile, "w") as output:
    		writer = csv.writer(output, lineterminator='\n')
    		for val in array:
            	    writer.writerow([val]) 
    	    print("data exported to csv")
	
    except: traceback.print_exc()

class MyDelegate(btle.DefaultDelegate):
    def __init__(self):
        btle.DefaultDelegate.__init__(self)

    def handleNotification(self, cHandle, data):
        print("A notification was received: ")
        #print(int(binascii.hexlify(data)[:2],16))
	#data
        hexString = (split_string(2,binascii.hexlify(data)))
	#begin plot code
	global i        
        if(i < 1000):
            for j in range(0, len(hexString)):
                print(int(hexString[j],16))
                data = int(hexString[j],16)
                datArray.insert(i,data)
	        i = i + 1

	
        else:
            update_file_data(datArray)
            i = 1
            #plt.plot(datArray)
            #plt.show()
	    #update_file_data(export_data_array)
	    #write_to_csv(export_data_array)
            #while(1):
            #    time.sleep(1)

#for breadboard
#p = btle.Peripheral('4C:55:CC:14:2F:61')
#for full working prototype
p = btle.Peripheral('4C:55:CC:15:69:5D')
p.setDelegate( MyDelegate() ) #<UUID>

# Setup to turn notifications on, e.g.
svc = p.readCharacteristic(0x0046)

print(svc)

#turns on notifications
p.writeCharacteristic(0x0046+1, "\x01\x00")

while True:

    if p.waitForNotifications(1.0):
        # handleNotification() was called
        continue

    print("Waiting...")
    # Perhaps do something else here
