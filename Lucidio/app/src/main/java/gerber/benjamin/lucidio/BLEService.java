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

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;


public class BLEService extends Service {

    //Microchip variables
    public static final String INTENT_EXTRA_SERVICE_ADDRESS = "BLE_SERVICE_DEVICE_ADDRESS";
    public static final String INTENT_EXTRA_SERVICE_NAME = "BLE_SERVICE_DEVICE_NAME";
    public static final String INTENT_EXTRA_SERVICE_DATA = "BLE_SERVICE_DATA";

    public final static String ACTION_BLE_REQ_ENABLE_BT = "com.microchip.mldpterminal3.ACTION_BLE_REQ_ENABLE_BT";
    public final static String ACTION_BLE_SCAN_RESULT = "com.microchip.mldpterminal3.ACTION_BLE_SCAN_RESULT";
    public final static String ACTION_BLE_CONNECTED = "com.microchip.mldpterminal3.ACTION_BLE_CONNECTED";
    public final static String ACTION_BLE_DISCONNECTED = "com.microchip.mldpterminal3.ACTION_BLE_DISCONNECTED";
    public final static String ACTION_BLE_DATA_RECEIVED = "com.microchip.mldpterminal3.ACTION_BLE_DATA_RECEIVED";

    //The MLDP UUID will be included in the RN4020 Advertising packet unless a private service and characteristic exists. In that case use the private service UUID here instead.
    private final static byte[] SCAN_RECORD_MLDP_PRIVATE_SERVICE = {0x00, 0x03, 0x00, 0x3a, 0x12, 0x08, 0x1a, 0x02, (byte) 0xdd, 0x07, (byte) 0xe6, 0x58, 0x03, 0x5b, 0x03, 0x00};

    private final static UUID UUID_MLDP_PRIVATE_SERVICE = UUID.fromString("00035b03-58e6-07dd-021a-08123a000300"); //Private service for Microchip MLDP
    private final static UUID UUID_MLDP_DATA_PRIVATE_CHAR = UUID.fromString("00035b03-58e6-07dd-021a-08123a000301"); //Characteristic for MLDP Data, properties - notify, write
    private final static UUID UUID_MLDP_CONTROL_PRIVATE_CHAR = UUID.fromString("00035b03-58e6-07dd-021a-08123a0003ff"); //Characteristic for MLDP Control, properties - read, write

    private final static UUID UUID_TANSPARENT_PRIVATE_SERVICE = UUID.fromString("49535343-fe7d-4ae5-8fa9-9fafd205e455"); //Private service for Microchip Transparent
    private final static UUID UUID_TRANSPARENT_TX_PRIVATE_CHAR = UUID.fromString("49535343-1e4d-4bd9-ba61-23c647249616"); //Characteristic for Transparent Data from BM module, properties - notify, write, write no response
    private final static UUID UUID_TRANSPARENT_RX_PRIVATE_CHAR = UUID.fromString("49535343-8841-43f4-a8d4-ecbe34729bb3"); //Characteristic for Transparent Data to BM module, properties - write, write no response

    private final static UUID UUID_CHAR_NOTIFICATION_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"); //Special descriptor needed to enable notifications
    private UUID[] uuidScanList = {UUID_MLDP_PRIVATE_SERVICE, UUID_TANSPARENT_PRIVATE_SERVICE};
    private final Queue<BluetoothGattDescriptor> descriptorWriteQueue = new LinkedList<BluetoothGattDescriptor>();
    private final Queue<BluetoothGattCharacteristic> characteristicWriteQueue = new LinkedList<BluetoothGattCharacteristic>();

    //Service details & Binding
    private final static String TAG = BLEService.class.getSimpleName();
    public static final String DATA_VALUE = "DATA_KEY";
    private final IBinder bleBinder = new BluetoothLeBinder();
    private int connectionAttemptCountdown = 0;
    private BluetoothGattCharacteristic mldpDataCharacteristic, mldpControlCharacteristic, transparentTxDataCharacteristic, transparentRxDataCharacteristic;

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
    public final static String ACTION_MLDP_NOT_FOUND =
            "com.example.bluetooth.le.MLDP_NOT_FOUND";

    //Characteristic Definitions
    public final static UUID MLDP_SERVICE = UUID.fromString("00035b03-58e6-07dd-021a-08123a000300");
    public final static UUID MLDP_CHARACTERISTIC = UUID.fromString("00035b03-58e6-07dd-021a-08123a000301");
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


