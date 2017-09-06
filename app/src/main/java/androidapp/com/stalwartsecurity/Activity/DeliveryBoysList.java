package androidapp.com.stalwartsecurity.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

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

import androidapp.com.stalwartsecurity.Adapter.DeliveryBoysAdapter;
import androidapp.com.stalwartsecurity.Adapter.FlatAdapter;
import androidapp.com.stalwartsecurity.Pojo.DeliveryBoys;
import androidapp.com.stalwartsecurity.Pojo.Flats;
import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;

public class DeliveryBoysList extends AppCompatActivity {
    ListView lv_deliveryBoys;
    TextView tv_nodeliveryboys;
    SearchView searchViewdelivey;
    RelativeLayout rel_delivery;
    int server_status;
    String server_message;
    ArrayList<DeliveryBoys>DBList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boys_list);
        DBList=new ArrayList<>();
        lv_deliveryBoys=(ListView)findViewById(R.id.deliveryBoysList);
        searchViewdelivey=(SearchView)findViewById(R.id.searchViewdelivey);
        tv_nodeliveryboys=(TextView)findViewById(R.id.dbblank_text);
        rel_delivery=(RelativeLayout)findViewById(R.id.reldeliveryboys);
        if (CheckInternet.getNetworkConnectivityStatus(DeliveryBoysList.this)) {
            // Toast.makeText(Signup.this, R.string.otp_sent, Toast.LENGTH_LONG).show();
            new getDeliveryBList().execute();

        }else {
            Snackbar snackbar = Snackbar
                    .make(rel_delivery, "No Internet", Snackbar.LENGTH_LONG);
            snackbar.show();        }
    }
    /*
* GET FLAT LIST ASYNTASK*/
    private class getDeliveryBList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(DeliveryBoysList.this, "Please Wait",
                    "Loading List...", true);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.DELIVERYBOYS;
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
                        .appendQueryParameter("user_id", "");
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
    "DeliveryType": [
        {
            "id": "10",
            "title": "Amazon",
            "is_enable": "1",
            "created": null,
            "modified": "2017-08-17 05:20:56"
        },
        {
            "id": "12",
            "title": "BlueDart",
            "is_enable": "1",
            "created": "2017-08-17 05:21:29",
            "modified": "2017-08-17 05:21:29"
        },
                    },*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response);
                    server_status=res.getInt("status");
                    if(server_status==1) {
                        JSONArray DBListArray = res.getJSONArray("DeliveryType");
                        for (int i = 0; i < DBListArray.length(); i++) {
                            JSONObject o_list_obj = DBListArray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String title = o_list_obj.getString("title");
                            String is_enable = o_list_obj.getString("is_enable");
                            DeliveryBoys list1 = new DeliveryBoys(id,title,is_enable);
                            DBList.add(list1);
                        }
                    }
                    else{
                        server_message="No List found";
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
                DeliveryBoysAdapter DBadapter = new DeliveryBoysAdapter(DeliveryBoysList.this,DBList );
                lv_deliveryBoys.setAdapter(DBadapter);
                progress.dismiss();
            }
            else{
                lv_deliveryBoys.setVisibility(View.GONE);
                tv_nodeliveryboys.setVisibility(View.VISIBLE);
                Snackbar snackbar = Snackbar
                        .make(rel_delivery, server_message, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            progress.dismiss();

        }
    }
}
