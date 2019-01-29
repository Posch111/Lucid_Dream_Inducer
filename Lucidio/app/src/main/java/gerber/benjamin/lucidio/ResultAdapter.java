/*
 * POSSIBLY OBSOLETE OR FOR LATER USE
 *
 */




package gerber.benjamin.lucidio;

import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben on 2/24/18.
 */

public class ResultAdapter extends BaseAdapter {

    Context context;
    List<String> deviceList  = new ArrayList<String>();
    LayoutInflater inflter;

    public ResultAdapter(Context applicationContext, List<String> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.fragment_home, null);
        TextView device = (TextView) view.findViewById(R.id.scanResults);
        device.setText(deviceList.get(i));
        return view;
    }

}
