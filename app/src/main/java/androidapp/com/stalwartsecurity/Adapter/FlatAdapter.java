package androidapp.com.stalwartsecurity.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidapp.com.stalwartsecurity.Activity.FlatList;
import androidapp.com.stalwartsecurity.Pojo.Flats;
import androidapp.com.stalwartsecurity.Pojo.PrimaryNotice;
import androidapp.com.stalwartsecurity.R;

/**
 * Created by User on 02-08-2017.
 */

public class FlatAdapter extends BaseAdapter{
    Context _context;
    ArrayList<Flats> flatList;
    Holder holder;
    public FlatAdapter(FlatList flatList, ArrayList<Flats> flatList1) {
        this._context=flatList;
        this.flatList=flatList1;
    }

    @Override
    public int getCount() {
        return flatList.size();
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
        TextView flatName,reside_name;
        CheckBox chkbox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Flats _pos=flatList.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.flat_adapter, parent, false);
            holder.flatName=(TextView)convertView.findViewById(R.id.flatname);
            holder.reside_name=(TextView) convertView.findViewById(R.id.reside_name);
            holder.chkbox=(CheckBox) convertView.findViewById(R.id.flatChk);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.flatName.setTag(position);
        holder.reside_name.setTag(position);
        holder.chkbox.setTag(position);
        holder.flatName.setText(_pos.getFlat());
        holder.reside_name.setText(_pos.getName());
        holder.chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();
                flatList.get(getPosition).setSelected(buttonView.isChecked());
              /* if(context instanceof SelectPreferedLocationReg) {
                   ((SelectPreferedLocationReg) context).onItemClickOfListView(getPosition, buttonView.isChecked());
               }*/
            }
        });

        holder.chkbox.setChecked(flatList.get(position).isSelected());

        return convertView;
    }
}
