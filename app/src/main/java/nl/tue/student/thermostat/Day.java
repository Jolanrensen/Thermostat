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
import android.widget.NumberPicker;

import com.quentindommerc.superlistview.SuperListview;
import com.quentindommerc.superlistview.SwipeDismissListViewTouchListener;

import java.util.List;
import java.util.ArrayList;

public class Day extends AppCompatActivity {
    private SuperListview superList;
    String day;
    //private ArrayAdapter<String> mAdapter;
    private CustomListAdapter2 adapter;
    static Dialog d;
    static Dialog addDialog;
    FloatingActionButton fab;

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
        //mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, lst);
        adapter = new CustomListAdapter2(getApplicationContext());
        adapter.viewListVisible(true);

        superList = (SuperListview) findViewById(R.id.superList);
        superList.setAdapter(adapter);
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
                        adapter.removeItem(selectedPosition);
                        ableDisableFab(adapter);
                        d.dismiss();
                    }
                });

                d.show();
            }
        },false);

        //Retrieve items from the server and add them to the list.
        adapter.addItem("Item1","Item1");
        adapter.addItem("Item2","Item2");
        adapter.addItem("Item3","Item3");
        adapter.addItem("Item4","Item4");

        fab = (FloatingActionButton) findViewById(R.id.fab);
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

                NumberPicker np0 = (NumberPicker) addDialog.findViewById(R.id.numberPicker3);
                np0.setMinValue(00);
                np0.setMaxValue(11);
                np0.setWrapSelectorWheel(false);
                NumberPicker dp0 = (NumberPicker) addDialog.findViewById(R.id.numberPicker4);
                dp0.setMinValue(00);
                dp0.setMaxValue(59);
                dp0.setWrapSelectorWheel(false);

                NumberPicker np1 = (NumberPicker) addDialog.findViewById(R.id.numberPicker5);
                np1.setMinValue(00);
                np1.setMaxValue(11);
                np1.setWrapSelectorWheel(false);
                NumberPicker dp1 = (NumberPicker) addDialog.findViewById(R.id.numberPicker6);
                dp1.setMinValue(00);
                dp1.setMaxValue(59);
                dp1.setWrapSelectorWheel(false);

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
                        adapter.addItem("new","new");
                        ableDisableFab(adapter);
                        addDialog.dismiss();
                    }
                });

                addDialog.show();
            }
        });
        ableDisableFab(adapter);
    }

    private void ableDisableFab(Adapter a){
        if(a.getCount() > 4){
            fab.hide();
        }else{
            fab.show();
        }
    }

}
