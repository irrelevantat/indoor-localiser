package hk.ust.cse.com107x.indoor;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by shubham-kapoor on 18/12/15.
 */
public class MapActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int X = getIntent().getIntExtra("cordx", 0);//these values are for pixels
        int Y = getIntent().getIntExtra("cordy",0);
        //float X = getIntent().getFloatExtra("cordx", 0);
        //float Y = getIntent().getFloatExtra("cordy", 0);
        //int xi = X.intValue();
        Log.d("Result0", Integer.toString(X));
        int xdp = X/(metrics.densityDpi/160);
        int ydp = Y/(metrics.densityDpi/160);
        int Z = getIntent().getIntExtra("cordy", 0);// this value is for floor
        setContentView(R.layout.map_activity);
        LinearLayout layout = (LinearLayout) findViewById(R.id.IL_map);
        ImageView img = new ImageView(this);
        img = (ImageView)findViewById(R.id.imageView2);
//        layout.addView(img);
        if(img.getParent()!=null){
            ((ViewGroup)img.getParent()).removeView(img);
        }
        layout.addView(img);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80,80);
        layoutParams.setMargins(xdp, ydp, 0, 0);

        img.setLayoutParams(layoutParams);
        img.setBackgroundResource(R.drawable.marker);


        if(Z == 0)
        {layout.setBackgroundResource(R.drawable.exactum0);
        }
        else if (Z == 1)
            layout.setBackgroundResource(R.drawable.exactum1);
        else if (Z == 2)
            layout.setBackgroundResource(R.drawable.exactum2);
        else
            layout.setBackgroundResource(R.drawable.exactum3);
        Log.d("Result",Integer.toString(Z));
//        ImageView img2 = (ImageView)
////                layoutparams
////                imgview


    }


}
