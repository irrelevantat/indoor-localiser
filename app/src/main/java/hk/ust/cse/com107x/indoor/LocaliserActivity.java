package hk.ust.cse.com107x.indoor;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.qozix.tileview.TileView;
import com.qozix.tileview.markers.MarkerLayout;

import java.io.IOException;
import java.util.Date;
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
 * Created by sebastian on 08/01/16.
 */
public class LocaliserActivity extends MapActivity implements View.OnTouchListener, MarkerLayout.MarkerTapListener, LocaliserController.Callback {

    private LocaliserController lc;
    private long lastTimeUserScrolled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AbstractLocaliserAlgorithm ala = new AverageAlgorithm(new CosineComparator());
        try {
            lc = new LocaliserController(ala, this);
            lc.registerForLocationUpdates(this);
        } catch (LocaliserController.NoWIFIException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < FLOORS; i++)
        {
            tileViews[i].setOnTouchListener(this);
            tileViews[i].setMarkerTapListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        lc.unregisterForLocationUpdates(this);
        super.onDestroy();
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

    private void showPOI()
    {
        for(View iv: poiMarkers)
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_other)
        {
            this.showPOI();
        }

        return true;
    }

    protected void setFloor(int floor){
        super.setFloor(floor);
        ab.setTitle("Localiser at " + floor);
    }
}
