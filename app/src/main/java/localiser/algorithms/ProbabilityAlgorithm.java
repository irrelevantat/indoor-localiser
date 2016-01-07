package localiser.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import localiser.algorithms.comparators.InterfaceLocaliserComparator;
import localiser.database.Fingerprint;
import localiser.database.FingerprintDatabase;
import localiser.units.Coordinates;
import localiser.units.Tuple;

/**
 * Created by sebastian on 07/01/16.
 */
public class ProbabilityAlgorithm extends kNearestNeighborsAlgorithm {


    public ProbabilityAlgorithm(InterfaceLocaliserComparator comp) {
        super(comp);
    }

    private final int DELTA = 1000;
    private final List<Coordinates> queue = new LinkedList<>();


    public Coordinates getLocation(Fingerprint p, FingerprintDatabase db){

        Coordinates c = null;
        final int size = queue.size();
        //algorithm only works if we got more than two coordinates already
        if(size<=2)
        {
            c = super.getLocation(p,db);
        }
        //what if we don't have two similar points?
        else if(queue.get(size-1).distance(queue.get(size-2)) > DELTA)
        {
            c = super.getLocation(p,db);
        }
        else{
            //get twice as much fingerprints
            final List<Tuple<Double, Fingerprint>> closestPoints = this.getClosestFingerprints(p,db,K*2);

            //short cut to the last points
            final Coordinates lastPoint = queue.get(size-1);

            //sort by distance to then filter out unrealistic ones
            Collections.sort(closestPoints, new Comparator<Tuple<Double, Fingerprint>>() {
                @Override
                public int compare(Tuple<Double, Fingerprint> lhs, Tuple<Double, Fingerprint> rhs) {
                    Double distanceLhs = new Double(lhs.second.getCoordinates().distance(lastPoint));
                    Double distanceRhs = new Double(rhs.second.getCoordinates().distance(lastPoint));
                   return distanceLhs.compareTo(distanceRhs);
                }
            });
            //reverse to get closest ones first
            //Collections.reverse(closestPoints);
            c = this.getLocation(closestPoints);
        }

        if(c != null)
            queue.add(c);
        return c;



    }


}
