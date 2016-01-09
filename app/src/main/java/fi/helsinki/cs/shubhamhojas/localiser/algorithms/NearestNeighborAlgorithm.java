package fi.helsinki.cs.shubhamhojas.localiser.algorithms;

import fi.helsinki.cs.shubhamhojas.localiser.algorithms.comparators.InterfaceLocaliserComparator;
import fi.helsinki.cs.shubhamhojas.localiser.database.FingerprintDatabase;
import fi.helsinki.cs.shubhamhojas.localiser.units.Coordinates;
import fi.helsinki.cs.shubhamhojas.localiser.units.Fingerprint;

/**
 * Created by sebastian on 05/01/16.
 */
public class NearestNeighborAlgorithm extends AbstractLocaliserAlgorithm {

    public NearestNeighborAlgorithm(InterfaceLocaliserComparator comp) {
        super(comp);
    }

    @Override
    public Coordinates getLocation(Fingerprint p, FingerprintDatabase db){

        double maxSimilarity = 0;
        int maxIndex = 0;

        int index;
        for(int counter = 0; counter<db.size(); counter++)
        {
            double similarity = this.comp.similarity(p, db.get(counter));
            if(similarity>maxSimilarity)
            {
                maxIndex = counter;
                maxSimilarity = similarity;
            }
        }

        return  db.get(maxIndex).getCoordinates();
    }

}
