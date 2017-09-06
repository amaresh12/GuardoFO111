package androidapp.com.stalwartsecurity.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidapp.com.stalwartsecurity.Pojo.User;
import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.Constants;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by mobileapplication on 9/4/17.
 */

public class FOMainActivity extends AppCompatActivity {
    RelativeLayout fo_chkin,fo_incident,fo_tickt,fo_attendance;
    CircleImageView fo_img;
    TextView fo_name,fo_number;
    User user;
    Bitmap img_bitmap;
    String photo,foname,mobno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fo_activity_main);



        foname =FOMainActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_NAME, null);

        photo =FOMainActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_PHOTO, null);
        mobno =FOMainActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_MOBILE, null);

        fo_img=(CircleImageView) findViewById(R.id.img);
        fo_name=(TextView)findViewById(R.id.fo_name);
        fo_number=(TextView)findViewById(R.id.fo_number);
        fo_chkin=(RelativeLayout)findViewById(R.id.fo_checkin);
        fo_incident=(RelativeLayout)findViewById(R.id.fo_incident);
        fo_tickt=(RelativeLayout)findViewById(R.id.fo_ticket);
        fo_attendance=(RelativeLayout)findViewById(R.id.fo_attendance);

        fo_chkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(FOMainActivity.this,CheckinActivity.class);
                i.putExtra("name",foname);
                i.putExtra("photo",photo);
                startActivity(i);
            }
        });

        fo_incident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(FOMainActivity.this,IncidentActivity.class);
                startActivity(i);
            }
        });

        fo_tickt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(FOMainActivity.this,TicketActivity.class);
                startActivity(i);
            }
        });

        fo_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(FOMainActivity.this,AttendanceActivity.class);
                startActivity(i);
            }
        });



        fo_name.setText(foname);
        fo_number.setText(mobno);

        if (!photo.isEmpty()) {
            Picasso.with(this).load(photo).into(fo_img);
        }


    }
}
