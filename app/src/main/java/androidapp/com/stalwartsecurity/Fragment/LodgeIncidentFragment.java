package androidapp.com.stalwartsecurity.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidapp.com.stalwartsecurity.Adapter.ImageListAdapter;
import androidapp.com.stalwartsecurity.R;

public class LodgeIncidentFragment extends Fragment {
    Button next,next1;
    RelativeLayout log_form,log_form1,log_form2,camera,gallery;
    Spinner statustype,intencity,incident;
    FloatingActionButton click;
    Dialog dialog;
    ListView lv_img;
    ImageListAdapter imageadapter;
    private final static int RESULT_SELECT_IMAGE = 100;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    EditText log_date;
    Calendar myCalendar;
    ImageView camera_img,gallery_img;
    //Integer array= new Integer(R.drawable.image);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.lodge_fragment, container, false);
        log_date=(EditText)view.findViewById(R.id.log_date);
        click=(FloatingActionButton)view.findViewById(R.id.float_btn);
        statustype=(Spinner)view.findViewById(R.id.status_id);
        camera=(RelativeLayout)view.findViewById(R.id.choose_camera);
        gallery=(RelativeLayout)view.findViewById(R.id.choose_gallery);
        lv_img=(ListView)view.findViewById(R.id.photo_list);
        intencity=(Spinner)view.findViewById(R.id.intencity_type);
        incident=(Spinner)view.findViewById(R.id.incident_spin);
        log_form=(RelativeLayout)view.findViewById(R.id.log_incident_form);
        log_form1=(RelativeLayout)view.findViewById(R.id.log_form1);
        log_form2=(RelativeLayout)view.findViewById(R.id.log_form2);
        myCalendar= Calendar.getInstance();
       final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };




        log_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               DatePickerDialog datepickerDialog= new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datepickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());

                datepickerDialog.show();

            }
        });

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClicltoFloat();
            }
        });
        next=(Button)view.findViewById(R.id.log_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                log_form.setVisibility(View.GONE);
                log_form1.setVisibility(View.VISIBLE);
                log_form2.setVisibility(View.GONE);
            }
        });


        next1=(Button)view.findViewById(R.id.log_next1);
        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                        log_form.setVisibility(View.GONE);
                        log_form1.setVisibility(View.GONE);
                        log_form2.setVisibility(View.VISIBLE);
                    }
                });

        List<String> list = new ArrayList<String>();
        list.add("Open");
        list.add("Close");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statustype.setAdapter(dataAdapter);
        statustype.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        List<String> list1 = new ArrayList<String>();
        list1.add("High");
        list1.add("Low");
        list1.add("Medium");

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, list1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intencity.setAdapter(dataAdapter1);
        intencity.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        List<String> list2 = new ArrayList<String>();
        list2.add("Accident");
        list2.add("Theft");
        list2.add("Missing Material");
        list2.add("Fight/Misbehave");
        list2.add("Others..");


        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, list2);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incident.setAdapter(dataAdapter2);
        incident.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

       imagelIST();
        return view;
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        log_date.setText(sdf.format(myCalendar.getTime()));
    }


    private void imagelIST() {
        imageadapter=new ImageListAdapter(getActivity());
        lv_img.setAdapter(imageadapter);

    }

    private void ClicltoFloat() {
        ChooseDialog();
    }

    private void ChooseDialog() {

        dialog = new Dialog(getContext());
        // it remove the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set the laytout in the dialog
        dialog.setContentView(R.layout.dialogbox);
        // set the background partial transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        param.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        // it dismiss the dialog when click outside the dialog frame
        dialog.setCanceledOnTouchOutside(true);
        // initialize the item of the dialog box, whose id is demo1
        View demodialog =(View) dialog.findViewById(R.id.cross);
        View camera_img=(View)dialog.findViewById(R.id.camera);
        View gallery_img=(View)dialog.findViewById(R.id.gallery);



        camera_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
        });
        gallery_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    //Pick Image From Gallery
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i,RESULT_SELECT_IMAGE);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        // it call when click on the item whose id is demo1.
        demodialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // diss miss the dialog
                dialog.dismiss();
            }
        });

        // it show the dialog box
        dialog.show();

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null)
                {
                    try
                    {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}