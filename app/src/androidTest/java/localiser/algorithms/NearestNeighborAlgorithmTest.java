package localiser.algorithms;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.io.IOException;

import hk.ust.cse.com107x.indoor.MainActivity;
import hk.ust.cse.com107x.indoor.R;
import localiser.algorithms.comparators.CosineComparator;
import localiser.algorithms.comparators.SimpleComparator;
import localiser.units.Coordinates;
import localiser.database.FingerprintDatabase;
import localiser.units.Fingerprint;

/**
 * Created by sebastian on 05/01/16.
 */
public class NearestNeighborAlgorithmTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public NearestNeighborAlgorithmTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }


    public void testSimpleMatch_Simple() throws IOException {

        NearestNeighborAlgorithm nna = new NearestNeighborAlgorithm(new SimpleComparator());
        FingerprintDatabase db = new FingerprintDatabase(solo.getCurrentActivity(),R.raw.test_fingerprints);
        Fingerprint test = Fingerprint.fromScanResult("d8:b1:90:34:5d:70;-71;d8:b1:90:34:5d:71;-79;d8:b1:90:34:5d:72;-73;d8:b1:90:34:5d:73;-78;d8:b1:90:41:8a:3b;-88;d8:b1:90:41:8a:3c;-89;d8:b1:90:41:8a:3d;-88;d8:b1:90:41:8a:3e;-88;b0:aa:77:a8:cf:ec;-89;b0:aa:77:cc:ce:cd;-83;b0:aa:77:cc:ce:ce;-78;b0:aa:77:cc:ce:cc;-77;58:97:bd:62:3:9e;-73;58:97:bd:62:3:9d;-75;f4:4e:5:81:a8:5e;-89;f4:4e:5:81:a8:5d;-86;f4:4e:5:81:a8:5b;-87;58:97:bd:62:3:9b;-74;d8:b1:90:34:5d:7b;-86;d8:b1:90:34:5d:7d;-83;f4:4e:5:ad:65:80;-76;d8:b1:90:23:15:4f;-88;b0:aa:77:cc:ce:c2;-75;b0:aa:77:cc:ce:c3;-76;b0:aa:77:cc:ce:c0;-75;b0:aa:77:cc:ce:c1;-75;58:97:bd:62:3:91;-67;58:97:bd:62:3:90;-58;f4:4e:5:81:a8:51;-82;58:97:bd:62:3:92;-57;d8:b1:90:41:8a:32;-79;d8:b1:90:41:8a:33;-80;d8:b1:90:41:8a:30;-80;d8:b1:90:41:8a:31;-65;04:a1:51:30:c3:1c;-76;b0:aa:77:a8:b5:83;-76;b0:aa:77:a8:b5:82;-76;b0:aa:77:a8:b5:81;-79;b0:aa:77:a8:b5:80;-77;d8:b1:90:3c:7e:8f;-75;d8:b1:90:3c:7e:8d;-82;d8:b1:90:3c:7e:8c;-76;d8:b1:90:3c:7e:8b;-81;58:97:bd:6e:b0:be;-78;56:25:8d:aa:da:a8;-66;b0:aa:77:a8:bb:1d;-89;d8:b1:90:41:8d:21;-81;f4:cf:e2:62:da:1e;-87;34:21:9:14:de:60;-81;58:97:bd:62:3:93;-57;f4:cf:e2:62:da:1d;-87;b0:aa:77:9f:14:4f;-67;58:97:bd:6e:b0:b3;-80;58:97:bd:6e:b0:b2;-80;34:62:88:ea:b:2f;-88;d8:b1:90:45:18:de;-90;f4:4e:5:ad:65:82;-72;f4:4e:5:ad:65:83;-77;d8:b1:90:3c:83:4d;-68;d8:b1:90:3c:83:4e;-64;00:1d:e6:27:14:c2;-69;00:1d:e6:27:14:c3;-68;00:1d:e6:27:14:c0;-73;00:1d:e6:27:14:c1;-73;b0:aa:77:9f:11:2c;-68;b0:aa:77:9f:11:2d;-75;b0:aa:77:9f:11:2e;-90;b0:aa:77:9f:11:2f;-77;00:3a:98:b9:9c:cb;-63;00:3a:98:b9:9c:cc;-83;00:3a:98:b9:9c:cd;-64;00:3a:98:b9:9c:ce;-80;00:3a:98:b9:9c:cf;-64;b0:aa:77:9f:14:41;-68;b0:aa:77:9f:14:40;-70;84:b8:2:e2:a5:9c;-70;b0:aa:77:9f:12:cf;-81;b0:aa:77:9f:12:cd;-82;b0:aa:77:9f:12:ce;-64;b0:aa:77:9f:12:cb;-65;b0:aa:77:9f:12:cc;-63;00:1d:e6:27:14:cf;-74;00:1d:e6:27:14:ce;-81;d8:b1:90:3c:76:c;-85;b0:aa:77:9f:11:20;-65;b0:aa:77:9f:11:21;-65;b0:aa:77:9f:11:22;-65;b0:aa:77:9f:11:23;-64;f4:4e:5:ad:65:8f;-81;f4:4e:5:ad:65:8d;-78;f4:4e:5:ad:65:8e;-73;f4:4e:5:ad:65:8b;-79;58:97:bd:62:3:9f;-72;b0:aa:77:9f:12:c2;-62;b0:aa:77:9f:12:c3;-64;b0:aa:77:9f:12:c0;-63;b0:aa:77:9f:12:c1;-60;f4:cf:e2:62:da:10;-82;f4:cf:e2:62:da:13;-80;b0:aa:77:9f:14:4e;-90;b0:aa:77:9f:14:4d;-77;84:b8:2:e2:a1:4c;-86;84:b8:2:e2:a1:4b;-67;84:b8:2:e2:a1:4e;-89;84:b8:2:e2:a1:4d;-64;b0:aa:77:9f:14:4b;-77;84:b8:2:e2:a5:92;-71;84:b8:2:e2:a5:93;-75;84:b8:2:e2:a5:90;-67;84:b8:2:e2:a5:91;-83;d8:b1:90:41:8d:2b;-77;d8:b1:90:41:8d:2c;-72;d8:b1:90:41:8d:2f;-73;d8:b1:90:41:8d:2d;-77;88:1f:a1:f:e6:a8;-68;34:62:88:ea:b:2e;-85;28:37:37:13:b3:7b;-81;34:62:88:ea:b:2d;-83;d8:b1:90:41:8d:22;-72;d8:b1:90:41:8d:23;-70;d8:b1:90:41:8d:20;-71;58:97:bd:6e:b0:b0;-78;84:b8:2:e2:a5:9f;-78;84:b8:2:e2:a5:9e;-75;b0:aa:77:a8:cf:ed;-72;b0:aa:77:a8:cf:ee;-75;b0:aa:77:a8:cf:ef;-86;44:74:6c:44:ee:19;-79");
        //Is equal to db entry 1781.43285731;2273.33183673;0.0
        Coordinates result = nna.getLocation(test, db);

        assertNotNull(result);

        assertEquals(result.x, (float) 1781.43285731);
        assertEquals(result.y, (float) 2273.33183673);
        assertEquals(result.z, (float) 0);

    }

    public void testCloseMatch_Simple() throws IOException {

        NearestNeighborAlgorithm nna = new NearestNeighborAlgorithm(new SimpleComparator());
        FingerprintDatabase db = new FingerprintDatabase(solo.getCurrentActivity(),R.raw.test_fingerprints);
        Fingerprint test = Fingerprint.fromScanResult("d8:b1:90:34:5d:70;-71;d8:b1:90:34:5d:71;-50;d8:b1:90:34:5d:72;-73;d8:b1:90:34:5d:73;-78;d8:b1:90:41:8a:3b;-88;d8:b1:90:41:8a:3c;-89;d8:b1:90:41:8a:3d;-88;d8:b1:90:41:8a:3e;-88;b0:aa:77:a8:cf:ec;-89;b0:aa:77:cc:ce:cd;-83;b0:aa:77:cc:ce:ce;-78;b0:aa:77:cc:ce:cc;-77;58:97:bd:62:3:9e;-30;58:97:bd:62:3:9d;-75;f4:4e:5:81:a8:5e;-89;f4:4e:5:81:a8:5d;-86;f4:4e:5:81:a8:5b;-87;58:97:bd:62:3:9b;-74;d8:b1:90:34:5d:7b;-86;d8:b1:90:34:5d:7d;-83;f4:4e:5:ad:65:80;-90;d8:b1:90:23:15:4f;-88;b0:aa:77:cc:ce:c2;-75;b0:aa:77:cc:ce:c3;-76;b0:aa:77:cc:ce:c0;-75;b0:aa:77:cc:ce:c1;-75;58:97:bd:62:3:91;-67;58:97:bd:62:3:90;-58;f4:4e:5:81:a8:51;-82;58:97:bd:62:3:92;-57;d8:b1:90:41:8a:32;-79;d8:b1:90:41:8a:33;-80;d8:b1:90:41:8a:30;-80;d8:b1:90:41:8a:31;-76;b0:aa:77:a8:b5:83;-76;b0:aa:77:a8:b5:81;-79;b0:aa:77:a8:b5:80;-77;d8:b1:90:3c:7e:8f;-75;d8:b1:90:3c:7e:8d;-82;d8:b1:90:3c:7e:8c;-76;d8:b1:90:3c:7e:8b;-81;58:97:bd:6e:b0:be;-78;56:25:8d:aa:da:a8;-66;b0:aa:77:a8:bb:1d;-89;d8:b1:90:41:8d:21;-81;f4:cf:e2:62:da:1e;-87;34:21:9:14:de:60;-81;58:97:bd:62:3:93;-57;f4:cf:e2:62:da:1d;-87;b0:aa:77:9f:14:4f;-67;58:97:bd:6e:b0:b3;-80;58:97:bd:6e:b0:b2;-80;34:62:88:ea:b:2f;-88;d8:b1:90:45:18:de;-90;f4:4e:5:ad:65:82;-72;f4:4e:5:ad:65:83;-77;d8:b1:90:3c:83:4d;-68;d8:b1:90:3c:83:4e;-64;00:1d:e6:27:14:c2;-69;00:1d:e6:27:14:c3;-68;00:1d:e6:27:14:c0;-73;00:1d:e6:27:14:c1;-73;b0:aa:77:9f:11:2c;-68;b0:aa:77:9f:11:2d;-75;b0:aa:77:9f:11:2e;-90;b0:aa:77:9f:11:2f;-77;00:3a:98:b9:9c:cb;-63;00:3a:98:b9:9c:cc;-83;00:3a:98:b9:9c:cd;-64;00:3a:98:b9:9c:ce;-80;00:3a:98:b9:9c:cf;-64;b0:aa:77:9f:14:41;-68;b0:aa:77:9f:14:40;-70;84:b8:2:e2:a5:9c;-70;b0:aa:77:9f:12:cf;-81;b0:aa:77:9f:12:cd;-82;b0:aa:77:9f:12:ce;-64;b0:aa:77:9f:12:cb;-65;b0:aa:77:9f:12:cc;-63;00:1d:e6:27:14:cf;-74;00:1d:e6:27:14:ce;-81;d8:b1:90:3c:76:c;-85;b0:aa:77:9f:11:20;-65;b0:aa:77:9f:11:21;-65;b0:aa:77:9f:11:22;-65;b0:aa:77:9f:11:23;-64;f4:4e:5:ad:65:8f;-81;f4:4e:5:ad:65:8d;-78;f4:4e:5:ad:65:8e;-73;f4:4e:5:ad:65:8b;-79;58:97:bd:62:3:9f;-72;b0:aa:77:9f:12:c2;-62;b0:aa:77:9f:12:c3;-64;b0:aa:77:9f:12:c0;-63;b0:aa:77:9f:12:c1;-60;f4:cf:e2:62:da:10;-82;f4:cf:e2:62:da:13;-80;b0:aa:77:9f:14:4e;-90;b0:aa:77:9f:14:4d;-77;84:b8:2:e2:a1:4c;-86;84:b8:2:e2:a1:4b;-67;84:b8:2:e2:a1:4e;-89;84:b8:2:e2:a1:4d;-64;b0:aa:77:9f:14:4b;-77;84:b8:2:e2:a5:92;-71;84:b8:2:e2:a5:93;-75;84:b8:2:e2:a5:90;-67;84:b8:2:e2:a5:91;-83;d8:b1:90:41:8d:2b;-77;d8:b1:90:41:8d:2c;-72;d8:b1:90:41:8d:2f;-73;d8:b1:90:41:8d:2d;-77;88:1f:a1:f:e6:a8;-68;34:62:88:ea:b:2e;-85;28:37:37:13:b3:7b;-81;34:62:88:ea:b:2d;-83;d8:b1:90:41:8d:22;-72;d8:b1:90:41:8d:23;-70;d8:b1:90:41:8d:20;-71;58:97:bd:6e:b0:b0;-78;84:b8:2:e2:a5:9f;-78;84:b8:2:e2:a5:9e;-75;b0:aa:77:a8:cf:ed;-72;b0:aa:77:a8:cf:ee;-75;b0:aa:77:a8:cf:ef;-86;44:74:6c:44:ee:19;-79");
        //Is close to db entry 1781.43285731;2273.33183673;0.0
        Coordinates result = nna.getLocation(test, db);

        assertNotNull(result);

        assertEquals(result.x, (float) 1781.43285731);
        assertEquals(result.y, (float) 2273.33183673);
        assertEquals(result.z, (float) 0);

    }

    public void testSimpleMatch_Cosine() throws IOException {

        NearestNeighborAlgorithm nna = new NearestNeighborAlgorithm(new CosineComparator());
        FingerprintDatabase db = new FingerprintDatabase(solo.getCurrentActivity(),R.raw.test_fingerprints);
        Fingerprint test = Fingerprint.fromScanResult("d8:b1:90:34:5d:70;-71;d8:b1:90:34:5d:71;-79;d8:b1:90:34:5d:72;-73;d8:b1:90:34:5d:73;-78;d8:b1:90:41:8a:3b;-88;d8:b1:90:41:8a:3c;-89;d8:b1:90:41:8a:3d;-88;d8:b1:90:41:8a:3e;-88;b0:aa:77:a8:cf:ec;-89;b0:aa:77:cc:ce:cd;-83;b0:aa:77:cc:ce:ce;-78;b0:aa:77:cc:ce:cc;-77;58:97:bd:62:3:9e;-73;58:97:bd:62:3:9d;-75;f4:4e:5:81:a8:5e;-89;f4:4e:5:81:a8:5d;-86;f4:4e:5:81:a8:5b;-87;58:97:bd:62:3:9b;-74;d8:b1:90:34:5d:7b;-86;d8:b1:90:34:5d:7d;-83;f4:4e:5:ad:65:80;-76;d8:b1:90:23:15:4f;-88;b0:aa:77:cc:ce:c2;-75;b0:aa:77:cc:ce:c3;-76;b0:aa:77:cc:ce:c0;-75;b0:aa:77:cc:ce:c1;-75;58:97:bd:62:3:91;-67;58:97:bd:62:3:90;-58;f4:4e:5:81:a8:51;-82;58:97:bd:62:3:92;-57;d8:b1:90:41:8a:32;-79;d8:b1:90:41:8a:33;-80;d8:b1:90:41:8a:30;-80;d8:b1:90:41:8a:31;-65;04:a1:51:30:c3:1c;-76;b0:aa:77:a8:b5:83;-76;b0:aa:77:a8:b5:82;-76;b0:aa:77:a8:b5:81;-79;b0:aa:77:a8:b5:80;-77;d8:b1:90:3c:7e:8f;-75;d8:b1:90:3c:7e:8d;-82;d8:b1:90:3c:7e:8c;-76;d8:b1:90:3c:7e:8b;-81;58:97:bd:6e:b0:be;-78;56:25:8d:aa:da:a8;-66;b0:aa:77:a8:bb:1d;-89;d8:b1:90:41:8d:21;-81;f4:cf:e2:62:da:1e;-87;34:21:9:14:de:60;-81;58:97:bd:62:3:93;-57;f4:cf:e2:62:da:1d;-87;b0:aa:77:9f:14:4f;-67;58:97:bd:6e:b0:b3;-80;58:97:bd:6e:b0:b2;-80;34:62:88:ea:b:2f;-88;d8:b1:90:45:18:de;-90;f4:4e:5:ad:65:82;-72;f4:4e:5:ad:65:83;-77;d8:b1:90:3c:83:4d;-68;d8:b1:90:3c:83:4e;-64;00:1d:e6:27:14:c2;-69;00:1d:e6:27:14:c3;-68;00:1d:e6:27:14:c0;-73;00:1d:e6:27:14:c1;-73;b0:aa:77:9f:11:2c;-68;b0:aa:77:9f:11:2d;-75;b0:aa:77:9f:11:2e;-90;b0:aa:77:9f:11:2f;-77;00:3a:98:b9:9c:cb;-63;00:3a:98:b9:9c:cc;-83;00:3a:98:b9:9c:cd;-64;00:3a:98:b9:9c:ce;-80;00:3a:98:b9:9c:cf;-64;b0:aa:77:9f:14:41;-68;b0:aa:77:9f:14:40;-70;84:b8:2:e2:a5:9c;-70;b0:aa:77:9f:12:cf;-81;b0:aa:77:9f:12:cd;-82;b0:aa:77:9f:12:ce;-64;b0:aa:77:9f:12:cb;-65;b0:aa:77:9f:12:cc;-63;00:1d:e6:27:14:cf;-74;00:1d:e6:27:14:ce;-81;d8:b1:90:3c:76:c;-85;b0:aa:77:9f:11:20;-65;b0:aa:77:9f:11:21;-65;b0:aa:77:9f:11:22;-65;b0:aa:77:9f:11:23;-64;f4:4e:5:ad:65:8f;-81;f4:4e:5:ad:65:8d;-78;f4:4e:5:ad:65:8e;-73;f4:4e:5:ad:65:8b;-79;58:97:bd:62:3:9f;-72;b0:aa:77:9f:12:c2;-62;b0:aa:77:9f:12:c3;-64;b0:aa:77:9f:12:c0;-63;b0:aa:77:9f:12:c1;-60;f4:cf:e2:62:da:10;-82;f4:cf:e2:62:da:13;-80;b0:aa:77:9f:14:4e;-90;b0:aa:77:9f:14:4d;-77;84:b8:2:e2:a1:4c;-86;84:b8:2:e2:a1:4b;-67;84:b8:2:e2:a1:4e;-89;84:b8:2:e2:a1:4d;-64;b0:aa:77:9f:14:4b;-77;84:b8:2:e2:a5:92;-71;84:b8:2:e2:a5:93;-75;84:b8:2:e2:a5:90;-67;84:b8:2:e2:a5:91;-83;d8:b1:90:41:8d:2b;-77;d8:b1:90:41:8d:2c;-72;d8:b1:90:41:8d:2f;-73;d8:b1:90:41:8d:2d;-77;88:1f:a1:f:e6:a8;-68;34:62:88:ea:b:2e;-85;28:37:37:13:b3:7b;-81;34:62:88:ea:b:2d;-83;d8:b1:90:41:8d:22;-72;d8:b1:90:41:8d:23;-70;d8:b1:90:41:8d:20;-71;58:97:bd:6e:b0:b0;-78;84:b8:2:e2:a5:9f;-78;84:b8:2:e2:a5:9e;-75;b0:aa:77:a8:cf:ed;-72;b0:aa:77:a8:cf:ee;-75;b0:aa:77:a8:cf:ef;-86;44:74:6c:44:ee:19;-79");
        //Is equal to db entry 1781.43285731;2273.33183673;0.0
        Coordinates result = nna.getLocation(test, db);

        assertNotNull(result);

        assertEquals(result.x, (float) 1781.43285731);
        assertEquals(result.y, (float) 2273.33183673);
        assertEquals(result.z, (float) 0);

    }

    public void testCloseMatch_Cosine() throws IOException {

        NearestNeighborAlgorithm nna = new NearestNeighborAlgorithm(new CosineComparator());
        FingerprintDatabase db = new FingerprintDatabase(solo.getCurrentActivity(),R.raw.test_fingerprints);
        Fingerprint test = Fingerprint.fromScanResult("d8:b1:90:34:5d:70;-71;d8:b1:90:34:5d:71;-50;d8:b1:90:34:5d:72;-73;d8:b1:90:34:5d:73;-78;d8:b1:90:41:8a:3b;-88;d8:b1:90:41:8a:3c;-89;d8:b1:90:41:8a:3d;-88;d8:b1:90:41:8a:3e;-88;b0:aa:77:a8:cf:ec;-89;b0:aa:77:cc:ce:cd;-83;b0:aa:77:cc:ce:ce;-78;b0:aa:77:cc:ce:cc;-77;58:97:bd:62:3:9e;-30;58:97:bd:62:3:9d;-75;f4:4e:5:81:a8:5e;-89;f4:4e:5:81:a8:5d;-86;f4:4e:5:81:a8:5b;-87;58:97:bd:62:3:9b;-74;d8:b1:90:34:5d:7b;-86;d8:b1:90:34:5d:7d;-83;f4:4e:5:ad:65:80;-90;d8:b1:90:23:15:4f;-88;b0:aa:77:cc:ce:c2;-75;b0:aa:77:cc:ce:c3;-76;b0:aa:77:cc:ce:c0;-75;b0:aa:77:cc:ce:c1;-75;58:97:bd:62:3:91;-67;58:97:bd:62:3:90;-58;f4:4e:5:81:a8:51;-82;58:97:bd:62:3:92;-57;d8:b1:90:41:8a:32;-79;d8:b1:90:41:8a:33;-80;d8:b1:90:41:8a:30;-80;d8:b1:90:41:8a:31;-76;b0:aa:77:a8:b5:83;-76;b0:aa:77:a8:b5:81;-79;b0:aa:77:a8:b5:80;-77;d8:b1:90:3c:7e:8f;-75;d8:b1:90:3c:7e:8d;-82;d8:b1:90:3c:7e:8c;-76;d8:b1:90:3c:7e:8b;-81;58:97:bd:6e:b0:be;-78;56:25:8d:aa:da:a8;-66;b0:aa:77:a8:bb:1d;-89;d8:b1:90:41:8d:21;-81;f4:cf:e2:62:da:1e;-87;34:21:9:14:de:60;-81;58:97:bd:62:3:93;-57;f4:cf:e2:62:da:1d;-87;b0:aa:77:9f:14:4f;-67;58:97:bd:6e:b0:b3;-80;58:97:bd:6e:b0:b2;-80;34:62:88:ea:b:2f;-88;d8:b1:90:45:18:de;-90;f4:4e:5:ad:65:82;-72;f4:4e:5:ad:65:83;-77;d8:b1:90:3c:83:4d;-68;d8:b1:90:3c:83:4e;-64;00:1d:e6:27:14:c2;-69;00:1d:e6:27:14:c3;-68;00:1d:e6:27:14:c0;-73;00:1d:e6:27:14:c1;-73;b0:aa:77:9f:11:2c;-68;b0:aa:77:9f:11:2d;-75;b0:aa:77:9f:11:2e;-90;b0:aa:77:9f:11:2f;-77;00:3a:98:b9:9c:cb;-63;00:3a:98:b9:9c:cc;-83;00:3a:98:b9:9c:cd;-64;00:3a:98:b9:9c:ce;-80;00:3a:98:b9:9c:cf;-64;b0:aa:77:9f:14:41;-68;b0:aa:77:9f:14:40;-70;84:b8:2:e2:a5:9c;-70;b0:aa:77:9f:12:cf;-81;b0:aa:77:9f:12:cd;-82;b0:aa:77:9f:12:ce;-64;b0:aa:77:9f:12:cb;-65;b0:aa:77:9f:12:cc;-63;00:1d:e6:27:14:cf;-74;00:1d:e6:27:14:ce;-81;d8:b1:90:3c:76:c;-85;b0:aa:77:9f:11:20;-65;b0:aa:77:9f:11:21;-65;b0:aa:77:9f:11:22;-65;b0:aa:77:9f:11:23;-64;f4:4e:5:ad:65:8f;-81;f4:4e:5:ad:65:8d;-78;f4:4e:5:ad:65:8e;-73;f4:4e:5:ad:65:8b;-79;58:97:bd:62:3:9f;-72;b0:aa:77:9f:12:c2;-62;b0:aa:77:9f:12:c3;-64;b0:aa:77:9f:12:c0;-63;b0:aa:77:9f:12:c1;-60;f4:cf:e2:62:da:10;-82;f4:cf:e2:62:da:13;-80;b0:aa:77:9f:14:4e;-90;b0:aa:77:9f:14:4d;-77;84:b8:2:e2:a1:4c;-86;84:b8:2:e2:a1:4b;-67;84:b8:2:e2:a1:4e;-89;84:b8:2:e2:a1:4d;-64;b0:aa:77:9f:14:4b;-77;84:b8:2:e2:a5:92;-71;84:b8:2:e2:a5:93;-75;84:b8:2:e2:a5:90;-67;84:b8:2:e2:a5:91;-83;d8:b1:90:41:8d:2b;-77;d8:b1:90:41:8d:2c;-72;d8:b1:90:41:8d:2f;-73;d8:b1:90:41:8d:2d;-77;88:1f:a1:f:e6:a8;-68;34:62:88:ea:b:2e;-85;28:37:37:13:b3:7b;-81;34:62:88:ea:b:2d;-83;d8:b1:90:41:8d:22;-72;d8:b1:90:41:8d:23;-70;d8:b1:90:41:8d:20;-71;58:97:bd:6e:b0:b0;-78;84:b8:2:e2:a5:9f;-78;84:b8:2:e2:a5:9e;-75;b0:aa:77:a8:cf:ed;-72;b0:aa:77:a8:cf:ee;-75;b0:aa:77:a8:cf:ef;-86;44:74:6c:44:ee:19;-79");
        //Is close to db entry 1781.43285731;2273.33183673;0.0
        Coordinates result = nna.getLocation(test, db);

        assertNotNull(result);

        assertEquals(result.x, (float) 1781.43285731);
        assertEquals(result.y, (float) 2273.33183673);
        assertEquals(result.z, (float) 0);

    }

}
