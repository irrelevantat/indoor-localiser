package fi.helsinki.cs.shubhamhojas.gui;

import android.app.Activity;
import android.os.Bundle;

import java.io.IOException;

import fi.helsinki.cs.shubhamhojas.controller.LocaliserController;
import fi.helsinki.cs.shubhamhojas.localiser.algorithms.AbstractLocaliserAlgorithm;
import fi.helsinki.cs.shubhamhojas.localiser.algorithms.comparators.CosineComparator;
import fi.helsinki.cs.shubhamhojas.localiser.algorithms.kNearestNeighborsAlgorithm;
import fi.helsinki.cs.shubhamhojas.localiser.units.Coordinates;

/**
 * Created by sebastian on 07/01/16.
 */
public class FieldTestActivity extends Activity implements LocaliserController.Callback {

    private LocaliserController lc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AbstractLocaliserAlgorithm ala = new kNearestNeighborsAlgorithm(new CosineComparator());
        try {
            lc = new LocaliserController(ala,this);
            lc.registerForLocationUpdates(this);
        } catch (LocaliserController.NoWIFIException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onDestroy() {
        lc.unregisterForLocationUpdates(this);
        super.onDestroy();
    }

    @Override
    public void locationUpdated(Coordinates c) {
        System.out.println("Coordinate: " + c);
    }
}
