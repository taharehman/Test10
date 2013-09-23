package com.example.callingwebservice;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        
        // Setting all values in listview
        title.setText(song.get(MainActivity.KEY_NAME));
        artist.setText(song.get(MainActivity.KEY_DESC));
        duration.setText(song.get(MainActivity.KEY_DUR));
        imageLoader.DisplayImage(song.get(MainActivity.KEY_THUMB_URL), thumb_image);
        
        title.setHorizontallyScrolling(true);
		title.setEllipsize(TruncateAt.MARQUEE);
		title.setMarqueeRepeatLimit(-1);
		title.setSelected(true);
		
		artist.setHorizontallyScrolling(true);
		artist.setEllipsize(TruncateAt.MARQUEE);
		artist.setMarqueeRepeatLimit(-1);
		artist.setSelected(true);
		
        return vi;
    }
}