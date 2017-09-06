package androidapp.com.stalwartsecurity.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidapp.com.stalwartsecurity.Adapter.IncidentListAdapter;
import androidapp.com.stalwartsecurity.R;


public class IncidentReportFragment extends Fragment {

    String[] array={"jnd","dsd","dsd","jgjf","jjj","hbj","hbhj"};
    ListView lv;
    IncidentListAdapter adapter;
    SearchView incident_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.report_fragment, container, false);

        incident_search=(SearchView)view.findViewById(R.id.incident_search);
        incident_search.onActionViewExpanded();
        lv=(ListView)view.findViewById(R.id.lv);
        getDapter();
        return view;
    }

    private void getDapter() {

        adapter=new IncidentListAdapter(getActivity(),array);
        lv.setAdapter(adapter);
    }
}