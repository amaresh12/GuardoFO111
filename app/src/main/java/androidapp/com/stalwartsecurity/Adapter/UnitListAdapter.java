package androidapp.com.stalwartsecurity.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidapp.com.stalwartsecurity.Activity.CheckinActivity;
import androidapp.com.stalwartsecurity.Activity.UnitList;
import androidapp.com.stalwartsecurity.Pojo.UnityDetailsLocation;
import androidapp.com.stalwartsecurity.R;

/**
 * Created by mobileapplication on 8/14/17.
 */

public class UnitListAdapter extends BaseAdapter {

    Holder holder;
    Context context;
    ArrayList<UnityDetailsLocation> array;
    public static String unityName;
    public static EditText demo_unity;




    public UnitListAdapter(UnitList unitList, ArrayList<UnityDetailsLocation> array1) {
        this.context=unitList;
        this.array=array1;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return getItem(i);
    }
    public class Holder{
        TextView tv_name,tv_cmngfrom,tv_entrytime,tv_exittime;
        ImageView iv_exit;
        public Holder() {
        }
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final UnityDetailsLocation unity_pos=array.get(i);

        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.allunit,viewGroup, false);
            holder.tv_name=(TextView)convertView.findViewById(R.id.unit_name);


            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }
            holder.tv_name.setTag(i);

            String name=unity_pos.getLocation_name();
            String id=unity_pos.getLocation_id();
            holder.tv_name.setText(name);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id=unity_pos.getLocation_id();
                unityName=unity_pos.getLocation_name();

                Intent intent=new Intent(view.getContext(), CheckinActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);

            }
        });
        return convertView;
    }

}
