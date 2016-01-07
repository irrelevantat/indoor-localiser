package hk.ust.cse.com107x.indoor;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qozix.tileview.TileView;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import localiser.LocaliserController;
import localiser.algorithms.AbstractLocaliserAlgorithm;
import localiser.algorithms.AverageAlgorithm;
import localiser.algorithms.comparators.CosineComparator;
import localiser.units.Coordinates;
import localiser.units.PointOfInterest;
import localiser.units.Tuple;

/**
 * Created by shubham-kapoor on 18/12/15.
 */
public class MapActivity extends AppCompatActivity implements LocaliserController.Callback, View.OnTouchListener {

    private final int FLOORS = 4;

    private LocaliserController lc;
    private LinearLayout container;
    private  TileView tileViews[];
    private ImageView markers[];

    private long lastTimeUserScrolled;

    private int currentFloor = -1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        container = (LinearLayout) findViewById(R.id.tile_container);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();




        AbstractLocaliserAlgorithm ala = new AverageAlgorithm(new CosineComparator());
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
            tileViews[i].setSize(2959, 2782);  // the original size of the untiled image

            tileViews[i].addDetailLevel(1f, String.format("tile-%d-%%d_%%d.png", i));

            markers[i] = new ImageView(this);
            markers[i].setImageResource(R.drawable.marker2);

            tileViews[i].addMarker(markers[i], -100, 100, -0.5f, -1.0f);
            tileViews[i].setOnTouchListener(this);
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
        if(currentFloor== floor)
            return;
        if(currentFloor >= 0)
        {
            container.removeView(tileViews[currentFloor]);
        }


        currentFloor = floor;
        container.addView(tileViews[currentFloor], new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }

    @Override
    public void locationUpdated(Coordinates c) {

        setFloor(Math.round(c.z / 400));
        tileViews[currentFloor].moveMarker(markers[currentFloor], c.x, c.y);


        if(((new Date().getTime() - lastTimeUserScrolled)/ 1000 % 60) > 5)
        {
            tileViews[currentFloor].slideToAndCenter(c.x, c.y);
        }

        System.out.println(c);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        lastTimeUserScrolled = new Date().getTime();
        return tileViews[currentFloor].onTouchEvent(event);
    }

    public void onGroupItemClick(MenuItem item) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by onOptionsItemSelected()
        if(item.getItemId()==R.id.action_other)
        {
            System.out.println("Touched other");
            final List<Tuple<Double, PointOfInterest>> closestPOI = lc.getClosestPOI(null);
            for(Tuple<Double, PointOfInterest> poi: closestPOI)
            {
                System.out.println("POI: " + poi.second.name + ", " + poi.first + "px");
            }
        }

    }
}
