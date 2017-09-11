package androidapp.com.stalwartsecurity.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import androidapp.com.stalwartsecurity.Adapter.UnitListAdapter;
import androidapp.com.stalwartsecurity.Pojo.UnityDetailsLocation;
import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;

/**
 * Created by mobileapplication on 8/14/17.
 */

public class UnitList extends AppCompatActivity {



    RelativeLayout relative_snackbar;
    ListView unit_list;
    UnitListAdapter adapter;
    String[] array={"DSSD","dssd"};
    int server_status;
    String server_message;
    UnityDetailsLocation unityDetailsLoc;
    ArrayList<UnityDetailsLocation> unityDetailsLoc1;
    public static String Locationid;
    String data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        relative_snackbar=(RelativeLayout)findViewById(R.id.relative);
        unit_list=(ListView)findViewById(R.id.unit_list);

        Intent intent=getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
             data = extras.getString("pagename");
        }

        Unitylist();

        unit_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ss=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(UnitList.this,ss, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void Unitylist() {
        if (CheckInternet.getNetworkConnectivityStatus(UnitList.this)) {
            GetUnityDetails get_details=new GetUnityDetails();
            get_details.execute();

        }else {
            Snackbar snackbar = Snackbar
                    .make(relative_snackbar, "No Internet", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }



    private class GetUnityDetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(UnitList.this, "Please Wait",
                    "Loading UnityList...", true);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {

                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.LOCATION_DETAILS;
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
                        .appendQueryParameter("","");
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
    "Allotment": [
        {
            "location_id": "5",
            "location_name": "Salarpuria sattva"
        }
    ],
    "status": 1,
    "message": "Successfully"
}
                    },*/

                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    server_status=res.getInt("status");
                    if(server_status==1) {
                        JSONArray Allotment = res.getJSONArray("Allotment");
                        unityDetailsLoc1=new ArrayList<>();
                        for (int i = 0; i < Allotment.length(); i++) {
                            JSONObject o_list_obj = Allotment.getJSONObject(i);
                            String location_id = o_list_obj.getString("location_id");
                            String location_name = o_list_obj.getString("location_name");
                            Locationid=location_id;
                            for(int j=0;j<unityDetailsLoc1.size();j++){
                                if(unityDetailsLoc1.get(j).getLocation_id().contentEquals(location_id)){
                                    unityDetailsLoc1.remove(j);
                                }

                            }
                            unityDetailsLoc1.add(new UnityDetailsLocation(location_id,location_name));


                        }
                    }
                    else{
                        server_message="Error in data Load";
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
            if(server_status==1) {
                if(data.contentEquals("LogIncident")) {
                    adapter = new UnitListAdapter(UnitList.this, unityDetailsLoc1,data);
                    unit_list.setAdapter(adapter);
                }
                else{
                    adapter = new UnitListAdapter(UnitList.this, unityDetailsLoc1,data);
                    unit_list.setAdapter(adapter);
                }
            }
            else{
                Snackbar snackbar = Snackbar
                        .make(relative_snackbar, server_message, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            progress.dismiss();

        }
    }
}
