package gerber.benjamin.lucidio;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by ben on 1/23/18.
 */

public class AlarmFragment extends Fragment{

    TimePicker alarmTimePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_alarm, container, false);

        alarmTimePicker =  v.findViewById(R.id.timePicker);
        return v;
    }

    public void OnToggleClicked(View view)
    {
        long time;
        if (((ToggleButton) view).isChecked())
        {
            Toast.makeText(getContext(), "ALARM ON", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            ((MainActivity)getActivity()).alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

            time=(calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));
            if(System.currentTimeMillis()>time)
            {
                if (calendar.AM_PM == 0)
                    time = time + (1000*60*60*12);
                else
                    time = time + (1000*60*60*24);
            }
            ((MainActivity)getActivity()).alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, ((MainActivity)getActivity()).alarmIntent);
        }
        else
        {
            ((MainActivity)getActivity()).alarmMgr.cancel(((MainActivity)getActivity()).alarmIntent);
            Toast.makeText(getContext(), "ALARM OFF", Toast.LENGTH_SHORT).show();
        }


    }


}
