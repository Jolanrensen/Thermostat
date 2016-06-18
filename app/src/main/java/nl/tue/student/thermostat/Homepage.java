package nl.tue.student.thermostat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    TimerTask task; //Timertask that runs every clockdelay
    long clockDelay = 200; //delay for updating the clock

    static boolean upcomingChangesVisible = true;
    ImageView noSchedule;

    ImageView iv_icon0;
    TextView txt_name0;
    ImageView iv_icon1;
    TextView txt_name1;
    ImageView iv_icon2;
    TextView txt_name2;
    ArrayList<ImageView> imageViews = new ArrayList<>();
    ArrayList<TextView> textViews = new ArrayList<>();


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

        //import all the images and textviews for the upcoming changes
        iv_icon0 = (ImageView) view.findViewById(R.id.iv_icon0);
        txt_name0 = (TextView) view.findViewById(R.id.txt_name0);
        iv_icon1 = (ImageView) view.findViewById(R.id.iv_icon1);
        txt_name1 = (TextView) view.findViewById(R.id.txt_name1);
        iv_icon2 = (ImageView) view.findViewById(R.id.iv_icon2);
        txt_name2 = (TextView) view.findViewById(R.id.txt_name2);
        imageViews.add(iv_icon0);
        textViews.add(txt_name0);
        imageViews.add(iv_icon1);
        textViews.add(txt_name1);
        imageViews.add(iv_icon2);
        textViews.add(txt_name2);

        noSchedule = (ImageView) view.findViewById(R.id.noSchedule);



        //THIS IS HOW YOU ADD STUFF TO THE WEEK SCHEDULE
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WeekProgram weekProgram = HeatingSystem.getWeekProgram();
                    //weekProgram.setDefault();
                    //weekProgram.setDefault();
                    weekProgram.data.get("Friday").set(3, new Switch("night", true, "20:04"));
                    weekProgram.data.get("Friday").set(7, new Switch("day", true, "22:30"));
                    weekProgram.data.get("Friday").set(5, new Switch("night", true, "19:00"));
                    HeatingSystem.setWeekProgram(weekProgram);
                } catch (ConnectException e) {
                    //System.err.println("Error from getdata " + e);
                } catch (CorruptWeekProgramException e) {
                    //e.printStackTrace();
                }
            }
        }).start(); */




        //run the thread every clockDelay
        task = new TimerTask() {
            @Override
            public void run() {

                //THIS IS HOW YOU GET DATA FROM THE WEEKPROGRAM ON THE SERVER AND DO SOMETHING WITH IT
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //getting it from the server
                            WeekProgram weekProgram = HeatingSystem.getWeekProgram();

                            ArrayList<Switch> todaysSwitches = weekProgram.data.get(time.getDayString());
                            ArrayList<Switch> tomorrowsSwitches = weekProgram.data.get(time.getTomorrowString());
                            //todaysSwitches.add(new Switch("day", true, "22:00"));
                            //todaysSwitches.add(new Switch("night", true, "23:00"));

                            final ArrayList<Integer> icons = new ArrayList<Integer>();
                            final ArrayList<String> texts = new ArrayList<String>();

                            //putting all the correct texts and icons in the arraylists of today
                            for (int i=0; i < todaysSwitches.size(); i++) {
                                Switch aSwitch = todaysSwitches.get(i);
                                if (aSwitch.getState() && time.hasNotYetComeToPass(aSwitch.getTime())) {
                                    final int icon;
                                    final String getTime;
                                    final String temp;

                                    getTime = aSwitch.getTime();
                                    if (aSwitch.getType().equals("day")) {
                                        icon = R.drawable.day;
                                        temp = Double.toString(MainActivity.currentDayTemp);
                                    } else {
                                        icon = R.drawable.night;
                                        temp = Double.toString(MainActivity.currentNightTemp);
                                    }
                                    icons.add(icon);
                                    texts.add(time.getDayString() + " " + getTime + "H  |  " + temp + "°C");
                                }
                            }

                            //putting all the correct texts and icons in the arraylists of tomorrow
                            for (int i=0; i < tomorrowsSwitches.size(); i++) {
                                Switch aSwitch = tomorrowsSwitches.get(i);
                                if (aSwitch.getState()) {
                                    final int icon;
                                    final String getTime;
                                    final String temp;

                                    getTime = aSwitch.getTime();
                                    if (aSwitch.getType().equals("day")) {
                                        icon = R.drawable.day;
                                        temp = Double.toString(MainActivity.currentDayTemp);
                                    } else {
                                        icon = R.drawable.night;
                                        temp = Double.toString(MainActivity.currentNightTemp);
                                    }
                                    icons.add(icon);
                                    texts.add(time.getTomorrowString() + " " + getTime + "H  |  " + temp + "°C  tomorrow");
                                }
                            }

                            //getting the icons and texts from the arraylists to the correct imageviews and textviews
                            txt_name0.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (upcomingChangesVisible) {
                                        noSchedule.setVisibility(View.INVISIBLE);
                                        if (icons.size() > 2) {
                                            for (int i = 0; i < 3; i++) {
                                                imageViews.get(i).setImageResource(icons.get(i));
                                                textViews.get(i).setText(texts.get(i));
                                            }

                                        } else if (icons.size() == 2) {
                                            for (int i = 0; i < 2; i++) {
                                                imageViews.get(i).setImageResource(icons.get(i));
                                                textViews.get(i).setText(texts.get(i));
                                            }
                                            imageViews.get(2).setImageResource(0);
                                            textViews.get(2).setText("");

                                        } else if (icons.size() == 1) {
                                            imageViews.get(0).setImageResource(icons.get(0));
                                            textViews.get(0).setText(texts.get(0));
                                            for (int i = 1; i < 3; i++) {
                                                imageViews.get(i).setImageResource(0);
                                                textViews.get(i).setText(" ");
                                            }

                                        } else {
                                            for (int i = 0; i < 3; i++) {
                                                imageViews.get(i).setImageResource(0);
                                                textViews.get(i).setText("");
                                            }
                                            textViews.get(1).setText("There currently are no upcoming changes");
                                        }
                                    } else {
                                        for (int i=0; i<3; i++) {
                                            imageViews.get(i).setImageResource(0);
                                            textViews.get(i).setText("");
                                        }
                                        noSchedule.setVisibility(View.VISIBLE);
                                    }
                                }
                            });







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
                        currentDay.setText(time.getDayString());

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
            upcomingChangesVisible = false;
        } else if (b) {
            upcomingChangesVisible = true;
        }
    }




}

