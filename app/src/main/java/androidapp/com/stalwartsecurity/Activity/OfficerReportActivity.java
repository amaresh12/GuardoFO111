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

import androidapp.com.stalwartsecurity.R;


/**
 * Created by mobileapplication on 8/31/17.
 */

public class OfficerReportActivity extends AppCompatActivity {

    Button submit,ok;
    RelativeLayout ofcr_ratingform,ofcr_rason_form,linn;
    TextView ratings_value;
    RatingBar ratingBar;
    EditText reason_comment,ofcr_fdbck;
    public static String report;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.officer_fragment);


        reason_comment=(EditText)findViewById(R.id.ofcr_reason) ;
        ofcr_fdbck=(EditText)findViewById(R.id.text_area) ;
        submit=(Button)findViewById(R.id.ofcr_submit_btn);
        ok=(Button)findViewById(R.id.ofcr_reason_next);
        ratings_value=(TextView)findViewById(R.id.ofcr_ratings);
        linn=(RelativeLayout)findViewById(R.id.ofcr_layout);
        ofcr_ratingform=(RelativeLayout)findViewById(R.id.ofcr_ratings_form);
        ofcr_rason_form=(RelativeLayout)findViewById(R.id.ofcr_rason_form);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ofcrPage();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment();
            }
        });

        addListenerOnRatingBar();
    }


    private void showsnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(linn, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    private void comment() {

        String reason=reason_comment.getText().toString();

        if(reason.contentEquals("")){
            showsnackbar("Give Comments");
        }
    }



    private void ofcrPage() {

        validatefields();


    }

    private void validatefields() {

        String feedback=ofcr_fdbck.getText().toString();
         report=feedback;
        String rating_value=ratings_value.getText().toString();

        if(feedback.contentEquals("")){
            showsnackbar("Give Feedback ");
        }
        else if(rating_value.contentEquals("")){
            showsnackbar("Give Ratings");
        }
        else{
            ratingsbar();
        }

    }

    private void ratingsbar() {

        Double rating_value= Double.valueOf(ratings_value.getText().toString().trim());
        if(rating_value>=4.0){
            ofcr_ratingform.setVisibility(View.VISIBLE);
            ofcr_rason_form.setVisibility(View.GONE);


        }
        else {
            ofcr_ratingform.setVisibility(View.GONE);
            ofcr_rason_form.setVisibility(View.VISIBLE);
        }

    }

    private void addListenerOnRatingBar() {

        ratingBar = (RatingBar)findViewById(R.id.ofcr_rating);
        ratings_value = (TextView)findViewById(R.id.ofcr_ratings);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                ratings_value.setText(String.valueOf(rating));

            }
        });
    }
    }
