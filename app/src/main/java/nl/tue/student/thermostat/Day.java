package nl.tue.student.thermostat;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.quentindommerc.superlistview.SuperListview;
import com.quentindommerc.superlistview.SwipeDismissListViewTouchListener;

import java.util.List;
import java.util.ArrayList;

public class Day extends AppCompatActivity {
    private SuperListview superList;
    String day;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            day = extras.getString("day");
        }
        toolbar.setTitle(day);
        setSupportActionBar(toolbar);

        ArrayList<String> lst = new ArrayList<String>();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, lst);

        superList = (SuperListview) findViewById(R.id.superList);
        superList.setAdapter(mAdapter);
        superList.setupSwipeToDismiss(new SwipeDismissListViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(ListView listView, int[] reverseSortedPositions) {

            }
        },false);

        //Retrieve items from the server and add them to the list.
        mAdapter.add("Item1");
        mAdapter.add("Item2");
        mAdapter.add("Item3");
        mAdapter.add("Item4");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
