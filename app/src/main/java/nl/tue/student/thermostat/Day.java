package nl.tue.student.thermostat;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.quentindommerc.superlistview.SuperListview;
import com.quentindommerc.superlistview.SwipeDismissListViewTouchListener;

import java.util.List;
import java.util.ArrayList;

public class Day extends AppCompatActivity {
    private SuperListview superList;
    String day;
    private ArrayAdapter<String> mAdapter;
    static Dialog d;
    static Dialog addDialog;

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
            int selectedPosition;

            @Override
            public boolean canDismiss(int position) {
                selectedPosition = position;
                return true;
            }

            @Override
            public void onDismiss(final ListView listView, final int[] reverseSortedPositions) {
                d = new Dialog(superList.getContext());
                d.setTitle("Warning!");
                d.setContentView(R.layout.dialog2);

                Button cancel = (Button) d.findViewById(R.id.button3);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });

                Button ok = (Button) d.findViewById(R.id.button4);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdapter.remove(mAdapter.getItem(selectedPosition));
                        d.dismiss();
                    }
                });

                d.show();
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
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */
                addDialog = new Dialog(superList.getContext());
                addDialog.setTitle("Add schedule");
                addDialog.setContentView(R.layout.dialog3);

                Button cancel = (Button) addDialog.findViewById(R.id.button5);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addDialog.dismiss();
                    }
                });

                Button add = (Button) addDialog.findViewById(R.id.button6);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdapter.add("new");
                        addDialog.dismiss();
                    }
                });

                addDialog.show();
            }
        });
    }

}
