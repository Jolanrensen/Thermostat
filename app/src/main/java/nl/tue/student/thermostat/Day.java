package nl.tue.student.thermostat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
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
import android.widget.TextView;

import com.quentindommerc.superlistview.SuperListview;
import com.quentindommerc.superlistview.SwipeDismissListViewTouchListener;

import org.thermostatapp.util.HeatingSystem;
import org.thermostatapp.util.Switch;
import org.thermostatapp.util.WeekProgram;

import java.util.List;
import java.util.ArrayList;

public class Day extends AppCompatActivity {
    SuperListview superList;
    String day;
    //private ArrayAdapter<String> adapter;
    CustomScheduleListAdapter adapter;
    static Dialog d;
    static Dialog addDialog;
    FloatingActionButton fab;
    Thread loadFromServer;
    Thread uploadToServer;
    ArrayList<Switch> todaysSwitches;

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
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, lst);

        superList = (SuperListview) findViewById(R.id.superList);
        adapter = new CustomScheduleListAdapter(superList.getContext(),this);
        adapter.viewListVisible(true);

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
                        //adapter.removeItem(selectedPosition);
                        ableDisableFab(adapter);
                        d.dismiss();
                    }
                });

                d.show();
            }
        },false);

        //Retrieve items from the server and add them to the list.
        loadFromServer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WeekProgram weekprogram = HeatingSystem.getWeekProgram();
                    todaysSwitches = weekprogram.data.get(day);
                    superList.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.removeAll();
                            for (int i = 0; i < todaysSwitches.size(); i++) {
                                if (!todaysSwitches.get(i).getTime().equals("00:00")) {
                                    adapter.addItem(todaysSwitches.get(i).getTime(), todaysSwitches.get(i).getType());
                                }
                            }
                            ableDisableFab(adapter);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        loadFromServer.start();

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

                final NumberPicker np0 = (NumberPicker) addDialog.findViewById(R.id.numberPicker3);
                np0.setMinValue(00);
                np0.setMaxValue(11);
                np0.setValue(6);
                np0.setWrapSelectorWheel(false);
                final NumberPicker dp0 = (NumberPicker) addDialog.findViewById(R.id.numberPicker4);
                dp0.setMinValue(00);
                dp0.setMaxValue(59);
                dp0.setValue(30);
                dp0.setWrapSelectorWheel(false);

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
                        //adapter.addItem(np0.getValue()+":" + dp0.getValue(),"");
                        ableDisableFab(adapter);
                        addDialog.dismiss();
                    }
                });

                addDialog.show();
            }
        });
        ableDisableFab(adapter);
    }

    void ableDisableFab(Adapter a){
        if(a.getCount() > 4){
            fab.hide();
            Snackbar.make(fab.getRootView(), "You can not have more than 5 changes", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{
            fab.show();
        }
    }

    void uploadData(final int position, final String type, final boolean bool, final String time){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    WeekProgram weekprogram = HeatingSystem.getWeekProgram();
                    ArrayList<Switch> todaysSwitches = weekprogram.data.get(day);
                    System.out.println("started!");
                    int i = 0;
                    while(i<todaysSwitches.size()&&!todaysSwitches.get(i).getState()){
                        i++;
                    }
                    System.out.println("Day: " + day + ", position: " + position + ", i: " + i + "time of i: " + todaysSwitches.get(i).getTime() + ", type: " + type + ", bool: " + bool + ", time:" + time);
                    weekprogram.data.get(day).set(position+i,new Switch(type,bool,time));
                    HeatingSystem.setWeekProgram(weekprogram);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        recreate();
    }
}
