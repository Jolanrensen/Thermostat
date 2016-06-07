package nl.tue.student.thermostat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class Schedule extends Fragment {

    ListView listview;
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule,container,false);
        final TextView text = (TextView)view.findViewById(R.id.textView);

        listview = (ListView) view.findViewById(R.id.scheduleList);
        CustomScheduleAdapter customlistadapter = new CustomScheduleAdapter(this.getContext());
        listview.setAdapter(customlistadapter);

        customlistadapter.addItem("Day temperature: ",0);
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
                    case 0:
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
