package androidapp.com.stalwartsecurity.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

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

import androidapp.com.stalwartsecurity.Pojo.Visitors;
import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.CheckInternet;
import androidapp.com.stalwartsecurity.Util.Constants;

import static android.view.View.GONE;

/**
 * Created by User on 16-07-2017.
 */

public class VisitorsAdapter extends BaseAdapter {
    Context _context;
    ArrayList<Visitors> v_list;
    Holder holder;
     Holder holder1;
    Holder vHolder;
    int linpos;
    String curr_date,user_id;
    public VisitorsAdapter(Context context, ArrayList<Visitors> list_visitors) {
        this._context=context;
        this.v_list=list_visitors;
    }

    @Override
    public int getCount() {
        return v_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder{
        TextView tv_name,tv_cmngfrom,tv_entrytime,tv_exittime,tv_exited,tv_flat,tv_exitedBy;
        ImageView iv_exit,iv_verify_visit,overstay,visitorsPic;
        public Holder() {
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Visitors _pos=v_list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.allvisitors, parent, false);
            user_id = _context.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_ID, null);
            holder.tv_name=(TextView)convertView.findViewById(R.id.visitors_name);
            holder.tv_cmngfrom=(TextView)convertView.findViewById(R.id.tv_address);
            holder.tv_entrytime=(TextView)convertView.findViewById(R.id.ventry_time);
            holder.tv_exittime=(TextView)convertView.findViewById(R.id.ventry_exit);
            holder.tv_exitedBy=(TextView)convertView.findViewById(R.id.tv_exitedBy);
            holder.iv_exit=(ImageView) convertView.findViewById(R.id.tv_exitimage);
            holder.tv_flat=(TextView) convertView.findViewById(R.id.tv_flat);
            holder.tv_exited=(TextView) convertView.findViewById(R.id.exited);
            holder.overstay=(ImageView) convertView.findViewById(R.id.overstay);
            holder.iv_verify_visit=(ImageView) convertView.findViewById(R.id.verify_visit);
            holder.visitorsPic=(ImageView) convertView.findViewById(R.id.visitorsPic);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.tv_name.setTag(position);
        holder.tv_cmngfrom.setTag(position);
        holder.tv_entrytime.setTag(position);
        holder.tv_exittime.setTag(position);
        holder.tv_exited.setTag(position);
        holder.tv_exitedBy.setTag(position);
        holder.overstay.setTag(position);
        holder.iv_exit.setTag(holder);
        holder.tv_flat.setTag(holder);
        holder.visitorsPic.setTag(holder);
        holder.iv_verify_visit.setTag(holder);

        holder.tv_name.setText(_pos.getName()+"  ("+_pos.getMobile()+")");
        String e_time=_pos.getIn_time().substring(_pos.getIn_time().length() - 8);
        holder.tv_entrytime.setText("In time: "+e_time);
        String outime=_pos.getOut_time();
        holder.tv_flat.setText("Flat No.: "+_pos.getAppartment());
        if(!_pos.getPhoto().isEmpty()) {
            Picasso.with(_context).load(_pos.getPhoto()).into(holder.visitorsPic);
        }
        else {
            holder.visitorsPic.setImageResource(R.drawable.no_image);
        }

        if(_pos.getVerification().contentEquals("Not Verified")){

        }
        else {
            Resources res = _context.getResources();
            Drawable drawable = res.getDrawable(R.drawable.ic_verified_user_black_24dp);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, _context.getResources().getColor(R.color.lightgreen));
            holder.iv_verify_visit.setImageDrawable(drawable);
        }
        if(_pos.getOverstay().toString().contentEquals("1")){
            Resources res1 = _context.getResources();
            Drawable drawable1 = res1.getDrawable(R.drawable.ic_warning_black_24dp);
            drawable1 = DrawableCompat.wrap(drawable1);
            DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.red));
            holder.overstay.setImageDrawable(drawable1);
        }

       // holder.tv_exittime.setText(outime);
        if(outime.contentEquals("")){
            holder.iv_exit.setVisibility(View.VISIBLE);
        }
        else {
            holder.tv_exited.setVisibility(View.VISIBLE);
            holder.tv_exitedBy.setVisibility(View.VISIBLE);
            holder.tv_exitedBy.setText("Exited By: "+_pos.getExit_by());
            holder.iv_exit.setVisibility(GONE);
            String o_time=_pos.getOut_time().substring(_pos.getOut_time().length() - 8);
            holder.tv_exittime.setText("Out time: "+o_time);
        }
        holder.tv_cmngfrom.setText("Coming From :"+_pos.getComing_from());
        holder.iv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 holder1 = (Holder) v.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                builder.setMessage("Do you want to exit the visior?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mma");
                                dateFormatter.setLenient(false);
                                Date today = new Date();
                                curr_date = dateFormatter.format(today);
                               exitVisitors(_pos.getId(),curr_date);
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
        });
        holder.iv_verify_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vHolder=(Holder)v.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                builder.setMessage("Do you want to verify the visior?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                              /*  DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mma");
                                dateFormatter.setLenient(false);
                                Date today = new Date();
                                curr_date = dateFormatter.format(today);*/
                              String verification_status="Verified";
                                verifyVisitors(_pos.getId(),verification_status);
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
        });
        return convertView;
    }

    private void verifyVisitors(String id, String verification_status) {
        if (CheckInternet.getNetworkConnectivityStatus(_context)) {
            new verifyVisitor().execute(id,verification_status);
        }
        else{
            Toast.makeText(_context,"No internet",Toast.LENGTH_SHORT);
        }
    }

    private void exitVisitors(String id,String cdate) {
        if (CheckInternet.getNetworkConnectivityStatus(_context)) {
            new ExitVisitor().execute(id,cdate);
        }
        else{
            Toast.makeText(_context,"No internet",Toast.LENGTH_SHORT);
        }
    }

    /*
* EXIT VISITORS*/
    private class ExitVisitor extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;
        int server_status;
        String server_response;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(_context, "Please Wait",
                    "Loading Exit...", true);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                String _user_id = params[0];
                String _outime = params[1];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.ADDVISITORS;
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
                        .appendQueryParameter("id", _user_id)
                        .appendQueryParameter("exit_by", user_id)
                        .appendQueryParameter("out_time",_outime);

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
    "status": 1,
    "message": "Successfully inserted"
}
                    },*/

                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    server_status=res.getInt("status");
                    if(server_status==1) {
                        server_response="Exit Successful";
                    }
                    else{
                        server_response="Error in exit";
                    }

                }

                return null;


            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
                server_response="Error in Exit";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progress.dismiss();
            if(server_status==1){
                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                        _context).create();
                alertDialog.setTitle("Successful");
                alertDialog.setMessage("Exit Successful");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                    }
                });
                alertDialog.show();
                holder1.tv_exited.setVisibility(View.VISIBLE);
                holder1.iv_exit.setVisibility(View.GONE);


            }

        }
    }
    /*
* VERIFY VISITORS*/
    private class verifyVisitor extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;
        int server_status;
        String server_response;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(_context, "Please Wait",
                    "Verifying...", true);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                String _listid = params[0];
                String _verify = params[1];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.ADDVISITORS;
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
                        .appendQueryParameter("id", _listid)
                        .appendQueryParameter("verification_status",_verify);

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
    "status": 1,
    "message": "Successfully inserted"
}
                    },*/

                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    server_status=res.getInt("status");
                    if(server_status==1) {
                        server_response="Exit Successful";
                    }
                    else{
                        server_response="Error in exit";
                    }

                }

                return null;


            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
                server_response="Error in Exit";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progress.dismiss();
            if(server_status==1){
                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                        _context).create();
                alertDialog.setTitle("Successful");
                alertDialog.setMessage("Verfied");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();

                Resources res = _context.getResources();
                Drawable drawable = res.getDrawable(R.drawable.ic_verified_user_black_24dp);
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(drawable, _context.getResources().getColor(R.color.lightgreen));
                vHolder.iv_verify_visit.setImageDrawable(drawable);

            }

   /*         if(server_status==1) {
                mAdapter = new VisitorsAdapter(getContext(), list_visitors);
                visitorsList.setAdapter(mAdapter);
                progress.dismiss();
            }
            else{
                visitorsList.setVisibility(View.GONE);
                et_no_visitors.setVisibility(View.VISIBLE);
            }*/

        }
    }
}
