package localiser.algorithms;

import localiser.algorithms.comparators.InterfaceLocaliserComparator;
import localiser.units.Coordinates;
import localiser.database.FingerprintDatabase;
import localiser.units.Fingerprint;

/**
 * Created by sebastian on 27/12/15.
 */


public abstract class AbstractLocaliserAlgorithm {

    public final InterfaceLocaliserComparator comp;

    public AbstractLocaliserAlgorithm(InterfaceLocaliserComparator comp)
    {
        this.comp = comp;
    }

    public abstract Coordinates getLocation(Fingerprint p, FingerprintDatabase db);



}
