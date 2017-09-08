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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

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
import java.util.Date;

import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;

/**
 * Created by mobileapplication on 9/4/17.
 */

public class CheckinActivity1 extends AppCompatActivity {
    EditText alrt_cmnt,vaca_cmnt,grv_cmnt,clnt_cmnt,sum_cmnt,incdnt_cmnt;
    ImageView photo;
    Button submit;
    RatingBar alrt_rtng,vac_rtng,grv_rtng,clnt_rtng,incdnt_rtng;
    String alrt_rating_value,vac_rating_value,grv_rating_value,clnt_rating_value,incdnt_rating_value;
    RelativeLayout linn;
    String user_id,checkin_id,date_time;
    ImageButton bt_alertplus,bt_alertminus,bt_vacancymin,bt_vacancyplus,bt_grivplus,bt_griv_min,
            bt_satisfymin,bt_satisfyplus,bt_issuemin,bt_issuemax;
    EditText et_alertrating,et_vacancy,et_griv,et_satisfy,et_issue,et_commentalert,et_commentvacancy,et_comment_gric,
            et_comment_staisfy,et_comment_issue,et_summery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fo_checkin1);



        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mma");
        dateFormatter.setLenient(false);
        Date today = new Date();
        date_time = dateFormatter.format(today);
        user_id =CheckinActivity1.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_ID, null);
        checkin_id =CheckinActivity1.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.CHECKIN_ID, null);






        bt_alertminus=(ImageButton)findViewById(R.id.imgbt_alertmin);
        bt_alertplus=(ImageButton)findViewById(R.id.imgbt_alertplus);
        et_alertrating=(EditText)findViewById(R.id.et_alert);
        bt_satisfymin=(ImageButton)findViewById(R.id.imgbt_staisfymin);
        bt_satisfyplus=(ImageButton)findViewById(R.id.imgbt_staisfyplus);
        et_satisfy=(EditText)findViewById(R.id.et_satisfy);
        bt_issuemin=(ImageButton)findViewById(R.id.imgbt_issuemin);
        bt_issuemax=(ImageButton)findViewById(R.id.imgbt_issueplus);
        et_issue=(EditText)findViewById(R.id.et_issue);
        et_commentalert=(EditText)findViewById(R.id.et_comment_alert);
        et_comment_staisfy=(EditText)findViewById(R.id.et_comment_satis);
        et_comment_issue=(EditText)findViewById(R.id.et_comment_issue);


        linn=(RelativeLayout)findViewById(R.id.linn);

        photo=(ImageView)findViewById(R.id.unit_img);
        submit=(Button)findViewById(R.id.chkin_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatefields();
            }
        });
        bt_alertminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEditTextlow("alert");
            }
        });
        bt_alertplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEditTexthigh("alert");
            }
        });
        bt_vacancymin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEditTextlow("vacancy");
            }
        });
        bt_vacancyplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEditTexthigh("vacancy");
            }
        });
        bt_grivplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEditTexthigh("griv");
            }
        });
        bt_alertminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEditTextlow("griv");
            }
        });
        bt_satisfyplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEditTexthigh("satisfy");
            }
        });
        bt_alertminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEditTextlow("satisfy");
            }
        });
        bt_issuemax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEditTexthigh("issue");
            }
        });
        bt_issuemin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEditTextlow("issue");
            }
        });



    }

    private void showsnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(linn, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    private void updateEditTexthigh(String message) {
        if(message.contains("alert")){
            String alertRatings=et_alertrating.getText().toString().trim();
            if(alertRatings.contains("Good")){
                et_alertrating.setText("Better");
            }
            else if(alertRatings.contains("Average")){
                et_alertrating.setText("Good");
            }
            else if(alertRatings.contains("Bad")){
                et_alertrating.setText("Average");
            }
            else{
                showsnackbar("This is the maximum rating");
            }
        }
        else if(message.contains("vacancy")){
            String alertRatings=et_vacancy.getText().toString().trim();
            if(alertRatings.contains("Half")){
                et_vacancy.setText("Not-full");
            }
            else if(alertRatings.contains("Not-full")){
                et_vacancy.setText("Full");
            }
            else if(alertRatings.contains("Not-empty")){
                et_vacancy.setText("Half");
            }
            else{
                showsnackbar("This is the maximum rating");
            }
        }
        else if(message.contains("griv")){
            String alertRatings=et_griv.getText().toString().trim();
            if(alertRatings.contains("NA")){
                et_griv.setText("Less");
            }
            else if(alertRatings.contains("Less")){
                et_griv.setText("Average");
            }
            else if(alertRatings.contains("Average")){
                et_griv.setText("More");
            }
            else{
                showsnackbar("This is the maximum rating");
            }
        }
        else if(message.contains("satisfy")){
            String alertRatings=et_satisfy.getText().toString().trim();
            if(alertRatings.contains("Good")){
                et_satisfy.setText("Better");
            }
            else if(alertRatings.contains("Average")){
                et_satisfy.setText("Good");
            }
            else if(alertRatings.contains("Bad")){
                et_satisfy.setText("Average");
            }
            else{
                showsnackbar("This is the maximum rating");
            }
        }
        else if(message.contains("issue")){
            String alertRatings=et_issue.getText().toString().trim();
            if(alertRatings.contains("NA")){
                et_issue.setText("Less");
            }
            else if(alertRatings.contains("Less")){
                et_issue.setText("Average");
            }
            else if(alertRatings.contains("Average")){
                et_issue.setText("More");
            }
            else{
                showsnackbar("This is the maximum rating");
            }
        }
    }
    private void updateEditTextlow(String message) {
        if (message.contains("alert")) {
            String alertRatings = et_alertrating.getText().toString().trim();
            if (alertRatings.contains("Good")) {
                et_alertrating.setText("Average");
            } else if (alertRatings.contains("Average")) {
                et_alertrating.setText("Bad");
            } else if (alertRatings.contains("Better")) {
                et_alertrating.setText("Good");
            } else {
                showsnackbar("This is the minimum rating");
            }
        } else if (message.contains("vacancy")) {
            String alertRatings = et_vacancy.getText().toString().trim();
            if (alertRatings.contains("Half")) {
                et_vacancy.setText("Not-empty");
            } else if (alertRatings.contains("Not-full")) {
                et_vacancy.setText("Half&q");
            }
        }
    }

    private void validatefields() {

        String alertcomnt=alrt_cmnt.getText().toString();
        String vacaCmnt=vaca_cmnt.getText().toString();
        String grvCmnt=grv_cmnt.getText().toString();
        String clntCmnt=clnt_cmnt.getText().toString();
        String incdntCmnt=incdnt_cmnt.getText().toString();
        String sumCmnt=sum_cmnt.getText().toString();

        String alert_rating=alrt_rating_value;
        String vac_rating=vac_rating_value;
        String grv_rating=grv_rating_value;
        String client_rating=clnt_rating_value;
        String incdnt_rating=incdnt_rating_value;


     if(alertcomnt.contentEquals("")){
         showsnackbar("Give Comments");

     }
     else if(vacaCmnt.contentEquals("")){
         showsnackbar("Give Comments");
     }
     else if(grvCmnt.contentEquals("")){
         showsnackbar("Give Comments");
     }
     else if(vacaCmnt.contentEquals("")){
         showsnackbar("Give Comments");
     }
     else if(clntCmnt.contentEquals("")){
         showsnackbar("Give Comments");
     }
     else if(incdntCmnt.contentEquals("")){
         showsnackbar("Give Comments");
     }
     else if(sumCmnt.contentEquals("")){
         showsnackbar("Give Comments");
     }
     else if(alert_rating.contentEquals("")){
         showsnackbar("Give Ratings");
     }
     else if(vac_rating.contentEquals("")){
         showsnackbar("Give Ratings");
     }
     else if(grv_rating.contentEquals("")){
         showsnackbar("Give Ratings");
     }
     else if(incdnt_rating.contentEquals("")){
         showsnackbar("Give Ratings");
     }
     else if(client_rating.contentEquals("")){
         showsnackbar("Give Ratings");
     }
     else {
         CheckinServer();
     }





    }

    private void CheckinServer() {

        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            Checkin_asyn checkin = new Checkin_asyn();
            String checkin_type="1";
            String alertcomnt=alrt_cmnt.getText().toString();
            String vacaCmnt=vaca_cmnt.getText().toString();
            String grvCmnt=grv_cmnt.getText().toString();
            String clntCmnt=clnt_cmnt.getText().toString();
            String incdntCmnt=incdnt_cmnt.getText().toString();
            String sumCmnt=sum_cmnt.getText().toString();

            String alert_rating=alrt_rating_value;
            String vac_rating=vac_rating_value;
            String grv_rating=grv_rating_value;
            String client_rating=clnt_rating_value;
            String incdnt_rating=incdnt_rating_value;
            String photo="";
            checkin.execute(checkin_id,checkin_type,alertcomnt,alert_rating,vacaCmnt,vac_rating,grvCmnt,
                    grv_rating,clntCmnt,client_rating,incdntCmnt,incdnt_rating,sumCmnt,photo,date_time);
        } else {
            showsnackbar("No Internet");
        }

    }

    private class Checkin_asyn extends AsyncTask<String, Void, Void> {

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
                progressDialog = ProgressDialog.show(CheckinActivity1.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String checkin_id = params[0];
                String checkin_type = params[1];
                String alertness_comments = params[2];
                String alertness_ratings = params[3];
                String vacancies_comments = params[4];
                String vacancies_ratings = params[5];
                String gra_comments = params[6];
                String gra_ratings = params[7];
                String client_comments = params[8];
                String client_ratings = params[9];
                String incident_comments = params[10];
                String incident_ratings = params[11];
                String summary = params[12];
                String photo = params[13];
                String date_time = params[14];
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
                        .appendQueryParameter("alertness_comments", alertness_comments)
                        .appendQueryParameter("alertness_ratings", alertness_ratings)
                        .appendQueryParameter("vacancies_comments", vacancies_comments)
                        .appendQueryParameter("vacancies_ratings", vacancies_ratings)
                        .appendQueryParameter("grievance_comments", gra_comments)
                        .appendQueryParameter("grievance_ratings", gra_ratings)
                        .appendQueryParameter("client_comments", client_comments)
                        .appendQueryParameter("client_ratings", client_ratings)
                        .appendQueryParameter("incident_comments", incident_comments)
                        .appendQueryParameter("incident_ratings", incident_ratings)
                        .appendQueryParameter("summary", summary)
                        .appendQueryParameter("photo", photo)
                        .appendQueryParameter("date_time", date_time);

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
                        id = res.optString("checkintype_id");
                        server_message = res.optString("message");

                        //showsnackbar(server_message);

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
            progressDialog.cancel();
            if (server_status == 1) {

                SharedPreferences sharedPreferences = CheckinActivity1.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.CHECKIN_CHECKIN_TYPE_ID, id);
                editor.commit();
                Intent i=new Intent(CheckinActivity1.this,CheckinActivity.class);
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
        super.onBackPressed();
        Intent i=new Intent(CheckinActivity1.this,CheckinActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
        finish();
    }

    }
