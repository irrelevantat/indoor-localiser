package hk.ust.cse.com107x.indoor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qozix.tileview.hotspots.HotSpot;
import com.qozix.tileview.paths.CompositePathView;

import java.io.IOException;
import java.util.List;

import localiser.CollectorController;
import localiser.LocaliserController;
import localiser.units.Coordinates;
import localiser.units.Fingerprint;

import static android.R.color.holo_blue_light;
import static junit.framework.Assert.assertTrue;


/**
 * Created by sebastian on 08/01/16.
 */
public class DesignPathActivity extends MapActivity implements DialogInterface.OnClickListener, HotSpot.HotSpotTapListener {

    enum Mode
    {
        EDIT_MODE,
        PREPARE_MODE,
        WALK_MODE,
        DONE_MODE,
        SAVED_MODE
    };


    // 1. Set floor
    // 2. finalise ->

    private EditText et;
    private Mode mode = Mode.EDIT_MODE;
    private CollectorController cc;

    private int currentPoint = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            cc = new CollectorController(this);
        } catch (LocaliserController.NoWIFIException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ab.setTitle("Edit");

        et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setView(et)
            .setCancelable(false)
                .setPositiveButton("OK", this);
        alertDialogBuilder.create().show();


    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        System.out.println("Received: " + et.getText());
        setFloor(Integer.valueOf(et.getText().toString()).intValue());

        HotSpot hotSpot = new HotSpot();
        hotSpot.setTag( this );
        hotSpot.set(new Rect(0, 0, MAP_WIDTH, MAP_HEIGHT));  // or any other API to define the region
        hotSpot.setHotSpotTapListener(this);
        tileViews[currentFloor].addHotSpot(hotSpot);

    }

    @Override
    public void onHotSpotTap(HotSpot hotSpot, int x, int y) {

        if(mode==Mode.EDIT_MODE)
        {
            View iv = getNextPointView();
            currentPoint++;
            cc.addToPath(x,y,currentFloor);
            poiMarkers.add(iv);
            tileViews[currentFloor].addMarker(iv, x, y, -0.5f, -0.5f);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_aroundme
                && mode == Mode.EDIT_MODE)
        {
            mode = Mode.PREPARE_MODE;
            ab.setTitle("Prepare");
            this.prepareWalk();
        }
        else if(item.getItemId()==R.id.action_aroundme
                && mode == Mode.PREPARE_MODE)
        {
            mode = Mode.WALK_MODE;
            ab.setTitle("Walk");
            cc.startRecording(cc.path.get(currentPoint - 2));
        }
        else if(item.getItemId()==R.id.action_aroundme
                && mode == Mode.WALK_MODE)
        {
            addPath(cc.path.get(currentPoint - 2), cc.path.get(currentPoint - 1));
            List<Coordinates> addedPoints = cc.stopRecording(cc.path.get(currentPoint-1));

            //TODO show points properly
            //is it good to att markers?
            for(Coordinates c: addedPoints)
            {
                View iv = getNextPointView();
                tileViews[currentFloor].addMarker(iv, c.x, c.y, -0.5f, -0.5f);
            }

            if(currentPoint>= cc.path.size())
            {
                mode = Mode.DONE_MODE;
                ab.setTitle("Done");
            }
            else{
                mode = Mode.PREPARE_MODE;
                ab.setTitle("Prepare");
                addNextPoint(currentPoint++);
            }

        }
        else if(item.getItemId()==R.id.action_aroundme
                && mode == Mode.DONE_MODE)
        {
            cc.savePoints();
            ab.setTitle("Saved");
            finish();
        }

        return true;
    }

    private void addPath(Coordinates src, Coordinates end)
    {
        CompositePathView.DrawablePath drawablePath = new CompositePathView.DrawablePath();
        Path path = new Path();
        path.moveTo(src.x, src.y);
        path.lineTo(end.x,end.y);
        drawablePath.path = path;
        Paint paint = new Paint();
        paint.setColor(0xFFFF00FF);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(8);
        drawablePath.paint = paint;
        tileViews[currentFloor].getCompositePathView().addPath(drawablePath);
    }

    private View getNextPointView()
    {
        TextView iv = new TextView(this);
        iv.setText(String.valueOf(currentPoint));
        iv.setBackgroundColor(getResources().getColor(holo_blue_light));
        return iv;
    }


    private void addNextPoint(int i)
    {
        Coordinates c1 = cc.path.get(i);
        View iv = getNextPointView();
        tileViews[currentFloor].addMarker(iv, c1.x, c1.y, -0.5f, -0.5f);

    }

    private void prepareWalk()
    {
        currentPoint = 0;

        //remove all markers
        assertTrue(poiMarkers.size() >= 2);
        for(View v: poiMarkers)
        {
            tileViews[currentFloor].removeMarker(v);
        }
        //add first two points
        addNextPoint(currentPoint++);
        addNextPoint(currentPoint++);





    }
}
