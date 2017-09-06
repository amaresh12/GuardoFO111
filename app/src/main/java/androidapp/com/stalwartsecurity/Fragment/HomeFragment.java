package androidapp.com.stalwartsecurity.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidapp.com.stalwartsecurity.Activity.Home;
import androidapp.com.stalwartsecurity.Adapter.NoticeAdapter;
import androidapp.com.stalwartsecurity.Adapter.SlidingImage_Adapter;
import androidapp.com.stalwartsecurity.Adapter.VisitorsAdapter;
import androidapp.com.stalwartsecurity.Pojo.ImageModel;
import androidapp.com.stalwartsecurity.Pojo.PrimaryNotice;
import androidapp.com.stalwartsecurity.Pojo.Visitors;
import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
     ArrayList<ImageModel> imageModelArrayList;
    ArrayList<PrimaryNotice> primaryNoticeList;
    ViewPager viewPager;
    CirclePageIndicator indicator;
    RelativeLayout home_rel;
    TextView no_primaryNotice;
    ListView lv_primaryNotice;
    String user_id;
    NoticeAdapter noticeAdapter;
    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        user_id = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_ID, null);

        home_rel=(RelativeLayout) view.findViewById(R.id.home_rel);
        no_primaryNotice=(TextView) view.findViewById(R.id.no_notice);
        no_primaryNotice.setVisibility(View.GONE);
        lv_primaryNotice=(ListView)view.findViewById(R.id.primarynoticeList);
        getAllPrimaryNotice();
        getAllBanners();
       /* imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();*/
         viewPager = (ViewPager)view.findViewById(R.id.pager);
         indicator = (CirclePageIndicator)
                view.findViewById(R.id.indicator);

        return view;



    }


    private void getAllPrimaryNotice() {
        if (CheckInternet.getNetworkConnectivityStatus(getContext())) {
            // Toast.makeText(Signup.this, R.string.otp_sent, Toast.LENGTH_LONG).show();
            new primaryMessage().execute(user_id);

        }else {
            Snackbar snackbar = Snackbar
                    .make(home_rel, "No Internet", Snackbar.LENGTH_LONG);
            snackbar.show();        }

    }

    private void getAllBanners() {
        if (CheckInternet.getNetworkConnectivityStatus(getContext())) {
            // Toast.makeText(Signup.this, R.string.otp_sent, Toast.LENGTH_LONG).show();
            new GetAllimages().execute();

        }else {
            Snackbar snackbar = Snackbar
                    .make(home_rel, "No Internet", Snackbar.LENGTH_LONG);
            snackbar.show();        }


    }

    private void init() {
        viewPager.setAdapter(new SlidingImage_Adapter(this.getActivity(),imageModelArrayList));
        indicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
/*    private ArrayList<ImageModel> populateList(){

     *//*   ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 6; i++){
            ImageModel imageModel = new ImageModel();
            // here adding the value to models

            imageModel.setImage_drawable(myImageList[i]);
            imageModel.setTitle(titleList[i]);
            list.add(imageModel);
        }*//*

        return list;
    }*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    /*
* GET BANNER LIST*/
    private class GetAllimages extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";

        @Override
        protected void onPreExecute() {


        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.BANNERS;
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
                        .appendQueryParameter("added_by", "");

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
                          {
    "Banner": [
        {
            "title": "first banner",
            "photo": "http://applicationworld.net/stallwart/files/photo/photo1500062092.jpeg"
        },
        {
            "title": "decond banner",
            "photo": "http://applicationworld.net/stallwart/files/photo/photo1500062116.jpg"
        }
    ]
}
                                },
                    },*/

                if (response != null && response.length() > 0) {
                    imageModelArrayList=new ArrayList<>();
                    JSONObject res = new JSONObject(response);
                    JSONArray bannerList = res.getJSONArray("Banner");
                        for (int i = 0; i < bannerList.length(); i++) {
                            JSONObject o_list_obj = bannerList.getJSONObject(i);
                            String title = o_list_obj.getString("title");
                            String banner = o_list_obj.getString("photo");
                            ImageModel list1 = new ImageModel(title,banner);
                            imageModelArrayList.add(list1);
                        }
                }



                return null;


            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            if(getActivity()!=null) {

                init();
            }

        }
    }
    /*
* GET NOTICE LIST*/
    private class primaryMessage extends AsyncTask<String, Void, Void> {

        private static final String TAG = "primary details";
        ProgressDialog progress;
        int serverstatus;

        @Override
        protected void onPreExecute() {
         /*   progress = ProgressDialog.show(getContext(), "Please Wait",
                    "Loading ...", true);
*/
        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                String _userid=params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.PRIMARYNOTICE;
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
                        .appendQueryParameter("to_user_id",_userid);

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
                          {{
   "PrimayNotification": [
       {
           "id": "5",
           "message": "Test for notification for singleuser",
           "to_user_id": ",9",
           "is_enable": "1",
           "created": "2017-07-16 19:54:53",
           "modified": "2017-07-16 19:56:28"
       },
       {
           "id": "4",
           "message": "Test for prinmary notification",
           "to_user_id": "",
           "is_enable": "1",
           "created": "2017-07-16 19:54:15",
           "modified": "2017-07-16 19:54:15"
       }
   ],
   "status": 1,
   "message": "Successfully"
}
                },
                    },*/
                if (response != null && response.length() > 0) {
                    primaryNoticeList=new ArrayList<>();
                    JSONObject res = new JSONObject(response);
                     serverstatus=res.getInt("status");
                    if(serverstatus==1) {
                        JSONArray noticeList = res.getJSONArray("PrimayNotification");
                        for (int i = 0; i < noticeList.length(); i++) {
                            JSONObject o_list_obj = noticeList.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String message = o_list_obj.getString("message");
                            String date = o_list_obj.getString("created");

                            primaryNoticeList.add(new PrimaryNotice(id,message,date));
                        }
                    }
                }
                return null;


            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
           // progress.dismiss();
            if(serverstatus==1){
                noticeAdapter=new NoticeAdapter(getContext(),primaryNoticeList);
                lv_primaryNotice.setAdapter(noticeAdapter);
            }
            else{
                lv_primaryNotice.setVisibility(View.GONE);
                no_primaryNotice.setVisibility(View.VISIBLE);
            }
        }
    }


}
