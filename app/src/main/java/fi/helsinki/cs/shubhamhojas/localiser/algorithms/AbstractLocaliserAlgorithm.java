package fi.helsinki.cs.shubhamhojas.localiser.algorithms;

import fi.helsinki.cs.shubhamhojas.localiser.algorithms.comparators.InterfaceLocaliserComparator;
import fi.helsinki.cs.shubhamhojas.localiser.database.FingerprintDatabase;
import fi.helsinki.cs.shubhamhojas.localiser.units.Coordinates;
import fi.helsinki.cs.shubhamhojas.localiser.units.Fingerprint;

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
