package localiser.algorithms;

import java.util.ArrayList;
import java.util.Collections;

import localiser.algorithms.comparators.InterfaceLocaliserComparator;
import localiser.units.Coordinates;
import localiser.database.FingerprintDatabase;
import localiser.database.Fingerprint;

/**
 * Created by sebastian on 05/01/16.
 */
public class kNearestNeighborsAlgorithm extends NearestNeighborAlgorithm {

    private final int K = 5;

    public kNearestNeighborsAlgorithm(InterfaceLocaliserComparator comp) {
        super(comp);
    }

    public Coordinates getLocation(Fingerprint p, FingerprintDatabase db){


        ArrayList<Double> difference = new ArrayList<>();

        ArrayList<Fingerprint> closestPoints = new ArrayList<>();
        ArrayList<Double> closestPointsDifferences = new ArrayList<>();

        //calculate differences for all points
        for(int i = 0; i<db.size(); i++)
        {
            difference.add(new Double(comp.similarity(p, db.get(i))));
        }
        //get K closest points
        for(int i = 0; i<K && i<difference.size(); i++)
        {
            int indexOfMax = difference.indexOf(Collections.min(difference));
            closestPoints.add(db.get(indexOfMax));
            closestPointsDifferences.add(difference.get(indexOfMax));
            difference.set(indexOfMax, new Double(Double.MAX_VALUE));
        }

        double x = 0,y = 0,z = 0;
        double sumWeights = 0;

        //mix those closest together
        for(int i = 0; i<closestPoints.size(); i++)
        {
            x += closestPoints.get(i).getCoordinates().x * closestPointsDifferences.get(i);
            y += closestPoints.get(i).getCoordinates().y * closestPointsDifferences.get(i);
            z += closestPoints.get(i).getCoordinates().z * closestPointsDifferences.get(i);
            sumWeights += closestPointsDifferences.get(i);
        }
        x /= sumWeights;
        y /= sumWeights;
        z /= sumWeights;

        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
            return null;
        }

        return new Coordinates((int)x,(int)y,(int)z);

    }

}
