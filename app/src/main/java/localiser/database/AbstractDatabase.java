package localiser.database;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.LinkedList;

import hk.ust.cse.com107x.indoor.R;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by sebastian on 05/01/16.
 */
public abstract class AbstractDatabase<T> extends AbstractList<T> implements Iterable<T> {

    protected final LinkedList<T> collection = new LinkedList<>();

    public AbstractDatabase(Context c, int resourceID) throws IOException {
        InputStream is = c.getResources().openRawResource(resourceID);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer buf = new StringBuffer();
        assertNotNull(is);
        String str;
        while ((str = reader.readLine()) != null) {
            this.readLine(str);
        }
        is.close();
    }

    protected abstract void readLine(String line);

    @Override
    public T get(int location) {
        return this.collection.get(location);
    }

    @Override
    public Iterator<T> iterator() {
        return this.collection.iterator();
    }

    @Override
    public int size() {
        return this.collection.size();
    }
}