    // ----------------------------------------------------------------------------------------------------------------
    // Implements callback methods for GATT events such as connecting, discovering services, write completion, etc.
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        //Connected or disconnected
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            try {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    connectionAttemptCountdown = 0;                                                     //Stop counting connection attempts
                    if (newState == BluetoothProfile.STATE_CONNECTED) {                                 //Connected
                        final Intent intent = new Intent(ACTION_GATT_CONNECTED);
                        sendBroadcast(intent);
                        Log.i(TAG, "Connected to BLE device");
                        descriptorWriteQueue.clear();                                                   //Clear write queues in case there was something left in the queue from the previous connection
                        characteristicWriteQueue.clear();
                        bluetoothGatt.discoverServices();                                               //Discover services after successful connection
                    }
                    else if (newState == BluetoothProfile.STATE_DISCONNECTED) {                         //Disconnected
                        final Intent intent = new Intent(ACTION_GATT_DISCONNECTED);
                        sendBroadcast(intent);
                        Log.i(TAG, "Disconnected from BLE device");
                    }
                }
                else {                                                                                  //Something went wrong with the connection or disconnection request
                    if (connectionAttemptCountdown-- > 0) {                                             //See is we should try another attempt at connecting
                        gatt.connect();                                                                 //Use the existing BluetoothGatt to try connect
                        Log.d(TAG, "Connection attempt failed, trying again");
                    }
                    else if (newState == BluetoothProfile.STATE_DISCONNECTED) {                         //Not trying another connection attempt and are not connected
                        final Intent intent = new Intent(ACTION_GATT_DISCONNECTED);
                        sendBroadcast(intent);
                        Log.i(TAG, "Unexpectedly disconnected from BLE device");
                    }
                }
            }
            catch (Exception e) {
                Log.e(TAG, "Oops, exception caught in " + e.getStackTrace()[0].getMethodName() + ": " + e.getMessage());
            }
        }

        //Service discovery completed
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            try {
                mldpDataCharacteristic = transparentTxDataCharacteristic = transparentRxDataCharacteristic = null;
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    List<BluetoothGattService> gattServices = gatt.getServices();                       //Get the list of services discovered
                    if (gattServices == null) {
                        Toast.makeText(getApplicationContext(), "No Services Found", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "No BLE services found");
                        return;
                    }
                    UUID uuid;
                    for (BluetoothGattService gattService : gattServices) {                             //Loops through available GATT services
                        uuid = gattService.getUuid();                                                   //Get the UUID of the service
                        if (uuid.equals(UUID_MLDP_PRIVATE_SERVICE) || uuid.equals(UUID_TANSPARENT_PRIVATE_SERVICE)) { //See if it is the MLDP or Transparent private service UUID
                            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) { //Loops through available characteristics
                                uuid = gattCharacteristic.getUuid();                                    //Get the UUID of the characteristic
                                if (uuid.equals(UUID_TRANSPARENT_TX_PRIVATE_CHAR)) {                    //See if it is the Transparent Tx data private characteristic UUID
                                    transparentTxDataCharacteristic = gattCharacteristic;
                                    final int characteristicProperties = gattCharacteristic.getProperties(); //Get the properties of the characteristic
                                    if ((characteristicProperties & (BluetoothGattCharacteristic.PROPERTY_NOTIFY)) > 0) { //See if the characteristic has the Notify property
                                        bluetoothGatt.setCharacteristicNotification(gattCharacteristic, true); //If so then enable notification in the BluetoothGatt
                                        BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID_CHAR_NOTIFICATION_DESCRIPTOR); //Get the descriptor that enables notification on the server
                                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE); //Set the value of the descriptor to enable notification
                                        descriptorWriteQueue.add(descriptor);                           //put the descriptor into the write queue
                                        if(descriptorWriteQueue.size() == 1) {                          //If there is only 1 item in the queue, then write it.  If more than 1, we handle asynchronously in the callback above
                                            bluetoothGatt.writeDescriptor(descriptor);                 //Write the descriptor
                                        }
                                    }
                                    if ((characteristicProperties & (BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) > 0) { //See if the characteristic has the Write (unacknowledged) property
                                        gattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE); //If so then set the write type (write with no acknowledge) in the BluetoothGatt
                                    }
                                    Log.d(TAG, "Found Transparent service Tx characteristics");
                                }
                                if (uuid.equals(UUID_TRANSPARENT_RX_PRIVATE_CHAR)) {                    //See if it is the Transparent Rx data private characteristic UUID
                                    transparentRxDataCharacteristic = gattCharacteristic;
                                    final int characteristicProperties = gattCharacteristic.getProperties(); //Get the properties of the characteristic
                                    if ((characteristicProperties & (BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) > 0) { //See if the characteristic has the Write (unacknowledged) property
                                        gattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE); //If so then set the write type (write with no acknowledge) in the BluetoothGatt
                                    }
                                    Log.d(TAG, "Found Transparent service Rx characteristics");
                                }

                                if (uuid.equals(UUID_MLDP_DATA_PRIVATE_CHAR)) {                         //See if it is the MLDP data private characteristic UUID
                                    mldpDataCharacteristic = gattCharacteristic;
                                    final int characteristicProperties = gattCharacteristic.getProperties(); //Get the properties of the characteristic
                                    if ((characteristicProperties & (BluetoothGattCharacteristic.PROPERTY_NOTIFY)) > 0) { //See if the characteristic has the Notify property
                                        bluetoothGatt.setCharacteristicNotification(gattCharacteristic, true); //If so then enable notification in the BluetoothGatt
                                        BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID_CHAR_NOTIFICATION_DESCRIPTOR); //Get the descriptor that enables notification on the server
                                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE); //Set the value of the descriptor to enable notification
                                        descriptorWriteQueue.add(descriptor);                           //put the descriptor into the write queue
                                        if(descriptorWriteQueue.size() == 1) {                          //If there is only 1 item in the queue, then write it.  If more than 1, we handle asynchronously in the callback above
                                            bluetoothGatt.writeDescriptor(descriptor);                 //Write the descriptor
                                        }
                                    }

