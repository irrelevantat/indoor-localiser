package localiser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by sebastian on 27/12/15.
 */
public abstract class LocaliserAlgorithm extends BroadcastReceiver {

    public class Coordinates
    {
        public int x,y,z;
        public Coordinates(int x, int y, int z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
    public interface Callback
    {
        public void locationUpdated(Coordinates c);
    }


    private Callback callback;
    private WifiManager wifiManager;

    public LocaliserAlgorithm(Context c)
    {
        //TODO make sure WIFI is enabled before that
        this.wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        assert(wifiManager.startScan()==true);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (callback != null){
             callback.locationUpdated(this.getLocation(wifiManager.getScanResults()));
        }
    }

    protected abstract Coordinates getLocation(List<ScanResult> sr);

    public void getLocation(Callback c)
    {
        wifiManager.startScan();
        this.callback = c;
    }



}
