package androidapp.com.stalwartsecurity.Activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import androidapp.com.stalwartsecurity.R;

/**
 * Created by mobileapplication on 8/31/17.
 */

public class CustomerFeddbackActivity extends AppCompatActivity {


    RatingBar ratingBar;
    TextView txtRatingValue;
    RelativeLayout fdbck,reason,signature;
    Button fdbck_next,rsn_next,signa_submit,clear;
    RelativeLayout linn;
    EditText customer_name,customer_desig,customer_reson;
    SignaturePad signature_pad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_fragment);


        signature_pad=(SignaturePad)findViewById(R.id.signatory);
        customer_name=(EditText)findViewById(R.id.customer_name);
        customer_desig=(EditText)findViewById(R.id.customer_designation);
        customer_reson=(EditText)findViewById(R.id.et_reason);
        fdbck=(RelativeLayout)findViewById(R.id.fedbck_form);
        reason=(RelativeLayout)findViewById(R.id.reason_form);
        signature=(RelativeLayout)findViewById(R.id.signatore_form);
        fdbck_next=(Button)findViewById(R.id.fdbck_next);
        rsn_next=(Button)findViewById(R.id.reason_next);
        signa_submit=(Button)findViewById(R.id.save_button);
        clear=(Button)findViewById(R.id.clear);
        linn=(RelativeLayout)findViewById(R.id.fedbck);
        addListenerOnRatingBar();
        fdbck_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forButtonData();
            }
        });
        rsn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateForm();
            }
        });
        signature_pad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                Toast.makeText(CustomerFeddbackActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                signa_submit.setEnabled(true);
                clear.setEnabled(true);
            }

            @Override
            public void onClear() {
                signa_submit.setEnabled(false);
                clear.setEnabled(false);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CustomerFeddbackActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();

                signature_pad.clear();
            }
        });

    }

    private void ValidateForm() {
        String cu_reason=customer_reson.getText().toString();

        if(cu_reason.contentEquals("")){
            showsnackbar("Put the Reason...");
        }
        else {
            signature.setVisibility(View.VISIBLE);
            fdbck.setVisibility(View.GONE);
            reason.setVisibility(View.GONE);


        }


    }

    private void forButtonData() {
        String name=customer_name.getText().toString();
        String ratings=txtRatingValue.getText().toString();



        if(name.contentEquals("")) {

            showsnackbar("Give your Name..");


        }
        else if(ratings.contentEquals("")){
            showsnackbar("Give Ratings");
        }
        else{
            ValidateField();


        }


    }
    private void showsnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(linn, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void ValidateField() {

        Double ratings_value= Double.valueOf(txtRatingValue.getText().toString().trim());


        if(ratings_value>=4.0){

            fdbck.setVisibility(View.GONE);
            reason.setVisibility(View.GONE);
            signature.setVisibility(View.VISIBLE);


        }
        else{
            fdbck.setVisibility(View.GONE);
            reason.setVisibility(View.VISIBLE);
            signature.setVisibility(View.GONE);

        }


    }




    private void addListenerOnRatingBar() {


        ratingBar = (RatingBar) findViewById(R.id.customer_ratings);
        txtRatingValue = (TextView) findViewById(R.id.rating_value);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue.setText(String.valueOf(rating));

            }
        });
    }
}