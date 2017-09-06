package androidapp.com.stalwartsecurity.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import androidapp.com.stalwartsecurity.Activity.DeliveryBoysList;
import androidapp.com.stalwartsecurity.Pojo.DeliveryBoys;
import androidapp.com.stalwartsecurity.R;

/**
 * Created by User on 17-08-2017.
 */

public class DeliveryBoysAdapter extends BaseAdapter {
    Context _context;
    ArrayList<DeliveryBoys> dbList;
    Holder holder;
    public  static EditText selected;


    public DeliveryBoysAdapter(DeliveryBoysList deliveryBoysList, ArrayList<DeliveryBoys> dbList) {
        this._context=deliveryBoysList;
        this.dbList=dbList;
    }

    @Override
    public int getCount() {
        return dbList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder{
        TextView boyName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DeliveryBoys _pos=dbList.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.db_adapter, parent, false);
            holder.boyName=(TextView)convertView.findViewById(R.id.dbname);
            convertView.setTag(holder);
        }
        else{
           holder = (Holder) convertView.getTag();
        }
        holder.boyName.setTag(position);
        holder.boyName.setText(_pos.getTitle());
        if(_pos.getIs_enable().contentEquals("0")){
            holder.boyName.setTextColor(R.color.red);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected.setText(_pos.getTitle().trim());
                ((Activity)_context).finish();

            }
        });
        return convertView;
    }
}
