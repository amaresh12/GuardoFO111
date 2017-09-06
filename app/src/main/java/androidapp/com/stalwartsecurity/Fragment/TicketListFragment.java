package androidapp.com.stalwartsecurity.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidapp.com.stalwartsecurity.Adapter.TicketListAdapter;
import androidapp.com.stalwartsecurity.R;


/**
 * Created by mobileapplication on 8/22/17.
 */

public class TicketListFragment extends Fragment {


    RatingBar ratingBar;
    TextView txtRatingValue;
    ListView ticket_list;
    String[] array={"jnd","dsd","dsd","jgjf","jjj","hbj","hbhj"};
    TicketListAdapter adapter;
    SearchView ticket_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ticket_list_fragment, container, false);
        ticket_search=(SearchView)view.findViewById(R.id.tickt_search);
        ticket_list=(ListView)view.findViewById(R.id.Ticket_list);
        getAdapter();
        ticket_search.onActionViewExpanded();
        //addListenerOnRatingBar(view);
        return view;
    }

    private void getAdapter() {

        adapter=new TicketListAdapter(getActivity(),array);
        ticket_list.setAdapter(adapter);
    }

    private void addListenerOnRatingBar(View view) {


        ratingBar = (RatingBar) view.findViewById(R.id.customer_ratings);
        txtRatingValue = (TextView) view.findViewById(R.id.rating_value);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue.setText(String.valueOf(rating));

            }
        });
    }
}
