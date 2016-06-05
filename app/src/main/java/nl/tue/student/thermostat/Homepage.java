package nl.tue.student.thermostat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.triggertrap.seekarc.SeekArc;

/**
 * Created by Belal on 2/3/2016.
 */


public class Homepage extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage, container, false);
        SeekArc seekArc = (SeekArc)view.findViewById(R.id.seekArc);

        ListView listview= (ListView) view.findViewById(R.id.upcomingChangesList);
        listview.setAdapter(new CustomListAdapter(this.getContext()));

        return view;
    }
}
