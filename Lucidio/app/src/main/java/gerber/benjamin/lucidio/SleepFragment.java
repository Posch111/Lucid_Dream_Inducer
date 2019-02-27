package gerber.benjamin.lucidio;

import android.content.Context;
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

public class SleepFragment extends Fragment implements View.OnClickListener{


    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private LineGraphSeries<DataPoint> mSeries;

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

        if (MainActivity.device == null) {
            Toast.makeText(getActivity(), "Bluetooth Not Connected", Toast.LENGTH_SHORT).show();
            SleepActivity.cmdrun = false;
            SleepActivity.datarun = false;
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            Fragment frag = new BleFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
            transaction.commit(); // commit the changes
        }

        //Initializes an dummy array for the creation of the graph (array will be offset by 1920)
        for (int i = 0; i < 1920; i++) {
            MainActivity.currentLocalTime.getTime();
            MainActivity.mEogDataTime.add(System.currentTimeMillis());
            MainActivity.mEogData.add(i, 150);
        }
        //Initialize data streaming
        BLEService.sleeping = true;

        //Initializes second graphview
        GraphView graph = (GraphView) v.findViewById(R.id.sleep_graph_rt);
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
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(250);

        //BUttons
        final Button button = (Button) v.findViewById(R.id.end_sleep_butt);
        button.setOnClickListener(this);

        SleepActivity.cmdrun = true;
        SleepActivity.datarun = true;

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        mTimer = new Runnable() {
            @Override
            public void run() {
                mSeries.appendData(new DataPoint(((MainActivity) getActivity()).mEogDataTime.get(
                        ((MainActivity) getActivity()).mEogDataTime.size() - 1),
                        ((MainActivity) getActivity()).mEogData.get(
                                ((MainActivity) getActivity()).mEogData.size() - 1)), true, 1920);
                mHandler.postDelayed(this, 100);
            }
        };
        mHandler.postDelayed(mTimer, 1000);

            new Thread(new dataRunnable()).start();
            new Thread(new cmdRunnable()).start();

    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer);
        super.onPause();
    }

    //Created a click listener for all of the buttons
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case (R.id.end_sleep_butt):
                SleepActivity.cmdrun = false;
                SleepActivity.datarun = false;
                byte[] data = new byte[] {(byte)75};
                ((MainActivity)getActivity()).bleService.writeData(data);
                try{
                    Thread.sleep(150);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                data[0] = (byte)76;
                ((MainActivity)getActivity()).bleService.writeData(data);
                BLEService.sleeping = false;
                Fragment frag = new DataFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
                transaction.commit(); // commit the changes
            break;
        }
    }

    /*
     * Separate Thread tasks
     */

    public class dataRunnable implements Runnable {
        public void run(){
            while (SleepActivity.datarun) {
                while (!Thread.currentThread().isInterrupted()) {
                    ((MainActivity) getActivity()).saveEogData();
                    try{
                        Thread.sleep(5000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public class cmdRunnable implements Runnable {
        public void run() {
            while (SleepActivity.cmdrun) {
                while (!Thread.currentThread().isInterrupted()) {
                    // do something in the loop
                    try {
                        ((MainActivity) getActivity()).bleService.writeData(motorOn);
                        Thread.sleep(333);
                        ((MainActivity) getActivity()).bleService.writeData(led3On);
                        Thread.sleep(333);
                        ((MainActivity) getActivity()).bleService.writeData(led4On);
                        Thread.sleep(333);
                        ((MainActivity) getActivity()).bleService.writeData(motorOff);
                        Thread.sleep(333);
                        ((MainActivity) getActivity()).bleService.writeData(led3Off);
                        Thread.sleep(333);
                        ((MainActivity) getActivity()).bleService.writeData(led4Off);
                        Thread.sleep(333);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}