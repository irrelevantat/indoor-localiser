package hk.ust.cse.com107x.indoor;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener {
    Button OkButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkButton= (Button) findViewById(R.id.button);
        OkButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onClick(View v)
    {

        EditText xcor = (EditText) findViewById(R.id.xcor);
        EditText ycor = (EditText) findViewById(R.id.ycor);
        EditText zcor = (EditText) findViewById(R.id.zcor);
        //String x = xcor.getText().toString();
        Double X = Double.parseDouble(xcor.getText().toString());
        Double Y = Double.parseDouble(ycor.getText().toString());
        Double Z = Double.parseDouble(zcor.getText().toString());
        switch(v.getId())
        {
            case R.id.button:
                //textMessage.setText("You are at " + X + Y +Z +" !");
                Intent inf = new Intent(MainActivity.this,MapActivity.class);
                inf.putExtra("cordx", X);
                inf.putExtra("cordy",Y);
                inf.putExtra("cordz",Z);
                startActivity(inf);
                break;
            default:
                break;
        }

    }
}
