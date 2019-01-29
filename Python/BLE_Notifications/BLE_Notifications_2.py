import gatt
import time
import binascii
import csv
from pathlib import Path
import traceback
import os
import matplotlib.pyplot as plt
import numpy

homedir = os.environ['HOME']

sleep_cmd = b'\x10'
led_cmd = b'\x45'
rem_cmd = b'\x4a'
stream_characteristic = 'cacc07ff-ffff-4c48-8fae-a9ef71b75e26'
manager = gatt.DeviceManager(adapter_name='hci0')
export_data = 1
export_data_array = []
csvfile = homedir+"/Documents/REM_Sleep_Cycle_Monitor/Python/BLE_Notifications/data.csv"
i = 0
datArray = []  # data coming in
hexString = []
data = 0

#initialize stuff for graphing code
xdata = range(0,5000)
ydata = range(0,5000)
#initialize stuff for graphing code
plt.ion()
fig = plt.figure()
ax = fig.add_subplot(111)
line1, = ax.plot(xdata, ydata, 'r-') # Returns a tuple of line objects, thus the comma




#function when called opened a file and writes more data to it, or makes the file if it doesn't exist
def update_file_data(array):
    try:
        print("update file with new data")
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
            print("created new file")
            with open(csvfile, "w") as output:
                writer = csv.writer(output, lineterminator='\n')
                for val in array:
                    writer.writerow([val]) 
                print("data exported to csv")
    except: traceback.print_exc()


def split_string(n, s):
    return [ s[i:i+n] for i in range(0, len(s), n) ]



class AnyDevice(gatt.Device):
    xdata = range(0,1000)
    ydata = range(0,1000)
    #initialize stuff for graphing code
    plt.ion()
    fig = plt.figure()
    ax = fig.add_subplot(111)

    line1, = ax.plot(xdata, ydata, 'r-') # Returns a tuple of line objects, thus the comma
    
    def connect_succeeded(self):
        super().connect_succeeded()
        print("[%s] Connected" % (self.mac_address))
        
        
    def connect_failed(self, error):
        super().connect_failed(error)
        print("[%s] Connection failed: %s" % (self.mac_address, str(error)))

    def disconnect_succeeded(self):
        super().disconnect_succeeded()
        print("[%s] Disconnected" % (self.mac_address))


    def characteristic_value_updated(self, characteristic, value):
        hexString = (split_string(2,binascii.hexlify(value)))
        global i        
        if(i < 1000):
            for j in range(0, len(hexString)):
                data = int(hexString[j],16)
                #datArray.insert(i,data)
                #plot_dat1.append(data)
                datArray.append(data)
            i = i + 1
            self.update_graph(datArray)
        else:
            update_file_data(datArray)
            i = 1
            #plot_dat1[:] = []
            datArray[:] = []
            
            
    def services_resolved(self):
        super().services_resolved()
        print("[%s] Resolved services" % (self.mac_address))
        for service in self.services:
            print("[%s]  Service [%s]" % (self.mac_address, service.uuid))
            for characteristic in service.characteristics:
                print("[%s]    Characteristic [%s]" % (self.mac_address, characteristic.uuid))
                if characteristic.uuid == '1cce1ea8-bd34-4813-a00a-c76e028fadcb':
                    #characteristic.write_value(led_cmd)
                    #time.sleep(2)
                    #characteristic.write_value(sleep_cmd)
                    #time.sleep(1)
                    characteristic.write_value(rem_cmd)
                    print("sent")
                if characteristic.uuid == stream_characteristic:
                    characteristic.enable_notifications()
    #code for dynamic graphing    
    def update_graph(self,plot_data):
        length = len(plot_data)
        if length < 999:
            index_max = length
        else:
            index_max = 5000
        ydata = plot_data[-index_max:]
        xdata = range(0,len(ydata))
        #print(plot_data)
        line1.set_ydata(ydata)
        line1.set_xdata(xdata)
        fig.canvas.draw()
        fig.canvas.flush_events()
                             
device = AnyDevice(mac_address='4c:55:cc:14:2f:62', manager=manager)
device.connect()
manager.run()
