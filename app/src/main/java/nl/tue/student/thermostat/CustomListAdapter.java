/*package nl.tue.student.thermostat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomListAdapter extends BaseAdapter {
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<Integer> icon = new ArrayList<Integer>();
    boolean viewListVisible;
    private Context context;
    int count = 1;
    CustomListAdapter(Context context){
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
        notifyDataSetChanged();
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
        notifyDataSetChanged();
    }

    public void removeAll() {
        title.clear();
        icon.clear();
        title.add("");
        icon.add(0);
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

        if (title.isEmpty()) {
            removeAll();
        }

        View row=null;
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.upcoming_changes_list_item,parent,false);
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
*/