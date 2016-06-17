package nl.tue.student.thermostat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.thermostatapp.util.CorruptWeekProgramException;
import org.thermostatapp.util.HeatingSystem;
import org.thermostatapp.util.Switch;
import org.thermostatapp.util.WeekProgram;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

//import com.triggertrap.seekarc.SeekArc;


public class Homepage extends Fragment {
    TextView targetTemp;
    TextView currentTime;
    TextView currentTemp;
    TextView currentDay;
    ImageButton imageButtonDown;
    ImageButton imageButtonUp;
    SeekArc seekArc;
    boolean seekArcIsBeingTouched = false;
    double arcTemp;
    String getParamTime;    //time pulled from the server
    TimerTask task; //Timertask that runs every clockdelay
    Thread secondaryThreadHome; //Thread that gets started by task
    long clockDelay = 200; //delay for updating the clock


    Time time = MainActivity.time;
    //WeekProgram weekProgram = MainActivity.weekProgram;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage, container, false);

        //importing current temperature text
        targetTemp = (TextView)view.findViewById(R.id.targetTemp);

        //importing current time
        currentTime = (TextView)view.findViewById(R.id.currentTime);

        //importing current temp
        currentTemp = (TextView) view.findViewById(R.id.currentTemp);

        //importing current day
        currentDay = (TextView) view.findViewById(R.id.currentDay);

        //import down button
        imageButtonDown = (ImageButton) view.findViewById(R.id.imageButtonDown);

        //import up button
        imageButtonUp = (ImageButton) view.findViewById(R.id.imageButtonUp);

