package gerber.benjamin.lucidio;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BleFragment extends Fragment implements View.OnClickListener{

    private MainActivity activity;
    private BLEService bleService;
    private ProgressBar scanProgressBar;
    private RecyclerView scanResultsView;
    public Fragment dev = new DevFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity)getActivity();
        bleService = activity.bleService;
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ble, container, false);

        //Initializing texts, Progress Bar, and List
        scanResultsView = v.findViewById(R.id.scanResultsView);
        LinearLayoutManager listManager = new LinearLayoutManager(activity);
        scanResultsView.setLayoutManager(listManager);
        scanProgressBar = v.findViewById(R.id.scanProgressBar);
        scanProgressBar.setVisibility(View.INVISIBLE);
        scanResultsView.setAdapter(bleService.leDeviceListAdapter);
        scanResultsView.setVisibility(View.INVISIBLE);

        //Initializing button
        Button button = v.findViewById(R.id.button_scan);
        Button button1 = v.findViewById(R.id.button_disconnect);
        button.setOnClickListener(this);
        button1.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.button_scan):
                //Initiates Scan and makes indeterminate progressbar visible
                Log.i("BT", "Starting Bluetooth Scan");
                scanResultsView.setVisibility(View.VISIBLE);
                scanProgressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Scanning", Toast.LENGTH_SHORT).show();

                bleService.scanLE(true);

                break;
            case (R.id.button_disconnect):
                bleService.disconnect();
                break;
        }
    }
}