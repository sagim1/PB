package com.devangsinha.postboard;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Base64;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEventActivity extends AppCompatActivity {

    @NotEmpty(message="Name: field cannot be blank")
    private EditText editText1;
    @NotEmpty(message="Day: field cannot be blank")
    private EditText editText2;
    @NotEmpty(message="Time: field cannot be blank")
    private EditText editText3;
    @NotEmpty(message="Place: field cannot be blank")
    private EditText editText4;
    private Spinner spinner1;
    private ImageView imageView1;
    private Validator validator1;
    private Button button1;
    private DatePickerDialog datePickerDialog1;
    private TimePickerDialog timePickerDialog1;
    private Calendar calendar1;
    private String selectedYear;
    private String selectedMonth;
    private String selectedDay;
    private Bitmap bitmap1;
    private Firebase firebase;
    private List<Event> eventList;
    String eventImage;

    public AddEventActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_add_event);
        init();
        setValidator();
        setDatePickerDialog();
        setTimePickerDialog();
    }

    public void button1_onClick(View v)
    {
        validator1.validate();
    }

    public void init() {
        eventList=new ArrayList<Event>();
        firebase = new Firebase("https://postboard.firebaseio.com/");
        editText1=(EditText)findViewById(R.id.editText1);
        editText2=(EditText)findViewById(R.id.editText2);
        editText3=(EditText)findViewById(R.id.editText3);
        editText4=(EditText)findViewById(R.id.editText4);
        spinner1=(Spinner)findViewById(R.id.spinner1);
        button1=(Button)findViewById(R.id.button1);
        imageView1=(ImageView)findViewById(R.id.imageView1);
        validator1=new Validator(AddEventActivity.this);
        setDatePickerDialog();
        setTimePickerDialog();
    }

    public void setValidator()
    {
        validator1.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {

                String eventName = editText1.getText().toString();
                String eventDay = editText2.getText().toString();
                String eventTime = editText3.getText().toString();
                String eventPlace = editText4.getText().toString();
                String eventBy = spinner1.getSelectedItem().toString();

                bitmap1 =((BitmapDrawable)imageView1.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] bytes=byteArrayOutputStream.toByteArray();
                ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);

                Map config = new HashMap();
                config.put("cloud_name", "sinhadevang");
                config.put("api_key", "948898223435896");
                config.put("api_secret", "5JbeTSBbhtpwSNfzx_BZMVYyn4E");
                Cloudinary cloudinary = new Cloudinary(config);
                try {
                    Map uploadResult=cloudinary.uploader().upload(byteArrayInputStream, config);
                    String eventImage=uploadResult.get("url").toString();
                    Event event=new Event(eventName, eventDay, eventTime, eventPlace, eventBy, eventImage, String.valueOf(Math.random()*1000000).replace(".", ""), "0", "0");
                    firebase.child("Events").push().setValue(event);
                    Toast.makeText(AddEventActivity.this, "Event Added", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(AddEventActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch(Exception e)
                {
                }
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(AddEventActivity.this);
                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    }
                }
            }
        });
    }

    public void setDatePickerDialog()
    {
        calendar1=Calendar.getInstance();
        final int day=calendar1.get(Calendar.DAY_OF_MONTH);
        final int month=calendar1.get(Calendar.MONTH);
        final int year=calendar1.get(Calendar.YEAR);

        editText2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                datePickerDialog1 = new DatePickerDialog(AddEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String month = null;
                                String day = null;
                                if (monthOfYear < 10)
                                    month = "0" + (monthOfYear + 1);
                                else
                                    month = String.valueOf(monthOfYear);
                                if (dayOfMonth < 10)
                                    day = "0" + dayOfMonth;
                                else
                                    day = String.valueOf(dayOfMonth);
                                editText2.setText(month + "/" + day + "/" + year);
                                selectedYear = String.valueOf(year);
                                selectedMonth = String.valueOf(monthOfYear + 1);
                                selectedDay = String.valueOf(dayOfMonth);
                            }
                        }, year, month, day);
                datePickerDialog1.show();
            }
        });
    }

    public void setTimePickerDialog()
    {
        calendar1=Calendar.getInstance();
        final int hour=calendar1.get(Calendar.HOUR_OF_DAY);
        final int min=calendar1.get(Calendar.MINUTE);

        editText3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                timePickerDialog1 = new TimePickerDialog(AddEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker arg0, int hour1, int min1) {
                                // TODO Auto-generated method stub
                                editText3.setText(hour1 + ":" + min1);
                            }
                        }, hour, min, true);
                timePickerDialog1.show();
            }
        });
    }

    public void button2_onClick(View v)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImage = data.getData();
        imageView1.setImageURI(selectedImage);
        imageView1.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back
            Intent intent=new Intent(AddEventActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
