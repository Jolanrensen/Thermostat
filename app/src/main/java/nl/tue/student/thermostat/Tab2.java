package nl.tue.student.thermostat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Belal on 2/3/2016.
 */

public class Tab2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2, container, false);
        RotaryKnobView jogView = (RotaryKnobView)view.findViewById(R.id.jogView);
        jogView.setKnobListener(new RotaryKnobView.RotaryKnobListener() {
            @Override
            public void onKnobChanged(int arg) {
                if (arg > 0)
                    ; //rotate right
                else
                    ; //rotate left
            }
        });

        return view;
    }
}
