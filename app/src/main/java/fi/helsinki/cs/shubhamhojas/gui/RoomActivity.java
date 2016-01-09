package fi.helsinki.cs.shubhamhojas.gui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import fi.helsinki.cs.shubhamhojas.R;


public class RoomActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String INTENT_ROOM = "INTENT_ROOM";
    public static final String INTENT_DISTANCE = "INTENT_DISTANCE";
    public static final String INTENT_URL = "INTENT_URL";


    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_activity);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        webView = (WebView) findViewById(R.id.webView);

        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();

        ab.setTitle(getIntent().getStringExtra(INTENT_ROOM));
        ab.setSubtitle(getIntent().getStringExtra(INTENT_DISTANCE));
        webView.loadUrl(getIntent().getStringExtra(INTENT_URL));

    }

    @Override
    public void onClick(View v) {

    }
}
