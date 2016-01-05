package localiser.units;

/**
 * Created by sebastian on 05/01/16.
 */

public class Coordinates
{
    public final float x,y,z;
    public Coordinates(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double distance(Coordinates c)
    {
        return Math.sqrt(Math.pow(c.x-this.x,2) + Math.pow(c.y-this.y,2) + Math.pow(c.z-this.z,2));
    }

}