package localiser.algorithms;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import hk.ust.cse.com107x.indoor.MainActivity;
import localiser.units.Coordinates;

/**
 * Created by sebastian on 07/01/16.
 */
public abstract class AbstractAlgorithmTest extends ActivityInstrumentationTestCase2<MainActivity> {

    protected Solo solo;

    public AbstractAlgorithmTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    protected void assertSimilar(Coordinates c1, Coordinates cReference, int delta)
    {
        System.out.println("CMP: " + c1 + " vs. " + cReference);

        assertNotNull(c1);
        assertTrue(Math.abs(c1.x - cReference.x)<delta);
        assertTrue(Math.abs(c1.y - cReference.y)<delta);
        assertTrue(Math.abs(c1.z - cReference.z)<delta);
    }


}
