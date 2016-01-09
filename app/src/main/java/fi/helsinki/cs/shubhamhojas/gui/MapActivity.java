package fi.helsinki.cs.shubhamhojas.gui;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qozix.tileview.TileView;

import java.util.LinkedList;

import fi.helsinki.cs.shubhamhojas.R;


/**
 * Created by shubham-kapoor on 18/12/15.
 */
public class MapActivity extends AppCompatActivity implements View.OnClickListener {

    protected final int FLOORS = 4;
    protected final int MAP_WIDTH = 2959;
    protected final int MAP_HEIGHT = 2782;
    protected final int MAP_PADDING = 1400;

    private LinearLayout container;
    protected TileView tileViews[];
    protected ImageView markers[];

    protected LinearLayout infoBox;
    protected TextView infoTitle;
    protected TextView infoSubtitle;
    protected View infoImage;
    protected ActionBar ab;

    protected final LinkedList<View> poiMarkers = new LinkedList<>();
    protected int currentFloor = -1;

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
        ab = getSupportActionBar();


        infoBox = (LinearLayout) findViewById(R.id.tile_info);
        infoTitle = (TextView) findViewById(R.id.info_title);
        infoSubtitle = (TextView) findViewById(R.id.info_subtitle);
        infoImage = findViewById(R.id.info_image);
        infoBox.setVisibility(View.INVISIBLE);
        infoBox.setOnClickListener(this);

        tileViews = new TileView[FLOORS];
        markers = new ImageView[FLOORS];

        for (int i = 0; i < FLOORS; i++)
        {
            tileViews[i] = new TileView(this);
            tileViews[i].setSize(MAP_WIDTH+MAP_PADDING, MAP_HEIGHT+MAP_PADDING);  // the original size of the untiled image
            //define our own coordiante system from
            tileViews[i].defineBounds(-MAP_PADDING/2,-MAP_PADDING/2,MAP_WIDTH+MAP_PADDING/2,MAP_HEIGHT+MAP_PADDING/2);


            tileViews[i].addDetailLevel(1f, String.format("tile-%d-%%d_%%d.png", i));

            markers[i] = new ImageView(this);

            Resources r = getResources();
            Drawable[] layers = new Drawable[2];
            layers[1] = r.getDrawable(R.drawable.circle_inner);
            layers[0] = r.getDrawable(R.drawable.circle_outer);
            LayerDrawable layerDrawable = new LayerDrawable(layers);

            markers[i].setImageDrawable(layerDrawable);

            tileViews[i].addMarker(markers[i], -50, -50, -0.5f, -0.5f);
        }

        setFloor(1);

    }

    protected void setFloor(int floor)
    {
        if(currentFloor== floor)
            return;
        if(currentFloor >= 0)
        {
            container.removeView(tileViews[currentFloor]);
        }

        for(View iv: poiMarkers)
        {
            tileViews[currentFloor].removeMarker(iv);
        }
        poiMarkers.clear();

        currentFloor = floor;
        container.addView(tileViews[currentFloor], new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }


    public void onClick(View v) {

    }

}
