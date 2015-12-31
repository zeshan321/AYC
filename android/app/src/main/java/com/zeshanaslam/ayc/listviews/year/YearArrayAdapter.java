package com.zeshanaslam.ayc.listviews.year;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zeshanaslam.ayc.R;

import java.util.ArrayList;
import java.util.List;

public class YearArrayAdapter extends ArrayAdapter<YearObject> {

    private List<YearObject> yearList = new ArrayList<YearObject>();

    public YearArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(YearObject object) {
        yearList.add(object);

        super.add(object);
    }

    @Override
    public void clear() {
        this.yearList.clear();

        super.clear();
    }

    public void remove(YearObject object) {
        yearList.remove(object);
    }

    public void remove(int i) {
        yearList.remove(i);
    }

    public int getCount() {
        return this.yearList.size();
    }

    public View getView(int position, View row, ViewGroup parent) {
        YearObject yearObject = getItem(position);
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        row = inflater.inflate(R.layout.listview_year, parent, false);

        TextView yearMain = (TextView) row.findViewById(R.id.tv_year_main);
        TextView yearDesc = (TextView) row.findViewById(R.id.tv_year_desc);

        yearMain.setText(yearObject.year);

        if (yearObject.owned) {
            yearDesc.setText("Unlocked");
        } else {
            yearDesc.setText("Locked");
        }

        return row;
    }
}
