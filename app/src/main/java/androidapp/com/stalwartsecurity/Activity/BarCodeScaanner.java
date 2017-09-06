package androidapp.com.stalwartsecurity.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidapp.com.stalwartsecurity.R;

public class BarCodeScaanner extends AppCompatActivity {
    Button bar_scan,bar_ok;
    TextView barcodeResult;
    public static String barcodeRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_scaanner);
        bar_scan=(Button)findViewById(R.id.bar_scan);
        bar_ok=(Button)findViewById(R.id.bar_ok);
        barcodeResult=(TextView)findViewById(R.id.barcodeResult);
        bar_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcode();

            }
        });
        bar_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void scanBarcode() {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, 0);
        }
        catch (Exception e){
            showDialog(BarCodeScaanner.this,"No scanner found", "Download Scanner code Activity?"," Yes", "No" ).show();

        }
    }
    private Dialog showDialog (final Activity act, CharSequence title, CharSequence message, CharSequence yes, CharSequence no ) {

        // a subclass of dialog that can display buttons and message
        AlertDialog.Builder download = new AlertDialog.Builder( act );
        download.setTitle( title );
        download.setMessage ( message );
        download.setPositiveButton ( yes, new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick( DialogInterface dialog, int i ) {
                // TODO Auto-generated method stub
                //uri to download barcode scanner
                Uri uri = Uri.parse( "market://search?q=pname:" + "com.google.zxing.client.android" );
                Intent in = new Intent ( Intent.ACTION_VIEW, uri );
                try {
                    act.startActivity ( in );
                } catch ( ActivityNotFoundException e) {

                }
            }
        });
        download.setNegativeButton ( no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int i ) {
                // TODO Auto-generated method stub
            }
        });
        return download.show();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                bar_ok.setVisibility(View.VISIBLE);
                bar_scan.setVisibility(View.GONE);
                barcodeRes=contents;
                barcodeResult.setText("Scan Result: "+contents);
              //  getActivity().onBackPressed();
                /* Toast toast = Toast.makeText(getActivity(), "Content:" + contents + " Format:" + contents , Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();*/
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                bar_ok.setVisibility(View.GONE);
                bar_scan.setVisibility(View.VISIBLE);
                Toast toast = Toast.makeText(BarCodeScaanner.this, "Scan was Cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();

            }
        }
    }

}
