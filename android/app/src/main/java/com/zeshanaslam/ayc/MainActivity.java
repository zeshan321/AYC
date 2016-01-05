package com.zeshanaslam.ayc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.zeshanaslam.ayc.activity.LoginActivity;
import com.zeshanaslam.ayc.activity.SectionActivity;
import com.zeshanaslam.ayc.database.CacheDB;
import com.zeshanaslam.ayc.database.UserDB;
import com.zeshanaslam.ayc.listviews.year.YearArrayAdapter;
import com.zeshanaslam.ayc.listviews.year.YearObject;
import com.zeshanaslam.ayc.updater.UpdateCallBack;
import com.zeshanaslam.ayc.updater.Updater;
import com.zeshanaslam.ayc.utils.SettingsManager;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private UserDB userDB;
    private Context context;

    // Views
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout _swipeRefresh;
    @Bind(R.id.loading)
    RelativeLayout _progressLayout;
    @Bind(R.id.loading_bar)
    ProgressBar _progressBar;
    @Bind(R.id.lv_year)
    ListView _listView;
    @BindString(R.string.server_url)
    String _serverURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        // Check if the user is already registered
        userDB = new UserDB(this);

        if (!userDB.isSet()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            finish();
            return;
        }

        // Go to last selected year
        SettingsManager settingsManager = new SettingsManager(this);
        if (settingsManager.contains("lastYear")) {
            Intent intent_next = new Intent(this, SectionActivity.class);
            intent_next.putExtra("year", settingsManager.getString("lastYear"));

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            startActivity(intent_next);
            finish();
        }

        // Set main activity layout
        setContentView(R.layout.activity_main);

        // Bind views
        ButterKnife.bind(this);

        // Change progress bar color
        _progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.primary), android.graphics.PorterDuff.Mode.SRC_IN);

        // Setup listview data
        _progressLayout.setVisibility(View.VISIBLE);
        setupListView();

        // Auto update
        new Updater(this, _serverURL).updateYears(new UpdateCallBack() {
            @Override
            public void onUpdateComplete() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupListView();
                    }
                });
            }
        });

        _swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Updater(context, _serverURL).updateYears(new UpdateCallBack() {
                    @Override
                    public void onUpdateComplete() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setupListView();

                                _swipeRefresh.setRefreshing(false);
                            }
                        });
                    }
                });
            }
        });
    }

    private void setupListView() {

        CacheDB cacheDB = new CacheDB(this);
        final YearArrayAdapter yearArrayAdapter = new YearArrayAdapter(this, R.layout.listview_single);

        // Clear listview
        _listView.setAdapter(null);

        for (String years : cacheDB.getYears()) {
            if (userDB.getVideos().contains(years)) {
                yearArrayAdapter.add(new YearObject(years, true));
            } else {
                yearArrayAdapter.add(new YearObject(years, false));
            }
        }

        _progressLayout.setVisibility(View.GONE);
        _listView.setAdapter(yearArrayAdapter);

        // Listen for clicks
        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                YearObject yearObject = yearArrayAdapter.getItem(position);

                if (yearObject.owned) {
                    SettingsManager settingsManager = new SettingsManager(context);
                    settingsManager.set("lastYear", yearObject.year);

                    Activity activity = ((Activity) context);
                    Intent intent_next = new Intent(context, SectionActivity.class);
                    intent_next.putExtra("year", yearObject.year);

                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    activity.startActivity(intent_next);
                    activity.finish();
                } else {
                    // Do something if not owned
                }
            }
        });
    }
}