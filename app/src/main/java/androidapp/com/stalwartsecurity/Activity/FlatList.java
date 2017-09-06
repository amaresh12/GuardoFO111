package androidapp.com.stalwartsecurity.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidapp.com.stalwartsecurity.Adapter.FlatAdapter;
import androidapp.com.stalwartsecurity.Pojo.Flats;
import androidapp.com.stalwartsecurity.Pojo.OfflineVisitors;
import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;

public class FlatList extends AppCompatActivity {
    String user_id;
    String curdate;
    RelativeLayout flatListRel;
    int server_status;
    String server_message;
    public static ArrayList<Flats> flatList;
    ListView list;
    FlatAdapter flatadapter;
    TextView blank_text;
    Button bt_ok,bt_cancel;
    SearchView searchView1;
    public static EditText flatName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_list);
        flatList=new ArrayList<>();
        flatListRel=(RelativeLayout)findViewById(R.id.flatListRel);
        list=(ListView)findViewById(R.id.flatList);
        blank_text=(TextView) findViewById(R.id.blank_text);
        bt_ok=(Button)findViewById(R.id.bt_ok);
        bt_cancel=(Button)findViewById(R.id.bt_cancel);
        searchView1=(SearchView)findViewById(R.id.searchView1);
        searchView1.setQueryHint("Search Flat Number");
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlatList.this.finish();
            }
        });

        user_id = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_ID, null);
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mma");
        dateFormatter.setLenient(false);
        Date today = new Date();
        curdate = dateFormatter.format(today);
        getFlats();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox chk = (CheckBox) view.findViewById(R.id.checkbox);
                Flats bean = flatList.get(position);
                if (bean.isSelected()) {
                    bean.setSelected(false);
                    chk.setChecked(false);
                } else {
                    bean.setSelected(true);
                    chk.setChecked(true);
                }


            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer sb = new StringBuffer();

                    for (Flats bean : flatList) {
                        if (bean.isSelected()) {
                            if (sb.toString().trim().contains(bean.getFlat())) {
                            } else {
                                sb.append(bean.getFlat());
                                sb.append(",");
                            }
                        }
                        if (sb.length() <= 0) {
                            flatName.setText("");
                            FlatList.this.finish();
                        } else {
                            flatName.setText(sb.toString().trim().substring(0, sb.length() - 1));
                            FlatList.this.finish();
                        }

                    }
            }
        });
        searchView1.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        //*** setOnQueryTextListener ***
        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setQuestionList(newText);
                return false;
            }
        });

    }
    private void setQuestionList(String filterText) {

        final ArrayList<Flats> flatlist_search = new ArrayList<>();
        if (filterText != null && filterText.trim().length() > 0) {
            for (int i = 0; i < flatList .size(); i++) {
                String q_title = flatList.get(i).getFlat();
                if (q_title != null && filterText != null &&
                        q_title.toLowerCase().contains(filterText.toLowerCase())) {
                    flatlist_search.add(flatList.get(i));
                }
            }
        }else{
            flatlist_search.addAll(flatList);
        }
            // create an Object for Adapter
            flatadapter = new FlatAdapter(FlatList.this, flatlist_search);
            list.setAdapter(flatadapter);
            //  mAdapter.notifyDataSetChanged();


        if (flatList.isEmpty()) {
            list.setVisibility(View.GONE);
            blank_text.setVisibility(View.VISIBLE);
        } else {
            list.setVisibility(View.VISIBLE);
            blank_text.setVisibility(View.GONE);
        }

        flatadapter.notifyDataSetChanged();
    }


    private void getFlats() {
        if (CheckInternet.getNetworkConnectivityStatus(FlatList.this)) {
            // Toast.makeText(Signup.this, R.string.otp_sent, Toast.LENGTH_LONG).show();
            new getFlatList().execute(user_id,curdate);

        }else {
            Snackbar snackbar = Snackbar
                    .make(flatListRel, "No Internet", Snackbar.LENGTH_LONG);
            snackbar.show();        }
    }
    /*
* GET FLAT LIST ASYNTASK*/
    private class getFlatList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(FlatList.this, "Please Wait",
                    "Loading Flats...", true);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                String _userid=params[0];
                String _date=params[1];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.FLAT_DETAILS_LIST;
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
                        .appendQueryParameter("user_id", _userid)
                        .appendQueryParameter("date", _date);
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
            "plot_no": "E01",
            "mobile": "7205674061",
            "person_name": "Avinash Pathak",
            "email": "avinash@gmail.com",
            "location_name": "Salarpuria sattva"
        },
        {
            "plot_no": "A11",
            "mobile": "1234567890",
            "person_name": "Alok",
            "email": "a@gmail.com",
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
                        JSONArray flatListArray = res.getJSONArray("Allotment");
                        for (int i = 0; i < flatListArray.length(); i++) {
                            JSONObject o_list_obj = flatListArray.getJSONObject(i);
                            String flat = o_list_obj.getString("plot_no");
                            String mobile = o_list_obj.getString("mobile");
                            String person_name = o_list_obj.getString("person_name");
                            String loc_name = o_list_obj.getString("location_name");
                            Flats list1 = new Flats(flat,mobile,person_name,loc_name);
                            flatList.add(list1);
                        }
                    }
                    else{
                        server_message="No flat found";
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
                flatadapter = new FlatAdapter(FlatList.this,flatList );
                list.setAdapter(flatadapter);
                progress.dismiss();
            }
            else{
                list.setVisibility(View.GONE);
                blank_text.setVisibility(View.VISIBLE);
                Snackbar snackbar = Snackbar
                        .make(flatListRel, server_message, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            progress.dismiss();

        }
    }
}
