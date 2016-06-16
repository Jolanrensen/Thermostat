package nl.tue.student.thermostat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomListAdapter2 extends BaseAdapter {
    ArrayList<String> begin = new ArrayList<String>();
    ArrayList<String> end = new ArrayList<String>();
    boolean viewListVisible;
    private Context context;
    int count = 1;

    CustomListAdapter2(Context context){
        this.context=context;
    }


    public void addItem(String begin, String end) {
        if (!this.begin.isEmpty()) {
            if(this.begin.get(0).equals("")) {
                this.begin.clear();
                this.begin.add(begin);
            } else {
                this.begin.add(begin);
            }
        } else {
            this.begin.add(begin);
        }
        count = this.begin.size();
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        if (!begin.isEmpty()){
            if (!begin.get(position).equals("")) {
                begin.remove(position);
                count = begin.size();
            }
        } else {
            begin.add("");
            count = 0;
        }
        notifyDataSetChanged();
    }

    public void removeFirst() {
        if (!begin.isEmpty()){
            if (!begin.get(0).equals("")) {
                begin.remove(0);
                count = begin.size();
            }
        } else {
            begin.add("");
            count = 0;
        }
        notifyDataSetChanged();
    }

    public void removeAll() {
        begin.clear();
        begin.add("");
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (begin.isEmpty()) {
            removeAll();
        }

        View row=null;
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.schedule_list_item,parent,false);
            ImageView dayIcon = (ImageView) row.findViewById(R.id.day_icon);
            TextView textview= (TextView) row.findViewById(R.id.txt_begin);
            TextView textview2 = (TextView) row.findViewById(R.id.txt_end);
            ImageView nightIcon = (ImageView) row.findViewById(R.id.night_icon);
            ImageView editIcon = (ImageView) row.findViewById(R.id.edit_icon);
            ImageView deleteIcon = (ImageView) row.findViewById(R.id.delete_icon);

            dayIcon.setBackgroundResource(R.drawable.day);
            textview.setText(begin.get(position));
            //textview2.setText(end.get(position));
            nightIcon.setBackgroundResource(R.drawable.night);
            editIcon.setBackgroundResource(R.mipmap.edit);
            deleteIcon.setBackgroundResource(R.mipmap.delete);
        }else{
            row=convertView;
        }
        return row;
    }


}