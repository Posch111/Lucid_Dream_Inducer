package gerber.benjamin.lucidio;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.Time;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class SleepFragment extends Fragment implements View.OnClickListener {

    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private LineGraphSeries<DataPoint> mSeries;
    private MainActivity activity;
    private BLEService bleService;
    Calendar calendar = Calendar.getInstance();

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

        //Initializes an dummy array for the creation of the graph (array will be offset by 1920)
//        for (int i = 0; i < 1920; i++) {
//            MainActivity.currentLocalTime.getTime();
//            activity.mEogDataTime.add(System.currentTimeMillis());
//            activity.mEogData.add(i, 150);
//        }
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

        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(1920);

        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-250);
        graph.getViewport().setMaxY(250);
        graph.getViewport().setScalable(true);
        //Buttons
        final Button button = v.findViewById(R.id.end_sleep_butt);
        button.setOnClickListener(this);

        activity.resetEOGData();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.findViewById(R.id.sleep_butt).setVisibility(View.INVISIBLE);
        if (activity.mEogData.size() == 0) {
            activity.mEogData.add(0);
            activity.mEogDataTime.add((long) 0);
        }

        mTimer = new Runnable() {
            @Override
            public void run() {
                mSeries.appendData(new DataPoint(activity.mEogDataTime.get(
                        activity.mEogDataTime.size() - 1),
                        activity.mEogData.get(
                                activity.mEogData.size() - 1)), true, 1920);
                mHandler.postDelayed(this, 100);
            }
        };
        mHandler.postDelayed(mTimer, 1000);

        new Thread(new dataSaveRunnable()).start();
        //new Thread(new dataReceiveRunnable()).start();
        //new Thread(new cmdRunnable()).start();
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
        public void run() {
            while (BLEService.sleeping) {

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
