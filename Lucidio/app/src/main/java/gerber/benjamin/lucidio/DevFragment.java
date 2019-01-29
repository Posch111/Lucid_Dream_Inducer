package gerber.benjamin.lucidio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;

/**
 * Created by ben on 1/23/18.
 */

public class DevFragment extends Fragment implements View.OnClickListener{

    private int ledValue;
    private int tapCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dev, container, false);

        //LED Seekbar
        SeekBar seekBar = (SeekBar) v.findViewById(R.id.led_seek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ledValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Initializing buttons
        final ToggleButton toggleButton = (ToggleButton) v.findViewById(R.id.motor_butt);
        final ToggleButton toggleButton1 = (ToggleButton) v.findViewById(R.id.toggle_led_3_butt);
        final ToggleButton toggleButton2 = (ToggleButton) v.findViewById(R.id.toggle_led_4_butt);
        Button button2 = (Button) v.findViewById(R.id.set_led_butt);


        //Initiate the motor toggle button
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                byte data[] = null;
                if (isChecked){
                    ((MainActivity)getActivity()).settingsBytes[0] = (byte) 1;
                    Log.i("CMD", "Motor On");
                } else {
                    ((MainActivity)getActivity()).settingsBytes[0] = (byte) 0;
                    Log.i("CMD", "Motor Off");
                }
            }
        });


        //Initiate the LED toggle button
        toggleButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                byte data[] = null;
                if (isChecked) {
                    // The toggle is disabled
                    ((MainActivity)getActivity()).settingsBytes[1] = (byte) 1;
                    Log.i("CMD", "LED 3 On");
                } else {
                    // The toggle is disabled
                    ((MainActivity)getActivity()).settingsBytes[1] = (byte) 0;
                    Log.i("CMD", "LED 3 Off");
                }
            }
        });

        //Initiate the LED toggle button
        toggleButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                byte data[] = null;
                if (isChecked) {
                    // The toggle is disabled
                    ((MainActivity)getActivity()).settingsBytes[2] = (byte) 1;
                    Log.i("CMD", "LED 4 On");
                } else {
                    // The toggle is disabled
                    ((MainActivity)getActivity()).settingsBytes[2] = (byte) 0;
                    Log.i("CMD", "LED 4 Off");
                }
            }
        });

        //Initialize Dev buttons
        button2.setOnClickListener(this);
        final Button button3 = (Button) v.findViewById(R.id.stop_rem_butt);
        final Button button4 = (Button) v.findViewById(R.id.start_rem_butt);
        final Button button5 = (Button) v.findViewById(R.id.start_sleep_butt);
        final Button button7 = (Button) v.findViewById(R.id.button_default);
        final Button button8 = (Button) v.findViewById(R.id.stop_sleep_butt);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);

        //Initiate Dev button visibility check
        Button button6 = (Button) v.findViewById(R.id.butten_dev);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapCount++;
                if(tapCount > 2) {
                    button3.setVisibility(View.VISIBLE);
                    button4.setVisibility(View.VISIBLE);
                    button5.setVisibility(View.VISIBLE);
                    button7.setVisibility(View.VISIBLE);
                    button8.setVisibility(View.VISIBLE);
                }
            }
        });

        seekBar.setProgress((int)((MainActivity)getActivity()).settingsBytes[3]);
        if((int)(((MainActivity)getActivity()).settingsBytes[0]) == 1){
            toggleButton.setChecked(true);
        }
        if((int)(((MainActivity)getActivity()).settingsBytes[1]) == 1){
            toggleButton1.setChecked(true);
        }
        if((int)(((MainActivity)getActivity()).settingsBytes[2]) == 1){
            toggleButton2.setChecked(true);
        }

        return v;
    }

    //Created a click listener for all of the buttons
    @Override
    public void onClick(View view) {
        byte data[] = null;
        switch (view.getId()) {
            case (R.id.stop_rem_butt):                          //Stop REM Command
                data = new byte[]{(byte) 73};
                ((MainActivity) getActivity()).bleService.writeData(data);
                ((MainActivity)getActivity()).bleService.sleeping = false;
                break;

            case (R.id.set_led_butt):
                ((MainActivity)getActivity()).settingsBytes[3] = (byte) ledValue;                   //LED Value 0-100
                ((MainActivity)getActivity()).applyBrightness();
                data = new byte[]{(byte) 99};
                try{
                    Thread.sleep(200);
                    ((MainActivity)getActivity()).bleService.writeData(data);
                }catch (Exception e){return;}
                break;  

            case (R.id.start_rem_butt):
                data = new byte[]{(byte) 74};                   //Button to kick us into REM sleep
                ((MainActivity) getActivity()).bleService.writeData(data);
                break;

            case (R.id.start_sleep_butt):                       //Sleep Button
                data = new byte[]{(byte) 73};
                ((MainActivity)getActivity()).bleService.writeData(data);
                ((MainActivity)getActivity()).bleService.sleeping = true;
                break;

            case (R.id.stop_sleep_butt):
                data = new byte[]{(byte) 76};
                ((MainActivity)getActivity()).bleService.writeData(data);
                ((MainActivity)getActivity()).bleService.sleeping = false;
                break;

            case(R.id.button_default):                          //Reset to Default Settings
                ((MainActivity)getActivity()).firstTimeFlag = true;
                ((MainActivity)getActivity()).settingsBytes = new byte[] {(byte)0, (byte)0, (byte)0, (byte)50, };
        }
    }

}