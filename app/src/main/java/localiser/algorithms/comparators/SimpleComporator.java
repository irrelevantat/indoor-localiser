package localiser.algorithms.comparators;

import localiser.database.Fingerprint;

/**
 * Created by sebastian on 05/01/16.
 */
public class SimpleComporator implements InterfaceLocaliserComparator{

    public double similarity(Fingerprint p1, Fingerprint p2)
    {
        double similarity = 0;
        int count = 0;

        for(String BSSID: p1)
        {
            //both fingerprints have observed
            if(p2.getLevel(BSSID)!=null)
            {
                // calculate -40/-80 = 0.5

                similarity += Math.max(p1.getLevel(BSSID), p2.getLevel(BSSID)) / Math.min(p1.getLevel(BSSID),p2.getLevel(BSSID));
                count++;
            }
            //no penalty
            else{}

        }
        //calculate average similarity of overlapping
        return similarity/(double)count;
    }
}
