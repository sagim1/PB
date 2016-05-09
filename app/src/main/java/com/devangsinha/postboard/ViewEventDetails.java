package com.devangsinha.postboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ViewEventDetails extends AppCompatActivity {

    private ImageView imageView1;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private String eventName;
    private String eventDay;
    private String eventTime;
    private String eventPlace;
    private String eventBy;
    private String eventId;
    private String eventImage;
    private String going;
    private String notGoing;
    private Firebase firebase;
    private Button button1;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event_details);
        init();
        getData();
    }

    private void init()
    {
        imageView1=(ImageView)findViewById(R.id.imageView1);
        textView1=(TextView)findViewById(R.id.textView1);
        textView2=(TextView)findViewById(R.id.textView2);
        textView3=(TextView)findViewById(R.id.textView3);
        textView4=(TextView)findViewById(R.id.textView4);
        textView5=(TextView)findViewById(R.id.textView5);
        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);
        firebase = new Firebase("https://postboard.firebaseio.com");
    }

    private void getData()
    {
        eventName=getIntent().getStringExtra("eventName");
        eventDay=getIntent().getStringExtra("eventDay");
        eventTime=getIntent().getStringExtra("eventTime");
        eventPlace=getIntent().getStringExtra("eventPlace");
        eventBy=getIntent().getStringExtra("eventBy");
        eventImage=getIntent().getStringExtra("eventImage");
        eventId=getIntent().getStringExtra("eventId");
        going=getIntent().getStringExtra("going");
        notGoing=getIntent().getStringExtra("notGoing");

        textView1.setText("Event Name: " + eventName);
        textView2.setText("Event Day: " + eventDay);
        textView3.setText("Event Time: " + eventTime);
        textView4.setText("Event Place: " + eventPlace);
        textView5.setText("Event By: " + eventBy);

        /*firebase = new Firebase("https://postboard.firebaseio.com/Events");
        Query query=firebase.orderByChild("eventImage").equalTo(eventImage);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(ViewEventDetails.this, "" + dataSnapshot.getChildrenCount() , Toast.LENGTH_SHORT).show();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    Toast.makeText(ViewEventDetails.this, "Hello" , Toast.LENGTH_SHORT).show();
                    going = dataSnapshot1.child("going").getValue(String.class);
                    notGoing = dataSnapshot.child("notGoing").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/


        button1.setText("Going (" + going + ")");
        button2.setText("Not Going (" + notGoing + ")");

        imageView1.setImageBitmap(getImageFromURL(eventImage));
    }

    private Bitmap getImageFromURL(String src)
    {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void button1_onClick(View v)
    {
        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Toast.makeText(ViewEventDetails.this, dataSnapshot.getChildren().iterator().next().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //String id=firebase.getRef().getParent().getKey();
        Map<String, Object> goingMap=new HashMap<String, Object>();
        going=String.valueOf(Integer.valueOf(going)+1);
        goingMap.put("Events/" + eventId + "/going", going);
        button1.setText("Going (" + going + ")");
        firebase.updateChildren(goingMap);
    }

    public void button2_onClick(View v)
    {
        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Toast.makeText(ViewEventDetails.this, dataSnapshot.getChildren().iterator().next().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //String id=firebase.getRef().getParent().getKey();
        Map<String, Object> notGoingMap=new HashMap<String, Object>();
        notGoing=String.valueOf(Integer.valueOf(notGoing)+1);
        notGoingMap.put("Events/" + eventId + "/notGoing", notGoing);
        button2.setText("Not Going (" + notGoing + ")");
        firebase.updateChildren(notGoingMap);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back
            Intent intent=new Intent(ViewEventDetails.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
