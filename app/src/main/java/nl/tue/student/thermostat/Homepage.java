package nl.tue.student.thermostat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

//import com.triggertrap.seekarc.SeekArc;


import org.thermostatapp.util.HeatingSystem;

import java.util.Timer;
import java.util.TimerTask;


public class Homepage extends Fragment {
    TextView currentTemp;
    TextView currentTime;
    String getParamTime;
    TimerTask task;
    long clockDelay = 100; //delay for updating the clock

    ListView listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage, container, false);

        //importing current temperature text
        currentTemp = (TextView)view.findViewById(R.id.currentTemp);

        //importing current time
        currentTime = (TextView)view.findViewById(R.id.currentTime);

        //creating the timer for the clock

        task = new TimerTask() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getParamTime = HeatingSystem.get("time");
                            currentTime.post(new Runnable() {
                                @Override
                                public void run() {
                                    currentTime.setText(getParamTime);
                                    //update list
                                    listview.invalidateViews();

                                }
                            });
                        } catch (Exception e) {
                            System.err.println("Error from getdata "+e);
                        }
                    }
                }).start();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, clockDelay);


        //importing the arc
        SeekArc seekArc = (SeekArc)view.findViewById(R.id.seekArc);
        //com.triggertrap.seekarc.R.drawable.seek_arc_control_selector = R.drawable.jog;

        seekArc.setMax(250);
        seekArc.setStartAngle(0);
        seekArc.setSweepAngle(280);
        seekArc.setTouchInSide(true);
        seekArc.setArcWidth(10);
        seekArc.setArcRotation(220);
        seekArc.setProgressWidth(40);
        seekArc.setRoundedEdges(true);
        seekArc.setProgressColor(Color.parseColor("#448aff"));
        seekArc.setArcColor(Color.parseColor("#f44336"));

        seekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int i, boolean b) {
               currentTemp.setText(Double.toString(((double) seekArc.getProgress()/10+5)) + " \u00B0" + "C"); //tweaky temporary solution
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {

            }
        });

        //importing the upcoming changes list
        listview = (ListView) view.findViewById(R.id.upcomingChangesList);
        CustomListAdapter customlistadapter = new CustomListAdapter(this.getContext());
        listview.setAdapter(customlistadapter);

        customlistadapter.addItem("test123", R.drawable.jog);
        customlistadapter.addItem("again", R.mipmap.ic_launcher);
        customlistadapter.addItem("jemoeder", R.mipmap.ic_launcher);
        customlistadapter.removeFirst();
       // customlistadapter.removeFirst();
      //  customlistadapter.removeAll();


        currentTemp.setText(Integer.toString(seekArc.getProgress()) + " \u00B0" + "C");
        return view;
    }





}

