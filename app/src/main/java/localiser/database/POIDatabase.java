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


    public List<Tuple<Double,PointOfInterest>> getClosestPOI(Coordinates c)
    {
        List<Tuple<Double,PointOfInterest>> closest = new LinkedList<>();
        for(PointOfInterest poi: this)
        {
            closest.add(new Tuple<Double, PointOfInterest>(c.distance(poi.coordinates),poi));
        }

        //sort by distance
        Collections.sort(closest, new Comparator<Tuple<Double, PointOfInterest>>() {
            @Override
            public int compare(Tuple<Double, PointOfInterest> lhs, Tuple<Double, PointOfInterest> rhs) {
                return lhs.first.compareTo(rhs.first);
            }
        });

        return closest;
    }
    public List<Tuple<Double,PointOfInterest>> getClosestPOI(Coordinates c, int num)
    {
        List<Tuple<Double,PointOfInterest>> closest = this.getClosestPOI(c);

        //trim N
        if(closest.size()>num)
            return closest.subList(0,num);
        else
            return closest;
    }
}