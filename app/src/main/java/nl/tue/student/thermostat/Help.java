package nl.tue.student.thermostat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Guus on 27-Jun-16.
 */
public class Help extends AppCompatActivity {
    Intent starterIntent;
    String title, text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.help);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        starterIntent = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title = extras.getString("title");
            text = extras.getString("text");
        }
        toolbar.setTitle(title);

        TextView textview = (TextView) findViewById(R.id.helptext);
        textview.setText(text);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent parentIntent = NavUtils.getParentActivityIntent(this);
        if(parentIntent == null) {
            finish();
            return true;
        } else {
            parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(parentIntent);
            finish();
            return true;
        }
    }

}
