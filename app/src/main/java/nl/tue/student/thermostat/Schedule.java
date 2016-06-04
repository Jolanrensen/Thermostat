package nl.tue.student.thermostat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class Schedule extends Fragment {

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule,container,false);
        Button button = (Button)view.findViewById(R.id.button);
        final TextView text = (TextView)view.findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text.getText() == "hallo") {
                    text.setText("meneer");
                    Intent intent = new Intent(view.getContext(), TestingWS.class);
                    startActivity(intent);
                } else {
                    text.setText("hallo");
                }

            }
        });




        //Returning the layout file after inflating
        //Change R.layout.schedule in you classes
        return view;
    }
}
