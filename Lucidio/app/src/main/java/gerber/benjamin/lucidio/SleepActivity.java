//package gerber.benjamin.lucidio;
//
//import android.content.ComponentName;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.IBinder;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.jjoe64.graphview.DefaultLabelFormatter;
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.text.DateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Locale;
//import java.util.zip.Inflater;
//
//public class SleepActivity extends AppCompatActivity {
//
//    private final Handler mHandler = new Handler();
//    private Runnable mTimer;
//    private LineGraphSeries<DataPoint> mSeries;
//
//    BLEService bleService;
//    boolean serviceBound = false;
//    private boolean mConnected;
//
//    //Bluetooth Commands to MSP430 system
//    private byte[] motorOn = new byte[]{0x45};
//    private byte[] motorOff = new byte[]{0x46};
//    private byte[] led3On = new byte[]{0x47};
//    private byte[] led3Off = new byte[]{0x48};
//    private byte[] led4On = new byte[]{0x4D};
//    private byte[] led4Off = new byte[]{0x4E};
//
//    //Data Objects
//    public static ArrayList<Long>mEogDataTime = new ArrayList<>();
//    public static ArrayList<Integer>mEogData = new ArrayList<>();
//    public static Calendar calendar = Calendar.getInstance();
//    public static Date currentLocalTime = calendar.getTime();
//    public static final DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
//    public static final DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
//
//    //Misc Objects
//    public static volatile boolean cmdrun = false;
//    public static volatile  boolean datarun = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sleep);
//        Intent bleServiceIntent = new Intent(this, BLEService.class);
//        bindService(bleServiceIntent, BleServiceConnection, BIND_IMPORTANT);
//
//        if (bleService.getConnectionState() == BLEService.STATE_DISCONNECTED) {
//            Toast.makeText(this, "Bluetooth Not Connected", Toast.LENGTH_SHORT).show();
//            cmdrun = false;
//            datarun = false;
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        //Initializes an dummy array for the creation of the graph (array will be offset by 1920)
//        for (int i = 0; i < 1920; i++) {
//            MainActivity.currentLocalTime.getTime();
//            MainActivity.mEogDataTime.add(System.currentTimeMillis());
//            MainActivity.mEogData.add(i, 150);
//        }
//
//        //Initialize data streaming
//        bleService.sleeping = true;
//
//        //Initializes second graphview
//        GraphView graph = findViewById(R.id.sleep_graph_rt);
//        mSeries = new LineGraphSeries<>();
//        graph.addSeries(mSeries);
//
//        // custom label formatter to show x value as timestamp
//        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
//            @Override
//            public String formatLabel(double value, boolean isValueX) {
//                if (isValueX) {
//                    // show normal x values
//                    return (timeFormatter.format(value));
//                }
//                return null;
//            }
//        });
//
//
//
//        graph.getGridLabelRenderer().setNumHorizontalLabels(4);
//
//        // set manual X bounds
//        graph.getViewport().setXAxisBoundsManual(true);
//        graph.getViewport().setMinX(0);
//        graph.getViewport().setMaxX(1920);
//
//        // set manual Y bounds
//        graph.getViewport().setYAxisBoundsManual(true);
//        graph.getViewport().setMinY(0);
//        graph.getViewport().setMaxY(250);
//
//        //Buttons
//        final Button button = findViewById(R.id.end_sleep_butt);
//
//        cmdrun = true;
//        datarun = true;
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        mTimer = new Runnable() {
//            @Override
//            public void run() {
//                mSeries.appendData(new DataPoint(MainActivity.mEogDataTime.get(
//                        MainActivity.mEogDataTime.size() - 1),
//                        MainActivity.mEogData.get(
//                                MainActivity.mEogData.size() - 1)), true, 1920);
//                mHandler.postDelayed(this, 100);
//            }
//        };
//        mHandler.postDelayed(mTimer, 1000);
////TODO
//       // new Thread(new SleepFragment.dataRunnable()).start();
//       // new Thread(new SleepFragment.cmdRunnable()).start();
//
//    }
//
//    @Override
//    public void onPause() {
//        mHandler.removeCallbacks(mTimer);
//        super.onPause();
//    }
//
//    /*
//     *  This function sets up our bluetooth service
//     */
//
//    private ServiceConnection BleServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder service) {
//            BLEService.BluetoothLeBinder binder = (BLEService.BluetoothLeBinder) service;
//            bleService = binder.getService();
//            serviceBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            serviceBound = false;
//        }
//    };
//
//
//    //Created a click listener for all of the buttons
//    public void onClick(View view) {
//        switch(view.getId()) {
//            case (R.id.end_sleep_butt):
//                SleepActivity.cmdrun = false;
//                SleepActivity.datarun = false;
//                byte[] data = new byte[] {(byte)75};
//                bleService.writeData(data);
//                try{
//                    Thread.sleep(150);
//                }catch(InterruptedException e){
//                    e.printStackTrace();
//                }
//                data[0] = (byte)76;
//                bleService.writeData(data);
//                BLEService.sleeping = false;
//                Fragment frag = new DataFragment();
//                FragmentManager fragmentManager= getSupportFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
//                transaction.commit(); // commit the changes
//                break;
//        }
//    }
//
//    /*
//     * Separate Thread tasks
//     *
//
//    public class dataRunnable implements Runnable {
//        public void run(){
//            while (((MainActivity)getActivity()).datarun) {
//                while (!Thread.currentThread().isInterrupted()) {
//                    ((MainActivity) getActivity()).saveEogData();
//                    try{
//                        Thread.sleep(5000);
//                    }catch (InterruptedException e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//
//    public class cmdRunnable implements Runnable {
//        public void run() {
//            while (((MainActivity)getActivity()).cmdrun) {
//                while (!Thread.currentThread().isInterrupted()) {
//                    // do something in the loop
//                    try {
//                        ((MainActivity) getActivity()).writeData(motorOn);
//                        Thread.sleep(333);
//                        ((MainActivity) getActivity()).writeData(led3On);
//                        Thread.sleep(333);
//                        ((MainActivity) getActivity()).writeData(led4On);
//                        Thread.sleep(333);
//                        ((MainActivity) getActivity()).writeData(motorOff);
//                        Thread.sleep(333);
//                        ((MainActivity) getActivity()).writeData(led3Off);
//                        Thread.sleep(333);
//                        ((MainActivity) getActivity()).writeData(led4Off);
//                        Thread.sleep(333);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }*/
//
//    //Saves EOG Data and timestamp
//    public boolean saveEogData (){
//
//        String dateStamp = dateFormatter.format(currentLocalTime);
//        String filename = "Sleep_data_" + dateStamp.replace("/" ,"_") + ".csv";  //Datestamped Filename
//        String fullDir = getFullDir().getAbsolutePath().toString();                                 //Get Filepath
//        Log.i("STORAGE", fullDir + filename);
//        ArrayList<Long> mEogDataTimeWrite = new ArrayList<>();                                       //Temp data set array
//        ArrayList<Integer>mEogDataWrite = new ArrayList<>();                                        //
//        int eogSize = mEogData.size() - 1;                                                          //Current array size
//        int eogSizeRel = (mEogData.size() - 1) - MainActivity.eogLastWriteIndex;
//
//        for(int i = MainActivity.eogLastWriteIndex; i < eogSize; i++){                                           //Build temp arrays
//            mEogDataTimeWrite.add(mEogDataTime.get(i));                                             //to log last chunk of
//            mEogDataWrite.add(mEogData.get(i));                                                     //data since last call
//        }
//
//        //Uses OutputStreamWriter to print a csv format data set of type <timestamp>,<data>
//        try {
//            OutputStreamWriter outputStreamWriter =
//                    new OutputStreamWriter(new FileOutputStream(new File(fullDir, filename)));
//            for(int i = 0; i < eogSizeRel; i++){
//                String streamLine;
//                streamLine = mEogDataTime.get(i) + "," + mEogDataWrite.get(i) + "\n";
//                //Log.i("STORAGE", streamLine + "iteration:" + String.valueOf(i));
//                outputStreamWriter.write(streamLine);
//
//            }
//            outputStreamWriter.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.i("STORAGE", "EOG data not successfully saved");
//            return false;
//        }
//        MainActivity.eogLastWriteIndex = eogSize;
//        Log.i("STORAGE", "EOG data saved");
//        return true;
//    }
//
//    public File getFullDir() {
//        // Get the directory for the user's public pictures directory.
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "/eog/");
//
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//                Log.i("STORAGE", "created " + file.getPath());}
//            catch (IOException e){e.printStackTrace();}
//        }
//        return file;
//    }
//
//}
