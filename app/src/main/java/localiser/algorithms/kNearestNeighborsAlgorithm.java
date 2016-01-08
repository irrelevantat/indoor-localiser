package localiser.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import localiser.algorithms.comparators.InterfaceLocaliserComparator;
import localiser.units.Coordinates;
import localiser.database.FingerprintDatabase;
import localiser.units.Fingerprint;
import localiser.units.Tuple;

/**
 * Created by sebastian on 05/01/16.
 */
public class kNearestNeighborsAlgorithm extends NearestNeighborAlgorithm {

    protected final int K = 4;

    public kNearestNeighborsAlgorithm(InterfaceLocaliserComparator comp) {
        super(comp);
    }


    protected List<Tuple<Double, Fingerprint>> getClosestFingerprints(Fingerprint p, FingerprintDatabase db, Integer max)
    {
        ArrayList<Tuple<Double, Fingerprint>> closestPoints = new ArrayList<>();

        //calculate differences for all points
        for(int i = 0; i<db.size(); i++)
        {
            closestPoints.add(new Tuple<Double,Fingerprint>(new Double(comp.similarity(p, db.get(i))),db.get(i)));
        }

        //get K closest points
        Collections.sort(closestPoints, new Comparator<Tuple<Double, Fingerprint>>() {
            @Override
            public int compare(Tuple<Double, Fingerprint> lhs, Tuple<Double, Fingerprint> rhs) {
                return lhs.first.compareTo(rhs.first);
            }
        });
        Collections.reverse(closestPoints);

        if(max > closestPoints.size())
            return closestPoints;
        else
            return closestPoints.subList(0, max.intValue());

    }

    protected Coordinates getLocation(List<Tuple<Double, Fingerprint>> closestPoints)
    {
        double x = 0,y = 0,z = 0;
        double sumWeights = 0;

        //mix those closest together
        for(int i = 0; i<closestPoints.size() && i<K; i++)
        {
            Tuple<Double, Fingerprint> t = closestPoints.get(i);

            x += t.second.getCoordinates().x * t.first;
            y += t.second.getCoordinates().y * t.first;
            z += t.second.getCoordinates().z * t.first;

            sumWeights += t.first;
        }
        x /= sumWeights;
        y /= sumWeights;
        z /= sumWeights;

        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
            return null;
        }

        return new Coordinates((int)x,(int)y,(int)z);
    }

    public Coordinates getLocation(Fingerprint p, FingerprintDatabase db){

        List<Tuple<Double, Fingerprint>> closestPoints = this.getClosestFingerprints(p,db,K);
        return this.getLocation(closestPoints);

    }

}
