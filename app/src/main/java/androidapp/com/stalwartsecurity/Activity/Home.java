package androidapp.com.stalwartsecurity.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.viewpagerindicator.CirclePageIndicator;

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
import java.util.Timer;
import java.util.TimerTask;

import androidapp.com.stalwartsecurity.Adapter.SlidingImage_Adapter;
import androidapp.com.stalwartsecurity.Adapter.VisitorsAdapter;
import androidapp.com.stalwartsecurity.Fragment.AddIncidents;
import androidapp.com.stalwartsecurity.Fragment.AttendanceIn;
import androidapp.com.stalwartsecurity.Fragment.HomeFragment;
import androidapp.com.stalwartsecurity.Fragment.Inward;
import androidapp.com.stalwartsecurity.Fragment.MadeEntry;
import androidapp.com.stalwartsecurity.Fragment.Outward;
import androidapp.com.stalwartsecurity.Fragment.StaffExit;
import androidapp.com.stalwartsecurity.Fragment.VisitorsEntry;
import androidapp.com.stalwartsecurity.Fragment.Visitorsexit;
import androidapp.com.stalwartsecurity.Fragment.VisitorsexitDW;
import androidapp.com.stalwartsecurity.Pojo.ImageModel;
import androidapp.com.stalwartsecurity.Pojo.OfflineVisitors;
import androidapp.com.stalwartsecurity.Pojo.User;
import androidapp.com.stalwartsecurity.Pojo.UserDetailsLoc;
import androidapp.com.stalwartsecurity.Pojo.Visitors;
import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;
import androidapp.com.stalwartsecurity.Util.DBHelper;

