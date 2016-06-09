package nl.tue.student.thermostat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.thermostatapp.util.HeatingSystem;

import java.util.Timer;
import java.util.TimerTask;

//import com.triggertrap.seekarc.SeekArc;


public class Homepage extends Fragment {
    TextView targetTemp;
    TextView currentTime;
    String getParamTime;    //time pulled from the server
    TimerTask task; //Timertask that runs every clockdelay
    Thread secondaryThreadHome; //Thread that gets started by task
    long clockDelay = 100; //delay for updating the clock

    static CustomListAdapter customlistadapter;
    static ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage, container, false);

        //importing current temperature text
        targetTemp = (TextView)view.findViewById(R.id.targetTemp);

        //importing current time
        currentTime = (TextView)view.findViewById(R.id.currentTime);

        //secondary thread for pulling network data and refreshing the upcoming changes list
        secondaryThreadHome = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getParamTime = HeatingSystem.get("time");
                    currentTime.post(new Runnable() {
                        @Override
                        public void run() {
                            currentTime.setText(getParamTime);
                        }
                    });
                } catch (Exception e) {
                    System.err.println("Error from getdata "+e);
                }
            }
        });
        //run the thread every clockDelay
        task = new TimerTask() {
            @Override
            public void run() {
                secondaryThreadHome.start();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, clockDelay);





        //importing the arc
        SeekArc seekArc = (SeekArc)view.findViewById(R.id.seekArc);

        seekArc.setMax(250);
        seekArc.setStartAngle(0);
        seekArc.setSweepAngle(280);
        seekArc.setTouchInSide(true);
        seekArc.setArcWidth(10);
        seekArc.setArcRotation(220);
        seekArc.setProgressWidth(10);
        seekArc.setRoundedEdges(true);
        //seekArc.setProgressColor(Color.parseColor("#448aff"));
        seekArc.setProgressColor(Color.parseColor("#f44336"));
        seekArc.setArcColor(Color.parseColor("#f44336"));

        seekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int i, boolean b) {
                // hard to set the slider to the extremes, this takes care of that
                double snap = (double )seekArc.getProgress()/10+5;
                if(snap>29.5) snap = 30;
                if(snap<5.5) snap = 5;
                targetTemp.setText(Double.toString((snap)) + " \u00B0" + "C"); //tweaky temporary solution
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {

            }
        });

        //importing the upcoming changes list
        listView = (ListView) view.findViewById(R.id.upcomingChangesList);
        customlistadapter = new CustomListAdapter(this.getContext());
        listView.setAdapter(customlistadapter);

        customlistadapter.addItem("blah", R.drawable.jog);
        customlistadapter.addItem("again", R.drawable.day);
        customlistadapter.addItem("jemoeder", R.mipmap.ic_launcher);
        //customlistadapter.removeFirst();
       // customlistadapter.removeFirst();
       // customlistadapter.removeAll();

        //updating the current temperature
        targetTemp.setText(Double.toString(((double) seekArc.getProgress()/10+5)) + " \u00B0" + "C");





        return view;
    }

    public static void setViewListVisible(boolean b) {
        if (!b) {
            customlistadapter.viewListVisible(false);
            listView.setBackgroundResource(R.mipmap.ic_launcher);   ////background of upcoming changes!!
        } else if (b) {
            customlistadapter.viewListVisible(true);
            listView.setBackgroundResource(0);
        }
    }



}

