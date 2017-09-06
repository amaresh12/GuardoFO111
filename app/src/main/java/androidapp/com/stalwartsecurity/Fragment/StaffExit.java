package androidapp.com.stalwartsecurity.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidapp.com.stalwartsecurity.Activity.Home;
import androidapp.com.stalwartsecurity.Adapter.StaffListAdapter;
import androidapp.com.stalwartsecurity.Pojo.Staffs;
import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StaffExit.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StaffExit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffExit extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ListView stafflist;
    TextView nostaff_found;
    FrameLayout frame_stafflikst;
    private String user_id,location_id,server_response;
    private  int server_status;
    ArrayList<Staffs> staffList;

    private OnFragmentInteractionListener mListener;

    public StaffExit() {
        // Required empty public constructor
    }

    public static StaffExit newInstance(String param1, String param2) {
        StaffExit fragment = new StaffExit();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_staff_exit, container, false);
        user_id = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_ID, null);

        stafflist=(ListView)view.findViewById(R.id.stafflist);
        nostaff_found=(TextView)view.findViewById(R.id.tv_nostaff);
        frame_stafflikst=(FrameLayout)view.findViewById(R.id.frame_stafflist);
        if (CheckInternet.getNetworkConnectivityStatus(getActivity())) {
            new GetStaffList().execute(Home.Locationid);

        }
        else{
            showSnackBar("No Internet");

        }


            return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar
                .make(frame_stafflikst, message, Snackbar.LENGTH_LONG);
        snackbar.show();

    }

    /*
* GET STAFF LIST ASYNTASK*/
    private class GetStaffList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getContext(), "Please Wait",
                    "Loading StaffListAdapter...", true);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                String _locationId = params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.STAFFLIST;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setAllowUserInteraction(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("location_id", _locationId);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = conn.getInputStream();
                }
                if (in == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "", data = "";

                while ((data = reader.readLine()) != null) {
                    response += data + "\n";
                }

                Log.i(TAG, "Response : " + response);

                /*
                *
                * {
                *  "StaffVisit": [
        {
            "id": "12",
            "staff_type_id": "1",
            "staff_sub_type_id": "1",
            "location_id": "1",
            "plot_no": "25, 25, gh",
            "name": "hgjhgj",
            "phone": "546544545",
            "vehicle": "6544",
            "expiry_date": null,
            "status": "1",
            "auto_gen_id": null,
            "photo": "",
            "entry_by": "superadmin",
            "in_time": "08-08-2017 12:10 PM",
            "exit_by": "superadmin",
            "out_time": "08-08-2017 12:10 PM",
            "email": "fdfdfdf@dsfdsf.com",
            "created": "2017-08-13 11:26:54",
            "modified": "2017-08-13 11:26:54",
            "overstay": 0
        },
                    },*/

                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    server_status=res.getInt("status");
                    if(server_status==1) {
                        JSONArray staff_List = res.getJSONArray("StaffVisit");
                        staffList = new ArrayList<>();
                        for (int i = 0; i < staff_List.length(); i++) {
                            JSONObject o_list_obj = staff_List.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String name = o_list_obj.getString("name");
                            String mobile = o_list_obj.getString("phone");
                            String au_id = o_list_obj.getString("auto_gen_id");
                            String entry_by = o_list_obj.getString("entry_by");
                            String in_time = o_list_obj.getString("in_time");
                            String overstay = o_list_obj.getString("overstay");
                            String exit_by = o_list_obj.getString("exit_by");
                            String out_time = o_list_obj.getString("out_time");
                            String appartment = o_list_obj.getString("plot_no");
                            String photo = o_list_obj.getString("photo");

                            Staffs list1 = new Staffs(id,name,mobile,au_id,entry_by,in_time,overstay,appartment,out_time,exit_by,photo);
                            staffList.add(list1);
                        }
                    }

                }

                return null;


            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progress.dismiss();
            if(server_status==1) {
                StaffListAdapter mAdapter = new StaffListAdapter(getContext(), staffList);
                stafflist.setAdapter(mAdapter);
            }
            else{
                stafflist.setVisibility(View.GONE);
                nostaff_found.setVisibility(View.VISIBLE);
            }

        }
    }


}
