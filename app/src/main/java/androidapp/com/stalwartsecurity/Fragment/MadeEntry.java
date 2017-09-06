package androidapp.com.stalwartsecurity.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidapp.com.stalwartsecurity.Activity.BarCodeScaanner;
import androidapp.com.stalwartsecurity.Activity.Home;
import androidapp.com.stalwartsecurity.Adapter.VisitorsAdapter;
import androidapp.com.stalwartsecurity.Pojo.Visitors;
import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;
import androidapp.com.stalwartsecurity.Util.MultipartUtility;
import de.hdodenhof.circleimageview.CircleImageView;

public class MadeEntry extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Spinner sp_input;
    EditText sp_stafftype;
    Button bt_staffentry;
    LinearLayout staff_details;
    TextView tv_IDheading, tv_stafftypeHeading, tv_name_heading, tv_ph_heading, tv_flat_heading, tv_cmng_heading, tv_dateheading;
    EditText et_idnumber, et_sname, et_sphone, et_sfrm, et_sappt, et_svcle, et_sdtntime;
    RelativeLayout staffFrame;
    private String server_response, user_id,curr_date;
    private int server_status;
    private String id, name, mobile, staff_type, coming_from, vehicle_no, appartment, photo, expiry_date, block_status, stafftypeid, substaffid, locationid;
    Button bt_idsubmit;
    private Boolean dataget = false;
    private CircleImageView sprofile_image;

    public MadeEntry() {
        // Required empty public constructor
    }


    public static MadeEntry newInstance(String param1, String param2) {
        MadeEntry fragment = new MadeEntry();
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
        View view = inflater.inflate(R.layout.fragment_made_entry, container, false);
        // Inflate the layout for this fragment
        user_id = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_ID, null);
        bt_staffentry = (Button) view.findViewById(R.id.bt_staffentry);
        tv_stafftypeHeading = (TextView) view.findViewById(R.id.staffTypeheading);
        tv_name_heading = (TextView) view.findViewById(R.id.staffNameheading);
        tv_ph_heading = (TextView) view.findViewById(R.id.staffPhoneheading);
        tv_flat_heading = (TextView) view.findViewById(R.id.staffflatheading);
        tv_cmng_heading = (TextView) view.findViewById(R.id.staffcmngfromheading);
        tv_dateheading = (TextView) view.findViewById(R.id.staffDTheading);
        staffFrame = (RelativeLayout) view.findViewById(R.id.staffFrame);
        et_sname = (EditText) view.findViewById(R.id.et_sname);
        et_sfrm = (EditText) view.findViewById(R.id.et_sfrm);
        et_svcle = (EditText) view.findViewById(R.id.et_svcle);
        et_sappt = (EditText) view.findViewById(R.id.et_sappt);
        et_sphone = (EditText) view.findViewById(R.id.et_sphone);
        et_sdtntime = (EditText) view.findViewById(R.id.et_sdtntime);
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mma");
        dateFormatter.setLenient(false);
        Date today = new Date();
        et_sdtntime.setText(dateFormatter.format(today));
        curr_date = dateFormatter.format(today);

        tv_IDheading = (TextView) view.findViewById(R.id.idnumberheading);
        String a = "Enter ID Number";
        String b = "*";
        String a6 = "Type";
        String a1 = "Name";
        String a2 = "Phone";
        String a3 = "Flat";
        String a4 = "Coming From";
        String a5 = "Date";
        SpannableStringBuilder builder = new SpannableStringBuilder(a + b);
        builder.setSpan(new ForegroundColorSpan(Color.RED), a.length(), builder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_IDheading.setText(builder);
        //tv_stafftypeHeading,tv_name_heading,tv_ph_heading,tv_flat_heading,tv_cmng_heading,tv_dateheading
        SpannableStringBuilder builder5 = new SpannableStringBuilder(a5 + b);
        builder5.setSpan(new ForegroundColorSpan(Color.RED), a5.length(), builder5.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_dateheading.setText(builder5);
        SpannableStringBuilder builder6 = new SpannableStringBuilder(a6 + b);
        builder6.setSpan(new ForegroundColorSpan(Color.RED), a6.length(), builder6.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_stafftypeHeading.setText(builder6);
        SpannableStringBuilder builder1 = new SpannableStringBuilder(a1 + b);
        builder1.setSpan(new ForegroundColorSpan(Color.RED), a1.length(), builder1.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_name_heading.setText(builder6);
        SpannableStringBuilder builder2 = new SpannableStringBuilder(a2 + b);
        builder2.setSpan(new ForegroundColorSpan(Color.RED), a2.length(), builder2.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_ph_heading.setText(builder2);
        SpannableStringBuilder builder3 = new SpannableStringBuilder(a3 + b);
        builder3.setSpan(new ForegroundColorSpan(Color.RED), a3.length(), builder3.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_flat_heading.setText(builder3);
        SpannableStringBuilder builder4 = new SpannableStringBuilder(a4 + b);
        builder4.setSpan(new ForegroundColorSpan(Color.RED), a4.length(), builder4.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_cmng_heading.setText(builder4);
        sp_input = (Spinner) view.findViewById(R.id.sp_input);
        sp_stafftype = (EditText) view.findViewById(R.id.stafftype);
        et_idnumber = (EditText) view.findViewById(R.id.et_slnumber);
        staff_details = (LinearLayout) view.findViewById(R.id.staff_details);
        sprofile_image = (CircleImageView) view.findViewById(R.id.sprofile_image);
        bt_idsubmit = (Button) view.findViewById(R.id.bt_idsubmit);
        bt_idsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.getNetworkConnectivityStatus(getContext())) {
                    String auto_gen_id = et_idnumber.getText().toString().trim();
                    GetStaffDetails getStaffDetails = new GetStaffDetails();
                    getStaffDetails.execute(auto_gen_id);


                }
                else {
                  //  showSnackBar("No internet");
                }
            }
        });
        List<String> inputType = new ArrayList<String>();
        inputType.add("-- Select Input Type --");
        inputType.add("BAR Code");
        inputType.add("ID Number");
        ArrayAdapter<String> adapte_visitors = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, inputType);
        adapte_visitors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        sp_input.setAdapter(adapte_visitors);
        sp_input.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String _Itype = sp_input.getSelectedItem().toString();
                if (_Itype.contentEquals("ID Number")) {
                    tv_IDheading.setVisibility(View.VISIBLE);
                    et_idnumber.setVisibility(View.VISIBLE);
                    bt_idsubmit.setVisibility(View.VISIBLE);
                } else if (_Itype.contentEquals("BAR Code")) {
                    tv_IDheading.setVisibility(View.GONE);
                    et_idnumber.setVisibility(View.GONE);
                    bt_idsubmit.setVisibility(View.GONE);
                    Intent intent = new Intent(getActivity(), BarCodeScaanner.class);
                    startActivity(intent);
                } else {
                    tv_IDheading.setVisibility(View.GONE);
                    et_idnumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        bt_staffentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaidate();
            }
        });
        return view;
    }

    private void vaidate() {
        if (sp_input.getSelectedItem().toString().trim().contentEquals("-- Select Input Type --")) {
            showSnackBar("Please Select Input Type");
        } else if (sp_input.getSelectedItem().toString().trim().contentEquals("ID Number") && et_idnumber.getText().toString().trim().length() < 10) {
            showSnackBar("Enter Valid Id Number");
        } else if (dataget == false) {
            showSnackBar("No valid data for staffs");
        } else {
            submit();
        }
    }

    private void submit() {
        if (CheckInternet.getNetworkConnectivityStatus(getContext())) {
            SubmitDetails submitdetails = new SubmitDetails();
            submitdetails.execute(stafftypeid, substaffid, locationid, appartment, name, mobile, vehicle_no, user_id);


        } else {
            //showSnackBar("No internet");
        }
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

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar
                .make(staffFrame, message, Snackbar.LENGTH_LONG);
        snackbar.show();

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /*
* GET VISIORS LIST ASYNTASK*/
    private class GetStaffDetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "StaffShow";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getContext(), "Please Wait",
                    "Loading Details...", true);

        }

        @Override
        protected Void doInBackground(String... params) {


            try {
                String _autogen_id = params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.STAFF;
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
                        .appendQueryParameter("auto_gen_id", _autogen_id);

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
    "Staff": [
        {
            "id": "5",
            "staff_type_id": "1",
            "staff_sub_type_id": "2",
            "location_id": "3",
            "location_detail_id": "4",
            "name": "fgdsgfdg",
            "phone": null,
            "vehicle": "gfdgd",
            "expiry_date": "2017-08-16",
            "status": "1",
            "auto_gen_id": "STAFF1502612604",
            "created": "2017-08-13 10:23:24",
            "modified": "2017-08-13 10:23:24",
            "photo": "",
            "added_by": "superadmin",
            "email": null,
            "location": "Esha Appartments",
            "flat_no": "123",
            "staff_type": "Mades",
            "staff_sub_type": "aaaa"
        }*/

                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    server_status = res.getInt("status");
                    if (server_status == 1) {
                        JSONArray staffL = res.getJSONArray("Staff");
                        for (int i = 0; i < staffL.length(); i++) {
                            JSONObject o_list_obj = staffL.getJSONObject(i);
                            id = o_list_obj.getString("id");
                            stafftypeid = o_list_obj.getString("staff_type_id");
                            substaffid = o_list_obj.getString("staff_sub_type_id");
                            locationid = o_list_obj.getString("location_id");
                            name = o_list_obj.getString("name");
                            mobile = o_list_obj.getString("phone");
                            staff_type = o_list_obj.getString("staff_type");
                            coming_from = o_list_obj.getString("staff_sub_type");
                            vehicle_no = o_list_obj.getString("vehicle");
                            appartment = o_list_obj.getString("flat_no");
                            photo = o_list_obj.getString("photo");
                            expiry_date = o_list_obj.getString("expiry_date");
                            block_status = o_list_obj.getString("status");
                        }
                    } else {
                        server_response = "Data Not Found";
                    }

                }

                return null;


            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
                server_response = exception.toString();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progress.dismiss();
            if (server_status == 1) {
                dataget = true;
                setValues();
            } else {
                showSnackBar(server_response);
            }

        }
    }

    private void setValues() {
        if (!photo.isEmpty()) {
            Picasso.with(getActivity()).load(photo).into(sprofile_image);
        }
        sp_stafftype.setText(staff_type);
        et_sname.setText(name);
        et_sphone.setText(mobile);
        et_sfrm.setText(coming_from);
        et_svcle.setText(vehicle_no);
        et_sappt.setText(appartment);

    }

    /*
* GET VISIORS LIST ASYNTASK*/
    private class SubmitDetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "StaffShow";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getContext(), "Please Wait",
                    "Loading Details...", true);

        }

        @Override
        protected Void doInBackground(String... params) {

            //stafftypeid,substaffid,locationid,appartment,name,mobile,vehicle_no,user_id

            try {
                String _staff_id = params[0];
                String _sub_staff_id = params[1];
                String _location_id = params[2];
                String _appartment = params[3];
                String _name = params[4];
                String _phone = params[5];
                String _vehicle = params[6];
                String _user_id = params[7];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.ADDSTAFF;
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
                        //stafftypeid,substaffid,locationid,appartment,name,mobile,vehicle_no,user_id

                        .appendQueryParameter("staff_type_id",_staff_id)
                        .appendQueryParameter("staff_sub_type_id",_sub_staff_id)
                        .appendQueryParameter("location_id",_location_id)
                        .appendQueryParameter("plot_no",_appartment)
                        .appendQueryParameter("name",_name)
                        .appendQueryParameter("phone",_phone)
                        .appendQueryParameter("vehicle",_vehicle)
                        .appendQueryParameter("entry_by",_user_id)
                        .appendQueryParameter("photo",photo)
                        .appendQueryParameter("in_time",curr_date);

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
   {
    "status": 1,
    "message": "Successfully inserted"
}*/

                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    server_status = res.getInt("status");
                    if (server_status == 1) {
                        
                        server_response="Staff Entry Successful";          
                    } else {
                        server_response = "Error Found";
                    }

                }

                return null;


            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
                server_response = exception.toString();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progress.dismiss();
            showSnackBar(server_response);
            if(server_status==1){
                resetAll();
            }

        }
    }

    private void resetAll() {
        sp_stafftype.setText("");
        et_sname.setText("");
        et_sphone.setText("");
        et_sfrm.setText("");
        et_sappt.setText("");
        et_svcle.setText("");
        et_idnumber.setText("");

    }
}
