package androidapp.com.stalwartsecurity.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidapp.com.stalwartsecurity.Activity.MainActivity;
import androidapp.com.stalwartsecurity.Activity.UnitList;
import androidapp.com.stalwartsecurity.Adapter.ImageListAdapter;
import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;

public class LodgeIncidentFragment extends Fragment {
    Button next,next1;
    RelativeLayout log_form,log_form1,log_form2,camera,gallery;
    Spinner statustype,intencity,incident;
    FloatingActionButton click;
    Dialog dialog;
    ListView lv_img;
    ImageListAdapter imageadapter;
    private final static int RESULT_SELECT_IMAGE = 100;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    EditText log_date,log_time;
    Calendar myCalendar;
    ImageView camera_img,gallery_img;
    public static TextView unity_name;
    public static String unityid;
    FrameLayout linn;
    //Integer array= new Integer(R.drawable.image);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.lodge_fragment, container, false);
        linn=(FrameLayout)view.findViewById(R.id.linn);
        unity_name=(TextView)view.findViewById(R.id.unity_name);
        log_date=(EditText)view.findViewById(R.id.log_date);
        log_time=(EditText)view.findViewById(R.id.log_time);
        click=(FloatingActionButton)view.findViewById(R.id.float_btn);
        statustype=(Spinner)view.findViewById(R.id.status_id);
        camera=(RelativeLayout)view.findViewById(R.id.choose_camera);
        gallery=(RelativeLayout)view.findViewById(R.id.choose_gallery);
        lv_img=(ListView)view.findViewById(R.id.photo_list);
        intencity=(Spinner)view.findViewById(R.id.intencity_type);
        incident=(Spinner)view.findViewById(R.id.incident_spin);
        log_form=(RelativeLayout)view.findViewById(R.id.log_incident_form);
        log_form1=(RelativeLayout)view.findViewById(R.id.log_form1);
        log_form2=(RelativeLayout)view.findViewById(R.id.log_form2);
        myCalendar= Calendar.getInstance();
        unity_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getContext(), UnitList.class);
                i.putExtra("pagename","LogIncident");
                startActivity(i);

            }
        });




        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };




        log_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               DatePickerDialog datepickerDialog= new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datepickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                log_date.setText(date.toString());

                datepickerDialog.show();

            }
        });
        log_time.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(),new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        log_time.setText(selectedHour + ":" + selectedMinute);
                    }
                },hour, minute, true);//Yes 24 hour time
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }

        });

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClicltoFloat();
            }
        });
        next=(Button)view.findViewById(R.id.log_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                log_form.setVisibility(View.GONE);
                log_form1.setVisibility(View.VISIBLE);
                log_form2.setVisibility(View.GONE);

                Checkinserver();
            }
        });


        next1=(Button)view.findViewById(R.id.log_next1);
        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                        log_form.setVisibility(View.GONE);
                        log_form1.setVisibility(View.GONE);
                        log_form2.setVisibility(View.VISIBLE);
                    }
                });

        List<String> list = new ArrayList<String>();
        list.add("Open");
        list.add("Close");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statustype.setAdapter(dataAdapter);
        statustype.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        List<String> list1 = new ArrayList<String>();
        list1.add("High");
        list1.add("Low");
        list1.add("Medium");

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, list1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intencity.setAdapter(dataAdapter1);
        intencity.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        List<String> list2 = new ArrayList<String>();
        list2.add("Accident");
        list2.add("Theft");
        list2.add("Missing Material");
        list2.add("Fight/Misbehave");
        list2.add("Others..");


        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, list2);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incident.setAdapter(dataAdapter2);
        incident.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

       imagelIST();
        return view;
    }

    private void Checkinserver() {
        if (CheckInternet.getNetworkConnectivityStatus(getContext())) {
            Continue_log_Incident checkin = new Continue_log_Incident();
           // String unit_id = unitid;
           // String location = loc.getText().toString();
           // String dateTime = date_time.toString();
            checkin.execute(unityid);
        } else {
            showsnackbar("No Internet");
        }
    }

    private void showsnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(linn, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        log_date.setText(sdf.format(myCalendar.getTime()));
    }


    private void imagelIST() {
        imageadapter=new ImageListAdapter(getActivity());
        lv_img.setAdapter(imageadapter);

    }

    private void ClicltoFloat() {
        ChooseDialog();
    }

    private void ChooseDialog() {

        dialog = new Dialog(getContext());
        // it remove the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set the laytout in the dialog
        dialog.setContentView(R.layout.dialogbox);
        // set the background partial transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        param.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        // it dismiss the dialog when click outside the dialog frame
        dialog.setCanceledOnTouchOutside(true);
        // initialize the item of the dialog box, whose id is demo1
        View demodialog =(View) dialog.findViewById(R.id.cross);
        View camera_img=(View)dialog.findViewById(R.id.camera);
        View gallery_img=(View)dialog.findViewById(R.id.gallery);



        camera_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
        });
        gallery_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    //Pick Image From Gallery
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i,RESULT_SELECT_IMAGE);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        // it call when click on the item whose id is demo1.
        demodialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // diss miss the dialog
                dialog.dismiss();
            }
        });

        // it show the dialog box
        dialog.show();

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null)
                {
                    try
                    {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Continue_log_Incident extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        private ProgressDialog progressDialog = null;
        int server_status;
        String id, mobile, name;
        String server_message;
        String user_type;
        String photo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(getContext(), "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String unitid = params[0];
                String address = params[2];
                String datetime = params[3];
                String checkin_status = params[4];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.OFFLINE + Constants.CHECKIN;
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
                        .appendQueryParameter("unit_id", unitid)
                        .appendQueryParameter("date", address)
                        .appendQueryParameter("time", address)
                        .appendQueryParameter("place_of_incident", datetime)
                        .appendQueryParameter("reported_by_id", datetime)
                        .appendQueryParameter("status", datetime)
                        .appendQueryParameter("intensity", datetime)
                        .appendQueryParameter("incident_type", datetime)
                        .appendQueryParameter("description", datetime)
                        .appendQueryParameter("assigned_to_id", datetime)
                        .appendQueryParameter("action_taken", datetime)
                        .appendQueryParameter("photo", checkin_status);

                //.appendQueryParameter("deviceid", deviceid);
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

                /**
                 * {
                 "checkin_id": null,
                 "status": 1,
                 "message": "Successfully Checked In."
                 }
                 * */


                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if (server_status == 1) {
                        id = res.optString("checkin_id");
                        server_message = res.optString("message");

                    } else {
                        server_message = "Invalid Credentials";
                    }
                }

                return null;

            } catch (SocketTimeoutException exception) {
                server_message = "Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch (ConnectException exception) {
                server_message = "Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch (MalformedURLException exception) {
                server_message = "Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch (IOException exception) {
                server_message = "Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch (Exception exception) {
                server_message = "Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);

            if (server_status == 1) {
                showsnackbar("Successfully Checked In.");

                SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.CHECKIN_ID, id);
                editor.commit();


                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("checkinid",id);
                startActivity(intent);

            }
            else {
                showsnackbar(server_message);
            }
        }

    }
}