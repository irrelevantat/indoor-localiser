package localiser.database;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.io.IOException;
import java.util.List;

import hk.ust.cse.com107x.indoor.MainActivity;
import hk.ust.cse.com107x.indoor.R;
import localiser.LocaliserController;
import localiser.units.Coordinates;
import localiser.units.PointOfInterest;
import localiser.units.Tuple;

/**
 * Created by sebastian on 05/01/16.
 */
public class DatabaseTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public DatabaseTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }


    public void testFingerprintDatabaseConstruction() throws IOException {

        FingerprintDatabase db = new FingerprintDatabase(solo.getCurrentActivity(), R.raw.test_fingerprints);

        assertTrue(db.size()>0);
        for(Fingerprint finger: db)
        {
            assertTrue(finger.size()>0);
            assertNotNull(finger.getCoordinates());

            for(String BSSID: finger)
            {
                assertTrue(finger.getLevel(BSSID) < 0);
            }
        }
    }
    public void testFingerprintConstructionScan()
    {
        Fingerprint f = Fingerprint.fromScanResult("d8:b1:90:41:8a:3c;-68;d8:b1:90:41:8a:3f;-70;d8:b1:90:41:8a:3d;-84;b0:aa:77:a8:cf:ec;-51;58:97:bd:62:3:9e;-72;f4:4e:5:81:a8:5f;-84;58:97:bd:62:3:9c;-70;f4:4e:5:ad:65:82;-85;f4:4e:5:ad:65:80;-82;58:97:bd:62:3:91;-59;58:97:bd:62:3:90;-50;58:97:bd:62:3:93;-50;58:97:bd:62:3:92;-50;d8:b1:90:41:8a:32;-75;d8:b1:90:41:8a:33;-62;d8:b1:90:41:8a:30;-75;f4:4e:5:81:a8:52;-71;d8:b1:90:3c:7e:8f;-90;d8:b1:90:3c:7e:8b;-90;58:97:bd:6e:b0:bf;-82;58:97:bd:6e:b0:be;-90;58:97:bd:6e:b0:bd;-86;58:97:bd:6e:b0:bb;-82;56:25:8d:aa:da:a8;-82;b0:aa:77:cc:d3:e1;-84;34:21:9:14:de:60;-61;84:b8:2:e2:a1:4b;-71;34:62:88:ea:b:2f;-80;34:62:88:ea:b:2b;-81;34:62:88:ea:b:2d;-84;58:97:bd:6e:b0:b0;-73;d8:b1:90:3c:83:4b;-72;d8:b1:90:3c:83:4c;-80;d8:b1:90:3c:83:4f;-72;f4:4e:5:ad:65:83;-82;d8:b1:90:3c:83:4d;-73;d8:b1:90:3c:83:4e;-83;00:1d:e6:27:14:c2;-79;00:1d:e6:27:14:c3;-69;00:1d:e6:27:14:c0;-77;00:1d:e6:27:14:c1;-79;b0:aa:77:9f:11:2b;-89;b0:aa:77:9f:11:2d;-88;84:b8:2:e2:a5:9d;-75;b0:aa:77:9f:11:2f;-88;00:3a:98:b9:9c:cb;-70;00:3a:98:b9:9c:cc;-77;00:3a:98:b9:9c:cd;-70;00:3a:98:b9:9c:cf;-73;b0:aa:77:9f:14:41;-69;b0:aa:77:9f:14:40;-79;b0:aa:77:9f:14:43;-80;b0:aa:77:9f:14:42;-79;b0:aa:77:9f:12:cc;-83;00:1d:e6:27:14:cf;-78;d8:b1:90:3c:76:d;-85;00:1d:e6:27:14:ce;-86;00:1d:e6:27:14:cc;-89;d8:b1:90:3c:76:b;-91;b0:aa:77:9f:11:20;-76;b0:aa:77:9f:11:22;-76;b0:aa:77:9f:11:23;-76;f4:4e:5:ad:65:8d;-88;f4:4e:5:ad:65:8e;-86;f4:4e:5:ad:65:8c;-88;b0:aa:77:9f:14:4e;-74;b0:aa:77:9f:14:4d;-85;84:b8:2:e2:a1:4c;-76;b0:aa:77:9f:14:4f;-85;84:b8:2:e2:a1:4d;-81;b0:aa:77:9f:14:4c;-76;84:b8:2:e2:a1:4f;-71;d8:b1:90:3c:76:e;-87;84:b8:2:e2:a5:91;-82;d8:b1:90:41:8d:2b;-87;d8:b1:90:41:8d:2c;-91;d8:b1:90:41:8d:2f;-86;d8:b1:90:41:8d:2d;-74;d8:b1:90:41:8d:2e;-73;58:97:bd:6e:b0:b2;-77;d8:b1:90:41:8d:22;-83;d8:b1:90:41:8d:20;-77;d8:b1:90:41:8d:21;-75;84:b8:2:e2:a5:9f;-77;b0:aa:77:a8:cf:eb;-59;84:b8:2:e2:a5:9e;-88;b0:aa:77:a8:cf:ef;-73");
        assertEquals(f.getLevel("d8:b1:90:41:8a:3c").intValue(),-68);
        assertEquals(f.size(), 87);
    }
    public void testFingerprintConstructionDB()
    {
        Fingerprint f = Fingerprint.fromDBString("1114.04547393;1599.40798577;0.0;d8:b1:90:41:8a:3c;-68;d8:b1:90:41:8a:3f;-70;d8:b1:90:41:8a:3d;-84;b0:aa:77:a8:cf:ec;-51;58:97:bd:62:3:9e;-72;f4:4e:5:81:a8:5f;-84;58:97:bd:62:3:9c;-70;f4:4e:5:ad:65:82;-85;f4:4e:5:ad:65:80;-82;58:97:bd:62:3:91;-59;58:97:bd:62:3:90;-50;58:97:bd:62:3:93;-50;58:97:bd:62:3:92;-50;d8:b1:90:41:8a:32;-75;d8:b1:90:41:8a:33;-62;d8:b1:90:41:8a:30;-75;f4:4e:5:81:a8:52;-71;d8:b1:90:3c:7e:8f;-90;d8:b1:90:3c:7e:8b;-90;58:97:bd:6e:b0:bf;-82;58:97:bd:6e:b0:be;-90;58:97:bd:6e:b0:bd;-86;58:97:bd:6e:b0:bb;-82;56:25:8d:aa:da:a8;-82;b0:aa:77:cc:d3:e1;-84;34:21:9:14:de:60;-61;84:b8:2:e2:a1:4b;-71;34:62:88:ea:b:2f;-80;34:62:88:ea:b:2b;-81;34:62:88:ea:b:2d;-84;58:97:bd:6e:b0:b0;-73;d8:b1:90:3c:83:4b;-72;d8:b1:90:3c:83:4c;-80;d8:b1:90:3c:83:4f;-72;f4:4e:5:ad:65:83;-82;d8:b1:90:3c:83:4d;-73;d8:b1:90:3c:83:4e;-83;00:1d:e6:27:14:c2;-79;00:1d:e6:27:14:c3;-69;00:1d:e6:27:14:c0;-77;00:1d:e6:27:14:c1;-79;b0:aa:77:9f:11:2b;-89;b0:aa:77:9f:11:2d;-88;84:b8:2:e2:a5:9d;-75;b0:aa:77:9f:11:2f;-88;00:3a:98:b9:9c:cb;-70;00:3a:98:b9:9c:cc;-77;00:3a:98:b9:9c:cd;-70;00:3a:98:b9:9c:cf;-73;b0:aa:77:9f:14:41;-69;b0:aa:77:9f:14:40;-79;b0:aa:77:9f:14:43;-80;b0:aa:77:9f:14:42;-79;b0:aa:77:9f:12:cc;-83;00:1d:e6:27:14:cf;-78;d8:b1:90:3c:76:d;-85;00:1d:e6:27:14:ce;-86;00:1d:e6:27:14:cc;-89;d8:b1:90:3c:76:b;-91;b0:aa:77:9f:11:20;-76;b0:aa:77:9f:11:22;-76;b0:aa:77:9f:11:23;-76;f4:4e:5:ad:65:8d;-88;f4:4e:5:ad:65:8e;-86;f4:4e:5:ad:65:8c;-88;b0:aa:77:9f:14:4e;-74;b0:aa:77:9f:14:4d;-85;84:b8:2:e2:a1:4c;-76;b0:aa:77:9f:14:4f;-85;84:b8:2:e2:a1:4d;-81;b0:aa:77:9f:14:4c;-76;84:b8:2:e2:a1:4f;-71;d8:b1:90:3c:76:e;-87;84:b8:2:e2:a5:91;-82;d8:b1:90:41:8d:2b;-87;d8:b1:90:41:8d:2c;-91;d8:b1:90:41:8d:2f;-86;d8:b1:90:41:8d:2d;-74;d8:b1:90:41:8d:2e;-73;58:97:bd:6e:b0:b2;-77;d8:b1:90:41:8d:22;-83;d8:b1:90:41:8d:20;-77;d8:b1:90:41:8d:21;-75;84:b8:2:e2:a5:9f;-77;b0:aa:77:a8:cf:eb;-59;84:b8:2:e2:a5:9e;-88;b0:aa:77:a8:cf:ef;-73");
        assertEquals(f.getLevel("00:1d:e6:27:14:ce").intValue(),-86);
        assertEquals(f.size(), 87);
        assertEquals(f.getCoordinates().x,(float)1114.04547393);
        assertEquals(f.getCoordinates().y,(float)1599.40798577);
        assertEquals(f.getCoordinates().z,(float)0);
    }
    public void testPOIConstruction()
    {
        PointOfInterest poi = PointOfInterest.fromDBString("Emergency Exit;45.0;34.0;1.0");
        assertEquals(poi.name, "Emergency Exit");
        assertEquals((int)poi.coordinates.x,(int)45.0);
        assertEquals((int)poi.coordinates.y,(int)34.0);
        assertEquals((int)poi.coordinates.z,(int)1.0);
    }
    public void testPOIDatabaseConstruction() throws IOException {

        POIDatabase db = new POIDatabase(solo.getCurrentActivity(), R.raw.poi);

        assertTrue(db.size() > 0);

        for(PointOfInterest poi: db)
        {
            assertNotNull(poi.name);
            assertNotNull(poi.coordinates);
        }
    }
    public void testNearestPOI() throws IOException, LocaliserController.NoWIFIException {

        LocaliserController lc = new LocaliserController(null, solo.getCurrentActivity());

        Coordinates c = new Coordinates(45,34,1);
        List<Tuple<Double,PointOfInterest>> list = lc.getClosestPOI(c,10);

        assertTrue(list.size()>0 && list.size()<=10);

        //should be an exact match with emergency exit POI
        assertTrue(list.get(0).first==0);
        assertEquals(list.get(0).second.name,"Emergency Exit");
        assertTrue(list.get(0).second.coordinates.x==45);
        assertTrue(list.get(0).second.coordinates.y==34);
        assertTrue(list.get(0).second.coordinates.z==1);

        //list should be ascending
        double last = 0;
        for(int i = 0; i<list.size(); i++)
        {
            assertTrue(list.get(i).first>=last);
            last = list.get(i).first;
        }
    }



}
