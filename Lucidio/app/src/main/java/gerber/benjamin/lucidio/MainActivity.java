package gerber.benjamin.lucidio;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.ServiceConnection;
import android.content.BroadcastReceiver;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import gerber.benjamin.lucidio.BLEService.BluetoothLeBinder;

public class MainActivity extends AppCompatActivity {

    //Bluetooth Objects
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 3000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    public static List<BluetoothDevice> mDeviceList = new ArrayList<>();
    public static BluetoothDevice device;
    BLEService bleService;
    boolean serviceBound = false;
    private boolean mConnected;

    //Layout Objects
    private DrawerLayout dLayout;
    private Fragment frag = new HomeFragment(); // create a Fragment Object

    //Data Objects
    public static ArrayList<Long>mEogDataTime = new ArrayList<>();
    public static ArrayList<Integer>mEogData = new ArrayList<>();
    public static Calendar calendar = Calendar.getInstance();
    public static Date currentLocalTime = calendar.getTime();
    public static final DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
    public static final DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

    //Alarm Objects
    public static AlarmManager alarmMgr;
    public static PendingIntent alarmIntent;
    public static int timeHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    public static int timeMinute = Calendar.getInstance().get(Calendar.MINUTE);

    //Data Storage
    public final static String SETTINGS_FILENAME = "settings.dat";
    public static byte[] settingsBytes = new byte[5];
    public final static String ALARM_FILENAME = "alarm.dat";
    public static byte[] alarmBytes;
    public static boolean firstTimeFlag;
    public static boolean extWrite;
    public static boolean extReadOnly;
    public static FileInputStream inputStream;
    public static FileOutputStream outputStream;
    public static int eogLastWriteIndex = 0;

    //Misc Objects
    public int mFCount;
    public final String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    //Characteristic Definitions
    public final static UUID STREAM_SERVICE = UUID.fromString("00035b03-58e6-07dd-021a-08123a000300");
    public final static UUID STREAM_CHARACTERISTIC = UUID.fromString("00035b03-58e6-07dd-021a-08123a000301");
    public final static UUID COMMAND_CHARACTERISTIC = UUID.fromString("00035b03-58e6-07dd-021a-08123a000301");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializes floating action  button and the switching to the sleep fragment on press
        final FloatingActionButton sleep_butt = (FloatingActionButton) findViewById(R.id.sleep_butt);
        sleep_butt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                applyBrightness();
                try{
                    byte[] data = new byte[] {(byte) 73};
                    bleService.writeData(data);
                    Thread.sleep(120);
                    data[0] = (byte) 74;
                    bleService.writeData(data);
                    Thread.sleep(120);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }

