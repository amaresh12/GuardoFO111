package androidapp.com.stalwartsecurity.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidapp.com.stalwartsecurity.Activity.Home;
import androidapp.com.stalwartsecurity.Adapter.VisitorsAdapter;
import androidapp.com.stalwartsecurity.Pojo.Visitors;
import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;
import androidapp.com.stalwartsecurity.Util.MultipartUtility;

public class Inward extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private EditText et_desc,et_qnty,et_pname,et_pmobile,et_inno,et_cmpny,et_chkd,et_order,et_comngfrom,et_authby,et_date,et_comments;
    private TextView tv_desc,tv_qnty,tv_pname,tv_inno,tv_cmpny,tv_chkd,tv_order,tv_comngfrom,tv_authby,in_date;
    private Button bt_submit;
    private FrameLayout inwardFrame;
    private String curr_date;
    private Spinner sp_mtype;
    private String user_id;
    Calendar myCalendar=Calendar.getInstance();


    private OnFragmentInteractionListener mListener;

    public Inward() {
        // Required empty public constructor
    }

    public static Inward newInstance(String param1, String param2) {
        Inward fragment = new Inward();
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
        View v=inflater.inflate(R.layout.fragment_inward, container, false);
        user_id = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_ID, null);
        et_desc=(EditText)v.findViewById(R.id.et_descin);
        et_qnty=(EditText)v.findViewById(R.id.et_qntyin);
        et_pname=(EditText)v.findViewById(R.id.et_pname);
        et_inno=(EditText)v.findViewById(R.id.et_inno);
        et_cmpny=(EditText)v.findViewById(R.id.et_cmpny);
        et_chkd=(EditText)v.findViewById(R.id.et_chkd);
        et_order=(EditText)v.findViewById(R.id.et_orderby);
        et_comngfrom=(EditText)v.findViewById(R.id.et_comingfrom);
        et_pmobile=(EditText)v.findViewById(R.id.et_pmobile);
        et_authby=(EditText)v.findViewById(R.id.et_authby);
        et_comments=(EditText)v.findViewById(R.id.et_comments);
        et_date=(EditText)v.findViewById(R.id.et_date);
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mma");
        dateFormatter.setLenient(false);
        Date today = new Date();
        curr_date = dateFormatter.format(today);
        et_date.setText(curr_date);
        inwardFrame=(FrameLayout) v.findViewById(R.id.inwardFrame);
        bt_submit=(Button)v.findViewById(R.id.bt_submit);

        tv_inno=(TextView)v.findViewById(R.id.in_voice);
        tv_desc=(TextView)v.findViewById(R.id.in_desc);
        tv_qnty=(TextView)v.findViewById(R.id.in_quantity);
        tv_order=(TextView)v.findViewById(R.id.in_order);
        tv_comngfrom=(TextView)v.findViewById(R.id.in_cmngfrom);
        tv_pname=(TextView)v.findViewById(R.id.in_pname);
        tv_chkd=(TextView)v.findViewById(R.id.in_chkby);
        tv_authby=(TextView)v.findViewById(R.id.in_authBy);
        String a= "Enter Invoice Number";
        String a1= "Enter Description";
        String a2= "Enter Quantity";
        String a3= "Enter Ordered by";
        String a4= "Coming From";
        String a5= "Delivered By";
        String a6= "Checked By";
        String a7= "Authorised By";
        String b="*";
        //0
        SpannableStringBuilder builder = new SpannableStringBuilder(a+b);
        builder.setSpan(new ForegroundColorSpan(Color.RED), a.length(), builder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_inno.setText(builder);
        //1
        SpannableStringBuilder builder1 = new SpannableStringBuilder(a1+b);
        builder1.setSpan(new ForegroundColorSpan(Color.RED), a1.length(), builder1.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_desc.setText(builder1);
        //2
        SpannableStringBuilder builder2 = new SpannableStringBuilder(a2+b);
        builder2.setSpan(new ForegroundColorSpan(Color.RED), a2.length(), builder2.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_qnty.setText(builder2);
        //3
        SpannableStringBuilder builder3 = new SpannableStringBuilder(a3+b);
        builder3.setSpan(new ForegroundColorSpan(Color.RED), a3.length(), builder3.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_order.setText(builder3);
         //4
        SpannableStringBuilder builder4 = new SpannableStringBuilder(a4+b);
        builder4.setSpan(new ForegroundColorSpan(Color.RED), a4.length(), builder4.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_comngfrom.setText(builder4);
        //5
        SpannableStringBuilder builder5 = new SpannableStringBuilder(a5+b);
        builder5.setSpan(new ForegroundColorSpan(Color.RED), a5.length(), builder5.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_pname.setText(builder5);
        //6
        SpannableStringBuilder builder6 = new SpannableStringBuilder(a6+b);
        builder6.setSpan(new ForegroundColorSpan(Color.RED), a6.length(), builder6.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_chkd.setText(builder6);
        //7
        SpannableStringBuilder builder7 = new SpannableStringBuilder(a7+b);
        builder7.setSpan(new ForegroundColorSpan(Color.RED), a7.length(), builder7.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_authby.setText(builder7);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.getNetworkConnectivityStatus(getContext())) {
                    // Toast.makeText(Signup.this, R.string.otp_sent, Toast.LENGTH_LONG).show();
                    validation();
                }else {
                    Snackbar snackbar = Snackbar
                            .make(inwardFrame, "No Internet", Snackbar.LENGTH_LONG);
                    snackbar.show();        }
            }
        });

        return v;
    }
    private void updateStartDate() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_date.setText(sdf.format(myCalendar.getTime()));
    }
      //  curr_date=sdf.format(myCalendar.getTime());
    private void validation() {
    if(et_inno.getText().toString().trim().length()<=0){
        showDialog("Enter invoice Number");
    }
    else if(et_desc.getText().toString().trim().length()<=0){
        showDialog("Enter Description");
    }
    else if(et_qnty.getText().toString().trim().length()<=0){
        showDialog("Enter Quantity");
    }
  /*  else if(sp_mtype.getSelectedItem().toString().trim().contentEquals("-- Select Material Type --")){
        showDialog("Enter Material Type");
    }
    else if(sp_mtype.getSelectedItem().toString().trim().contentEquals("Returnable") && et_date.getText().toString().trim().length()<=0){

        showDialog("Enter Return Date");
    }*/
    else if(et_order.getText().toString().trim().length()<=0){
        showDialog("Enter Order By");
    }
    else if(et_comngfrom.getText().toString().trim().length()<=0){
        showDialog("Enter Coming From");
    }
    else if(et_pname.getText().toString().trim().length()<=0){
        showDialog("Enter Person Name");
    }
    else if(et_pmobile.getText().toString().trim().length()<=0){
        showDialog("Enter Person Mobile");
    }
    else if(et_chkd.getText().toString().trim().length()<=0){
        showDialog("Enter Checked By");
    }
    else if(et_authby.getText().toString().trim().length()<=0){
        showDialog("Enter Authorized By");
    }
    else{
        submitInward();
    }
    }

    private void submitInward() {

        String invoice=et_inno.getText().toString().trim();
        String desc=et_desc.getText().toString().trim();
        String qnty=et_qnty.getText().toString().trim();
        String orderBy=et_order.getText().toString().trim();
        String comingfrom=et_comngfrom.getText().toString().trim();
        String pname=et_pname.getText().toString().trim();
        String pmobile=et_pmobile.getText().toString().trim();
        String checkedBy=et_chkd.getText().toString().trim();
        String authby=et_authby.getText().toString().trim();
        String vechcle=et_cmpny.getText().toString().trim();
        String comments=et_comments.getText().toString();
        new Uploadinwards().execute(invoice,desc,qnty,orderBy,comingfrom,pmobile,pname,checkedBy,authby,vechcle,comments,user_id,curr_date);
    }

    private void showDialog(String message){
        AlertDialog alertDialog = new AlertDialog.Builder(
                getContext()).create();
        alertDialog.setTitle("Warning");
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    /**
     * Uploading the file to server
     * */
    private class Uploadinwards extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Inwards";
        ProgressDialog progress;
        int server_status;
        String server_message;


        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getContext(), "Please Wait",
                    "Updating...", true);

        }
        @Override
        protected Void doInBackground(String... params) {

//        new Uploadinwards().execute(invoice,desc,qnty,orderBy,comingfrom,pmobile,pname,checkedBy,
// authby,vechcle,comments,user_id,curr_date);

            try {
                String _invoice = params[0];
                String _desc = params[1];
                String _qnty = params[2];
                String _order_by = params[3];
                String _comingfrom = params[4];
                String _pmobile = params[5];
                String _pname = params[6];
                String _checkedby = params[7];
                String _authby = params[8];
                String _vechle = params[9];
                String _comments = params[10];
                String _user_id = params[11];
                String _curr_date = params[12];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.INWARD;
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
                        .appendQueryParameter("invoice_no", _invoice)
                        .appendQueryParameter("description", _desc)
                        .appendQueryParameter("quentity", _qnty)
                        .appendQueryParameter("order_by", _order_by)
                        .appendQueryParameter("delivered_by", _pname)
                        .appendQueryParameter("delevery_mobile", _pmobile)
                        .appendQueryParameter("coming_from", _comingfrom)
                        .appendQueryParameter("vehicle_number", _vechle)
                        .appendQueryParameter("checked_by", _checkedby)
                        .appendQueryParameter("authorised_by", _authby)
                        .appendQueryParameter("entry_by", _user_id)
                        .appendQueryParameter("comments", _comments)
                        .appendQueryParameter("received_on", _curr_date);

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
                    {{
    "status": 1,
    "message": "Successfully inserted"
}
                    },*/

                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    server_status=res.getInt("status");
                    if(server_status==1) {

                       server_message="Entry Successful";
                    }
                    else{
                        server_message="Upload Error";
                    }

                }

                return null;


            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
                server_message="Network Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progress.dismiss();
            showSnackBar(server_message);
            if(server_status==1){
                clearAll();
            }

        }
    }

    private void clearAll() {
        et_inno.setText("");
        et_chkd.setText("");
        et_authby.setText("");
        et_cmpny.setText("");
        et_comngfrom.setText("");
        et_order.setText("");
        et_chkd.setText("");
        et_pmobile.setText("");
        et_pname.setText("");
        et_comments.setText("");
    }

    void showSnackBar(String message){
        Snackbar snackbar = Snackbar
                .make(inwardFrame, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
