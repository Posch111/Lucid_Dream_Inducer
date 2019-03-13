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
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.AsyncTask;
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
    public static final String DATA_VALUE = "DATA_KEY";
    private final IBinder bleBinder = new BluetoothLeBinder();

    //Bluetooth Objects
    public BluetoothAdapter bluetoothAdapter;
    public BluetoothManager bluetoothManager;
    private BluetoothGatt bluetoothGatt;
    public  BluetoothDevice bluetoothDevice;
    private ScanSettings settings;
    private int mData;
    private Handler mHandler;
    private ArrayList<BluetoothDevice> leDeviceList;
    public BLEDeviceListAdapter leDeviceListAdapter;

    public static boolean sleeping = false;
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;

    public final static String ACTION_SERVICE_BOUND =
            "com.example.bluetooth.le.ACTION_SERVICE_BOUND";
    public final static String ACTION_SCAN_COMPLETE =
            "com.example.bluetooth.le.ACTION_SCAN_COMPLETE";
    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    //Characteristic Definitions
    public final static UUID STREAM_SERVICE = UUID.fromString("00035b03-58e6-07dd-021a-08123a000300");
    public final static UUID STREAM_CHARACTERISTIC = UUID.fromString("00035b03-58e6-07dd-021a-08123a000301");
    public final static UUID COMMAND_CHARACTERISTIC = UUID.fromString("00035b03-58e6-07dd-021a-08123a000301");

    private static final long SCAN_PERIOD = 3000;


    @Override
    public void onCreate() {
        leDeviceList = new ArrayList<>();
        leDeviceListAdapter = new BLEDeviceListAdapter(this, leDeviceList);
        bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter= bluetoothManager.getAdapter();
        broadcastUpdate(ACTION_SERVICE_BOUND);
        mHandler = new Handler();
    }

// Various callback methods defined by the BLE API.
    private final BluetoothGattCallback gattCallback =
            new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                                    int newState) {
                    String intentAction;
                    bluetoothGatt = gatt;
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        intentAction = ACTION_GATT_CONNECTED;
                        broadcastUpdate(intentAction);
                        Log.i(TAG, "Connected to GATT server.");
                        Log.i(TAG, "Attempting to start service discovery:" +
                                gatt.discoverServices());

                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        intentAction = ACTION_GATT_DISCONNECTED;
                        Log.i(TAG, "Disconnected from GATT server.");
                        bluetoothDevice = null;
                        broadcastUpdate(intentAction);
                    }
                }

                @Override
                // New services discovered
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);

                        List<BluetoothGattService> services = gatt.getServices();
                        Log.i("Services:", services.toString());
                        //mGatt.readCharacteristic(services.get(1).getCharacteristics().get(0));
                        BluetoothDevice device = gatt.getDevice();

                        String name = device.getName();
                        int state = device.getBondState();
                        BluetoothGattService service = gatt.getService(STREAM_SERVICE);
                        if(service != null){
                            //Sets the characteristic notification and indication to true
                            BluetoothGattCharacteristic CharacteristicNotify = service.getCharacteristic(STREAM_CHARACTERISTIC);
                            for (BluetoothGattDescriptor descriptor : CharacteristicNotify.getDescriptors()) {
                                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                gatt.writeDescriptor(descriptor);
                                descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                                gatt.writeDescriptor(descriptor);
                            }
                            gatt.setCharacteristicNotification(CharacteristicNotify, true);
                        }
                        else {
                            Log.w(TAG, "Stream service not found");
                        }

                    } else {
                        Log.w(TAG, "onServicesDiscovered received: " + status);
                    }
                }

                @Override
                // Result of a characteristic read operation
                public void onCharacteristicRead(BluetoothGatt gatt,
                                                 BluetoothGattCharacteristic characteristic,
                                                 int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);

                        //Process of receiving, parsing, and adding data to the data array
                        if (sleeping) {
                            //This is the code section to receive analog data and outputting it to an array
                            final byte data[] = characteristic.getValue();
                            mData = buildByteData(data);
                            broadcastUpdate(ACTION_DATA_AVAILABLE);
                        }
                    }
                }
            };

    public void scanLE (final boolean enabled){
        disconnect();
        if(enabled){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bluetoothAdapter.stopLeScan(scanCallback);
                    broadcastUpdate(ACTION_SCAN_COMPLETE);
                }
            }, SCAN_PERIOD);
            leDeviceList.clear();
            bluetoothAdapter.startLeScan(scanCallback);
        } else {
            bluetoothAdapter.stopLeScan(scanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    String name = device.getName();
                    if(!(name == null) && !(name.contains("null")) && !(leDeviceList.contains(device))){
                        leDeviceList.add(device);
                        leDeviceListAdapter.notifyDataSetChanged();

                    }
                }
            });
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        int data = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 1);
        intent.putExtra(DATA_VALUE, data);

        sendBroadcast(intent);
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

    public int getConnectionState (){
        if (bluetoothManager.getConnectedDevices(BluetoothProfile.GATT_SERVER).size() > 0)
            return STATE_CONNECTED;
        else return STATE_DISCONNECTED;
    }

    /*
     * This Section of code deals with scanning for BLE devices
     * and setting up the GATT server along with sending and
     * receiving data to the BLE Device
     */

    //Connect to device call to handle disconnects
    public void connectToDevice(BluetoothDevice device){
        if(bluetoothManager.getConnectedDevices(BluetoothProfile.GATT).size() > 0)
        {
            disconnect();
        }
        bluetoothDevice = device;
        bluetoothGatt = device.connectGatt(this, false, gattCallback);

    }

    //Disconnects device and removes associated variables
    public void disconnect() {
        if((bluetoothDevice != null) &&
                (bluetoothManager.getConnectionState(bluetoothDevice,BluetoothProfile.GATT)
                        == STATE_CONNECTED)){
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
            bluetoothGatt = null;
            Toast.makeText(this,"Disconnected",Toast.LENGTH_SHORT).show();
        }
        bluetoothDevice = null;
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
    }

    /*
     * Writes data passed to the function to the command characteristic
     */
    public boolean writeData (byte data[]){

        //check mBluetoothGatt is available
        if (bluetoothGatt == null) {
            Log.e("GATT", "lost connection");
            return false;
        }

        BluetoothGattService Service = bluetoothGatt.getService(STREAM_SERVICE);
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
        BluetoothGattCharacteristic commandChar = bluetoothGatt.getService(STREAM_SERVICE).getCharacteristic(COMMAND_CHARACTERISTIC);
        commandChar.setValue(data); //Set the value of the data
        boolean status = bluetoothGatt.writeCharacteristic(commandChar); //Write to the characteristic
        Log.i("DATA", "Command returned " + Boolean.toString(status)); //Log pass or fail status
        return status;
    }

    @Override
    public void onDestroy() {
        bluetoothAdapter.cancelDiscovery();
        stopSelf();
        Log.d("service: ", "onDestroy");
        super.onDestroy();
    }
}
