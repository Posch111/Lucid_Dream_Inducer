package gerber.benjamin.lucidio;

/**
 * Created by ben on 3/23/18.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben on 1/23/18.
 */

public class RetainFragment extends Fragment {

    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    public static List<BluetoothDevice> mDeviceList = new ArrayList<>();
    public static BluetoothDevice device;
    public static ArrayList<Integer> mEogData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_retain, container, false);
    }
}