//Use Indicate for RN4020 module firmware prior to 1.20 (not recommended)
//                                    if ((characteristicProperties & (BluetoothGattCharacteristic.PROPERTY_INDICATE)) > 0) { //Only see if the characteristic has the Indicate property if it does not have the Notify property
//                                        bluetoothGatt.setCharacteristicNotification(gattCharacteristic, true); //If so then enable notification (and indication) in the BluetoothGatt
//                                        BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID_CHAR_NOTIFICATION_DESCRIPTOR); //Get the descriptor that enables indication on the server
//                                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE); //Set the value of the descriptor to enable indication
//                                        descriptorWriteQueue.add(descriptor);                           //put the descriptor into the write queue
//                                        if(descriptorWriteQueue.size() == 1) {                          //If there is only 1 item in the queue, then write it.  If more than 1, we handle asynchronously in the callback above
//                                            bluetoothGatt.writeDescriptor(descriptor);                  //Write the descriptor
//                                        }
//                                    }
                                    if ((characteristicProperties & (BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) > 0) { //See if the characteristic has the Write (unacknowledged) property
                                        gattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE); //If so then set the write type (write with no acknowledge) in the BluetoothGatt
                                    }
//Use Write With Response for RN4020 module firmware prior to 1.20 (not recommended)
//                                    if ((characteristicProperties & (BluetoothGattCharacteristic.PROPERTY_WRITE)) > 0) { //See if the characteristic has the Write (acknowledged) property
//                                        gattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT); //If so then set the write type (write with acknowledge) in the BluetoothGatt
//                                    }
                                    Log.d(TAG, "Found MLDP service and characteristics");
                                }
                                if(uuid.equals(UUID_MLDP_CONTROL_PRIVATE_CHAR)) {
                                    mldpControlCharacteristic = gattCharacteristic;
                                    final int characteristicProperties = gattCharacteristic.getProperties(); //Get the properties of the characteristic
                                    if ((characteristicProperties & (BluetoothGattCharacteristic.PROPERTY_NOTIFY)) > 0) { //See if the characteristic has the Notify property
                                        bluetoothGatt.setCharacteristicNotification(gattCharacteristic, true); //If so then enable notification in the BluetoothGatt
                                        BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID_CHAR_NOTIFICATION_DESCRIPTOR); //Get the descriptor that enables notification on the server
                                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE); //Set the value of the descriptor to enable notification
                                        descriptorWriteQueue.add(descriptor);                           //put the descriptor into the write queue
                                        if (descriptorWriteQueue.size() == 1) {                          //If there is only 1 item in the queue, then write it.  If more than 1, we handle asynchronously in the callback above
                                            bluetoothGatt.writeDescriptor(descriptor);                 //Write the descriptor
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                    if(mldpDataCharacteristic == null && (transparentTxDataCharacteristic == null || transparentRxDataCharacteristic == null)) {
                        disconnect();
                        Toast.makeText(getApplicationContext(),"No MLDP", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Did not find MLDP or Transparent service");
                    }
                }
                else {
                    Log.w(TAG, "Failed service discovery with status: " + status);
                }
            }
            catch (Exception e) {
                Log.e(TAG, "Oops, exception caught in " + e.getStackTrace()[0].getMethodName() + ": " + e.getMessage());
            }
        }

        //Received notification or indication with new value for a characteristic
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            try {
                if (UUID_MLDP_DATA_PRIVATE_CHAR.equals(characteristic.getUuid()) || UUID_TRANSPARENT_TX_PRIVATE_CHAR.equals(characteristic.getUuid())) {                     //See if it is the MLDP data characteristic
                    byte[] data = characteristic.getValue();                       //Get the data in string format
                    //byte[] dataValue = characteristic.getValue();                                     //Example of getting data in a byte array
                    String value = data.toString();
                    Log.d(TAG, "Data :" + value);
                    final Intent intent = new Intent(BLEService.ACTION_DATA_AVAILABLE);                         //Create the intent to announce the new data
                    intent.putExtra(INTENT_EXTRA_SERVICE_DATA, value);                              //Add the data to the intent
                    sendBroadcast(intent);                                                              //Broadcast the intent
                }
                if(UUID_MLDP_CONTROL_PRIVATE_CHAR.equals(characteristic.getUuid())){
                    byte[] data = characteristic.getValue();                       //Get the data in string format
                    //byte[] dataValue = characteristic.getValue();                                     //Example of getting data in a byte array
                    String value = data.toString();
                    Log.d(TAG, "Control Data :" + value);
                    final Intent intent = new Intent(BLEService.ACTION_DATA_AVAILABLE);                         //Create the intent to announce the new data
                    intent.putExtra(INTENT_EXTRA_SERVICE_DATA, value);                              //Add the data to the intent
                    sendBroadcast(intent);
                }
            }
            catch (Exception e) {
                Log.e(TAG, "Oops, exception caught in " + e.getStackTrace()[0].getMethodName() + ": " + e.getMessage());
            }
        }

        //Write completed
        //Use write queue because BluetoothGatt can only do one write at a time
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            try {
                if (status != BluetoothGatt.GATT_SUCCESS) {                                             //See if the write was successful
                    Log.w(TAG, "Error writing GATT characteristic with status: " + status);
                }
                if(characteristicWriteQueue.size() > 0)
                {
                    characteristicWriteQueue.remove(); //Pop the item that we just finishing writing
                }

                if(characteristicWriteQueue.size() > 0) {                                               //See if there is more to write
                    bluetoothGatt.writeCharacteristic(characteristicWriteQueue.element());              //Write characteristic
                }
            }
            catch (Exception e) {
                Log.e(TAG, "Oops, exception caught in " + e.getStackTrace()[0].getMethodName() + ": " + e.getMessage());
            }
        }

        //Write descriptor completed
        //Use write queue because BluetoothGatt can only do one write at a time
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            try {
                if (status != BluetoothGatt.GATT_SUCCESS) {
                    Log.w(TAG, "Error writing GATT descriptor with status: " + status);
                }
                descriptorWriteQueue.remove();                                                          //Pop the item that we just finishing writing
                if(descriptorWriteQueue.size() > 0) {                                                   //See if there is more to write
                    bluetoothGatt.writeDescriptor(descriptorWriteQueue.element());                      //Write descriptor
                }
            }
            catch (Exception e) {
                Log.e(TAG, "Oops, exception caught in " + e.getStackTrace()[0].getMethodName() + ": " + e.getMessage());
            }
        }

        //Read completed. For information only. This application uses Notification or Indication to receive updated characteristic data, not Read
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            String x = descriptor.getValue().toString();
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
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

    @Override
    public void onDestroy() {
        bluetoothAdapter.cancelDiscovery();
        stopSelf();
        Log.d("service: ", "onDestroy");
        super.onDestroy();
    }

    //Microchip write to MLDP example code
    // Write to the MLDP data characteristic
    public void writeMLDP(String string) {                                                          //Write string (may need to add code to limit write to 20 bytes)
        try {
            Log.i("DATA","Sending CMD: " + string);

            BluetoothGattCharacteristic writeDataCharacteristic;
            if (mldpDataCharacteristic != null) {
                writeDataCharacteristic = mldpDataCharacteristic;
            }
            else {
                writeDataCharacteristic = transparentRxDataCharacteristic;
            }
            if (bluetoothAdapter == null || bluetoothGatt == null || writeDataCharacteristic == null) {
                Log.w(TAG, "Write attempted with Bluetooth uninitialized or not connected");
                return;
            }
            writeDataCharacteristic.setValue(string);
            Log.i("Data", "Characteristic: " + writeDataCharacteristic.getStringValue(0));
            characteristicWriteQueue.add(writeDataCharacteristic);                                       //Put the characteristic into the write queue
            if(characteristicWriteQueue.size() == 1){                                                   //If there is only 1 item in the queue, then write it.  If more than 1, we handle asynchronously in the callback above
                if (!bluetoothGatt.writeCharacteristic(writeDataCharacteristic)) {                       //Request the BluetoothGatt to do the Write
                    Log.d(TAG, "Failed to write characteristic");                                       //Write request was not accepted by the BluetoothGatt
                }
            }
        }
        catch (Exception e) {
            Log.e(TAG, "Oops, exception caught in " + e.getStackTrace()[0].getMethodName() + ": " + e.getMessage());
        }
    }

    public void writeMLDP(byte[] byteValues) {                                                      //Write bytes (may need to add code to limit write to 20 bytes)
        try {
            Log.i("DATA","Sending CMD: " + Arrays.toString(byteValues));
                    BluetoothGattCharacteristic writeDataCharacteristic;
            if (mldpDataCharacteristic != null) {
                writeDataCharacteristic = mldpDataCharacteristic;
            }
            else {
                writeDataCharacteristic = transparentRxDataCharacteristic;
            }
            if (bluetoothAdapter == null || bluetoothGatt == null || writeDataCharacteristic == null) {
                Log.w(TAG, "Write attempted with Bluetooth uninitialized or not connected");
                return;
            }
            writeDataCharacteristic.setValue(byteValues);
            characteristicWriteQueue.add(writeDataCharacteristic);                                       //Put the characteristic into the write queue
            if(characteristicWriteQueue.size() == 1){                                                   //If there is only 1 item in the queue, then write it.  If more than 1, we handle asynchronously in the callback above
                if (!bluetoothGatt.writeCharacteristic(writeDataCharacteristic)) {                       //Request the BluetoothGatt to do the Write
                    Log.d(TAG, "Failed to write characteristic");                                       //Write request was not accepted by the BluetoothGatt
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Oops, exception caught in " + e.getStackTrace()[0].getMethodName() + ": " + e.getMessage());
        }
    }
    public void writeMLDPControl(byte[] byteValues) {                                                      //Write bytes (may need to add code to limit write to 20 bytes)
        try {
            Log.i("DATA","Sending Control CMD: " + Arrays.toString(byteValues));
            BluetoothGattCharacteristic writeDataCharacteristic;
            if (mldpControlCharacteristic != null) {
                writeDataCharacteristic = mldpDataCharacteristic;
            }
            else {
                writeDataCharacteristic = transparentRxDataCharacteristic;
            }
            if (bluetoothAdapter == null || bluetoothGatt == null || writeDataCharacteristic == null) {
                Log.w(TAG, "Write attempted with Bluetooth uninitialized or not connected");
                return;
            }
            writeDataCharacteristic.setValue(byteValues);
            characteristicWriteQueue.add(writeDataCharacteristic);                                       //Put the characteristic into the write queue
            if(characteristicWriteQueue.size() == 1){                                                   //If there is only 1 item in the queue, then write it.  If more than 1, we handle asynchronously in the callback above
                if (!bluetoothGatt.writeCharacteristic(writeDataCharacteristic)) {                       //Request the BluetoothGatt to do the Write
                    Log.d(TAG, "Failed to write characteristic");                                       //Write request was not accepted by the BluetoothGatt
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Oops, exception caught in " + e.getStackTrace()[0].getMethodName() + ": " + e.getMessage());
        }
    }
}