                Intent myIntent = new Intent(MainActivity.this, SleepActivity.class);
                myIntent.putExtra("Device", device); //Optional parameters
                MainActivity.this.startActivity(myIntent);
                /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment sleepfrag = new SleepFragment();
                transaction.replace(R.id.frame, sleepfrag); // replace a Fragment with Frame Layout
                transaction.addToBackStack(null);
                transaction.commit(); // commit the changes*/
                Toast.makeText(getApplicationContext(), "Good Night", Toast.LENGTH_SHORT).show();
            }
        });

        //Requests permissions for required location services
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }

        if(isExternalStorageWritable())
        {
            extWrite = true;
            extReadOnly = false;
        }else if(isExternalStorageReadable())
        {
            extReadOnly = false;
        }


        //Checks if BluetoothLE is a feature on this device
        mHandler = new Handler();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        //getting BLE service intent
        Intent bleintent = new Intent(this, BLEService.class);
        bindService(bleintent, BleServiceConnection, Context.BIND_AUTO_CREATE);

        //Initializes Navigation Drawer
        setNavigationDrawer();
        //Sets the Default View
        setDefaultFragment();

        //Load initial Settings
        loadSettings();

        //Alarms
        alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

    }

    /*
     * Section deals with system functions and making sure bluetooth doesn't crash during transistions
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else if (Build.VERSION.SDK_INT >= 21) {
            mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
            settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
            filters = new ArrayList<ScanFilter>();
        } else if (device != null) {
            bleService.connectToDevice(device);
            Log.i("BT", "RECONNECTED_BY_RESUME");
        }


    }

    //Disconnects on pause
    @Override
    protected void onPause() {
        super.onPause();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            scanLeDevice(false);
        }
        saveSettings();
    }

    //Closes the gatt server on app destroy
    @Override
    protected void onDestroy() {
        super.onDestroy();

        saveSettings();
        if (bleService.mGatt == null) {
            return;
        }
        bleService.mGatt.close();
        bleService.mGatt = null;
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                //Bluetooth not enabled.
                Toast.makeText(getApplicationContext(), "Bluetooth must be enabled", Toast.LENGTH_LONG);
                finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     *  This function sets up our bluetooth service
     */

    private ServiceConnection BleServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            BluetoothLeBinder binder = (BluetoothLeBinder) service;
            bleService = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceBound = false;
        }
    };


    /*
     * This Section of code deals with scanning for BLE devices
     * and setting up the GATT server along with sending and
     * receiving data to the BLE Device
     */

    //Handles connecting to devices on API < 21 and API > 21
    public void scanLeDevice(final boolean enable) {
        mDeviceList.clear();

        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < 21) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    } else {
                        mLEScanner.stopScan(mScanCallback);

                    }
                }
            }, SCAN_PERIOD);
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mLEScanner.startScan(filters, settings, mScanCallback);
            }
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else {
                mLEScanner.stopScan(mScanCallback);
            }
        }
    }


    //The callback handling bluetooth scanning past API > 21
    public ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            //Log.i("callbackType", String.valueOf(callbackType));
            //Log.i("result", result.toString());

            BluetoothDevice btDevice = result.getDevice();
            if (mDeviceList.contains(btDevice) == false)
                mDeviceList.add(btDevice);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    ///Handled API < 21 callback from scannning
    public BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("onLeScan", device.toString());
                            bleService.connectToDevice(device);
                        }
                    });
                }
            };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (bleService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                runOnUiThread(new Runnable() {
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Bluetooth Connected", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (bleService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                runOnUiThread(new Runnable() {
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Bluetooth Disconnected", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (bleService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {

            }
            if (bleService.ACTION_DATA_AVAILABLE.equals(action)) {
                mEogData.add(bleService.mData);
            }
        }
    };

    /*
     * Setting up Navigational Drawer that creates other fragments
     * This section takes care of Navigation and UI setups along with
     * saving and restoring instance states in case of config change
     */
    private void setNavigationDrawer() {

        Intent intent = null;
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = (NavigationView) findViewById(R.id.navigation); // initiate a Navigation View

        // implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                int itemId = menuItem.getItemId(); // get selected menu item's id

                // check selected menu item's id and replace a Fragment Accordingly
                if (itemId == R.id.nav_home){
                    frag = new HomeFragment();
                } else if (itemId == R.id.nav_alarms) {
                    frag = new AlarmFragment();
                } else if (itemId == R.id.nav_cues) {
                    frag = new CueFragment();
                } else if (itemId == R.id.nav_data) {
                    frag = new DataFragment();
                } else if (itemId == R.id.nav_settings){
                    frag = new SettingFragment();
                } else if (itemId == R.id.nav_help){
                    frag = new HelpFragment();
                }else
                    frag = new HomeFragment();

                // display a toast message with menu item's title
                Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    //transaction.add(R.id.frame, frag);
                    transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
                    transaction.addToBackStack(null);
                    transaction.commit(); // commit the changes
                    dLayout.closeDrawers(); // close the all open Drawer Views
                    return true;
                }

                return false;

            }
        });
    }

    //Function sets the main fragment when the app starts
    public void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
        transaction.commit(); // commit the changes
    }

    //Alarms Section
    public void setAlarm (int hour, int minute){
        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
    }

    public void disableAlarm(){
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
    }

    /*
     *  DATA STORAGE AND MANAGEMENT
     *
     *  Save settings writes the settings array to a file
     *  Load settings reads in the settings from the file, and creates one if none exists
     *  Apply settings sends the settings to the microcontroller over bluetooth
     *
     */

    //Function for saving all the settings
    public void saveSettings(){

        //Initialize First Time settings
        if(firstTimeFlag){
            settingsBytes = new byte[] {(byte)0, (byte)0, (byte)0, (byte)50, };
            firstTimeFlag = false;
        }

        //Opens file, creates a new file if none exists, and writes the settings
        File settingsFile = new File(this.getFilesDir(), SETTINGS_FILENAME);
        try{
            if(settingsFile.exists()) {
                settingsFile.delete();
                Log.i("CONFIG", "Created Config File");
            }

            settingsFile.createNewFile();
            FileOutputStream outputStream = getApplicationContext().openFileOutput(SETTINGS_FILENAME, Context.MODE_PRIVATE);
            for(byte byteChar : settingsBytes) {
                outputStream.write(byteChar);
                int byteInt = byteChar;
                Log.i("CONFIG", String.valueOf(byteInt));
            }
            outputStream.close();
            Log.i("CONFIG", "Settings Saved");
        }catch (IOException e){
            e.printStackTrace();
            Log.i("CONFIG", "Settings Failed to Save"); }
    }

    //Function to load settings from config file
    public void loadSettings(){

        byte[] data = new byte[5];                                          //Initialize temp array
        File settingsFile = new File(this.getFilesDir(), SETTINGS_FILENAME);//Get full filepath
        if(settingsFile.exists()) {
            firstTimeFlag = false;
        } else {saveSettings();}
        try{
            inputStream = new FileInputStream(settingsFile);
            inputStream.read(data);                                         //Open File
            inputStream.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        //Iterate through read values, and assign them to settings
        for(int i = 0; i < 5; i++){
            int byteInt = data[i];
            settingsBytes[i] = data[i];
            Log.i("CONFIG", String.valueOf(byteInt));
        }
    }

    //Function for applying settings to MSP430
    public void applyBrightness(){
        /*
                Here we will set initial settings when we connect with the Bluetooth
        */
            byte[] data = new byte[] {settingsBytes[3]};                            //Set LED Brightness from settings
            byte[] ledset = new byte[]{(byte) 68};                                  //Set LED cmd

            try{
                bleService.writeData(ledset);
                Thread.sleep(120); //Extra delay for sending LED value after receive cmd
                bleService.writeData(data);
                Thread.sleep(120);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
    }

    //Saves EOG Data and timestamp
    public boolean saveEogData (){

        String dateStamp = dateFormatter.format(currentLocalTime);
        String filename = "Sleep_data_" + dateStamp.replace("/" ,"_") + ".csv";  //Datestamped Filename
        String fullDir = getFullDir().getAbsolutePath().toString();                                 //Get Filepath
        Log.i("STORAGE", fullDir + filename);
        ArrayList<Long>mEogDataTimeWrite = new ArrayList<>();                                       //Temp data set array
        ArrayList<Integer>mEogDataWrite = new ArrayList<>();                                        //
        int eogSize = mEogData.size() - 1;                                                          //Current array size
        int eogSizeRel = (mEogData.size() - 1) - eogLastWriteIndex;

        for(int i = eogLastWriteIndex; i < eogSize; i++){                                           //Build temp arrays
            mEogDataTimeWrite.add(mEogDataTime.get(i));                                             //to log last chunk of
            mEogDataWrite.add(mEogData.get(i));                                                     //data since last call
        }

        //Uses OutputStreamWriter to print a csv format data set of type <timestamp>,<data>
        try {
                OutputStreamWriter outputStreamWriter =
                        new OutputStreamWriter(new FileOutputStream(new File(fullDir, filename)));
            for(int i = 0; i < eogSizeRel; i++){
                String streamLine;
                streamLine = mEogDataTime.get(i) + "," + mEogDataWrite.get(i) + "\n";
                //Log.i("STORAGE", streamLine + "iteration:" + String.valueOf(i));
                outputStreamWriter.write(streamLine);

            }
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("STORAGE", "EOG data not successfully saved");
            return false;
        }
        eogLastWriteIndex = eogSize;
        Log.i("STORAGE", "EOG data saved");
        return true;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getFullDir() {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "/eog/");

        if (!file.exists()) {
            try {
                file.createNewFile();
                Log.i("STORAGE", "created " + file.getPath());}
            catch (IOException e){e.printStackTrace();}
        }
        return file;
    }

    /*
     *   PERMISSIONS
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}