        //THIS IS HOW YOU ADD STUFF TO THE WEEK SCHEDULE
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WeekProgram weekProgram = HeatingSystem.getWeekProgram();
                    weekProgram.setDefault();
                    weekProgram.data.get("Tuesday").set(3, new Switch("night", true, "20:04"));
                    weekProgram.data.get("Tuesday").set(7, new Switch("day", true, "22:30"));
                    weekProgram.data.get("Sunday").set(5, new Switch("night", true, "19:00"));
                    HeatingSystem.setWeekProgram(weekProgram);
                } catch (ConnectException e) {
                    //System.err.println("Error from getdata " + e);
                } catch (CorruptWeekProgramException e) {
                    //e.printStackTrace();
                }
            }
        }).start();




        //run the thread every clockDelay
        task = new TimerTask() {
            @Override
            public void run() {

                //THIS IS HOW YOU GET DATA FROM THE WEEKPROGRAM ON THE SERVER AND DO SOMETHING WITH IT
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WeekProgram weekProgram = HeatingSystem.getWeekProgram();
                            ArrayList<Switch> todaysSwitches = weekProgram.data.get(time.getDaysString());



                            for (int i=0; i < todaysSwitches.size(); i++) {
                                Switch aSwitch = todaysSwitches.get(i);
                                if (aSwitch.getState() /*and if the time is after the current time*/) {
                                    final int icon;
                                    final String time;
                                    final String temp;

                                    time = aSwitch.getTime();
                                    if (aSwitch.getType().equals("day")) {
                                        icon = R.drawable.day;
                                        temp = Double.toString(MainActivity.currentDayTemp);
                                    } else {
                                        icon = R.drawable.night;
                                        temp = Double.toString(MainActivity.currentNightTemp);
                                    }

                                    //THESE ARE NEEDED BECAUSE THIS THREAD CAN'T ACCESS THE THINGS YOU DECLARE IN ONCREATE


                                }
                            }
                        } catch (ConnectException e) {
                            //System.err.println("Error from getdata " + e);
                        } catch (CorruptWeekProgramException e) {
                            //e.printStackTrace();
                        }
                    }
                }).run();

                currentTime.post(new Runnable() {
                    @Override
                    public void run() {
                        currentTime.setText(time.getHoursString() + ":" + time.getMinutesString());
                        time.increaseTime();
                        currentTemp.setText(Double.toString(MainActivity.currentTemp) + " \u00B0" + "C");
                        currentDay.setText(time.getDaysString());

                        if (!seekArcIsBeingTouched) {
                            seekArc.setProgress((int) ((10 *MainActivity.targetTemp) - 50));
                        }

                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 0, clockDelay);



        //importing the arc
        seekArc = (SeekArc)view.findViewById(R.id.seekArc);

        seekArc.setMax(250);
        seekArc.setStartAngle(0);
        seekArc.setSweepAngle(280);
        seekArc.setTouchInSide(true);
        seekArc.setArcWidth(10);
        seekArc.setArcRotation(220);
        seekArc.setProgressWidth(50);
        seekArc.setRoundedEdges(true);

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

            long mid = (long) (((1 - progress)*l1) + (progress * l2)); //truncating not rounding

            midStr = Long.toString(mid, 16);
            if (midStr.length() == 1) {
                result.append("0");
            }
            result.append(midStr.toUpperCase());

        }
        seekArc.setProgressColor(Color.parseColor(result.toString()));
        seekArc.setArcColor(Color.parseColor(result.toString()));

        seekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int j, boolean b) {

                // hard to set the slider to the extremes, this takes care of that
                arcTemp = (double)seekArc.getProgress()/10+5;
                if(arcTemp>30) arcTemp = 30;
                if(arcTemp<5) arcTemp = 5;

                targetTemp.setText(Double.toString(arcTemp) + " \u00B0" + "C"); //tweaky temporary solution



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

                    long mid = (long) (((1 - progress)*l1) + (progress * l2)); //truncating not rounding

                    midStr = Long.toString(mid, 16);
                    if (midStr.length() == 1) {
                        result.append("0");
                    }
                    result.append(midStr.toUpperCase());

                }
                seekArc.setProgressColor(Color.parseColor(result.toString()));
                seekArc.setArcColor(Color.parseColor(result.toString()));
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {
                seekArcIsBeingTouched = true;
            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HeatingSystem.put("targetTemperature", Double.toString(arcTemp));

                        } catch (Exception e) {
                            System.err.println("Error from getdata "+e);
                        }
                    }
                }).start();
                seekArcIsBeingTouched = false;
                MainActivity.targetTemp = ((double) seekArc.getProgress())/10 + 5;
            }
        });

        imageButtonDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                seekArc.setProgress(seekArc.getProgress()-1);
                arcTemp = (double)seekArc.getProgress()/10+5;
                if(arcTemp>30) arcTemp = 30;
                if(arcTemp<5) arcTemp = 5;
                arcTemp = (double) Math.round(arcTemp * 10) / 10;
                System.out.println(arcTemp);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HeatingSystem.put("targetTemperature", Double.toString(arcTemp));

                        } catch (Exception e) {
                            System.err.println("Error from getdata "+e);
                        }
                    }
                }).start();
               MainActivity.targetTemp = MainActivity.targetTemp - 0.1;
            }
        });

        imageButtonUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                seekArc.setProgress(seekArc.getProgress()+1);
                arcTemp = (double)seekArc.getProgress()/10+5;
                if(arcTemp>30) arcTemp = 30;
                if(arcTemp<5) arcTemp = 5;
                arcTemp = (double) Math.round(arcTemp * 10) / 10;
                System.out.println(arcTemp);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HeatingSystem.put("targetTemperature", Double.toString(arcTemp));

                        } catch (Exception e) {
                            System.err.println("Error from getdata "+e);
                        }
                    }
                }).start();
                MainActivity.targetTemp = MainActivity.targetTemp + 0.1;
            }
        });


        //updating the current temperature
        targetTemp.setText(Double.toString(((double) seekArc.getProgress())/10 + 5) + " \u00B0" + "C");





        return view;
    }

    public static void setUpcomingChangesListVisible(boolean b) {
        if (!b) {
            //customlistadapter.viewListVisible(false);
            //listView.setBackgroundResource(R.drawable.noschedule);   ////background of upcoming changes!!
        } else if (b) {
            //customlistadapter.viewListVisible(true);
            //listView.setBackgroundResource(0);
        }
    }




}

