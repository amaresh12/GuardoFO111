package androidapp.com.stalwartsecurity.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;

/**
 * Created by mobileapplication on 9/7/17.
 */

public class NightCheckActivity extends AppCompatActivity {

    Spinner grd_spinner;
    EditText reason,id,name;
    Button submit;
    RelativeLayout gard,linn,spinner;
    String checkin_id;
    String spinvalue,date_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night_check);





        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mma");
        dateFormatter.setLenient(false);
        Date today = new Date();
        date_time = dateFormatter.format(today);
        checkin_id =NightCheckActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.CHECKIN_ID, null);


        linn=(RelativeLayout)findViewById(R.id.linn);
        gard=(RelativeLayout)findViewById(R.id.gard_no);
        grd_spinner=(Spinner)findViewById(R.id.grd_spinner);
        reason=(EditText)findViewById(R.id.et_reason);
        id=(EditText)findViewById(R.id.et_id);
        name=(EditText)findViewById(R.id.et_name);
        submit=(Button)findViewById(R.id.nt_chkn_btn);


        List<String> list = new ArrayList<String>();
        list.add("YES");
        list.add("NO");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grd_spinner.setAdapter(dataAdapter);
        grd_spinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        grd_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 spinvalue=grd_spinner.getSelectedItem().toString().toLowerCase();
                if(spinvalue.contentEquals("no")){
                    gard.setVisibility(View.VISIBLE);

                }
                else {
                    gard.setVisibility(View.GONE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spin_value = spinvalue;

                if (spin_value.contentEquals("yes")) {
                    NightcheckProceed();
                } else {

                    String reasn = reason.getText().toString();
                    String idd = id.getText().toString();
                    String namee = name.getText().toString();
                    if (reasn.contentEquals("")) {
                        showsnackbar("Give Reason");
                    } else if (idd.contentEquals("")) {
                        showsnackbar("give id");
                    } else if (namee.contentEquals("")) {
                        showsnackbar("give name");
                    } else {
                        NightcheckProceed();
                    }
                }
            }
        });


    }

    private void showsnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(linn, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    private void NightcheckProceed() {
        if (CheckInternet.getNetworkConnectivityStatus(this)) {
           Nightchk_asyn checkin = new Nightchk_asyn();
            String checkin_type="4";
            String grd_id=id.getText().toString();
            String grd_name=name.getText().toString();
            String grd_rsn=reason.getText().toString();
            String grd_alert=spinvalue;
            String photo="https://static.independent.co.uk/s3fs-public/styles/article_small/public/thumbnails/image/2017/03/16/17/sleeping-baby.jpg";

                checkin.execute(checkin_id,checkin_type, grd_id, grd_name, grd_rsn, grd_alert,date_time,photo);


        } else {
            showsnackbar("No Internet");
        }



    }

    private class Nightchk_asyn extends AsyncTask<String, Void, Void> {

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
                progressDialog = ProgressDialog.show(NightCheckActivity.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String checkin_id = params[0];
                String checkin_type = params[1];
                String guard_id = params[2];
                String guard_name = params[3];
                String reason = params[4];
                String guard_found_alert = params[5];
                String datetime = params[6];
                String photo = params[7];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.demo_url;
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
                        .appendQueryParameter("checkin_id", checkin_id)
                        .appendQueryParameter("checkin_type", checkin_type)
                        .appendQueryParameter("guard_found_alert", guard_found_alert)
                        .appendQueryParameter("guard_id", guard_id)
                        .appendQueryParameter("guard_name", guard_name)
                        .appendQueryParameter("reason", reason)
                        .appendQueryParameter("date_time", datetime)
                        .appendQueryParameter("photo", photo);

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
                 "checkintype_id": "15",
                 "status": 1,
                 "message": "Successfully added"
                 }
                 * */


                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if (server_status == 1) {
                        id = res.optString("checkintype_id");
                        server_message = res.optString("message");

                        showsnackbar(server_message);

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
                Log.e( "SynchMobnum ", exception.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progressDialog.cancel();

            if (server_status == 1) {

                SharedPreferences sharedPreferences = NightCheckActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.N_CHECKIN_CHECKIN_TYPE_ID, id);
                editor.commit();
                Intent i=new Intent(NightCheckActivity.this,CheckinActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                finish();


            } else {
                showsnackbar(server_message);
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(NightCheckActivity.this, CheckinActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
        finish();
    }
}
