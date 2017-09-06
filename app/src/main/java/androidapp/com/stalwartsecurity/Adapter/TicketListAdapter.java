package androidapp.com.stalwartsecurity.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidapp.com.stalwartsecurity.R;

/**
 * Created by mobileapplication on 8/22/17.
 */

public class TicketListAdapter extends BaseAdapter {


    String[] array;
    Context context;
    Holder holder;

    public TicketListAdapter(FragmentActivity activity, String[] array) {
        this.context = activity;
        this.array = array;

    }



    @Override
    public int getCount() {
        return array.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class Holder {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View rowView;
        if (convertView == null) {

            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.ticket_item, parent, false);



        }
        return convertView;
    }
}
