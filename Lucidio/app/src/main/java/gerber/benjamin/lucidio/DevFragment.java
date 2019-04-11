package gerber.benjamin.lucidio;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
/**
 * Created by ben on 1/23/18.
 */

public class DevFragment extends Fragment implements View.OnClickListener{

    private int ledValue= 50;
    private int tapCount;
    private MainActivity activity;
    private EditText commandText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dev, container, false);
        commandText = v.findViewById(R.id.text_comand);
        Button sendTextButton = v.findViewById(R.id.button_send_command);
        //LED Seekbar
        SeekBar seekBar = v.findViewById(R.id.led_seek);
        seekBar.setMax(50);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ledValue = progress;
                byte[] data = new byte[]{(byte) ledValue};
                activity.bleService.writeMLDP(data);
                Log.i("led brightness", String.valueOf(ledValue));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                byte[] data = new byte[]{(byte) 68};
                activity.bleService.writeMLDP(data);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                byte[] data = new byte[]{(byte) 101};
                activity.bleService.writeMLDP(data);
            }
        });

        //Initializing buttons
        final ToggleButton toggleButton = v.findViewById(R.id.motor_butt);
        final ToggleButton toggleButton2 =  v.findViewById(R.id.toggle_led_4_butt);
        final ToggleButton toggleButton1 =  v.findViewById(R.id.toggle_led_3_butt);
        Button button2 =  v.findViewById(R.id.set_led_butt);

        sendTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = commandText.getText().toString();
                if(!text.isEmpty()){

                    if((text.length() >= 2) &&(text.substring(0,2).equals("SR"))){
                        byte[] command = "SR,".getBytes();
                        String hexString = text.substring(3);
                        byte[] value = MainActivity.decodeHexString(hexString);
                        byte[] message = new byte[command.length + value.length];
                        System.arraycopy(command,0,message,0,command.length);
                        System.arraycopy(value,0,message,command.length,value.length);
                        activity.bleService.writeMLDP(message);

                    }
                    activity.bleService.writeMLDP(commandText.getText().toString().getBytes());
                }

            }
        });

        //Initiate the motor toggle button
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                byte data[] = null;
                if (isChecked){
                    MainActivity.settingsBytes[0] = (byte) 1;
                    Log.i("CMD", "Motor On");
                } else {
                    MainActivity.settingsBytes[0] = (byte) 0;
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
                    MainActivity.settingsBytes[1] = (byte) 1;

                    byte[] ledEnableCommand = new byte[] {77};
                    try{
                        activity.bleService.writeMLDP(ledEnableCommand);
                        Thread.sleep(120); //Extra delay for sending LED value after receive cmd
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                    Log.i("CMD", "LED 3 On");

                } else {
                    // The toggle is disabled
                    MainActivity.settingsBytes[1] = (byte) 0;
                    Log.i("CMD", "LED 3 Off");
                    byte[] ledEnableCommand = new byte[] {77};
                    try{
                        activity.bleService.writeMLDP(ledEnableCommand);
                        Thread.sleep(120); //Extra delay for sending LED value after receive cmd
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        //Initiate the LED toggle button
        toggleButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                byte data[] = null;
                if (isChecked) {
                    // The toggle is disabled
                    MainActivity.settingsBytes[2] = (byte) 1;
                    Log.i("CMD", "LED 4 On");
                } else {
                    // The toggle is disabled
                    MainActivity.settingsBytes[2] = (byte) 0;
                    Log.i("CMD", "LED 4 Off");
                }
            }
        });

        //Initialize Dev buttons
        button2.setOnClickListener(this);
        final Button button3 = v.findViewById(R.id.stop_rem_butt);
        final Button button4 = v.findViewById(R.id.start_rem_butt);
        final Button button5 = v.findViewById(R.id.start_sleep_butt);
        final Button button7 = v.findViewById(R.id.button_test_protocol);
        final Button button8 = v.findViewById(R.id.stop_sleep_butt);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);

        //Initiate Dev button visibility check
        Button button6 = v.findViewById(R.id.butten_dev);
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

        seekBar.setProgress((int)(MainActivity.settingsBytes[3]));
        if((int)(MainActivity.settingsBytes[0]) == 1){
            toggleButton.setChecked(true);
        }
        if((int)(MainActivity.settingsBytes[1]) == 1){
            toggleButton1.setChecked(true);

        }
        if((int)(MainActivity.settingsBytes[2]) == 1){
            toggleButton2.setChecked(true);
        }

        return v;
    }

    //Created a click listener for all of the buttons
    @Override
    public void onClick(View view) {
        byte data[];
        switch (view.getId()) {
            case (R.id.stop_rem_butt):                          //Stop REM Command
                data = new byte[]{(byte) 75};
                activity.bleService.writeMLDP(data);
                BLEService.sleeping = false;
                break;
//
//            case (R.id.set_led_butt):
//                MainActivity.settingsBytes[3] = (byte) ledValue;                   //LED Value 0-100
//                activity.applyBrightness();
//                data = new byte[]{(byte) ledValue};
//                Log.i("ledvalue",String.valueOf(ledValue));
//                try{
//                    Thread.sleep(200);
//                    activity.bleService.writeData(data);
//                }catch (Exception e){return;}
//                break;

            case (R.id.start_rem_butt):
                data = new byte[]{(byte) 74};                   //Button to kick us into REM sleep
                activity.bleService.writeMLDP(data);
                break;

            case (R.id.start_sleep_butt):                       //Sleep Button
                data = new byte[]{(byte) 73};
                activity.bleService.writeMLDP(data);
                BLEService.sleeping = true;
                break;

            case (R.id.stop_sleep_butt):
                data = new byte[]{(byte) 76};
                activity.bleService.writeMLDP(data);
                BLEService.sleeping = false;
                break;

            case(R.id.button_test_protocol):                          //test light protocol
                data = new byte[] {(byte) 66};
                activity.bleService.writeMLDP(data);
        }
    }

}