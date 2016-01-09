package fi.helsinki.cs.shubhamhojas.localiser.algorithms.comparators;

import java.util.HashSet;
import java.util.Set;

import fi.helsinki.cs.shubhamhojas.localiser.units.Fingerprint;


/**
 * Created by sebastian on 05/01/16.
 */
public class CosineComparator implements InterfaceLocaliserComparator{

    /*
    COSINE similarity based on this stackoverflow answer
    http://stackoverflow.com/questions/3622112/vector-space-model-algorithm-in-java-to-get-the-similarity-score-between-two-peo
     */
    public double similarity(Fingerprint p1, Fingerprint p2)
    {
        Set<String> both = new HashSet(p1.getKeySet());
        both.retainAll(p2.getKeySet());

        double sclar = 0, norm1 = 0, norm2 = 0;
        for (String k : both) sclar += p1.getLevel(k) * p2.getLevel(k);
        for (String k : p1.getKeySet()) norm1 += p1.getLevel(k) * p1.getLevel(k);
        for (String k : p2.getKeySet()) norm2 += p2.getLevel(k) * p2.getLevel(k);
        return sclar / Math.sqrt(norm1 * norm2);

    }

}
