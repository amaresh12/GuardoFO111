package androidapp.com.stalwartsecurity.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.squareup.picasso.Picasso;

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
import java.util.Locale;

import androidapp.com.stalwartsecurity.Adapter.UnitListAdapter;
import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;


/**
 * Created by mobileapplication on 8/8/17.
 */

public class CheckinActivity extends AppCompatActivity implements LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    TextView name, dateTime, unit, unit1;
    TextView loc, fo_name;
    Button btn_continue;
    private LocationManager lom;
    private Location los;
    private Context context;
    RelativeLayout linn;
    private String provider;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    Spinner visit;
    String unityName = null;
    String unitid;
    Geocoder geocode;
    String foname, photo, date_time;
    ImageView image;
    String user_id;
    Boolean isGPSEnabled,isNetworkEnabled;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 111;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 100;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);



            foname =CheckinActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_NAME, null);

            photo =CheckinActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_PHOTO, null);



        setUpGClient();
        user_id = CheckinActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_ID, null);

        image = (ImageView) findViewById(R.id.person_img);
        linn = (RelativeLayout) findViewById(R.id.checkin_rel);
        fo_name = (TextView) findViewById(R.id.fo_name);

        btn_continue = (Button) findViewById(R.id.btn_continue);
        unit = (TextView) findViewById(R.id.unit);
        unit1 = (TextView) findViewById(R.id.unit1);
        //dateTime = (TextView) findViewById(R.id.date_time);
        loc = (TextView) findViewById(R.id.loc);
        fo_name.setText(foname);
        if (!photo.isEmpty()) {
            Picasso.with(this).load(photo).into(image);
        }

        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mma");
        dateFormatter.setLenient(false);
        Date today = new Date();
        date_time = dateFormatter.format(today);
        //dateTime.setText(dateFormatter.format(today));


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateform();
                //Intent i = new Intent(CheckinActivity.this, MainActivity.class);
                //startActivity(i);
            }
        });


        unityName = UnitListAdapter.unityName;
        unitid = UnitListAdapter.unityid;
        SharedPreferences sharedPreferences = CheckinActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 2); // 0 - for private mode
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.N_UNIT_ID, unitid);
        editor.commit();

        unit.setText(unityName);
        unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(CheckinActivity.this, UnitList.class);
                startActivity(i);
            }
        });


        lom = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        // getting GPS status
        isGPSEnabled = lom.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = lom.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Criteria criteria = new Criteria();
        provider = lom.getBestProvider(criteria, false);
        if (!isGPSEnabled && !isNetworkEnabled) {
            // no GPS Provider and no network provider is enabled
        }
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            //Toast.makeText(CheckinActivity.this, "Allow to GPS", Toast.LENGTH_LONG).show();
        }
        // lom.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, CheckinActivity.this);







        Location location = lom.getLastKnownLocation(provider);

*/

        if (!isGPSEnabled && !isNetworkEnabled) {
            showsnackbar("no GPS Provider and no network provider is enabled");
            // no GPS Provider and no network provider is enabled
        } else {   // Either GPS provider or network provider is enabled

            // First get location from Network Provider
            if (isNetworkEnabled) {
                //lom.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                // MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (lom != null) {
                    Location location = lom.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        System.out.println("Provider " + provider + " has been selected.");
                        onLocationChanged(location);
                    } else {
                        loc.setText("Location not available");
                    }

                }
            }// End of IF network enabled



            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                //lom.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                //MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (lom != null) {
                    Location location = lom.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        System.out.println("Provider " + provider + " has been selected.");
                        onLocationChanged(location);
                    } else {
                        loc.setText("Location not available");
                    }

                }
            }


            // Initialize the location fields
        }

    }


    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, CheckinActivity.this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void validateform() {
        String unit_name = unit.getText().toString();
        String location = loc.getText().toString();


        if (unit_name.contentEquals("")) {
            showsnackbar("UnitName");
        } /*else if (!location.isEmpty()) {
            showsnackbar("Address Not found");
        }*/ else {
            ContinueCheckin();
        }
    }

    private void ContinueCheckin() {
        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            Continue_asyn checkin = new Continue_asyn();
            String unit_id = unitid;
            String location = loc.getText().toString();
            String dateTime = date_time.toString();
            checkin.execute(user_id, unit_id, location, dateTime, "1");
        } else {
            showsnackbar("No Internet");
        }


    }

    private void showsnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(linn, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();
    }

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(CheckinActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }


    private void getMyLocation() {

        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(CheckinActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    los = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    geocode = new Geocoder(this, Locale.getDefault());
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationCallback lol = null;
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(CheckinActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        los = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(CheckinActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(CheckinActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(Location location) {

            double lat = location.getLatitude();
            double lng = location.getLongitude();

            Geocoder geocode;
            List<Address> addresses;
            geocode = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocode.getFromLocation(lat, lng, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                loc.setText(address + ", " + city);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Location not found",e.toString());
            }
    }


    private class Continue_asyn extends AsyncTask<String, Void, Void> {

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
                progressDialog = ProgressDialog.show(CheckinActivity.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String user_id = params[0];
                String unityid = params[1];
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
                        .appendQueryParameter("user_id", user_id)
                        .appendQueryParameter("unit_id", unitid)
                        .appendQueryParameter("address", address)
                        .appendQueryParameter("checkin_time", datetime)
                        .appendQueryParameter("checkin_status", checkin_status);

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

                SharedPreferences sharedPreferences = CheckinActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.CHECKIN_ID, id);
                editor.commit();


                    Intent intent = new Intent(CheckinActivity.this, MainActivity.class);
                    intent.putExtra("checkinid",id);
                    startActivity(intent);

            }
            else {
                showsnackbar(server_message);
            }
        }

    }
}