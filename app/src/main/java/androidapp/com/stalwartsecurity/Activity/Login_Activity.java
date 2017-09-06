package androidapp.com.stalwartsecurity.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;

import androidapp.com.stalwartsecurity.Pojo.User;
import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;

public class Login_Activity extends AppCompatActivity {
    EditText et_phone,et_pass;
    Button bt_login,bt_reset;
    /*    private static final int PERMISSION_ACCESS_CAMERA=101;
        private static final int PERMISSION_READ_STORAGE=103;
        private static final int PERMISSION_WRITE_STORAGE=104;*/
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

    ArrayList<User> userDetails;
    RelativeLayout rel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // here it is checking whether the permission is granted previously or not
            if (!hasPermissions(this, PERMISSIONS)) {
                //Permission is granted
                ActivityCompat.requestPermissions(this, PERMISSIONS, 1);

            }
        }
        rel=(RelativeLayout)findViewById(R.id.rel);
        et_phone=(EditText)findViewById(R.id.mobile_no);
        et_pass=(EditText)findViewById(R.id.password);
        bt_login=(Button)findViewById(R.id.btnLogin);
        bt_reset=(Button)findViewById(R.id.btnReset);
        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAll();
            }
        });
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
                /*Intent intent= new Intent(Login_Activity.this,ResidentHome.class);
                startActivity(intent);*/
            }
        });

    }

    private void checkValidation() {
        String phone_num=et_phone.getText().toString();
        String username=et_phone.getText().toString();
        String password=et_pass.getText().toString();
        String password1=et_pass.getText().toString();

        if(phone_num.trim().length()<10){
            et_phone.setError("Enter valid phone number");
        }
        else if(password.trim().length()<0){
            et_pass.setError("Enter password");
        }
        else{
            login(phone_num,password);
        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    private void login(String phone,String password) {
        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            // Toast.makeText(Signup.this, R.string.otp_sent, Toast.LENGTH_LONG).show();
            Login_asyn login=new Login_asyn();
            login.execute(phone,password);
        }else {
            Snackbar snackbar = Snackbar
                    .make(rel, "No Internet", Snackbar.LENGTH_LONG);
            snackbar.show();        }

    }

    private void resetAll() {
        et_phone.setText("");
        et_pass.setText("");
    }

/*
* Login Asyntask for security
* */

    private class Login_asyn extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        private ProgressDialog progressDialog = null;
        int server_status;
        String id,mobile,name;
        String server_message;
        String user_type;
        String photo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(Login_Activity.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _userphone = params[0];
                String _userpass=params[1];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.OFFLINE+Constants.LOGIN;
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
                        .appendQueryParameter("mobile", _userphone)
                        .appendQueryParameter("password", _userpass);

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
                if(in == null){
                    return null;
                }
                BufferedReader reader =new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "",data="";

                while ((data = reader.readLine()) != null){
                    response += data + "\n";
                }

                Log.i(TAG, "Response : "+response);

                /**
                 * {
                 "id": "9",
                 "name": "Amaresh Samantaray",
                 "email_id": "amaresh@gmail.com",
                 "mobile": "9090403050",
                 "photo": "photo1500115850.jpg",
                 "address": "bbsr",
                 "user_type": "Security",
                 "is_enable": "1",
                 "login_status": 1,
                 "message": "Successfully Logged In"
                 }
                 * */

                /*    FOR fo
                {
                "id": "25",
                "name": "Rasmita",
                "email_id": null,
                "mobile": "8594938936",
                "photo": "http://192.168.0.2/FO/files/photo/photo1504604209.jpg",
                "address": "",
                "user_type": "FO",
                "is_enable": "1",
                "login_status": 1,
                "message": "Successfully Logged In"
            }
                * */


                userDetails=new ArrayList<>();
                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("login_status");
                    if(server_status==1) {
                        id=res.optString("id");
                        name = res.optString("name");
                        String email_id = res.optString("email_id");
                        mobile = res.optString("mobile");
                        photo = res.optString("photo");
                        String address = res.optString("address");
                        String IS_ENABLE = res.optString("is_enable");
                        String login_status = res.optString("login_status");
                        user_type = res.optString("user_type");
                        server_message=res.optString("message");
                        User ulist=new User(id,name,email_id,mobile,photo,address,user_type);
                        userDetails.add(ulist);
                    }
                    else{
                        server_message="Invalid Credentials";
                    }
                }

                return null;

            } catch(SocketTimeoutException exception){
                server_message="Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch(ConnectException exception){
                server_message="Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch(MalformedURLException exception){
                server_message="Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch (IOException exception){
                server_message="Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch(Exception exception){
                server_message="Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progressDialog.cancel();
            if(server_status==1) {
                SharedPreferences sharedPreferences = Login_Activity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.N_USER_ID, id);
                editor.putString(Constants.N_USER_MOBILE, mobile);
                editor.putString(Constants.N_USER_NAME, name);
                editor.putString(Constants.N_USER_PHOTO, photo);
                editor.putString(Constants.N_USER_TYPE, user_type);
                editor.commit();

                if(user_type.contentEquals("Security")){
                    Intent intent=new Intent(Login_Activity.this,Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                else {

                    Intent intent=new Intent(Login_Activity.this,FOMainActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("mobile",mobile);
                    intent.putExtra("photo",photo);
                    startActivity(intent);

                }
            }

            else {
                // Toast.makeText(Login_Activity.this, server_message, Toast.LENGTH_LONG).show();
                Snackbar snackbar = Snackbar
                        .make(rel, server_message, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }
}
