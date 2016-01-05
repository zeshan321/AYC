package com.zeshanaslam.ayc.listviews.video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zeshanaslam.ayc.R;

import java.util.ArrayList;
import java.util.List;

public class VideoArrayAdapter extends ArrayAdapter<VideoObject> {

    private List<VideoObject> list = new ArrayList<>();

    public VideoArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(VideoObject object) {
        list.add(object);

        super.add(object);
    }

    @Override
    public void clear() {
        this.list.clear();

        super.clear();
    }

    public void remove(VideoObject object) {
        list.remove(object);
    }

    public void remove(int i) {
        list.remove(i);
    }

    public int getCount() {
        return this.list.size();
    }

    public View getView(int position, View row, ViewGroup parent) {
        VideoObject videoObject = getItem(position);
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        row = inflater.inflate(R.layout.listview_single, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.tv_name);
        textView.setText(videoObject.name);

        return row;
    }
}
