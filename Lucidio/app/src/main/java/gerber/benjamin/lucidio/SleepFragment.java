package gerber.benjamin.lucidio;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.security.ConfirmationAlreadyPresentingException;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SleepFragment extends Fragment implements View.OnClickListener {
    private int graphIndex = 0;
    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private LineGraphSeries<DataPoint> mSeries;
    private MainActivity activity;
    private BLEService bleService;
    private boolean protocolStarted = false;
    Timer timer = new Timer();
    Calendar calendar;

    //Bluetooth Commands to MSP430 system
    private byte[] motorOn = new byte[]{0x45};
    private byte[] motorOff = new byte[]{0x46};
    private byte[] led3On = new byte[]{0x47};
    private byte[] led3Off = new byte[]{0x48};
    private byte[] led4On = new byte[]{0x4D};
    private byte[] led4Off = new byte[]{0x4E};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sleep, container, false);
        activity = (MainActivity) getActivity();
        bleService = activity.bleService;
        if (bleService.getConnectionState() == BLEService.STATE_DISCONNECTED) {
            Toast.makeText(getActivity(), "Bluetooth Not Connected", Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Fragment frag = new SettingFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
            transaction.commit(); // commit the changes
        }
        //Set up time for protocol timer
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE,0);

        //Initialize data streaming
        BLEService.sleeping = true;

        //Initializes second graphview
        GraphView graph = v.findViewById(R.id.sleep_graph_rt);
        mSeries = new LineGraphSeries<>();
        graph.addSeries(mSeries);

        // custom label formatter to show x value as timestamp
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return MainActivity.timeFormatter.format(value);
                }
                return null;
            }
        });

        graph.getGridLabelRenderer().setNumHorizontalLabels(4);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("EOG Signal");
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(100);

        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-150);
        graph.getViewport().setMaxY(150);
        graph.getViewport().setScalable(true);
        //Buttons
        final Button button = v.findViewById(R.id.end_sleep_butt);
        button.setOnClickListener(this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.findViewById(R.id.sleep_butt).setVisibility(View.INVISIBLE);


        mTimer = new Runnable() {
            @Override
            public void run() {
                if (activity.mEogData.size()>0) {
                    mSeries.appendData(new DataPoint(graphIndex,
                            activity.mEogData.get(
                                    activity.mEogData.size() - 1)), true, 500);

                }
                graphIndex++;
                if(graphIndex > 500){
                    mSeries.resetData(new DataPoint[] {new DataPoint(0,0)});
                    graphIndex = 0;
                }
                mHandler.postDelayed(this, 100);
            }
        };
        mHandler.postDelayed(mTimer, 1000);

        new Thread(new dataSaveRunnable()).start();
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer);
        super.onPause();
    }

    //Created a click listener for all of the buttons
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.end_sleep_butt):
                activity.sendBLECmd(BleCmds.WAKE);
                BLEService.sleeping = false;
                Fragment frag = new SettingFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
                transaction.commit(); // commit the changes
                Toast.makeText(activity, "Good Morning", Toast.LENGTH_SHORT).show();
                activity.findViewById(R.id.sleep_butt).setVisibility(View.VISIBLE);
                break;
        }
    }

    /*
     * Separate Thread tasks
     */

    public class dataSaveRunnable implements Runnable {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void run() {
            while (BLEService.sleeping) {
                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                if(!protocolStarted && (hour <= 7) && (hour >=3)){ //if time is before 7am
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            activity.sendBLECmdOnce('R');
                        }
                    },0,1800000); //every 1800000 ms (30 min)
                }

                try {
                    Thread.sleep(10000);
                    activity.saveEogData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
