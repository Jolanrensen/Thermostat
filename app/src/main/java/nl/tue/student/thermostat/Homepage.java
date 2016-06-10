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

        seekArc.setMax(270);
        seekArc.setStartAngle(0);
        seekArc.setSweepAngle(280);
        seekArc.setTouchInSide(true);
        seekArc.setArcWidth(10);
        seekArc.setArcRotation(220);
        seekArc.setProgressWidth(40);
        seekArc.setRoundedEdges(true);
        //seekArc.setProgressColor(Color.parseColor("#448aff"));





        seekArc.setArcColor(Color.parseColor("#f44336"));

        seekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int j, boolean b) {
                // hard to set the slider to the extremes, this takes care of that
                double snap = (double )seekArc.getProgress()/10+4;
                if(snap>30) snap = 30;
                if(snap<5) snap = 5;
                targetTemp.setText(Double.toString((snap)) + " \u00B0" + "C"); //tweaky temporary solution



                String cold = "#448aff";
                String hot = "#d32f2f";
                String midStr = "0";

                StringBuilder result = new StringBuilder("#");
                for (int i=0;i<3;i++) {
                    String h1 = cold.substring(i*2+1, 3+(i*2));
                    String h2 = hot.substring(i*2+1, 3+(i*2));

                    double l1 = Long.parseLong(h1, 16);
                    double l2 = Long.parseLong(h2, 16);

                    double progress = (double) seekArc.getProgress();

                    progress = progress/255;
                    System.out.println(progress);
                    long mid = (long) (((1 - progress)*l1) + (progress * l2)); //truncating not rounding

                    midStr = Long.toString(mid, 16);
                    if (midStr.length() == 1) {
                        result.append("0");
                    }
                    result.append(midStr.toUpperCase());

                }
                seekArc.setProgressColor(Color.parseColor(result.toString()));

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

        customlistadapter.addItem("12:45 PM  |  20 °C", R.drawable.night);
        customlistadapter.addItem("14:30 PM  |  18 °C", R.drawable.day);
        customlistadapter.addItem("18:00 PM  |  20 °C", R.drawable.night);
        customlistadapter.addItem("20:00 PM  |  20 °C", R.drawable.night);
        customlistadapter.addItem("22:00 PM  |  18 °C", R.drawable.day);
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

