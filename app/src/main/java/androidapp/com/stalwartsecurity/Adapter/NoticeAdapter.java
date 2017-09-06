package androidapp.com.stalwartsecurity.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidapp.com.stalwartsecurity.Pojo.PrimaryNotice;
import androidapp.com.stalwartsecurity.Pojo.Visitors;
import androidapp.com.stalwartsecurity.R;

/**
 * Created by User on 20-07-2017.
 */

public class NoticeAdapter extends BaseAdapter {
    Context _context;
    ArrayList<PrimaryNotice> primaryNotices;
    Holder holder;
    public NoticeAdapter(Context context, ArrayList<PrimaryNotice> primaryNoticeList) {
        this._context=context;
        this.primaryNotices=primaryNoticeList;
    }

    @Override
    public int getCount() {
        return primaryNotices.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class Holder{

        TextView message,date;
        public  Holder(){

        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PrimaryNotice _pos=primaryNotices.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.allmesssage, parent, false);
            holder.message=(TextView)convertView.findViewById(R.id.messageDetails);
            holder.date=(TextView)convertView.findViewById(R.id.Mdate);

            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.message.setTag(position);
        holder.date.setTag(position);

        holder.message.setText(_pos.getMessage());
        holder.date.setText(_pos.getDate());
        return convertView;
    }
}
