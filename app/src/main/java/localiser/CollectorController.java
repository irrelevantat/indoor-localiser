package localiser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import localiser.units.Coordinates;
import localiser.units.Fingerprint;
import localiser.units.Tuple;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sebastian on 08/01/16.
 */
public class CollectorController extends LocaliserController {

    public final List<Coordinates> path = new ArrayList<>();
    private final Stack<Fingerprint> localisedPoints = new Stack<>();



    private Date startDate;
    private Coordinates startCoordinate;
    private final Stack<Tuple<Date,Fingerprint>> nonlocalisedPoints = new Stack<>();

    public CollectorController(Context c) throws NoWIFIException, IOException {
        super(null, c);
        c.registerReceiver(CollectorController.this, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }


    public void addToPath(int x, int y, int z)
    {
        path.add(new Coordinates(x, y, z));
    }

    public void startRecording(Coordinates src)
    {
        c.registerReceiver(CollectorController.this, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        this.startCoordinate = src;
        this.startDate = new Date();
        this.nonlocalisedPoints.empty();
    }

    public List<Coordinates> stopRecording(Coordinates end)
    {
        Date c_endDate = new Date();
        Date c_startDate = this.startDate;
        long timeDiff = c_endDate.getTime() - c_startDate.getTime();

        Tuple<Date,Fingerprint> result;
        List<Coordinates> addedPoints = new LinkedList<>();

        System.out.println(String.format("%f: %d - %d", 1.0, (int) end.x, (int) end.y));

        while(!nonlocalisedPoints.isEmpty())
        {
            result = nonlocalisedPoints.pop();
            long timeElapsed = result.first.getTime() - c_startDate.getTime();
            double percentage = (double) timeElapsed / (double) timeDiff;

            double x = (end.x - startCoordinate.x) * percentage + startCoordinate.x;
            double y = (end.y - startCoordinate.y) * percentage + startCoordinate.y;

            System.out.println(String.format("%f: %d - %d", (float) percentage, (int) x, (int) y));
            Coordinates c = new Coordinates((float)x,(float)y,startCoordinate.z);
            result.second.setCoordinates(c);

            addedPoints.add(c);
            localisedPoints.add(result.second);
        }

        System.out.println(String.format("%f: %d - %d", 0.0, (int) startCoordinate.x, (int) startCoordinate.y));


        //reset
        this.startDate = null;

        return addedPoints;
    }

    public void savePoints()
    {
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            sdCard.mkdirs();
            File file = new File(sdCard,"output.db");
            FileOutputStream fos = new FileOutputStream(file, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);

            for(Fingerprint f: localisedPoints)
            {
                String s = f.toDatabaseString();
                outputStreamWriter.append(s+"\n");
            }

            outputStreamWriter.close();
            System.out.println("Saved " + localisedPoints.size() + " fingerprints.");
            localisedPoints.clear();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(startDate!=null)
        {
            System.out.println("Received WIFI data");
            List<ScanResult> result = wifiManager.getScanResults();
            Fingerprint f = Fingerprint.fromScanResult(result);
            nonlocalisedPoints.push(new Tuple<Date, Fingerprint>(new Date(), f));

        }
        wifiManager.startScan();

    }



}
