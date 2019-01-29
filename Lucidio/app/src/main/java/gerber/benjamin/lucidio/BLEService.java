package gerber.benjamin.lucidio;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Binder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BLEService extends Service {

    //Service details & Binding
    private final static String TAG = BLEService.class.getSimpleName();
    private final IBinder bleBinder = new BluetoothLeBinder();

    //Bluetooth Objects
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 3000;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothLeScanner mLEScanner;
    public static BluetoothGatt mGatt;
    //public static List<BluetoothDevice> mDeviceList = new ArrayList<>();
    public static BluetoothDevice mDevice;
    public int mData;

    //Characteristic Definitions
    public final static UUID STREAM_SERVICE = UUID.fromString("00035b03-58e6-07dd-021a-08123a000300");
    public final static UUID STREAM_CHARACTERISTIC = UUID.fromString("00035b03-58e6-07dd-021a-08123a000301");
    public final static UUID COMMAND_CHARACTERISTIC = UUID.fromString("00035b03-58e6-07dd-021a-08123a000301");

    //Broadcast Definitions
    public final static String ACTION_GATT_CONNECTED =
            "gerber.benjamin.lucidio.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "gerber.benjamin.lucidio.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "gerber.benjamin.lucidio.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "gerber.benjamin.lucidio.ACTION_DATA_AVAILABLE";

    //Misc Objects
    public static boolean sleeping = false;
    public int fCount;

    public boolean initialize() {

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        mBluetoothAdapter = bluetoothManager.getAdapter();
        mHandler = new Handler();

        return true;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return bleBinder;
    }

    public class BluetoothLeBinder extends Binder{
        BLEService getService(){
            return BLEService.this;
        }
    }


    /*
     * This Section of code deals with scanning for BLE devices
     * and setting up the GATT server along with sending and
     * receiving data to the BLE Device
     */

    //Connect to device call to handle disconnects
    public void connectToDevice(BluetoothDevice device) {
        mDevice = device;
        if (mGatt == null) {
            mGatt = device.connectGatt(this, false, gattCallback);
        }
    }

    //Disconnects device and removes associated variables
    public void disconnectDevice() {
        if (mGatt != null) {
            mGatt.disconnect();
            mGatt = null;
            MainActivity.device = null;
        }
    }

    /*  Sets up GATT characteristic handling and state changes
     *  Contains functions to apply settings after connection
     *  Contains functionality to write incoming bluetooth data to EPOCH timestamped array
     */
    public final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    broadcastUpdate(ACTION_GATT_CONNECTED);
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    broadcastUpdate(ACTION_GATT_DISCONNECTED);
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);

            List<BluetoothGattService> services = mGatt.getServices();
            Log.i("Services:", services.toString());
            //mGatt.readCharacteristic(services.get(1).getCharacteristics().get(0));

            //Sets the characteristic notification and indication to true
            BluetoothGattCharacteristic CharacteristicNotify = gatt.getService(STREAM_SERVICE).getCharacteristic(STREAM_CHARACTERISTIC);
            for (BluetoothGattDescriptor descriptor : CharacteristicNotify.getDescriptors()) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mGatt.writeDescriptor(descriptor);
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                mGatt.writeDescriptor(descriptor);
            }
            gatt.setCharacteristicNotification(CharacteristicNotify, true);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            //Process of receiving, parsing, and adding data to the data array
            if (sleeping == true) {
                    //This is the code section to receive analog data and outputting it to an array
                    final byte data[] = characteristic.getValue();
                    mData = buildByteData(data);
                    broadcastUpdate(ACTION_DATA_AVAILABLE);
                //}
            }
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
    }

    public int buildByteData(byte data[]){
        if (data != null && data.length > 0) {
            //Builds string, converts data, and sets values
            final StringBuilder stringBuilder = new StringBuilder(data.length);
            for (byte byteChar : data) {
                stringBuilder.append(String.format("%02X ", byteChar));
                int intbyte = byteChar;
                int dataint;
                if (intbyte <= 0) {
                    dataint = intbyte + 256;
                } else {
                    dataint = intbyte;
                }
                //Log.i("DATA", String.valueOf(dataint));

                //Set Values in data array
                mData = dataint;

                //Log.i("DATA", "Time" + time);
            }
        }
        return mData;
    };

    /*
     * Writes data passed to the function to the command characteristic
     */
    public boolean writeData (byte data[]){
        //check mBluetoothGatt is available
        if (mGatt == null) {
            Log.e("GATT", "lost connection");
            return false;
        }

        BluetoothGattService Service = mGatt.getService(STREAM_SERVICE);
        if (Service == null) {
            Log.e("GATT", "service not found!");
            return false;
        }
        BluetoothGattCharacteristic charac = Service
                .getCharacteristic(COMMAND_CHARACTERISTIC);
        if (charac == null) {
            Log.e("GATT", "char not found!");
            return false;
        }

        Log.i("DATA","Sending CMD: " + Byte.toString(data[0])); //Display command being sent in log
        BluetoothGattCharacteristic commandChar = mGatt.getService(MainActivity.STREAM_SERVICE).getCharacteristic(MainActivity.COMMAND_CHARACTERISTIC);
        commandChar.setValue(data); //Set the value of the data
        boolean status = mGatt.writeCharacteristic(commandChar); //Write to the characteristic
        Log.i("DATA", "Command returned " + Boolean.toString(status)); //Log pass or fail status
        return status;
    }

}
