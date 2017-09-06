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
import java.util.List;
import java.util.Locale;

import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;

public class Outward extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private TextView seldate,gatepassheading,descriptionheading,quantityheading,selType,goingheading,takenbyheading,authheading,chkheading;
    private EditText et_gatepass,et_desc,et_qnty,out_date,et_goingto,et_taken_name,et_taken_phone,
            et_vclenum,et_authby,et_currdate,et_chkby,et_coments;
    private Spinner sp_type;
    private Button but_suboutward;
    private FrameLayout outwardframe;
    private String cur_date;
    Calendar myCalendar=Calendar.getInstance();
    private String user_id;


    public Outward() {
        // Required empty public constructor
    }
    public static Outward newInstance(String param1, String param2) {
        Outward fragment = new Outward();
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
        View v=inflater.inflate(R.layout.fragment_outward, container, false);
        user_id = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_ID, null);

        gatepassheading=(TextView)v.findViewById(R.id.gatepassheading);
        descriptionheading=(TextView)v.findViewById(R.id.descriptionheading);
        quantityheading=(TextView)v.findViewById(R.id.quantityheading);
        selType=(TextView)v.findViewById(R.id.selType);
        sp_type=(Spinner)v.findViewById(R.id.sp_type);
        seldate=(TextView)v.findViewById(R.id.seldate);
        goingheading=(TextView)v.findViewById(R.id.goingheading);
        takenbyheading=(TextView)v.findViewById(R.id.takenbyheading);
        authheading=(TextView)v.findViewById(R.id.authheading);
        chkheading=(TextView)v.findViewById(R.id.chkheading);
        outwardframe=(FrameLayout)v.findViewById(R.id.outwardframe);
        out_date=(EditText)v.findViewById(R.id.out_date);
        et_coments=(EditText)v.findViewById(R.id.et_coments);
        et_gatepass=(EditText)v.findViewById(R.id.et_gatepass);
        et_desc=(EditText)v.findViewById(R.id.et_desc);
        et_qnty=(EditText)v.findViewById(R.id.et_qnty);
        et_goingto=(EditText)v.findViewById(R.id.et_goingto);
        et_taken_name=(EditText)v.findViewById(R.id.et_taken_name);
        et_taken_phone=(EditText)v.findViewById(R.id.et_taken_phone);
        et_vclenum=(EditText)v.findViewById(R.id.et_vclenum);
        et_authby=(EditText)v.findViewById(R.id.et_authby);
        et_currdate=(EditText)v.findViewById(R.id.et_currdate);
        et_chkby=(EditText)v.findViewById(R.id.et_chkby);
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mma");
        dateFormatter.setLenient(false);
        Date today = new Date();
        cur_date = dateFormatter.format(today);
        et_currdate.setText(cur_date);

        but_suboutward=(Button)v.findViewById(R.id.but_suboutward);
        String a="Gate Pass";
        String a1="Description";
        String a2="Quantity";
        String a3="Material Type";
        String a4="Return Date";
        String a5="Going to";
        String a6="Taken By";
        String a7="Authorised By";
        String a8="Checked By";
        String b="*";
        SpannableStringBuilder builder = new SpannableStringBuilder(a+b);
        builder.setSpan(new ForegroundColorSpan(Color.RED), a.length(), builder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        gatepassheading.setText(builder);
        SpannableStringBuilder builder1 = new SpannableStringBuilder(a1+b);
        builder1.setSpan(new ForegroundColorSpan(Color.RED), a1.length(), builder1.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        descriptionheading.setText(builder1);
        SpannableStringBuilder builder2 = new SpannableStringBuilder(a2+b);
        builder2.setSpan(new ForegroundColorSpan(Color.RED), a2.length(), builder2.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        quantityheading.setText(builder2);
        SpannableStringBuilder builder3= new SpannableStringBuilder(a3+b);
        builder3.setSpan(new ForegroundColorSpan(Color.RED), a3.length(), builder3.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        selType.setText(builder3);
        SpannableStringBuilder builder4 = new SpannableStringBuilder(a4+b);
        builder4.setSpan(new ForegroundColorSpan(Color.RED), a4.length(), builder4.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        seldate.setText(builder4);
        SpannableStringBuilder builder5 = new SpannableStringBuilder(a5+b);
        builder5.setSpan(new ForegroundColorSpan(Color.RED), a5.length(), builder5.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        goingheading.setText(builder5);
        SpannableStringBuilder builder6 = new SpannableStringBuilder(a6+b);
        builder6.setSpan(new ForegroundColorSpan(Color.RED), a6.length(), builder6.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        takenbyheading.setText(builder6);
        SpannableStringBuilder builder7 = new SpannableStringBuilder(a7+b);
        builder7.setSpan(new ForegroundColorSpan(Color.RED), a7.length(), builder7.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        authheading.setText(builder7);
        SpannableStringBuilder builder8 = new SpannableStringBuilder(a8+b);
        builder8.setSpan(new ForegroundColorSpan(Color.RED), a8.length(), builder8.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        chkheading.setText(builder8);
        List<String> materialType = new ArrayList<String>();
        materialType.add("-- Select Material Type --");
        materialType.add("Returnable");
        materialType.add("Non-Returnable");

        ArrayAdapter<String> adapte_materials = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, materialType);
        adapte_materials.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        sp_type.setAdapter(adapte_materials);
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String Mtype=sp_type.getSelectedItem().toString().trim();
                if(Mtype.contentEquals("Returnable")){
                    seldate .setVisibility(View.VISIBLE);
                    out_date.setVisibility(View.VISIBLE);
                }
                else {
                    seldate.setVisibility(View.GONE);
                    out_date.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }



        });
        out_date.setOnClickListener(new View.OnClickListener() {
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
               datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
               datePickerDialog.show();


           }
       });

        but_suboutward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.getNetworkConnectivityStatus(getContext())) {
                    // Toast.makeText(Signup.this, R.string.otp_sent, Toast.LENGTH_LONG).show();
                    validation();
                }else {
                    showSnackBar("No Internet");
                }
            }
        });
        return v;
    }

    private void validation() {
    if(et_gatepass.getText().toString().trim().length()<=0){
        showDialog("Enter gate pass");
    }
    else if(et_desc.getText().toString().trim().length()<=0){
        showDialog("Enter Description");
    }
    else if(et_qnty.getText().toString().trim().length()<=0){
        showDialog("Enter Quantity");
    }
    else if(sp_type.getSelectedItem().toString().trim().contentEquals("-- Select Material Type --")){
        showDialog("Select Material Type");
    }
    else if(et_goingto.getText().toString().trim().length()<=0){
        showDialog("Enter Going to");
    }
    else if(et_taken_name.getText().toString().trim().length()<=0){
        showDialog("Enter Name");
    }
    else if(et_taken_phone.getText().toString().trim().length()<=0){
        showDialog("Enter Phone");
    }
    else if(et_authby.getText().toString().trim().length()<=0){
        showDialog("Enter Authorized By");
    }
    else if(et_chkby.getText().toString().trim().length()<=0){
        showDialog("Enter Checked By");
    }
    else{
        submitOutward();
    }
    }

    private void submitOutward() {
        String gatepass=et_gatepass.getText().toString().trim();
        String description=et_desc.getText().toString().trim();
        String quantity=et_qnty.getText().toString().trim();
        String materialType=sp_type.getSelectedItem().toString().trim();
        String date=out_date.getText().toString().trim();
        String goingto=et_goingto.getText().toString().trim();
        String pname=et_taken_name.getText().toString().trim();
        String pphone=et_taken_phone.getText().toString().trim();
        String vehiclenum=et_vclenum.getText().toString().trim();
        String authby=et_authby.getText().toString().trim();
        String checkedby=et_chkby.getText().toString().trim();
        String comments=et_coments.getText().toString().trim();
        new Uploadoutwards().execute(gatepass,description,quantity,materialType,date,goingto,pname,pphone,authby,vehiclenum,
                comments,user_id,cur_date,checkedby);

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
    private void updateStartDate() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        out_date.setText(sdf.format(myCalendar.getTime()));
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    void showSnackBar(String message){
        Snackbar snackbar = Snackbar
                .make(outwardframe, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    /**
     * Uploading the file to server
     * */
    private class Uploadoutwards extends AsyncTask<String, Void, Void> {

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

        //    (gatepass,description,quantity,materialType,date,goingto,pname,pphone,authby,vehiclenum,
          //          comments,user_id,cur_date,checkedby);

            try {
                String _gatepass = params[0];
                String _desc = params[1];
                String _qnty = params[2];
                String _material_by = params[3];
                String _date = params[4];
                String _goingto = params[5];
                String _pname = params[6];
                String _pmobile = params[7];
                String _authby = params[8];
                String _vechle = params[9];
                String _comments = params[10];
                String _user_id = params[11];
                String _curr_date = params[12];
                String _checked_by = params[13];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.OUTWARD;
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
                        .appendQueryParameter("gatepass_no", _gatepass)
                        .appendQueryParameter("description", _desc)
                        .appendQueryParameter("quentity", _qnty)
                        .appendQueryParameter("gatepass_type", _material_by)
                        .appendQueryParameter("return_date", _date)
                        .appendQueryParameter("going_to", _goingto)
                        .appendQueryParameter("taken_by", _pname)
                        .appendQueryParameter("taken_mobile", _pmobile)
                        .appendQueryParameter("authorised_by", _authby)
                        .appendQueryParameter("vehicle_number", _vechle)
                        .appendQueryParameter("comments", _comments)
                        .appendQueryParameter("entry_by", _user_id)
                        .appendQueryParameter("checked_by", _checked_by)
                        .appendQueryParameter("released_on", _curr_date);

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
        et_gatepass.setText("");
        et_desc.setText("");
        et_qnty.setText("");
        et_goingto.setText("");
        sp_type.setSelection(0);
        et_taken_phone.setText("");
        et_taken_name.setText("");
        et_vclenum.setText("");
        et_authby.setText("");
        et_chkby.setText("");
        et_coments.setText("");
    }

}
