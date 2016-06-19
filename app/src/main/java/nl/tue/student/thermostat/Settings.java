package nl.tue.student.thermostat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);


        listView = (ListView) findViewById(R.id.list);

        String[] values = new String[] { "",
                "THIS IS FOR DEMO PURPOSES, NONE OF THESE SETTINGS HAVE ACTUALLY BEEN IMPLEMENTED",
                "Change temperature scale",
                "Change date and time",
                "Connect to a new thermostat",
                "Reboot thermostat",
                "Reset the app",
                "Change app's theme",
                "Notification behaviour"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, values);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                //Toast.makeText(getApplicationContext(),
                //        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                //        .show();

            }

        });

    }
        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent parentIntent = NavUtils.getParentActivityIntent(this);
                    if (parentIntent == null) {
                        finish();
                        return true;
                    } else {
                        parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(parentIntent);
                        finish();
                        return true;
                    }
                case R.id.help:
                    //do something helpful
                    ;
                    //Toast toast = Toast.makeText(getApplicationContext(), "Not yet implemented", Toast.LENGTH_LONG);
                    //toast.show();
                    for (int i = 0; i < 4; i++) {
                        Toast.makeText(getApplicationContext(), "This is a demo of how the settings activity would look. It's not working currently though. You can go back to the homepage by pressing the 'back' button.", Toast.LENGTH_LONG).show();
                    }

                    return true;
            }
            return super.onOptionsItemSelected(item);
        }


        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.help_menu, menu);
            return true;
        }

}
