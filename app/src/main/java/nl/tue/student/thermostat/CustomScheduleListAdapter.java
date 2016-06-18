package nl.tue.student.thermostat;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.quentindommerc.superlistview.SuperListview;

import java.util.ArrayList;


public class CustomScheduleListAdapter extends BaseAdapter {
    ArrayList<String> time = new ArrayList<String>();
    ArrayList<String> switchto = new ArrayList<String>();
    boolean viewListVisible;
    Day day;
    private Context context;
    Dialog d;
    int count = 1;
    Adapter a;

    CustomScheduleListAdapter(Context context, Day day){
        this.context=context;
        this.day = day;
        a = this;
    }

    public void addItem(String begin, String to) {
        if (!time.isEmpty()) {
            if(time.get(0).equals("")) {
                time.clear();
                switchto.clear();
                time.add(begin);
                switchto.add(to);
            } else {
                time.add(begin);
                switchto.add(to);
            }
        } else {
            time.add(begin);
            switchto.add(to);
        }
        count = time.size();
        notifyDataSetChanged();
    }

    public void removeFirst() {
        if (!time.isEmpty()){
            if (!time.get(0).equals("")) {
                time.remove(0);
                switchto.remove(0);
                count = time.size();
            }
        } else {
            time.add("");
            switchto.add(null);
            count = 0;
        }
        notifyDataSetChanged();
    }

    public void removeAll() {
        time.clear();
        switchto.clear();
        time.add("");
        switchto.add("");
        count = 0;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (viewListVisible) {
            return count;
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void viewListVisible(boolean b) {
        viewListVisible = b;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (time.isEmpty()) {
            removeAll();
        }

        View row=null;
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.schedule_list_item,parent,false);

            ImageView dayIcon= (ImageView) row.findViewById(R.id.day_icon);
            TextView textview= (TextView) row.findViewById(R.id.txt_begin);
            TextView textview1 = (TextView) row.findViewById(R.id.txt_end);
            ImageView nightIcon = (ImageView) row.findViewById(R.id.night_icon);
            ImageView editIcon = (ImageView) row.findViewById(R.id.edit_icon);
            ImageView deleteIcon = (ImageView) row.findViewById(R.id.delete_icon);

            ImageView abegin = (ImageView) row.findViewById(R.id.abegin_icon);
            ImageView aend = (ImageView) row.findViewById(R.id.aend_icon);

            dayIcon.setBackgroundResource(R.drawable.day);
            //textview1.setText(switchto.get(position));
            textview.setText(time.get(position));
            nightIcon.setBackgroundResource(R.drawable.night);
            editIcon.setBackgroundResource(R.drawable.edit);
            deleteIcon.setBackgroundResource(R.drawable.delete);

            abegin.setBackgroundResource(R.drawable.abegin);
            aend.setBackgroundResource(R.drawable.aend);

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d = new Dialog(context);
                    d.setTitle("Warning!");
                    d.setContentView(R.layout.dialog2);

                    Button cancel = (Button) d.findViewById(R.id.button3);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.dismiss();
                        }
                    });

                    Button ok = (Button) d.findViewById(R.id.button4);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            day.uploadData(position,"night",false,"00:00");
                            d.dismiss();
                        }
                    });

                    d.show();
                }
            });
        }else{
            row=convertView;
        }
        return row;
    }


}