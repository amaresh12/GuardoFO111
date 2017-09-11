package androidapp.com.stalwartsecurity.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidapp.com.stalwartsecurity.R;

public class LodgeTicketFragment extends Fragment {


    RatingBar ratingBar;
    TextView txtRatingValue;
    Spinner statustype,spin_department;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.lodge_ticket_fragment, container, false);

        statustype = (Spinner) view.findViewById(R.id.status_id);
        spin_department = (Spinner) view.findViewById(R.id.spin_department);

        List<String> list = new ArrayList<String>();
        list.add("Open");
        list.add("Close");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statustype.setAdapter(dataAdapter);
        statustype.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        List<String> list1 = new ArrayList<String>();
        list1.add("Reqruitment");
        list1.add("Uniform");
        list1.add("ESI/PF");
        list1.add("Admin");
        list1.add("Operations");
        list1.add("Management");
        list1.add("Others..");


        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, list1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_department.setAdapter(dataAdapter1);
        spin_department.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        return view;

    }
}