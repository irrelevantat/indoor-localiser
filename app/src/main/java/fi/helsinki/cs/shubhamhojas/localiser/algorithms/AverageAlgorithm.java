package fi.helsinki.cs.shubhamhojas.localiser.algorithms;

import java.util.LinkedList;
import java.util.List;

import fi.helsinki.cs.shubhamhojas.localiser.algorithms.comparators.InterfaceLocaliserComparator;
import fi.helsinki.cs.shubhamhojas.localiser.database.FingerprintDatabase;
import fi.helsinki.cs.shubhamhojas.localiser.units.Coordinates;
import fi.helsinki.cs.shubhamhojas.localiser.units.Fingerprint;


/**
 * Created by sebastian on 07/01/16.
 */
public class AverageAlgorithm extends kNearestNeighborsAlgorithm {


    public AverageAlgorithm(InterfaceLocaliserComparator comp) {
        super(comp);
    }


    private final int AVG = 5;
    private final List<Coordinates> lastPoints = new LinkedList<>();

    private Coordinates avg(List<Coordinates> points)
    {
        float x=0;
        float y=0;
        float z=0;

        for(Coordinates c: points)
        {
            x+= c.x;
            y+= c.y;
            z+= c.z;
        }

        return new Coordinates(x/points.size(),y/points.size(),z/points.size());
    }

    public Coordinates getLocation(Fingerprint p, FingerprintDatabase db){

        Coordinates c = super.getLocation(p,db);
        if(c!=null)
        {
            lastPoints.add(c);
        }

        //algorithm only works if we got more than two coordinates already
        if(lastPoints.size()%AVG==0 && lastPoints.size()>= AVG)
        {
            return avg(lastPoints.subList(lastPoints.size() - AVG, lastPoints.size()));
        }
        return null;

    }


}
