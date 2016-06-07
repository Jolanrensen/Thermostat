package nl.tue.student.thermostat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomScheduleAdapter extends BaseAdapter {
    //String [] title={"GingerBread","HoneyComb","IceCreamSandwitch","JellyBean","test", "test2", "testnogwat"};
    ArrayList<String> title = new ArrayList<String>();
    // int [] icon={R.drawable.jog, R.drawable.jog};
    ArrayList<Integer> icon = new ArrayList<Integer>();
    private Context context;
    int count = 1;
    CustomScheduleAdapter(Context context){
        this.context=context;
    }


    public void addItem(String name, int icon1) {
        if (!title.isEmpty()) {
            if(title.get(0).equals("")) {
                title.clear();
                icon.clear();
                title.add(name);
                icon.add(icon1);
            } else {
                title.add(name);
                icon.add(icon1);
            }
        } else {
            title.add(name);
            icon.add(icon1);
        }
        count = title.size();
    }

    public void removeFirst() {
        if (!title.isEmpty()){
            if (!title.get(0).equals("")) {
                title.remove(0);
                icon.remove(0);
                count = title.size();
            }
        } else {
            title.add("");
            icon.add(null);
            count = 0;
        }
    }

    public void removeAll() {
        title.clear();
        icon.clear();
        title.add("");
        icon.add(0);
        count = 0;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (title.isEmpty()) {
            title.add("");
            icon.add(0);
            count = 0;
        }

        View row=null;
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.schedule_list_item,parent,false);
            ImageView imageview= (ImageView) row.findViewById(R.id.iv_icon);
            TextView textview= (TextView) row.findViewById(R.id.txt_name);


            imageview.setBackgroundResource(icon.get(position));
            textview.setText(title.get(position));

        }else{
            row=convertView;
        }
        return row;
    }
}