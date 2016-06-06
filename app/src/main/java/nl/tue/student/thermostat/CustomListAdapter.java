package nl.tue.student.thermostat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomListAdapter extends BaseAdapter {
    //String [] title={"GingerBread","HoneyComb","IceCreamSandwitch","JellyBean","test", "test2", "testnogwat"};
    ArrayList<String> title = new ArrayList<String>();
    int [] icon={R.drawable.jog, R.drawable.jog};
    ArrayList<Integer> icon2 = new ArrayList<Integer>();
    private Context context;
    int count = 1;
    CustomListAdapter(Context context){
        this.context=context;
    }

    public void addItem(String name, int icon) {


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

        //title and icon cannot be empty, but items can be added and removed. Will implement this in a method
        title.clear();
        icon2.clear();
        title.add("");
        icon2.add(0);
        title.add("test2");
        icon2.add(R.mipmap.ic_launcher);
        //icon2.add(R.drawable.jog);
        count = title.size();
        View row=null;
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.upcoming_changes_list_item,parent,false);
            ImageView imageview= (ImageView) row.findViewById(R.id.iv_icon);
            TextView textview= (TextView) row.findViewById(R.id.txt_name);


            imageview.setBackgroundResource(icon2.get(position));
            textview.setText(title.get(position));

        }else{
            row=convertView;
        }
        return row;
    }
}