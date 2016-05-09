package com.devangsinha.postboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class MyAdapter extends ArrayAdapter {

    private Context context;
    private List<Event> eventList;

    public MyAdapter(Context context, int resource, List<Event> eventList) {
        super(context, resource, eventList);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.context=context;
        this.eventList=eventList;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.row, parent, false);
        TextView textView=(TextView)view.findViewById(R.id.textView1);
        ImageView imageView=(ImageView)view.findViewById(R.id.imageView1);
        textView.setText(eventList.get(position).getEventName());
        textView.setTag(R.string.eventDay, eventList.get(position).getEventDay());
        textView.setTag(R.string.eventTime, eventList.get(position).getEventTime());
        textView.setTag(R.string.eventPlace, eventList.get(position).getEventPlace());
        textView.setTag(R.string.eventBy, eventList.get(position).getEventBy());
        textView.setTag(R.string.eventName, eventList.get(position).getEventName());
        textView.setTag(R.string.eventImage, eventList.get(position).getEventImage());
        textView.setTag(R.string.eventId, eventList.get(position).getEventId());
        textView.setTag(R.string.going, String.valueOf(eventList.get(position).getGoing()));
        textView.setTag(R.string.notGoing, String.valueOf(eventList.get(position).getNotGoing()));
        String url=eventList.get(position).getEventImage();
        Bitmap bitmap=getImageFromURL(url);
        imageView.setImageBitmap(bitmap);
        return view;
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
}
