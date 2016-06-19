package nl.tue.student.thermostat;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;

import com.quentindommerc.superlistview.SuperListview;
import com.quentindommerc.superlistview.SwipeDismissListViewTouchListener;

import org.thermostatapp.util.HeatingSystem;
import org.thermostatapp.util.Switch;
import org.thermostatapp.util.WeekProgram;

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
    boolean daysAvailable = false;
    boolean nightsAvailable = false;
    public String choice;
    public String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
                        time = "00:00";
                        System.out.println(todaysSwitches.get(selectedPosition).getType());
                        uploadData(selectedPosition,todaysSwitches.get(selectedPosition).getType(),false);
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
                    for (int i = 0; i < todaysSwitches.size(); i++) {
                        if(!todaysSwitches.get(i).getState()&&todaysSwitches.get(i).getType().equals("night")){
                            nightsAvailable = true;
                        }
                        if(!todaysSwitches.get(i).getState()&&todaysSwitches.get(i).getType().equals("day")){
                            daysAvailable = true;
                        }
                    }
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
                np0.setFormatter(new NumberPicker.Formatter() {
                    @Override
                    public String format(int i) {
                        return String.format("%02d", i);
                    }
                });
                np0.setMinValue(00);
                np0.setMaxValue(23);
                np0.setValue(6);
                np0.setWrapSelectorWheel(false);
                final NumberPicker dp0 = (NumberPicker) addDialog.findViewById(R.id.numberPicker4);
                dp0.setFormatter(new NumberPicker.Formatter() {
                    @Override
                    public String format(int i) {
                        return String.format("%02d", i);
                    }
                });
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
                        String hours = ""+np0.getValue();
                        if(hours.length() == 1){
                            hours = "0" + hours;
                        }

                        String minutes = ""+dp0.getValue();
                        if(minutes.length() == 1){
                            minutes = "0" + minutes;
                        }
                        time = hours + ":" + minutes;

                        String preChoice = "day";

                        if(daysAvailable && nightsAvailable){

                        }else if(daysAvailable){

                        }else if(nightsAvailable){

                        }

                        choice = preChoice;

                        uploadData(-1,choice,true);
                        addDialog.dismiss();
                    }
                });

                addDialog.show();
            }
        });
        ableDisableFab(adapter);
    }

    void ableDisableFab(Adapter a){
        if(a.getCount() > 9){
            fab.hide();
            Snackbar.make(fab.getRootView(), "You can not have more than 5 changes", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{
            fab.show();
        }
    }

    void uploadData(final int position, final String type, final boolean bool){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    WeekProgram weekprogram = HeatingSystem.getWeekProgram();
                    ArrayList<Switch> todaysSwitches = weekprogram.data.get(day);
                    int newPosition = 0;
                    if(position == -1) {
                        for (int i = 0; i < todaysSwitches.size(); i++) {
                            if (todaysSwitches.get(i).getType().equals(type) && !todaysSwitches.get(i).getState()){
                                newPosition = i;
                            }
                        }
                    }else{
                        int j = 0;
                        while(j < todaysSwitches.size() && !todaysSwitches.get(j).getState()){
                            j++;
                        }
                        newPosition = position + j;
                    }
                    String newType;
                    System.out.println("type: " + type);
                    if(type.equals("")){
                        System.out.println(todaysSwitches.get(newPosition).getType());
                        newType = todaysSwitches.get(newPosition).getType();
                    }else{
                        newType = type;
                    }
                    System.out.println("Day: " + day + ", position: " + newPosition + "time of position: " + todaysSwitches.get(newPosition).getTime() + ", type: " + newType + ", bool: " + bool + ", time:" + time);
                    weekprogram.data.get(day).set(newPosition,new Switch(newType,bool,time));
                    HeatingSystem.setWeekProgram(weekprogram);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        recreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
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
        return super.onOptionsItemSelected(item);
    }
}
