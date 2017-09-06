package androidapp.com.stalwartsecurity.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import androidapp.com.stalwartsecurity.R;

public class ResidentHome extends AppCompatActivity {
    CardView inviteVisitors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_home);
        inviteVisitors=(CardView)findViewById(R.id.invite);
        inviteVisitors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ResidentHome.this,InviteVisitors.class);
                startActivity(intent);
            }
        });
    }
}
