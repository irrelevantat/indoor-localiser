package localiser.database;

import android.net.wifi.ScanResult;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import localiser.units.Coordinates;

/**
 * Created by sebastian on 05/01/16.
 */

public class Fingerprint implements Iterable<String>{


    public static Fingerprint fromDBString(String fingerprint)
    {
        Fingerprint f = new Fingerprint();

        String[] split = fingerprint.split(";");
        //at least three coordinates
        assert(split.length>=3);
        //needs to be uneven: 3cordinates + 2 * (UDDI,signal)
        assert(split.length%2 == 1);
        f.c = new Coordinates(Float.valueOf(split[0]),Float.valueOf(split[1]),Float.valueOf(split[2]));

        for(int i = 3; i<(split.length-1); i+=2)
        {
            String BSSID = split[i];
            String signalStrength = split[i+1];
            f.accessPoints.put(BSSID, Integer.valueOf(signalStrength));
        }
        return f;
    }
    public static Fingerprint fromScanResult(List<ScanResult> results)
    {
        Fingerprint f = new Fingerprint();
        f.c = null;
        for(ScanResult sr: results)
        {
            f.accessPoints.put(sr.BSSID, new Integer(sr.level));
        }
        return f;
    }
    public static Fingerprint fromScanResult(String results)
    {
        Fingerprint f = new Fingerprint();

        String[] split = results.split(";");
        //at least three coordinates
        assert(split.length>=2);
        //needs to be uneven:  2 * (UDDI,signal)
        assert(split.length%2 == 0);

        for(int i = 0; i<(split.length-1); i+=2)
        {
            String BSSID = split[i];
            String signalStrength = split[i+1];
            f.accessPoints.put(BSSID, Integer.valueOf(signalStrength));
        }
        return f;
    }


    private Coordinates c;
    private HashMap<String,Integer> accessPoints = new LinkedHashMap<>();


    public Fingerprint(){}

    public int size() {
        return this.accessPoints.size();
    }
    public Integer getLevel(String BSSID)
    {
        return this.accessPoints.get(BSSID);
    }


    public Coordinates getCoordinates()
    {
        return this.c;
    }

    @Override
    public Iterator<String> iterator() {
        return this.accessPoints.keySet().iterator();
    }

    public Set<Map.Entry<String, Integer>> getEntrySet(){ return this.accessPoints.entrySet(); }
    public Set<String> getKeySet(){ return this.accessPoints.keySet(); }

}
