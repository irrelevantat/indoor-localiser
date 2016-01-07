package localiser.database;

import android.content.Context;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import hk.ust.cse.com107x.indoor.R;
import localiser.units.Coordinates;
import localiser.units.PointOfInterest;
import localiser.units.Tuple;

/**
 * Created by sebastian on 05/01/16.
 */
public class POIDatabase  extends AbstractDatabase<PointOfInterest> {

    public POIDatabase(Context c, int resource) throws IOException {
        super(c,resource);
    }
    public POIDatabase(Context c) throws IOException {
        super(c, R.raw.test_poi);
    }

    @Override
    protected void readLine(String line) {
        collection.add(PointOfInterest.fromDBString(line));
    }


}