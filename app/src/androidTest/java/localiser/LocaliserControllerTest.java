package localiser;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import hk.ust.cse.com107x.indoor.MainActivity;
import localiser.algorithms.AbstractLocaliserAlgorithm;
import localiser.algorithms.comparators.SimpleComporator;
import localiser.database.FingerprintDatabase;
import localiser.database.Fingerprint;
import localiser.units.Coordinates;


/**
 * Created by sebastian on 04/01/16.
 */
public class LocaliserControllerTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public LocaliserControllerTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }


    public void testLocationUpdates() throws LocaliserController.NoWIFIException, InterruptedException, IOException {

        //need to receive at least 2 updates
        final CountDownLatch semaphore = new CountDownLatch(2);

        AbstractLocaliserAlgorithm testAlgo = new AbstractLocaliserAlgorithm(new SimpleComporator()) {
            @Override
            public Coordinates getLocation(Fingerprint p, FingerprintDatabase db) {
                return new Coordinates(0,0,0);
            }
        };

        LocaliserController testController = new LocaliserController(testAlgo,solo.getCurrentActivity());
        testController.registerForLocationUpdates(new LocaliserController.Callback() {
            @Override
            public void locationUpdated(Coordinates c) {
                assertEquals(c.x, 0);
                assertEquals(c.y, 0);
                assertEquals(c.z, 0);
                semaphore.countDown();
            }
        });

        semaphore.await();

    }




}
