
package gerber.benjamin.lucidio;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class BLEDeviceListAdapter extends RecyclerView.Adapter<BLEDeviceListAdapter.DeviceViewHolder> {

    private List<BluetoothDevice> deviceList;
    private BLEService bleService;

    public BLEDeviceListAdapter(BLEService bleService, List<BluetoothDevice> deviceList) {
        this.deviceList = deviceList;
        this.bleService = bleService;
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ViewGroup view;

        DeviceViewHolder(TextView textview, ViewGroup parent) {
            super(textview);
            this.textView = textview;
            this.view = parent;
        }
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new DeviceViewHolder(v, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final DeviceViewHolder deviceViewHolder, int i) {
        deviceViewHolder.textView.setText(deviceList.get(i).getName());
        final int position = deviceViewHolder.getAdapterPosition();
        deviceViewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleService.connectToDevice(deviceList.get(position));
                Log.d("recycler", "CLICKED " + deviceList.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(deviceList != null)
            return deviceList.size();
        else return 0;
    }
}
