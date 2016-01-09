package fi.helsinki.cs.shubhamhojas.localiser.database;

import android.content.Context;

import java.io.IOException;

import fi.helsinki.cs.shubhamhojas.R;
import fi.helsinki.cs.shubhamhojas.localiser.units.Fingerprint;


/**
 * Created by sebastian on 05/01/16.
 */
public class FingerprintDatabase extends AbstractDatabase<Fingerprint> {

    public FingerprintDatabase(Context c, int resource) throws IOException {
        super(c,resource);
    }
    public FingerprintDatabase(Context c) throws IOException {
        super(c, R.raw.test_fingerprints);
    }

    @Override
    protected void readLine(String line) {
        collection.add(Fingerprint.fromDBString(line));
    }
}
