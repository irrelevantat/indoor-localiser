package hk.ust.cse.com107x.indoor;

import android.content.Intent;
import android.media.Image;
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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qozix.tileview.TileView;
import com.qozix.tileview.markers.MarkerLayout;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import localiser.LocaliserController;
import localiser.algorithms.AbstractLocaliserAlgorithm;
import localiser.algorithms.AverageAlgorithm;
import localiser.algorithms.comparators.CosineComparator;
import localiser.database.POIDatabase;
import localiser.units.Coordinates;
import localiser.units.PointOfInterest;
import localiser.units.Tuple;


/**
 * Created by shubham-kapoor on 18/12/15.
 */
public class MapActivity extends AppCompatActivity implements LocaliserController.Callback, View.OnTouchListener, MarkerLayout.MarkerTapListener, View.OnClickListener {

    private final int FLOORS = 4;

    private LocaliserController lc;
    private LinearLayout container;
    private  TileView tileViews[];
    private ImageView markers[];

    private LinearLayout infoBox;
    private TextView infoTitle;
    private TextView infoSubtitle;
    private View infoImage;

    private final LinkedList<ImageView> poiMarkers = new LinkedList<>();

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

        infoBox = (LinearLayout) findViewById(R.id.tile_info);
        infoTitle = (TextView) findViewById(R.id.info_title);
        infoSubtitle = (TextView) findViewById(R.id.info_subtitle);
        infoImage = findViewById(R.id.info_image);
        infoBox.setVisibility(View.INVISIBLE);
        infoBox.setOnClickListener(this);

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
            tileViews[i].setMarkerTapListener(this);
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

        for(ImageView iv: poiMarkers)
        {
            tileViews[currentFloor].removeMarker(iv);
        }
        poiMarkers.clear();

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
        if(v==tileViews[currentFloor])
        {
            if(infoBox.getVisibility()==View.VISIBLE)
                infoBox.setVisibility(View.INVISIBLE);

            lastTimeUserScrolled = new Date().getTime();
            return tileViews[currentFloor].onTouchEvent(event);
        }

        return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_other)
        {
            for(ImageView iv: poiMarkers)
            {
                tileViews[currentFloor].removeMarker(iv);
            }
            poiMarkers.clear();

            final List<Tuple<Double, PointOfInterest>> closestPOI = lc.getClosestPOI(null,10);
            for(Tuple<Double, PointOfInterest> poi: closestPOI)
            {
                Coordinates coordinates = poi.second.coordinates;
                if(coordinates.z/400 == currentFloor)
                {
                    ImageView iv = new ImageView(this);
                    iv.setImageResource(R.drawable.marker2);
                    //Save poi
                    iv.setTag(poi.second);
                    poiMarkers.add(iv);

                    tileViews[currentFloor].addMarker(iv, coordinates.x, coordinates.y, -0.5f, -1.0f);

                }

            }
        }

        return true;
    }

    @Override
    public void onMarkerTap(View view, int x, int y) {

        PointOfInterest poi = (PointOfInterest) view.getTag();
        if(poi!=null)
        {
            infoTitle.setText(poi.name);
            infoSubtitle.setText(String.format("Distance: %d meter", (int) (POIDatabase.METERS_PER_PIXEL * poi.coordinates.distance(lc.getLastCoordinates()))));
            infoBox.setVisibility(View.VISIBLE);
            if(poi.hasWebsite)
            {
                infoImage.setVisibility(View.VISIBLE);
                String url = String.format("http://www.helsinki.fi/teknos/opetustilat/kumpula/gh2b/%s.htm", poi.name.toLowerCase());
                infoBox.setTag(url);
            }
            else{
                infoImage.setVisibility(View.INVISIBLE);
            }

        }


    }

    @Override
    public void onClick(View v) {

        if(infoBox.getTag()!=null)
        {

            Intent intent = new Intent(this, RoomActivity.class);
            intent.putExtra(RoomActivity.INTENT_DISTANCE,infoSubtitle.getText());
            intent.putExtra(RoomActivity.INTENT_ROOM,infoTitle.getText());
            intent.putExtra(RoomActivity.INTENT_URL, (String) infoBox.getTag());
            startActivity(intent);

            infoBox.setTag(null);

        }
    }
}
