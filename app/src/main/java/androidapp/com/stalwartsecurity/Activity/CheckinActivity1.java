package androidapp.com.stalwartsecurity.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
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
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    SignaturePad signaturePad;
    Button clearpad, savesignatory;



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
                //validatefields();
                CheckinServer();
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

       /*
       * For Signature...
       *
       * */



        clearpad = (Button) findViewById(R.id.clear_button);
        savesignatory = (Button) findViewById(R.id.save_button);
        signaturePad = (SignaturePad) findViewById(R.id.signator);
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                Toast.makeText(CheckinActivity1.this, "OnStartSigning", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSigned() {
                clearpad.setEnabled(true);
                savesignatory.setEnabled(true);

            }

            @Override
            public void onClear() {
                clearpad.setEnabled(false);
                savesignatory.setEnabled(false);

            }
        });


        clearpad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signaturePad.clear();
            }
        });

        savesignatory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap)) {
                    Toast.makeText(CheckinActivity1.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CheckinActivity1.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }
                if (addSvgSignatureToGallery(signaturePad.getSignatureSvg())) {
                    Toast.makeText(CheckinActivity1.this, "SVG Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CheckinActivity1.this, "Unable to store the SVG signature", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(CheckinActivity1.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        CheckinActivity1.this.sendBroadcast(mediaScanIntent);
    }

    public boolean addSvgSignatureToGallery(String signatureSvg) {
        boolean result = false;
        try {
            File svgFile = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.svg", System.currentTimeMillis()));
            OutputStream stream = new FileOutputStream(svgFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(signatureSvg);
            writer.close();
            stream.flush();
            stream.close();
            scanMediaFile(svgFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity the activity from which permissions are checked
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


}

    private void showsnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(linn, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    private void updateEditTexthigh(String message) {
        if(message.contains("alert")){
            String alertRatings=et_alertrating.getText().toString().trim();
            if(alertRatings.contains("Poor")){
                et_alertrating.setText("Average");
            }
            else if(alertRatings.contains("Average")){
                et_alertrating.setText("Good");
            }
            else{
                showsnackbar("This is the maximum rating");
            }
        }

        else if(message.contains("satisfy")){
            String alertRatings=et_satisfy.getText().toString().trim();
            if(alertRatings.contains("Poor")){
                et_satisfy.setText("Average");
            }
            else if(alertRatings.contains("Average")){
                et_satisfy.setText("Good");
            }

            else{
                showsnackbar("This is the maximum rating");
            }
        }
        else if(message.contains("issue")){
            String alertRatings=et_issue.getText().toString().trim();
            if(alertRatings.contains("Poor")){
                et_issue.setText("Average");
            }
            else if(alertRatings.contains("Average")){
                et_issue.setText("Good");
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
                et_alertrating.setText("Poor");
            } else {
                showsnackbar("This is the minimum rating");
            }
        } else if (message.contains("satisfy")) {
            String alertRatings = et_satisfy.getText().toString().trim();
            if (alertRatings.contains("Good")) {
                et_satisfy.setText("Average");
            } else if (alertRatings.contains("Average")) {
                et_satisfy.setText("Poor");
            }else {
                showsnackbar("This is the minimum rating");
            }
        }
        else if(message.contains("issue")){
            String alertRatings=et_issue.getText().toString().trim();
            if(alertRatings.contains("Good")){
                et_issue.setText("Average");
            }
            else if(alertRatings.contains("Average")){
                et_issue.setText("Poor");
            }

            else{
                showsnackbar("This is the minimum rating");
            }
        }
    }

    private void validatefields() {

        String alertcomnt=et_commentalert.getText().toString();
        String clntCmnt=et_comment_staisfy.getText().toString();
        String incdntCmnt=et_comment_issue.getText().toString();

        String alert_rating=et_alertrating.getText().toString();
        String client_rating=et_satisfy.getText().toString();
        String incdnt_rating=et_issue.getText().toString();

    }

    private void CheckinServer() {

        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            Checkin_asyn checkin = new Checkin_asyn();
            String checkin_type="1";
            String alertcomnt=et_commentalert.getText().toString();
            String clntCmnt=et_comment_staisfy.getText().toString();
            String incdntCmnt=et_comment_issue.getText().toString();

            String alert_rating=et_alertrating.getText().toString();
            String client_rating=et_satisfy.getText().toString();
            String incdnt_rating=et_issue.getText().toString();
            String photo="";
            checkin.execute(checkin_id,checkin_type,alertcomnt,alert_rating,
                    clntCmnt,client_rating,incdntCmnt,incdnt_rating,photo,date_time);
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
                String client_comments = params[4];
                String client_ratings = params[5];
                String incident_comments = params[6];
                String incident_ratings = params[7];
                String photo = params[8];
                String date_time = params[9];
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
                        .appendQueryParameter("client_comments", client_comments)
                        .appendQueryParameter("client_ratings", client_ratings)
                        .appendQueryParameter("incident_comments", incident_comments)
                        .appendQueryParameter("incident_ratings", incident_ratings)
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
