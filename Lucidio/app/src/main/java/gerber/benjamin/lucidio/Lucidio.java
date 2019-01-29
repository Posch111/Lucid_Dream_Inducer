package gerber.benjamin.lucidio;


import android.app.Application;

public class Lucidio extends Application {

    public BLEService BluetoothService;

    @Override
    public void onCreate()
    {
        super.onCreate();
        BluetoothService = new BLEService();
    }

}
