package localiser.algorithms.comparators;

import localiser.database.Fingerprint;

/**
 * Created by sebastian on 05/01/16.
 */
public interface InterfaceLocaliserComparator {
    double similarity(Fingerprint p1, Fingerprint p2);

    /*
    TODO Comparators I would like to implement:
        - Jaccard index
        - SimRank
        - Sørensen–Dice
        - Hamming Distance

        Implemented algorithms for string comparison: https://github.com/rrice/java-string-similarity/tree/master/src/test/java/net/ricecode/similarity
     */
}
