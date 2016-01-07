package hk.ust.cse.com107x.indoor;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qozix.tileview.TileView;

import java.io.IOException;

import localiser.LocaliserController;
import localiser.algorithms.AbstractLocaliserAlgorithm;
import localiser.algorithms.ProbabilityAlgorithm;
import localiser.algorithms.comparators.CosineComparator;
import localiser.algorithms.kNearestNeighborsAlgorithm;
import localiser.units.Coordinates;

/**
 * Created by shubham-kapoor on 18/12/15.
 */
public class MapActivity extends Activity implements LocaliserController.Callback {

    private final int FLOORS = 4;

    private LocaliserController lc;
    private  TileView tileViews[];
    private ImageView markers[];
    private int currentFloor = -1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AbstractLocaliserAlgorithm ala = new ProbabilityAlgorithm(new CosineComparator());
        try {
            lc = new LocaliserController(ala, this);
            lc.registerForLocationUpdates(this);
        } catch (LocaliserController.NoWIFIException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tileViews = new TileView[FLOORS];
        markers = new ImageView[FLOORS];

        for (int i = 0; i < FLOORS; i++)
        {
            tileViews[i] = new TileView(this);
            tileViews[i] = new TileView( this );
            tileViews[i].setSize(2959, 2782);  // the original size of the untiled image

            tileViews[i].addDetailLevel(1f, String.format("tile-%d-%%d_%%d.png", i));

            markers[i] = new ImageView(this);
            markers[i].setImageResource(R.drawable.marker);

            tileViews[i].addMarker(markers[i], -100, 100, -0.8f, -1.0f);
        }

        setFloor(1);



    }

    @Override
    protected void onDestroy() {
        lc.unregisterForLocationUpdates(this);
        super.onDestroy();
    }

    private void setFloor(int floor)
    {
        if(floor==currentFloor)
            return;

        System.out.println("Set floor: " + floor);
        currentFloor = floor;
        setContentView(tileViews[currentFloor]);

    }

    @Override
    public void locationUpdated(Coordinates c) {

        setFloor(Math.round(c.z / 400));
        tileViews[currentFloor].moveMarker(markers[currentFloor],c.x,c.y);

        System.out.println(c);
    }
}
