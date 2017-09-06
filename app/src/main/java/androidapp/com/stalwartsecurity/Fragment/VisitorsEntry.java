package androidapp.com.stalwartsecurity.Fragment;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidapp.com.stalwartsecurity.Activity.DeliveryBoysList;
import androidapp.com.stalwartsecurity.Activity.FlatList;
import androidapp.com.stalwartsecurity.Activity.Home;
import androidapp.com.stalwartsecurity.Adapter.DeliveryBoysAdapter;
import androidapp.com.stalwartsecurity.Pojo.UserDetailsLoc;
import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;
import androidapp.com.stalwartsecurity.Util.MultipartUtility;
import de.hdodenhof.circleimageview.CircleImageView;

public class VisitorsEntry extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    Boolean picAvailable=false;
    CircleImageView profile_image;
    private static final int CAMERA_REQUEST = 1888;
    Spinner v_type;
    Uri picUri=null;
    EditText v_name,v_cmng,v_appt,v_vhcl,v_pass,et_dtntime;
    AutoCompleteTextView v_ph;
    TextView passheading;
    Button submit;
    String imPath;
    Uri fileUri;
    File imageFile;
    String phone;
    String name;
    String apprt;
    String cmngFrom;
    String vechleNo;
    String passNo;
    String visType;
    int server_status;
    String server_message;
    long totalSize = 0;
    String curr_date;
    String user_id;
    KeyListener variable;

    RelativeLayout v_frame;
    TextView tvh_name,tvh_phone,tvh_cmng,tvh_vcl,tvh_pass,tvh_date,tvh_appart;



    private OnFragmentInteractionListener mListener;

    public VisitorsEntry() {
        // Required empty public constructor
    }
    public static VisitorsEntry newInstance(String param1, String param2) {
        VisitorsEntry fragment = new VisitorsEntry();
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
        View v=inflater.inflate(R.layout.fragment_visitors_entry, container, false);
        user_id = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_ID, null);
        profile_image=(CircleImageView)v.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
        v_ph=(AutoCompleteTextView)v.findViewById(R.id.et_phone);
        v_ph.setAdapter(getMobileNumber(getActivity()));
        v_name=(EditText)v.findViewById(R.id.et_name);
        v_cmng=(EditText)v.findViewById(R.id.et_frm);
        v_appt=(EditText)v.findViewById(R.id.et_appt);
        v_vhcl=(EditText)v.findViewById(R.id.et_vcle);
        et_dtntime=(EditText)v.findViewById(R.id.et_dtntime);
        variable = v_cmng.getKeyListener();

        v_type=(Spinner)v.findViewById(R.id.sp_visitors);
        List<String> VisitorsType = new ArrayList<String>();
        VisitorsType.add("-- Select Visitors Type --");
        VisitorsType.add("Guest");
        VisitorsType.add("Pre-authorised Guest");
        VisitorsType.add("Delivery Boys");
        VisitorsType.add("Others");
        passheading=(TextView)v.findViewById(R.id.passheading);
        v_pass=(EditText)v.findViewById(R.id.et_pass);
        ArrayAdapter<String>adapte_visitors = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, VisitorsType);
        adapte_visitors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        v_type.setAdapter(adapte_visitors);
        v_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String _vtype=v_type.getSelectedItem().toString();
                if(_vtype.contentEquals("Pre-authorised Guest")){
                    v_pass.setVisibility(View.VISIBLE);
                    passheading.setVisibility(View.VISIBLE);
                }
                else{
                    passheading.setVisibility(View.GONE);
                    v_pass.setVisibility(View.GONE);
                }
                if(_vtype.contentEquals("Delivery Boys")) {
                    v_cmng.setKeyListener(null);
                }
                else{
                    v_cmng.setKeyListener(variable);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        v_cmng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v_type.getSelectedItem().toString().trim().contentEquals("Delivery Boys")){
                    Intent intent=new Intent(getActivity(),DeliveryBoysList.class);
                    getContext().startActivity(intent);
                    DeliveryBoysAdapter.selected=v_cmng;
                }
            }
        });

        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mma");
        dateFormatter.setLenient(false);
        Date today = new Date();
        et_dtntime.setText( dateFormatter.format(today));
        v_appt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlatList.flatName=v_appt;
                Intent intent=new Intent(getActivity(),FlatList.class);
                startActivity(intent);
            }
        });

        submit=(Button)v.findViewById(R.id.submit);
        tvh_phone=(TextView)v.findViewById(R.id.phone_heading);
        tvh_name=(TextView)v.findViewById(R.id.nameheading);
        tvh_appart=(TextView)v.findViewById(R.id.apptnoheading);
        tvh_cmng=(TextView)v.findViewById(R.id.cmngheading);
        tvh_date=(TextView)v.findViewById(R.id.dateheading);
        tvh_pass=(TextView)v.findViewById(R.id.passheading);
        String a="Enter Phone";
        String a1="Enter name";
        String a2="Enter Flat";
        String a3="Coming from";
        String a4="Pass Code";
        String b="*";
        SpannableStringBuilder builder = new SpannableStringBuilder(a+b);
        builder.setSpan(new ForegroundColorSpan(Color.RED), a.length(), builder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvh_phone.setText(builder);
        SpannableStringBuilder builder1 = new SpannableStringBuilder(a1+b);
        builder1.setSpan(new ForegroundColorSpan(Color.RED), a1.length(), builder1.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvh_name.setText(builder1);
        SpannableStringBuilder builder2 = new SpannableStringBuilder(a2+b);
        builder2.setSpan(new ForegroundColorSpan(Color.RED), a2.length(), builder2.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvh_appart.setText(builder2);

        SpannableStringBuilder builder3 = new SpannableStringBuilder(a3+b);
        builder3.setSpan(new ForegroundColorSpan(Color.RED), a3.length(), builder3.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvh_cmng.setText(builder3);

        SpannableStringBuilder builder4 = new SpannableStringBuilder(a4+b);
        builder4.setSpan(new ForegroundColorSpan(Color.RED), a4.length(), builder4.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvh_pass.setText(builder4);

        v_frame=(RelativeLayout)v.findViewById(R.id.v_frame);
        v_frame=(RelativeLayout)v.findViewById(R.id.v_frame);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.getNetworkConnectivityStatus(getContext())) {
                    // Toast.makeText(Signup.this, R.string.otp_sent, Toast.LENGTH_LONG).show();
                    validation();
                }else {
                    Snackbar snackbar = Snackbar
                            .make(v_frame, "No Internet", Snackbar.LENGTH_LONG);
                    snackbar.show();        }

            }
        });

        v_ph.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {if(v_ph.getText().toString().trim().length()>9){
                    for(int i=0;i<Home.offlineVList.size();i++){
                        if(v_ph.getText().toString().trim().contentEquals(Home.offlineVList.get(i).getPhone().trim())){
                            v_name.setText(Home.offlineVList.get(i).getName());
                            v_cmng.setText(Home.offlineVList.get(i).getComing_from());
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }

    private void captureImage() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        imPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
         imageFile = new File(imPath);
        picUri = Uri.fromFile(imageFile); // convert path to Uri
        cameraIntent.putExtra( MediaStore.EXTRA_OUTPUT, picUri );
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
           // imPath=picUri.getPath();
           // Bitmap photo = (Bitmap) data.getExtras().get("data");
            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),picUri);
                picAvailable=true;
                profile_image.setImageBitmap(photo);

            } catch (IOException e) {
                e.printStackTrace();
            }

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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        super.onDetach();
        mListener = null;
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private  void validation(){
       /* if(!picAvailable){
            showDialog("Photo is mandatory");
        }*/
         if(v_type.getSelectedItem().equals("-- Select Visitors Type --")){
            showDialog("Select Visitor Type");
        }
        else if(v_type.getSelectedItem().equals("Pre-authorised Guest") && v_pass.getText().toString().length()<=0){
            showDialog("Enter Pass Number");
        }
        else if(v_ph.getText().toString().trim().length()<10){
            showDialog("Enter valid Phone number");
        }
        else if(v_name.getText().toString().trim().length()<=0){
            showDialog("Enter Name");

        }
        else if(v_appt.getText().toString().trim().length()<=0){
            showDialog("Enter Apartment");

        }
        else if(v_cmng.getText().toString().trim().length()<=0){
            showDialog("Coming From is blank");

        }
        else if(Home.Locationid.trim()==null){
            showDialog("Location not found");
        }
        else{
            submittingDetails();
        }
    }

    private void submittingDetails() {
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mma");
        dateFormatter.setLenient(false);
        Date today = new Date();
        curr_date = dateFormatter.format(today);

         phone=v_ph.getText().toString().trim();
         name=v_name.getText().toString().trim();
         apprt=v_appt.getText().toString().trim();
         cmngFrom=v_cmng.getText().toString().trim();
         vechleNo=v_vhcl.getText().toString().trim();
         passNo=v_pass.getText().toString().trim();
         visType=v_type.getSelectedItem().toString().trim();
         new UploadFileToServer().execute(phone,name,apprt,cmngFrom,vechleNo,passNo,visType,user_id);

    }
    private ArrayAdapter<String> getMobileNumber(Context context) {

        String[] OfflineVArray = new String[Home.offlineVList.size()];
        for (int i = 0; i < Home.offlineVList.size(); i++) {
            OfflineVArray[i] = Home.offlineVList.get(i).getPhone();
        }
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, OfflineVArray);
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
    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<String, Void, Void> {

        String TAG = "FileUpload";
        private boolean is_success = false;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(getContext(), "Please Wait",
                    "Adding Visitor...", true);
        }

        @Override
        protected Void doInBackground(String... params) {
            String charset = "UTF-8";
            String requestURL = "";
            //requestURL = Config.API_BASE_URL + Config.POST_CITIZEN_NEWS;
            requestURL = Constants.MAINURL+Constants.ADDVISITORS;

            try {
                String _phone = params[0];
                String _name = params[1];
                String _apprt = params[2];
                String _comingfrom = params[3];
                String _veichle = params[4];
                String _pass = params[5];
                String _v_type = params[6];
                String _added_by = params[7];
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("mobile", _phone);
                multipart.addFormField("name", _name);
                multipart.addFormField("appartment_name", _apprt);
                multipart.addFormField("coming_from", _comingfrom);
                multipart.addFormField("vehicle_no", _veichle);
                multipart.addFormField("pass_no", _pass);
                multipart.addFormField("visiters_type", _v_type);
                multipart.addFormField("added_by", _added_by);
                multipart.addFormField("location_id",Home.Locationid.trim());
                multipart.addFormField("in_time", curr_date);
                // if (images_to_post != null && images_to_post.exists())
                if(imageFile!=null){
                    multipart.addFilePart("photo",imageFile );
                }
                List<String> response = multipart.finish();
                System.out.println("SERVER REPLIED:");
                String res = "";
                for (String line : response) {
                    res = res + line + "\n";
                }
                Log.i(TAG, res);


                /*
                    "status": 1,
    "message": "Successfully inserted"(
                * */

                if(res != null && res.length() > 0) {
                    JSONObject res_server = new JSONObject(res.trim());
                    server_status = res_server.optInt("status");
                    if (server_status == 1) {
                        server_message="Entry Successful";

                    } else {
                        server_message="Sorry !! Entry failed";
                    }
                }
            } catch (ConnectTimeoutException e) {
                server_message="Network Error";
                System.err.println(e);
            } catch (IOException ex) {
                server_message="Network Error";
                System.err.println(ex);
            } catch (JSONException e) {
                server_message="Network Error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            progress.dismiss();
            if(server_status==1){
            resetAll();
            }
            Snackbar snackbar = Snackbar
                    .make(v_frame, server_message, Snackbar.LENGTH_LONG);
            snackbar.show();

            //Toast.makeText(UploadAssets.this,server_message,Toast.LENGTH_LONG).show();
        }
    }

    private void resetAll() {
        v_ph.setText("");
        v_name.setText("");
        v_cmng.setText("");
        v_appt.setText("");
        v_vhcl.setText("");
        v_pass.setText("");
        profile_image.setImageResource(R.drawable.app_logo);

    }

}
