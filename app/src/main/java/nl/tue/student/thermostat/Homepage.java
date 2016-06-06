package nl.tue.student.thermostat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.triggertrap.seekarc.SeekArc;


public class Homepage extends Fragment {
    TextView currentTemp;
    TextView currentTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage, container, false);

        //importing current temperature text
        currentTemp = (TextView)view.findViewById(R.id.currentTemp);

        //importing current time
        currentTime = (TextView)view.findViewById(R.id.currentTime);

        //importing the arc
        SeekArc seekArc = (SeekArc)view.findViewById(R.id.seekArc);
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
               currentTemp.setText(Integer.toString(seekArc.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {

            }
        });

        //importing the upcoming changes list
        ListView listview = (ListView) view.findViewById(R.id.upcomingChangesList);
        listview.setAdapter(new CustomListAdapter(this.getContext()));


        currentTemp.setText(Integer.toString(seekArc.getProgress()));
        return view;
    }
}

