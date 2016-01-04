package localiser;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by sebastian on 26/12/15.
 */
public class LocaliserController {

    public class LocaliserException extends Exception
    {

    }

    public interface Callback{
        public void locationUpdated(int x, int y, int z);
        public void locationFail(LocaliserException exc);
    }


    private Timer timer;

    public void registerForLocationUpdates(final Callback callback, final int interval)
    {
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {

                //call algorithm and pass on to callback
                //temporarily return random values
                Random r = new Random();
                callback.locationUpdated(r.nextInt(2000), r.nextInt(2000), r.nextInt(2000));
            }
        };
        this.timer = new Timer();
        this.timer.schedule(task, 0, interval);


    }
    public void unregisterForLocationUpdates(){
        this.timer.cancel();
        this.timer = null;
    }

}
