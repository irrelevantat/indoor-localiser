package fi.helsinki.cs.shubhamhojas.gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.qozix.tileview.markers.MarkerLayout;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import fi.helsinki.cs.shubhamhojas.R;
import fi.helsinki.cs.shubhamhojas.controller.LocaliserController;
import fi.helsinki.cs.shubhamhojas.localiser.algorithms.AbstractLocaliserAlgorithm;
import fi.helsinki.cs.shubhamhojas.localiser.algorithms.AverageAlgorithm;
import fi.helsinki.cs.shubhamhojas.localiser.algorithms.comparators.CosineComparator;
import fi.helsinki.cs.shubhamhojas.localiser.database.POIDatabase;
import fi.helsinki.cs.shubhamhojas.localiser.units.Coordinates;
import fi.helsinki.cs.shubhamhojas.localiser.units.PointOfInterest;
import fi.helsinki.cs.shubhamhojas.localiser.units.Tuple;

/**
 * Created by sebastian on 08/01/16.
 */
public class LocaliserActivity extends MapActivity implements View.OnTouchListener, MarkerLayout.MarkerTapListener, LocaliserController.Callback {

    private LocaliserController lc;
    private long lastTimeUserScrolled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < FLOORS; i++)
        {
            tileViews[i].setOnTouchListener(this);
            tileViews[i].setMarkerTapListener(this);
        }
        ab.setTitle("Locating...");
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(lc!=null)
        {
            lc.unregisterForLocationUpdates(this);
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //do this as long as our controller is not registered
        //showWIFIDisabledAlert blocks
        if(lc == null)
        {
            try {
                AbstractLocaliserAlgorithm ala = new AverageAlgorithm(new CosineComparator());
                lc = new LocaliserController(ala, this);
                lc.registerForLocationUpdates(this);
            } catch (LocaliserController.NoWIFIException e) {
                //show error
                this.showWIFIDisabledAlert();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                //unresolvable database read error
                finish();
            }
        }
    }

    private void showWIFIDisabledAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need WIFI to calculate your position.");
        builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LocaliserActivity.this.finish();
            }

        });
        builder.setCancelable(false);
        //blocks
        builder.create().show();
    }


    @Override
    public void locationUpdated(Coordinates c) {

        if(c==null)
        {
            return;
        }

        setFloor(Math.round(c.z / 400));
        tileViews[currentFloor].moveMarker(markers[currentFloor], c.x, c.y);



        if(((new Date().getTime() - lastTimeUserScrolled)/ 1000 % 60) > 5)
        {
            //invalidate menu icon if this is for the first time
            if(lastTimeUserScrolled != 0)
            {
                invalidateOptionsMenu();
                lastTimeUserScrolled = 0;
            }
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

            //set menu to blue again
            lastTimeUserScrolled = new Date().getTime();
            invalidateOptionsMenu();
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

    private void onShowPOI()
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

    public void onShowCurrentLocation()
    {
        if(lc.getLastCoordinates()!=null)
          tileViews[currentFloor].slideToAndCenter(lc.getLastCoordinates().x, lc.getLastCoordinates().y);
        lastTimeUserScrolled=0;
        invalidateOptionsMenu();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_aroundme)
        {
            this.onShowPOI();
        }
        else if(item.getItemId()==R.id.action_location){
            this.onShowCurrentLocation();
        }

        return true;
    }

    protected void setFloor(int floor){
        super.setFloor(floor);
        
        char floorLabel;
        if(floor==0)
        {
            floorLabel = 'K';
        }
        else{
            floorLabel = (char)('0'+floor);
        }
        ab.setTitle("Floor " + floorLabel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        if(lastTimeUserScrolled==0)
            menu.getItem(1).setIcon(R.drawable.ic_my_location_blue_24dp);
        else
            menu.getItem(1).setIcon(R.drawable.ic_my_location_white_24dp);


        return true;
    }

}
