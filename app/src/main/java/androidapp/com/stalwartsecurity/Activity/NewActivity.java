package androidapp.com.stalwartsecurity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidapp.com.stalwartsecurity.R;

/**
 * Created by mobileapplication on 8/18/17.
 */

public class NewActivity extends AppCompatActivity {
    RelativeLayout linn,check_out;
    TextView date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin1);

        linn=(RelativeLayout)findViewById(R.id.chkin_rlatv);
        Button ofcr_rpt=(Button)findViewById(R.id.ofcr_report);
        Button customer_fdbk=(Button)findViewById(R.id.custmr_fdbck);
        Button check_out=(Button) findViewById(R.id.chk_out);
        date=(TextView)findViewById(R.id.dt);

        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mma");
        dateFormatter.setLenient(false);
        Date today = new Date();
        date.setText(dateFormatter.format(today));
        ofcr_rpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(NewActivity.this,OfficerReportActivity.class);
                startActivity(i);
            }
        });
        customer_fdbk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(NewActivity.this,CustomerFeddbackActivity.class);
                startActivity(i);
            }
        });


        check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewActivity.this, MainActivity.class);
                startActivity(i);

                finish();
                /*String report=OfficerReportActivity.report;

                if(report==null){
                    showsnackbar("Require Officer's Report");
                }
                else {
                    Intent i = new Intent(NewActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);

                    finish();
                }*/
            }
        });
    }



        private void showsnackbar(String message) {
            Snackbar snackbar = Snackbar
                    .make(linn, message, Snackbar.LENGTH_LONG);
            snackbar.show();
        }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }*/


   /*
   * For Disable back button..............
   * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "Do Checkout",
                    Toast.LENGTH_LONG).show();

        return false;

    }
}
