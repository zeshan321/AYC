package com.zeshanaslam.ayc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.zeshanaslam.ayc.activity.LoginActivity;
import com.zeshanaslam.ayc.database.CacheDB;
import com.zeshanaslam.ayc.database.UserDB;
import com.zeshanaslam.ayc.listviews.year.YearArrayAdapter;
import com.zeshanaslam.ayc.listviews.year.YearObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private UserDB userDB = null;

    // Views
    @Bind(R.id.lv_year)
    ListView _listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is already registered
        userDB = new UserDB(this);

        if (!userDB.isSet()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            finish();
            return;
        }

        // Set main activity layout
        setContentView(R.layout.activity_main);

        // Bind views
        ButterKnife.bind(this);

        // Setup listview data
        setupListView();
    }

    private void setupListView() {
        CacheDB cacheDB = new CacheDB(this);
        YearArrayAdapter yearArrayAdapter = new YearArrayAdapter(this, R.layout.listview_year);

        for (String years : cacheDB.getYears()) {
            if (userDB.getVideos().contains(years)) {
                yearArrayAdapter.add(new YearObject(years, true));
            } else {
                yearArrayAdapter.add(new YearObject(years, false));
            }
        }

        _listView.setAdapter(yearArrayAdapter);
    }
}