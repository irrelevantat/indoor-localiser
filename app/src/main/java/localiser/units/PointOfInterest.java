package localiser.units;

import android.graphics.Point;

import static junit.framework.Assert.assertTrue;

/**
 * Created by sebastian on 05/01/16.
 */
public class PointOfInterest {

    public static PointOfInterest fromDBString(String databaseLine)
    {
        String[] input = databaseLine.split(";");
        assertTrue(input.length == 5);

        String name = input[0];
        Coordinates c = new Coordinates(Float.valueOf(input[1]), Float.valueOf(input[2]),Float.valueOf(input[3]));
        boolean hasWebsite = (Integer.valueOf(input[4]).intValue()==1) ? true : false;
        return new PointOfInterest(name, c, hasWebsite);
    }

    public final String name;
    public final Coordinates coordinates;
    public final boolean hasWebsite;

    public PointOfInterest(String name, Coordinates coordinates, boolean hasWebsite)
    {
        this.name = name;
        this.coordinates = coordinates;
        this.hasWebsite = hasWebsite;
    }



}
