package localiser.algorithms;

import localiser.algorithms.comparators.InterfaceLocaliserComparator;
import localiser.units.Coordinates;
import localiser.database.FingerprintDatabase;
import localiser.database.Fingerprint;

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
