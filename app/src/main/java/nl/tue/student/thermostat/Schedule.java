package nl.tue.student.thermostat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.thermostatapp.util.HeatingSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class Schedule extends Fragment {
    TimerTask taskSchedule;

    long clockDelay = 1000;
    String dayTemp;
    String nightTemp;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule,container,false);
        final TextView text = (TextView)view.findViewById(R.id.textView);

        CardView dayTempCard = (CardView)view.findViewById(R.id.card_view4);
        CardView nightTempCard = (CardView)view.findViewById(R.id.card_view5);

        final TextView dayTempText = (TextView)view.findViewById(R.id.day_temp);
        final TextView nightTempText = (TextView)view.findViewById(R.id.night_temp);

        taskSchedule = new TimerTask() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dayTemp = MainActivity.currentDayTemp + "°C";
                            dayTempText.setText(dayTemp);

                            nightTemp = MainActivity.currentNightTemp + "°C";
                            nightTempText.setText(nightTemp);

                        } catch (Exception e) {
                            System.err.println("Error from getdata "+e);
                        }
                    }
                }).start();
            }
        };
        Timer timer = new Timer();
        timer.schedule(taskSchedule, 0, clockDelay);


        //new list
        final ListView listview = (ListView) view.findViewById(R.id.listView);
        String[] values = new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };


        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                if (position == 0) {
                    Intent intent = new Intent(view.getContext(), Monday.class);
                    startActivity(intent);
                }
            }

        });

        dayTempCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked!");
            }
        });

        nightTempCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //Returning the layout file after inflating
        //Change R.layout.schedule in you classes
        return view;
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}



