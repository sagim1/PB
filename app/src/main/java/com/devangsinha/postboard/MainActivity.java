package com.devangsinha.postboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView1;
    private Firebase firebase;
    private List<Event> eventList;
    private List<String> eventImageList;
    private String eventName;
    private String eventDay;
    private String eventTime;
    private String eventPlace;
    private String eventBy;
    private String eventImage;
    private String eventId;
    private String going;
    private String notGoing;
    private Intent intent;
    private Query query;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init()
    {
        listView1=(ListView)findViewById(R.id.listView1);
        listView1.setOnItemClickListener(this);
        eventImageList=new ArrayList<>();
        getAllEvents();
    }

    private void getAllEvents()
    {
        firebase = new Firebase("https://postboard.firebaseio.com");
        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                eventList=new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    {
                        //Toast.makeText(MainActivity.this, dataSnapshot1.getRef().getKey().toString(), Toast.LENGTH_SHORT).show();
                        eventId = dataSnapshot1.getRef().getKey().toString();
                        eventName = dataSnapshot1.child("eventName").getValue(String.class);
                        eventDay = dataSnapshot1.child("eventDay").getValue(String.class);
                        eventTime = dataSnapshot1.child("eventTime").getValue(String.class);
                        eventPlace = dataSnapshot1.child("eventPlace").getValue(String.class);
                        eventBy = dataSnapshot1.child("eventBy").getValue(String.class);
                        eventImage = dataSnapshot1.child("eventImage").getValue(String.class);
                        //eventId = dataSnapshot1.child("eventId").getValue(String.class);
                        going = dataSnapshot1.child("going").getValue(String.class);
                        notGoing = dataSnapshot1.child("notGoing").getValue(String.class);
                        eventImageList.add(eventImage);
                        eventList.add(new Event(eventName, eventDay, eventTime, eventPlace, eventBy, eventImage, eventId, going, notGoing));
                    }
                }
                populateListView();
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
    }

    private void getCategoryEvents()
    {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList=new ArrayList<Event>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    eventId = dataSnapshot1.getRef().getKey().toString();
                    eventName = dataSnapshot1.child("eventName").getValue(String.class);
                    eventDay = dataSnapshot1.child("eventDay").getValue(String.class);
                    eventTime = dataSnapshot1.child("eventTime").getValue(String.class);
                    eventPlace = dataSnapshot1.child("eventPlace").getValue(String.class);
                    eventBy = dataSnapshot1.child("eventBy").getValue(String.class);
                    eventImage = dataSnapshot1.child("eventImage").getValue(String.class);
                    //eventId = dataSnapshot1.child("eventId").getValue(String.class);
                    going = dataSnapshot1.child("going").getValue(String.class);
                    notGoing = dataSnapshot1.child("notGoing").getValue(String.class);
                    eventImageList.add(eventImage);
                    eventList.add(new Event(eventName, eventDay, eventTime, eventPlace, eventBy, eventImage, eventId, going, notGoing));
                }
                List<Event> events=new ArrayList<Event>();
                listView1.setAdapter(new MyAdapter(MainActivity.this, R.id.imageView1, events));
                populateListView();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void populateListView()
    {
        for(Event event:eventList)
        {
            adapter=new MyAdapter(MainActivity.this, R.id.imageView1, eventList);
            listView1.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item_1:
                displayOptionsDialog(1);
                break;
            case R.id.item_2:
                displayOptionsDialog(2);
                break;
            case R.id.item_3:
                intent=new Intent(MainActivity.this, AddEventActivity.class);
                startActivity(intent);
                finish();
        }
        return true;
    }

    public void displayOptionsDialog(int index)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        if(index==1)
        {
            String[] options=new String[]{"Science Clubs", "Art Clubs", "Sports Clubs", "Honor Clubs", "Greek Groups", "Charity Clubs", "Religious Groups", "All Clubs"};
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    firebase = new Firebase("https://postboard.firebaseio.com/Events");
                    switch(which) {
                        case 0:
                            query = firebase.orderByChild("eventBy").equalTo("Science Club");
                            getCategoryEvents();
                            break;
                        case 1:
                            query = firebase.orderByChild("eventBy").equalTo("Art Club");
                            getCategoryEvents();
                            break;
                        case 2:
                            query = firebase.orderByChild("eventBy").equalTo("Sports Club");
                            getCategoryEvents();
                            break;
                        case 3:
                            query = firebase.orderByChild("eventBy").equalTo("Honor Club");
                            getCategoryEvents();
                            break;
                        case 4:
                            query = firebase.orderByChild("eventBy").equalTo("Greek Club");
                            getCategoryEvents();
                            break;
                        case 5:
                            query = firebase.orderByChild("eventBy").equalTo("Charity Club");
                            getCategoryEvents();
                            break;
                        case 6:
                            query = firebase.orderByChild("eventBy").equalTo("Religous Club");
                            getCategoryEvents();
                            break;
                        case 7:
                            getAllEvents();
                            break;
                    }
                }
            });
        }
        if(index==2)
        {
            String[] options=new String[]{"Now or soon first", "Most upvotes", "Most downvotes", "Alphabetically", "Include past events for today"};
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    firebase = new Firebase("https://postboard.firebaseio.com/Events");
                    switch (which) {
                        case 0:
                            getAllEvents();
                            List<Event> events = new ArrayList<Event>();
                            int minIndex=-1;
                            DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
                            try {
                                for (int i = 0; i < eventList.size(); i++) {
                                    Date minDate = (Date) dateFormat.parse("01/01/9999");
                                    for (int j = 0; j < eventList.size(); j++) {
                                        DateTime dateTime1 = new DateTime(minDate);
                                        Date eventDate = (Date) dateFormat.parse(eventList.get(j).getEventDay());
                                        DateTime dateTime2 = new DateTime(eventDate);
                                        if (dateTime1.isAfter(dateTime2)&&!eventList.get(j).getEventName().equals("DONE")) {
                                            minDate = eventDate;
                                            minIndex=j;
                                        }
                                    }
                                    events.add(eventList.get(minIndex));
                                    eventList.set(minIndex,new Event("DONE", eventList.get(minIndex).getEventDay(), eventList.get(minIndex).getEventTime(), eventList.get(minIndex).getEventPlace(), eventList.get(minIndex).getEventBy(), eventList.get(minIndex).getEventImage(), eventList.get(minIndex).getEventId(), eventList.get(minIndex).getGoing(), eventList.get(minIndex).getNotGoing()));
                                }
                                Collections.copy(eventList, events);
                                adapter.clear();
                                adapter.notifyDataSetChanged();
                                MyAdapter adapter=new MyAdapter(MainActivity.this, R.id.imageView1, events);
                                listView1=(ListView)findViewById(R.id.listView1);
                                listView1.setAdapter(adapter);
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, e.toString() ,Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 1:
                            query = firebase.orderByChild("going");
                            getCategoryEvents();
                            break;
                        case 2:
                            query = firebase.orderByChild("notGoing");
                            getCategoryEvents();
                            break;
                        case 3:
                            query = firebase.orderByChild("eventName");
                            getCategoryEvents();
                            break;
                        case 4:
                            break;
                    }
                }
            });
        }
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView=(TextView)view.findViewById(R.id.textView1);
        eventName=textView.getTag(R.string.eventBy).toString();
        eventDay=textView.getTag(R.string.eventDay).toString();
        eventTime=textView.getTag(R.string.eventTime).toString();
        eventPlace=textView.getTag(R.string.eventPlace).toString();
        eventBy=textView.getTag(R.string.eventBy).toString();
        eventImage=textView.getTag(R.string.eventImage).toString();
        eventId=textView.getTag(R.string.eventId).toString();
        going=textView.getTag(R.string.going).toString();
        notGoing=textView.getTag(R.string.notGoing).toString();

        intent=new Intent(MainActivity.this, ViewEventDetails.class);
        intent.putExtra("eventName", eventName);
        intent.putExtra("eventDay", eventDay);
        intent.putExtra("eventBy", eventBy);
        intent.putExtra("eventTime", eventTime);
        intent.putExtra("eventPlace", eventPlace);
        intent.putExtra("eventImage", eventImage);
        intent.putExtra("eventId", eventId);
        intent.putExtra("going", going);
        intent.putExtra("notGoing", notGoing);
        startActivity(intent);
        finish();
    }
}
