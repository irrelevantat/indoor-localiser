package localiser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import localiser.algorithms.AbstractLocaliserAlgorithm;
import localiser.database.FingerprintDatabase;
import localiser.database.Fingerprint;
import localiser.database.POIDatabase;
import localiser.units.Coordinates;
import localiser.units.PointOfInterest;

/**
 * Created by sebastian on 26/12/15.
 */
public class LocaliserController extends BroadcastReceiver
{

    public class NoWIFIException extends Exception{
        public NoWIFIException(String exc){
            super(exc);
        }
    };

    public interface Callback{
        public void locationUpdated(Coordinates c);
    }


    private final AbstractLocaliserAlgorithm algorithm;
    private final Set<Callback> callbacks = new HashSet<>();
    private final FingerprintDatabase db_finger;
    private final POIDatabase db_poi;
    private final WifiManager wifiManager;

    private Callback callback;

    public LocaliserController(AbstractLocaliserAlgorithm algorithm, Context c) throws NoWIFIException, IOException {

        this.db_finger = new FingerprintDatabase(c);
        this.db_poi = new POIDatabase(c);

        this.wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);

        //check if WIFI is enabled and whether scanning launched
        if(!wifiManager.isWifiEnabled() || !wifiManager.startScan())
        {
            throw new NoWIFIException("WIFI is not enabled");
        }

        System.out.println(wifiManager.getScanResults());
        this.algorithm = algorithm;
    }

    public void registerForLocationUpdates(final Callback callback)
    {
        this.callbacks.add(callback);
    }
    public void unregisterForLocationUpdates(final Callback callback){
        this.callbacks.remove(callback);
    }

    private void locationUpdated(Coordinates c) {
        //get new location updates
        for (Callback ca : this.callbacks) {
            ca.locationUpdated(c);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.locationUpdated(this.algorithm.getLocation(Fingerprint.fromScanResult(wifiManager.getScanResults()), db_finger));
    }
}
