package nl.tue.student.thermostat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CustomListAdapter extends BaseAdapter {
    String [] title={"GingerBread","HoneyComb","IceCreamSandwitch","JellyBean","test", "test2", "testnogwat"};
    int [] icon={R.drawable.jog,R.drawable.jog,R.drawable.jog,R.drawable.jog, R.drawable.jog, R.drawable.jog, R.drawable.jog};
    private Context context;

    CustomListAdapter(Context context){
        this.context=context;
    }


    @Override
    public int getCount() {
        return icon.length;
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
        View row=null;
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.upcoming_changes_list_item,parent,false);
            ImageView imageview= (ImageView) row.findViewById(R.id.iv_icon);
            TextView textview= (TextView) row.findViewById(R.id.txt_name);
            LinearLayout linearlayout= (LinearLayout) row.findViewById(R.id.upcomingChangesList);

            imageview.setBackgroundResource(icon[position]);
            textview.setText(title[position]);

        }else{
            row=convertView;
        }
        return row;
    }
}