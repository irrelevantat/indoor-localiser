package localiser.units;

import android.graphics.Point;

/**
 * Created by sebastian on 05/01/16.
 */
public class PointOfInterest {

    public static PointOfInterest fromDBString(String databaseLine)
    {
        String[] input = databaseLine.split(";");
        assert(input.length==4);

        String name = input[0];
        Coordinates c = new Coordinates(Float.valueOf(input[1]), Float.valueOf(input[2]),Float.valueOf(input[3]));
        return new PointOfInterest(name, c);
    }

    public final String name;
    public final Coordinates coordinates;

    public PointOfInterest(String name, Coordinates coordinates)
    {
        this.name = name;
        this.coordinates = coordinates;
    }




}
