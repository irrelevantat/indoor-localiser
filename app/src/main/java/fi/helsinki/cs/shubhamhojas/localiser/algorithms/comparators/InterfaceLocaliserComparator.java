package fi.helsinki.cs.shubhamhojas.localiser.algorithms.comparators;
import fi.helsinki.cs.shubhamhojas.localiser.units.Fingerprint;

/**
 * Created by sebastian on 05/01/16.
 */
public interface InterfaceLocaliserComparator {
    double similarity(Fingerprint p1, Fingerprint p2);

    /*
    TODO Comparators I would still like to implement:
        - Jaccard index
        - SimRank
        - Sørensen–Dice
        - Hamming Distance

        Reference to an implementation of string comparison algorithms: https://github.com/rrice/java-string-similarity/tree/master/src/test/java/net/ricecode/similarity
     */
}
