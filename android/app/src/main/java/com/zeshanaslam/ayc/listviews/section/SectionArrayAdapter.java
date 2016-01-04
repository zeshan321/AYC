package com.zeshanaslam.ayc.listviews.section;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zeshanaslam.ayc.R;

import java.util.ArrayList;
import java.util.List;

public class SectionArrayAdapter extends ArrayAdapter<SectionObject> {

    private List<SectionObject> sectionList = new ArrayList<SectionObject>();

    public SectionArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(SectionObject object) {
        sectionList.add(object);

        super.add(object);
    }

    @Override
    public void clear() {
        this.sectionList.clear();

        super.clear();
    }

    public void remove(SectionObject object) {
        sectionList.remove(object);
    }

    public void remove(int i) {
        sectionList.remove(i);
    }

    public int getCount() {
        return this.sectionList.size();
    }

    public View getView(int position, View row, ViewGroup parent) {
        SectionObject sectionObject = getItem(position);
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        row = inflater.inflate(R.layout.listview_single, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.tv_name);
        textView.setText(sectionObject.name);

        return row;
    }
}