public class Home extends AppCompatActivity {
    private NavigationView navigationView;
    DBHelper db= new DBHelper(this);
    private DrawerLayout drawer;
    private View navHeader;
    private Handler mHandler;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private String[] activityTitles;
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";
    public static int navItemIndex = 0;
    public static int item_id;
    private static final String TAG_HOME = "home";
    private static final String TAG_VENTRY = "Visitors Entry";
    private static final String TAG_MENTRY = "Made Entry";
    private static final String TAG_VEXIT = "Visitors Exit";
    private static final String TAG_DVEXIT = "Visitors Datewise Exit";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String INWARD = "Inward";
    private static final String OUTWARD = "Outward";
    public static String CURRENT_TAG = TAG_HOME;
    private static long back_pressed;
    FrameLayout frame;
    String user_id,user_name,user_mobile;
    private int server_status;
    String server_message;
    public  static List<OfflineVisitors> offlineVList = new ArrayList<>();
    UserDetailsLoc userDetailsLoc=new UserDetailsLoc();
    User userd=new User();
    public static String Locationid;
    String curr_date;
    ProgressDialog progress;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mma");
        dateFormatter.setLenient(false);
        Date today = new Date();
         curr_date = dateFormatter.format(today);
        setContentView(R.layout.activity_home);
        frame = (FrameLayout) findViewById(R.id.frame);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        user_id =Home.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_ID, null);
        user_name =Home.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_NAME, null);
        user_mobile =Home.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_MOBILE, null);
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();
        refreshAllData();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

    }


    private void loadNavHeader() {
        txtName.setText(user_name+", "+user_mobile);

        if (CheckInternet.getNetworkConnectivityStatus(Home.this)) {
            // Toast.makeText(Signup.this, R.string.otp_sent, Toast.LENGTH_LONG).show();
            new getUserDetails().execute(user_id,curr_date);

        }else {
           Toast.makeText(Home.this,"No Internet",Toast.LENGTH_LONG).show();    }
        // name, website

    }
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();
        // set toolbar title
        setToolbarTitle();
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            // show or hide the fab button
            //toggleFab();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        //toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (item_id) {
            case R.id.nav_home:
                // home
                HomeFragment homeFragment = new HomeFragment();
                toolbar.setTitle("Home");
                return homeFragment;
            case R.id.add_visitors:
                // Entry for visitors
                VisitorsEntry visitorsEntry = new VisitorsEntry();
                toolbar.setTitle("Visitors Entry");
                return visitorsEntry;
            case R.id.exit_visitors:
                // Visitors Exit
                Visitorsexit visitorsexit = new Visitorsexit();
                toolbar.setTitle("Visitors Exit");
                return visitorsexit;
            case R.id.exitD_visitors:
                // Visitors Exit
                VisitorsexitDW visitorsexitdw = new VisitorsexitDW();
                toolbar.setTitle("Select dateExit");
                return visitorsexitdw;
            case R.id.entry_mades:
                // Made Entry
               MadeEntry madeEntry = new MadeEntry();
                toolbar.setTitle("Staff Entry");
                return madeEntry;
            // Made Exit
            case R.id.exit_mades:
               StaffExit staffExit = new StaffExit();
                toolbar.setTitle("Staff Exit");
                return staffExit;

            case R.id.inward:
                Inward inward = new Inward();
                toolbar.setTitle("Inwards");
                return inward;
              // Toast.makeText(Home.this,navItemIndex,Toast.LENGTH_LONG).show();
            case R.id.ourwards:
               Outward outward = new Outward();
                toolbar.setTitle("Outwards");
                return outward;
            case R.id.signout:

                logout();

            default:
                return new HomeFragment();
        }
    }

    private void logout() {
        SharedPreferences sharedPreferences = Home.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        Intent intent=new Intent(Home.this,Login_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle("Home");
    }

    private void selectNavMenu() {
        navigationView.getMenu().findItem(item_id).setChecked(true);

    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        //navItemIndex = 0;
                        item_id=R.id.nav_home;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.add_visitors:
                        //navItemIndex = 1;
                        item_id=R.id.add_visitors;

                        CURRENT_TAG = TAG_VENTRY;
                        break;
                   case R.id.exit_visitors:
                       // navItemIndex = 2;
                       item_id=R.id.exit_visitors;

                       CURRENT_TAG = TAG_VEXIT;
                        break;
                    case R.id.exitD_visitors:
                       // navItemIndex = 2;
                       item_id=R.id.exitD_visitors;

                       CURRENT_TAG = TAG_DVEXIT;
                        break;
                    case R.id.entry_mades:
                        //navItemIndex = 3;
                        item_id=R.id.entry_mades;

                        CURRENT_TAG = TAG_MENTRY;
                        break;
                    case R.id.exit_mades:
                        item_id=R.id.exit_mades;

                        //navItemIndex = 4;
                        CURRENT_TAG = "Exit Made";
                        break;
                    case R.id.inward:
                        item_id=R.id.inward;

                        //navItemIndex = 5;
                        CURRENT_TAG = INWARD;
                        break;
                    case R.id.ourwards:
                        item_id=R.id.ourwards;

                        //  navItemIndex = 6;
                        CURRENT_TAG = OUTWARD;
                       break;
                    case R.id.signout:
                        item_id=R.id.signout;

                        //  navItemIndex = 6;
                        CURRENT_TAG = "SignOut";
                        break;
                    default:

                        // navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(navigationView)) {
            drawer.closeDrawers();
            return;
        }
        else{
            if (back_pressed + 2000 > System.currentTimeMillis()) {
                //super.onBackPressed();
                   AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to exit from the application?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            }
            back_pressed = System.currentTimeMillis();

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);

        // show menu only when home fragment is selected
        /*if (navItemIndex == 0) {
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            if (CheckInternet.getNetworkConnectivityStatus(Home.this)) {
                // Toast.makeText(Signup.this, R.string.otp_sent, Toast.LENGTH_LONG).show();
                refreshAllData();
            }else {
                Snackbar snackbar = Snackbar
                        .make(frame, "No Internet", Snackbar.LENGTH_LONG);
                snackbar.show();        }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshAllData() {

        new VisiorsList().execute();

    }
    /*
   * GET VISIORS LIST ASYNTASK*/
    private class VisiorsList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.VISITORS;
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
                        .appendQueryParameter("location_id", Locationid);
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
                          "visiter": [
                                {
                                    "id": "15",
                                    "name": "amaresh",
                                    "email_id": "a@gmail.com",
                                    "address": "bangalore",
                                    "mobile": "2147483647",
                                    "visiter_type": null,
                                    "coming_from": "marathali",
                                    "vehicle_no": "7011",
                                    "appartment_name": "esha",
                                    "pass_no": "12345",
                                    "photo": "",
                                    "added_by": "avinash pathak",
                                    "in_time": "15-07-2017 12:00 AM",
                                    "out_time": "",
                                    "created": "2017-07-15 12:29:50",
                                    "modified": "2017-07-15 12:29:50"
                                },
                    },*/

                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    server_status=res.getInt("status");
                    if(server_status==1) {
                        JSONArray visitoslist = res.getJSONArray("visiter");
                        for (int i = 0; i < visitoslist.length(); i++) {
                            JSONObject o_list_obj = visitoslist.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String name = o_list_obj.getString("name");
                            String mobile = o_list_obj.getString("mobile");
                            for(int j=0;j< offlineVList.size();j++){
                                if(mobile.trim().contentEquals(offlineVList.get(j).getPhone().trim())){
                                    offlineVList.remove(j);
                                }
                            }
                            String visiter_type = o_list_obj.getString("visiter_type");
                            String coming_from = o_list_obj.getString("coming_from");
                            String vehicle_no = o_list_obj.getString("vehicle_no");
                            String appartment = o_list_obj.getString("appartment_name");
                            String pass_no = o_list_obj.getString("pass_no");
                            String photo = o_list_obj.getString("photo");
                            String in_time = o_list_obj.getString("in_time");
                            String out_time = o_list_obj.getString("out_time");
                  //          db.addVOffline(new OfflineVisitors(id,name,mobile,coming_from));
                            OfflineVisitors list1 = new OfflineVisitors(id,name,mobile,coming_from);
                            offlineVList.add(list1);
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

            }
            else{
                Snackbar snackbar = Snackbar
                        .make(frame, server_message, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
    //        progress.dismiss();

        }
    }

    /*
 * GET USERDETAILS ASYNTASK*/
    private class getUserDetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Home.this, "Please Wait",
                    "Loading Visiorslist...", true);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                String _user_id = params[0];
                String _date = params[1];
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
                        .appendQueryParameter("user_id", _user_id)
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
                        for (int i = 0; i < Allotment.length(); i++) {
                            JSONObject o_list_obj = Allotment.getJSONObject(i);
                            String location_id = o_list_obj.getString("location_id");
                            String location_name = o_list_obj.getString("location_name");
                            Locationid=location_id;
                            userDetailsLoc.setLocation_id(location_id);
                            userDetailsLoc.setLocation_name(location_name);
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
                txtWebsite.setText(userDetailsLoc.getLocation_name());
            }
            else{
                Snackbar snackbar = Snackbar
                        .make(frame, server_message, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            progress.dismiss();

        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

}
