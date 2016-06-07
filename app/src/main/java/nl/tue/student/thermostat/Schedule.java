package nl.tue.student.thermostat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.thermostatapp.util.HeatingSystem;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class Schedule extends Fragment {
    TimerTask taskSchedule;
    ListView listview;
    long clockDelay = 1000;
    String dayTemp;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule,container,false);
        final TextView text = (TextView)view.findViewById(R.id.textView);

        listview = (ListView) view.findViewById(R.id.scheduleList);
        final CustomListAdapter customlistadapter = new CustomListAdapter(this.getContext());
        listview.setAdapter(customlistadapter);

        taskSchedule = new TimerTask() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dayTemp = HeatingSystem.get("time");
                            String text = "Day temperature: " + dayTemp + "°C";
                            customlistadapter.title.set(0,text);
                        } catch (Exception e) {
                            System.err.println("Error from getdata "+e);
                        }
                    }
                }).start();
            }
        };
        Timer timer = new Timer();
        timer.schedule(taskSchedule, 0, clockDelay);

        customlistadapter.addItem("Day temperature: " + dayTemp + "°C",0);
        customlistadapter.addItem("Night temperature: ",0);


        customlistadapter.addItem("Monday",0);
        customlistadapter.addItem("Tuesday",0);
        customlistadapter.addItem("Wednesday",0);
        customlistadapter.addItem("Thursday",0);
        customlistadapter.addItem("Friday",0);
        customlistadapter.addItem("Saturday",0);
        customlistadapter.addItem("Sunday",0);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 2:
                        Intent intent = new Intent(view.getContext(), Monday.class);
                        startActivity(intent);
                        break;
                }
            }
        });


        //Returning the layout file after inflating
        //Change R.layout.schedule in you classes
        return view;
    }
}
