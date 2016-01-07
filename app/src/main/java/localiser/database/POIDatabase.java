package localiser.database;

import android.content.Context;

import java.io.IOException;

import hk.ust.cse.com107x.indoor.R;
import localiser.units.PointOfInterest;

/**
 * Created by sebastian on 05/01/16.
 */
public class POIDatabase  extends AbstractDatabase<PointOfInterest> {

    public static final double METERS_PER_PIXEL = 7.0/271.0;

    public POIDatabase(Context c, int resource) throws IOException {
        super(c,resource);
    }
    public POIDatabase(Context c) throws IOException {
        super(c, R.raw.poi);
    }

    @Override
    protected void readLine(String line) {
        collection.add(PointOfInterest.fromDBString(line));
    }


}