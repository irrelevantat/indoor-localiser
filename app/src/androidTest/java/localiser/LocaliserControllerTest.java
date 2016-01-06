package localiser;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import hk.ust.cse.com107x.indoor.MainActivity;
import localiser.algorithms.AbstractLocaliserAlgorithm;
import localiser.algorithms.NearestNeighborAlgorithm;
import localiser.algorithms.comparators.CosineComparator;
import localiser.algorithms.comparators.SimpleComparator;
import localiser.algorithms.kNearestNeighborsAlgorithm;
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


    private void helperTester(final AbstractLocaliserAlgorithm algo, int kTimes) throws InterruptedException, LocaliserController.NoWIFIException, IOException {
        final CountDownLatch semaphore = new CountDownLatch(kTimes);

        LocaliserController testController = new LocaliserController(algo, solo.getCurrentActivity());
        LocaliserController.Callback cb = new LocaliserController.Callback() {
            @Override
            public void locationUpdated(Coordinates c) {
                //it's enough if we receive some result
                System.out.println("Updated coordinates ("
                        + algo.getClass().getSimpleName()
                        + ", "
                        + algo.comp.getClass().getSimpleName()
                        + "): "
                        + c);

                assertNotNull(c);
                semaphore.countDown();
            }
        };
        testController.registerForLocationUpdates(cb);
        semaphore.await();
        testController.unregisterForLocationUpdates(cb);
    }

    public void testLocating_Abstract() throws LocaliserController.NoWIFIException, InterruptedException, IOException {

        AbstractLocaliserAlgorithm testAlgo = new AbstractLocaliserAlgorithm(new SimpleComparator()) {
            @Override
            public Coordinates getLocation(Fingerprint p, FingerprintDatabase db) {
                return new Coordinates(0,0,0);
            }
        };
        helperTester(testAlgo, 2);


    }


    public void testLocating_kNearest_cosine() throws LocaliserController.NoWIFIException, IOException, InterruptedException {

        AbstractLocaliserAlgorithm algo = new kNearestNeighborsAlgorithm(new CosineComparator());
        helperTester(algo, 2);
    }
    public void testLocating_kNearest_simple() throws LocaliserController.NoWIFIException, IOException, InterruptedException {

        AbstractLocaliserAlgorithm algo = new kNearestNeighborsAlgorithm(new SimpleComparator());
        helperTester(algo, 2);
    }
    public void testLocating_Nearest_cosine() throws LocaliserController.NoWIFIException, IOException, InterruptedException {

        AbstractLocaliserAlgorithm algo = new NearestNeighborAlgorithm(new CosineComparator());
        helperTester(algo, 2);
    }
    public void testLocating_Nearest_simple() throws LocaliserController.NoWIFIException, IOException, InterruptedException {

        AbstractLocaliserAlgorithm algo = new NearestNeighborAlgorithm(new SimpleComparator());
        helperTester(algo, 2);
    }

    public void testDocumentation()
    {











    }


}
