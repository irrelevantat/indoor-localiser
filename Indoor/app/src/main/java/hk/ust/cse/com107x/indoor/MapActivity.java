package hk.ust.cse.com107x.indoor;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by shubham-kapoor on 18/12/15.
 */
public class MapActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        ImageView img = (ImageView)findViewById(R.id.imageView2);
        Double X = getIntent().getDoubleExtra("cordx",0);
        Double Y = getIntent().getDoubleExtra("cordy",0);
        Double Z = getIntent().getDoubleExtra("cordz",0);
        if(Z.toString().equals("0.0"))
        {img.setBackgroundResource(R.drawable.exactum0);
        }
        else if (Z.toString().equals("1.0"))
            img.setBackgroundResource(R.drawable.exactum1);
        else if (Z.toString().equals("2.0"))
            img.setBackgroundResource(R.drawable.exactum2);
        else
            img.setBackgroundResource(R.drawable.exactum3);
        Log.d("Result", Z.toString());

    }
}
