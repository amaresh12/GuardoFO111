package androidapp.com.stalwartsecurity.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidapp.com.stalwartsecurity.Activity.Home;
import androidapp.com.stalwartsecurity.Adapter.VisitorsAdapter;
import androidapp.com.stalwartsecurity.Pojo.Visitors;
import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;


public class VisitorsexitDW extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ListView lv_VisitorsList;
    TextView et_no_visitors;
    RelativeLayout visiorslist_rel;
    String user_id;
    String server_message;
    int server_status;
    VisitorsAdapter mAdapter;
    ListView visitorsList;
    ArrayList<Visitors> list_visitors;
    Button selcetDate;
    String curr_date;
    SearchView visitersSearch;
    Calendar myCalendar=Calendar.getInstance();
    SwipeRefreshLayout exitDateswipe;


    private OnFragmentInteractionListener mListener;

    public VisitorsexitDW() {
        // Required empty public constructor
    }

    public static VisitorsexitDW newInstance(String param1, String param2) {
        VisitorsexitDW fragment = new VisitorsexitDW();
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
        View v= inflater.inflate(R.layout.fragment_dwvisitorsexit, container, false);
        user_id = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_ID, null);
        selcetDate=(Button)v.findViewById(R.id.selcetDate);
        visitersSearch=(SearchView)v.findViewById(R.id.visitorsDSearch);
       // lv_VisitorsList=(ListView)v.findViewById(R.id.visitorsList);
        exitDateswipe=(SwipeRefreshLayout)v.findViewById(R.id.exitDateswipe);
        et_no_visitors=(TextView)v.findViewById(R.id.no_viritors);
        visiorslist_rel=(RelativeLayout)v.findViewById(R.id.visiorslist_rel);
        visitorsList=(ListView)v.findViewById(R.id.visitorsList);
        selcetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateStartDate();

                    }

                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
              //  datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                datePickerDialog.show();

                //start.setText(sdf.format(myCalendar.getTime()));
            }

        });
        visitorsList.setVisibility(View.GONE);
        et_no_visitors.setVisibility(View.VISIBLE);
        exitDateswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateStartDate();

            }
        });
        visitersSearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        //*** setOnQueryTextListener ***
        visitersSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setQuestionList(newText);
                return false;
            }
        });

        // getVisiorsList();
        return v;
    }
    private void setQuestionList(String filterText) {

        final ArrayList<Visitors> visitors_search = new ArrayList<>();
        if (filterText != null && filterText.trim().length() > 0) {
            for (int i = 0; i < list_visitors .size(); i++) {
                String q_title = list_visitors.get(i).getName();
                if (q_title != null && filterText != null &&
                        q_title.toLowerCase().contains(filterText.toLowerCase())) {
                    visitors_search.add(list_visitors.get(i));
                }
            }
        }else{
            visitors_search.addAll(list_visitors);
        }
        // create an Object for Adapter
        mAdapter = new VisitorsAdapter(getActivity(), visitors_search);
        visitorsList.setAdapter(mAdapter);
        //  mAdapter.notifyDataSetChanged();


        if (visitors_search.isEmpty()) {
            visitorsList.setVisibility(View.GONE);
            et_no_visitors.setVisibility(View.VISIBLE);
        } else {
            visitorsList.setVisibility(View.VISIBLE);
            et_no_visitors.setVisibility(View.GONE);
        }

        mAdapter.notifyDataSetChanged();
    }


    private void updateStartDate() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        selcetDate.setText(sdf.format(myCalendar.getTime()));
        curr_date=sdf.format(myCalendar.getTime());
        if(curr_date!=null) {
            exitDateswipe.setRefreshing(false);
            getVisiorsList();
        }
        else{
            visitorsList.setVisibility(View.GONE);
            et_no_visitors.setVisibility(View.VISIBLE);

        }
    }

    private void getVisiorsList() {
        if (CheckInternet.getNetworkConnectivityStatus(getContext())) {
            // Toast.makeText(Signup.this, R.string.otp_sent, Toast.LENGTH_LONG).show();
            new VisiorsList().execute(user_id,curr_date);
        }else {
            Snackbar snackbar = Snackbar
                    .make(visiorslist_rel, "No Internet", Snackbar.LENGTH_LONG);
            snackbar.show();        }
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
       /* if (context instanceof OnFragmentInteractionListener) {
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

    /*
* GET VISIORS LIST ASYNTASK*/
    private class VisiorsList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getContext(), "Please Wait",
                    "Loading Visiorslist...", true);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                String _user_id = params[0];
                String _intime = params[1];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.VISITORS;
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
                        .appendQueryParameter("location_id", Home.Locationid)
                        .appendQueryParameter("in_time", _intime);

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
                    {
                          "visiter": [
                                {
                                    "id": "15",
                                    "name": "amaresh",
                                    "email_id": "a@gmail.com",
                                    "address": "bangalore",
                                    "mobile": "2147483647",
                                    "visiter_type": null,
                                    "coming_from": "marathali",
                                    "vehicle_no": "7011",
                                    "appartment_name": "esha",
                                    "pass_no": "12345",
                                    "photo": "",
                                    "added_by": "avinash pathak",
                                    "in_time": "15-07-2017 12:00 AM",
                                    "out_time": "",
                                    "created": "2017-07-15 12:29:50",
                                    "modified": "2017-07-15 12:29:50"
                                },
                    },*/

                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    server_status=res.getInt("status");
                    if(server_status==1) {
                        JSONArray visitoslist = res.getJSONArray("visiter");
                        list_visitors = new ArrayList<>();
                        for (int i = 0; i < visitoslist.length(); i++) {
                            JSONObject o_list_obj = visitoslist.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String name = o_list_obj.getString("name");
                            String mobile = o_list_obj.getString("mobile");
                            String visiter_type = o_list_obj.getString("visiter_type");
                            String coming_from = o_list_obj.getString("coming_from");
                            String vehicle_no = o_list_obj.getString("vehicle_no");
                            String appartment = o_list_obj.getString("appartment_name");
                            String pass_no = o_list_obj.getString("pass_no");
                            String photo = o_list_obj.getString("photo");
                            String in_time = o_list_obj.getString("in_time");
                            String out_time = o_list_obj.getString("out_time");
                            String exit_by = o_list_obj.getString("exit_by");
                            String overstay = o_list_obj.getString("overstay");
                            String verificstion = o_list_obj.getString("verification_status");
                            Visitors list1 = new Visitors(id,name,mobile,visiter_type,coming_from,vehicle_no,appartment,pass_no
                                                            ,photo,in_time,out_time,exit_by,overstay,verificstion);
                            list_visitors.add(list1);
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
            if(server_status==1) {
                visitorsList.setVisibility(View.VISIBLE);
                et_no_visitors.setVisibility(View.GONE);
                mAdapter = new VisitorsAdapter(getContext(), list_visitors);
                visitorsList.setAdapter(mAdapter);
                progress.dismiss();
            }
            else{
                visitorsList.setVisibility(View.GONE);
                et_no_visitors.setVisibility(View.VISIBLE);
            }
            progress.dismiss();

        }
    }
}
