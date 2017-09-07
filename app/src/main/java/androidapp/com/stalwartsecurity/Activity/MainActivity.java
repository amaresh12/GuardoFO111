package androidapp.com.stalwartsecurity.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidapp.com.stalwartsecurity.R;
import androidapp.com.stalwartsecurity.Util.Constants;

public class MainActivity extends AppCompatActivity {

    TextView dateTime;
    private ProgressBar progressBar1;
    int progressStatus = 0;
    Handler handler = new Handler();
    LinearLayout unit_visit,incidents,tickets;
    private Boolean exit = false;
    ImageView unitimg,meetingimg,nightchk_img,trng_img;
    RelativeLayout unit,meeting,night_chk_rel,trng_rel;
    TextView txt_time,user_welcm_name;
    String foname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        foname =MainActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_NAME, null);


        progressBar1=(ProgressBar)findViewById(R.id.progressBar1);
        unitimg=(ImageView)findViewById(R.id.unit_img);
        meetingimg=(ImageView)findViewById(R.id.metng_img);
        nightchk_img=(ImageView)findViewById(R.id.night_img);
        trng_img=(ImageView)findViewById(R.id.trng_img);
        user_welcm_name=(TextView) findViewById(R.id.user_welcm_name);

        unit=(RelativeLayout)findViewById(R.id.unit);
        meeting=(RelativeLayout)findViewById(R.id.meeting);
        night_chk_rel=(RelativeLayout)findViewById(R.id.night_chk);
        trng_rel=(RelativeLayout)findViewById(R.id.traing);
        txt_time=(TextView)findViewById(R.id.txt_time);



        user_welcm_name.setText("WELCOME"+","+" "+foname);
        DateFormat dateFormatter = new SimpleDateFormat("hh:mma");
        dateFormatter.setLenient(false);
        Date today = new Date();
        txt_time.setText(dateFormatter.format(today));

        unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callunit();
            }
        });

        trng_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calltraining();
            }
        });

        night_chk_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNightcheck();
            }
        });

        meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callMeeting();
            }
        });


    }

    private void callunit() {
        unit.setBackgroundResource(R.drawable.circle_light_purple_with_white_stroke);
        Resources res = this.getResources();
        Drawable drawable = res.getDrawable(R.mipmap.ic_account_balance_black_24dp);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, this.getResources().getColor(R.color.white));
        unitimg.setImageDrawable(drawable);

        trng_rel.setBackgroundResource(R.drawable.circle_light_onlywhite_with_white_stroke);
        Resources res1 = this.getResources();
        Drawable drawable1 = res1.getDrawable(R.mipmap.ic_security_black_24dp);
        drawable1 = DrawableCompat.wrap(drawable1);
        DrawableCompat.setTint(drawable1, this.getResources().getColor(R.color.black));
        trng_img.setImageDrawable(drawable1);

        meeting.setBackgroundResource(R.drawable.circle_light_onlywhite_with_white_stroke);
        Resources res2 = this.getResources();
        Drawable drawable2 = res2.getDrawable(R.mipmap.ic_people_black_24dp);
        drawable2 = DrawableCompat.wrap(drawable2);
        DrawableCompat.setTint(drawable2, this.getResources().getColor(R.color.black));
        meetingimg.setImageDrawable(drawable2);

        night_chk_rel.setBackgroundResource(R.drawable.circle_light_onlywhite_with_white_stroke);
        Resources res3 = this.getResources();
        Drawable drawable3 = res3.getDrawable(R.mipmap.ic_brightness_2_black_24dp);
        drawable1 = DrawableCompat.wrap(drawable3);
        DrawableCompat.setTint(drawable3, this.getResources().getColor(R.color.black));
        nightchk_img.setImageDrawable(drawable3);
        proceed();
    }

    private void proceed() {
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar1.setProgress(progressStatus);
                        }
                    });
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (progressStatus == 100) {
                        //Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        Intent i = new Intent(MainActivity.this, CheckinActivity1.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                    }
            }
        }).start();
    }

    private void callMeeting() {

        meeting.setBackgroundResource(R.drawable.circle_light_orange_with_white_stroke);
        Resources res = this.getResources();
        Drawable drawable = res.getDrawable(R.mipmap.ic_people_black_24dp);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, this.getResources().getColor(R.color.white));
        meetingimg.setImageDrawable(drawable);

        trng_rel.setBackgroundResource(R.drawable.circle_light_onlywhite_with_white_stroke);
        Resources res1 = this.getResources();
        Drawable drawable1 = res1.getDrawable(R.mipmap.ic_security_black_24dp);
        drawable1 = DrawableCompat.wrap(drawable1);
        DrawableCompat.setTint(drawable1, this.getResources().getColor(R.color.black));
        trng_img.setImageDrawable(drawable1);

        night_chk_rel.setBackgroundResource(R.drawable.circle_light_onlywhite_with_white_stroke);
        Resources res2 = this.getResources();
        Drawable drawable2 = res2.getDrawable(R.mipmap.ic_brightness_2_black_24dp);
        drawable2 = DrawableCompat.wrap(drawable2);
        DrawableCompat.setTint(drawable2, this.getResources().getColor(R.color.black));
        nightchk_img.setImageDrawable(drawable2);

        unit.setBackgroundResource(R.drawable.circle_light_onlywhite_with_white_stroke);
        Resources res3 = this.getResources();
        Drawable drawable3 = res3.getDrawable(R.mipmap.ic_account_balance_black_24dp);
        drawable1 = DrawableCompat.wrap(drawable3);
        DrawableCompat.setTint(drawable3, this.getResources().getColor(R.color.black));
        unitimg.setImageDrawable(drawable3);

        proceedMeeting();
    }

    private void proceedMeeting() {

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar1.setProgress(progressStatus);
                        }
                    });
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (progressStatus == 100) {
                    Intent i = new Intent(MainActivity.this, MeetingActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
            }
        }).start();
    }


    private void callNightcheck() {


        night_chk_rel.setBackgroundResource(R.drawable.circle_light_green_with_white_stroke);
        Resources res = this.getResources();
        Drawable drawable = res.getDrawable(R.mipmap.ic_brightness_2_black_24dp);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, this.getResources().getColor(R.color.white));
        nightchk_img.setImageDrawable(drawable);

        trng_rel.setBackgroundResource(R.drawable.circle_light_onlywhite_with_white_stroke);
        Resources res_trn = this.getResources();
        Drawable drawable1 = res_trn.getDrawable(R.mipmap.ic_security_black_24dp);
        drawable1 = DrawableCompat.wrap(drawable1);
        DrawableCompat.setTint(drawable1, this.getResources().getColor(R.color.black));
        trng_img.setImageDrawable(drawable1);

        meeting.setBackgroundResource(R.drawable.circle_light_onlywhite_with_white_stroke);
        Resources res2 = this.getResources();
        Drawable drawable2 = res2.getDrawable(R.mipmap.ic_people_black_24dp);
        drawable2 = DrawableCompat.wrap(drawable2);
        DrawableCompat.setTint(drawable2, this.getResources().getColor(R.color.black));
        meetingimg.setImageDrawable(drawable2);

        unit.setBackgroundResource(R.drawable.circle_light_onlywhite_with_white_stroke);
        Resources res3 = this.getResources();
        Drawable drawable3 = res3.getDrawable(R.mipmap.ic_account_balance_black_24dp);
        drawable1 = DrawableCompat.wrap(drawable3);
        DrawableCompat.setTint(drawable3, this.getResources().getColor(R.color.black));
        unitimg.setImageDrawable(drawable3);
        proceedNightCheck();


    }

    private void proceedNightCheck() {

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar1.setProgress(progressStatus);
                        }
                    });
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (progressStatus == 100) {
                    Intent i = new Intent(MainActivity.this, NightCheckActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
            }
        }).start();
    }





    private void calltraining() {
        unit.setBackgroundResource(R.drawable.circle_light_onlywhite_with_white_stroke);
        Resources res = this.getResources();
        Drawable drawable = res.getDrawable(R.mipmap.ic_account_balance_black_24dp);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, this.getResources().getColor(R.color.black));
        unitimg.setImageDrawable(drawable);


        trng_rel.setBackgroundResource(R.drawable.circle_light_blue_with_white_stroke);
        Resources res1 = this.getResources();
        Drawable drawable1 = res1.getDrawable(R.mipmap.ic_security_black_24dp);
        drawable1 = DrawableCompat.wrap(drawable1);
        DrawableCompat.setTint(drawable1, this.getResources().getColor(R.color.white));
        trng_img.setImageDrawable(drawable1);

        meeting.setBackgroundResource(R.drawable.circle_light_onlywhite_with_white_stroke);
        Resources res2 = this.getResources();
        Drawable drawable2 = res2.getDrawable(R.mipmap.ic_people_black_24dp);
        drawable2 = DrawableCompat.wrap(drawable2);
        DrawableCompat.setTint(drawable2, this.getResources().getColor(R.color.black));
        meetingimg.setImageDrawable(drawable2);

        night_chk_rel.setBackgroundResource(R.drawable.circle_light_onlywhite_with_white_stroke);
        Resources res3 = this.getResources();
        Drawable drawable3 = res3.getDrawable(R.mipmap.ic_brightness_2_black_24dp);
        drawable1 = DrawableCompat.wrap(drawable3);
        DrawableCompat.setTint(drawable3, this.getResources().getColor(R.color.black));
        nightchk_img.setImageDrawable(drawable3);
        proceedTraining();
    }

    private void proceedTraining() {

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar1.setProgress(progressStatus);
                        }
                    });
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (progressStatus == 100) {
                    Intent i = new Intent(MainActivity.this, TrainingActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
            }
        }).start();
    }



   /* @Override
    public void finish() {
        System.runFinalizersOnExit(true) ;
        super.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    @Override
    public void onBackPressed() {


        // TODO Auto-generated method stub
        if (exit)
        {
            System.exit(0);
           // finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }

            *//*super.onBackPressed();
        Intent i=new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();*//*
    }*/
}
