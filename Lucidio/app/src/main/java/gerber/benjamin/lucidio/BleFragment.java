package gerber.benjamin.lucidio;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;


/**
 * Created by ben on 1/12/18.
 *
 * This fragment contains the Homepage which contains limited functionality
 *
 *
 *
 */

public class BleFragment extends Fragment implements View.OnClickListener{

    private ListView listView;
    private ArrayList<String> resultList;
    private ArrayAdapter mDeviceListAdapter;
    private Handler mHandler;
    private ProgressBar scanProgressBar;
    public Fragment dev = new DevFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ble, container, false);
        mHandler = new Handler();

        //Initializing texts, Progress Bar, and List
        final ListView listView = (ListView) v.findViewById(R.id.scanResults);
        scanProgressBar = (ProgressBar) v.findViewById(R.id.scanProgressBar);
        scanProgressBar.setVisibility(View.INVISIBLE);
        resultList  = new ArrayList<String>();
        mDeviceListAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, resultList); //creates list adapter for viewing
        listView.setAdapter(mDeviceListAdapter);
        //Makes the list clickable and parses out device address
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String info = null;
                boolean clicked = false;
                Object deviceSelect = listView.getItemAtPosition(position);
                if(deviceSelect.toString() == info)
                    clicked = !clicked;
                if(clicked == true)
                    ((MainActivity)getActivity()).bleService.mGatt.disconnect();
                else
                    info = deviceSelect.toString();
                String address = info.substring(info.length() - 17);

                for(int i = 0; i < ((MainActivity)getActivity()).mDeviceList.size(); i++){
                    if(((MainActivity)getActivity()).mDeviceList.get(i).toString().contains(address)) {
                        ((MainActivity)getActivity()).device = ((MainActivity)getActivity()).mDeviceList.get(i);
                        Log.i("Device", ((MainActivity)getActivity()).device.toString());
                    }
                }
                ((MainActivity)getActivity()).bleService.connectToDevice(((MainActivity)getActivity()).device);

            }
        });


        //Initializing button
        Button button = (Button) v.findViewById(R.id.button_scan);
        Button button1 = (Button) v.findViewById(R.id.button_disconnect);
        button.setOnClickListener(this);
        button1.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.button_scan):
                //Clears scan result list
                resultList.clear();
                ((MainActivity)getActivity()).device = null;

                //Initiates Scan and makes indeterminate progressbar visible
                Log.i("BT", "Starting Bluetooth Scan");
                Toast.makeText(getActivity(), "Scanning", Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).scanLeDevice(true);
                scanProgressBar.setVisibility(View.VISIBLE);

                /*This code uses the Handler to create a new thread while being able to retain view
                 *compatability as opposed to a new thread. */
                Runnable r = new Runnable() {
                    @Override
                    public void run(){
                        //Creates and displays device list
                        if(((MainActivity)getActivity()).mDeviceList != null) {
                            for (int i = 0; i < ((MainActivity)getActivity()).mDeviceList.size(); i++){
                                //Log.i("Device ", "Address:"+ MainActivity.mDeviceList.get(i).getAddress()
                                //+ " Name:" + MainActivity.mDeviceList.get(i).getName()); //Logs the device list names
                                mDeviceListAdapter.add(((MainActivity)getActivity()).mDeviceList.get(i).getName() +
                                        "\n" + ((MainActivity)getActivity()).mDeviceList.get(i).getAddress()); //Adds the device list to the list adapter
                            }
                        }
                        mDeviceListAdapter.notifyDataSetChanged(); //Update List
                        scanProgressBar.setVisibility(View.INVISIBLE); //Make ProgressBar hidden again
                    }
                };
                mHandler.postDelayed(r, 3000); //Delays runnable by 3 seconds
                break;

            case (R.id.button_disconnect):
                if(((MainActivity)getActivity()).bleService.mGatt != null) {
                    ((MainActivity) getActivity()).bleService.disconnectDevice();
                }
                break;


        }
    }

}
