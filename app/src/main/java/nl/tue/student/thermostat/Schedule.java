package nl.tue.student.thermostat;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
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

    protected static TextView tv;
    static Dialog dialog;

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
                            dayTempText.post(new Runnable() {
                                @Override
                                public void run() {
                                    dayTemp = MainActivity.currentDayTemp + " °C";
                                    dayTempText.setText(dayTemp);

                                    nightTemp = MainActivity.currentNightTemp + " °C";
                                    nightTempText.setText(nightTemp);
                                }
                            });

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
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                String day = "";
                switch (position) {
                    case 0:
                        day = "Monday";
                        break;
                    case 1:
                        day = "Tuesday";
                        break;
                    case 2:
                        day = "Wednesday";
                        break;
                    case 3:
                        day = "Thursday";
                        break;
                    case 4:
                        day = "Friday";
                        break;
                    case 5:
                        day = "Saturday";
                        break;
                    case 6:
                        day = "Sunday";
                        break;
                    }
                Intent intent = new Intent(getContext(), Day.class);
                intent.putExtra("day", day);
                startActivity(intent);
                }


        });

        dayTempCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog("Day temperature", dayTempText);
            }
        });

        nightTempCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog("Night temperature", nightTempText);
            }
        });


        //Returning the layout file after inflating
        //Change R.layout.schedule in you classes
        return view;
    }



    private void dialog(final String name, final TextView tempText){
        double temp = 0.0;
        boolean oneLong = false;
        dialog = new Dialog(getContext());
        dialog.setTitle(name);
        dialog.setContentView(R.layout.dialog);
        Button b1 = (Button) dialog.findViewById(R.id.button1);
        Button b2 = (Button) dialog.findViewById(R.id.button2);
        // number picker for whole values
        final NumberPicker np = (NumberPicker) dialog.findViewById(R.id.numberPicker1);
        np.setMaxValue(30);
        np.setMinValue(5);
        try {
            int currentValue = Integer.parseInt(((String) tempText.getText()).substring(0, 2));
            np.setValue(currentValue);
            oneLong = false;
        }catch(NumberFormatException e){
            int currentValue = Integer.parseInt(((String) tempText.getText()).substring(0, 1));
            np.setValue(currentValue);
            oneLong = true;
        }
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });
        // number picker for decimals
        final NumberPicker dp = (NumberPicker) dialog.findViewById(R.id.numberPicker2);
        dp.setMaxValue(9);
        dp.setMinValue(0);
        if(!oneLong){
            dp.setValue(Integer.parseInt((String) ((String) tempText.getText()).substring(3,4)));
        }else{
            dp.setValue(Integer.parseInt((String) ((String) tempText.getText()).substring(2,3)));
        }
        dp.setWrapSelectorWheel(false);
        dp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tempText.setText(String.valueOf(np.getValue()) + "." + String.valueOf(dp.getValue()) + " °C");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            double value = (double)np.getValue() + (double)dp.getValue()/10;
                            if(name.equals("Day temperature")){
                                HeatingSystem.put("dayTemperature", String.valueOf(value));
                            }else{
                                HeatingSystem.put("nightTemperature", String.valueOf(value));
                            }
                        } catch (Exception e) {
                            System.err.println("Error from getdata "+e);
                        }
                    }
                }).start();
                dialog.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


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



