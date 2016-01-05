package com.zeshanaslam.ayc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.zeshanaslam.ayc.R;
import com.zeshanaslam.ayc.database.CacheDB;
import com.zeshanaslam.ayc.listviews.video.VideoArrayAdapter;
import com.zeshanaslam.ayc.listviews.video.VideoObject;
import com.zeshanaslam.ayc.updater.UpdateCallBack;
import com.zeshanaslam.ayc.updater.Updater;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

public class VideoActivity extends AppCompatActivity {

    private Context context;
    private CacheDB cacheDB;
    private String year;
    private String section;

    // Views
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout _swipeRefresh;
    @Bind(R.id.loading)
    RelativeLayout _progressLayout;
    @Bind(R.id.loading_bar)
    ProgressBar _progressBar;
    @Bind(R.id.lv_video)
    ListView _listView;
    @BindString(R.string.server_url)
    String _serverURL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        this.context = this;

        cacheDB = new CacheDB(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get intent extras
        Bundle bundleExtras = getIntent().getExtras();
        if (bundleExtras != null) {
            year = bundleExtras.getString("year");
            section = bundleExtras.getString("section");

            // Set action bar to section
            getSupportActionBar().setTitle(section);
        }

        // Bind views
        ButterKnife.bind(this);

        // Change progress bar color
        _progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.primary), android.graphics.PorterDuff.Mode.SRC_IN);

        // Setup listview data
        _progressLayout.setVisibility(View.VISIBLE);
        if (!cacheDB.isVideoEmpty()) {
            setupListView();
        }

        // Auto update
        new Updater(this, _serverURL).updateVideos(year, section, new UpdateCallBack() {
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

        // Swipe down refresh
        _swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Updater(context, _serverURL).updateVideos(year, section, new UpdateCallBack() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent_next = new Intent(this, SectionActivity.class);
                intent_next.putExtra("year", year);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                startActivity(intent_next);
                finish();
                return true;
            case 1:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupListView() {
        final VideoArrayAdapter videoArrayAdapter = new VideoArrayAdapter(this, R.layout.listview_single);

        // Clear listview
        _listView.setAdapter(null);

        // Add sections
        for (VideoObject videoObject : cacheDB.getVideos(year, section)) {
            videoArrayAdapter.add(videoObject);
        }

        _progressLayout.setVisibility(View.GONE);
        _listView.setAdapter(videoArrayAdapter);

        // Listen for clicks
        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                VideoObject videoObject = videoArrayAdapter.getItem(position);

                Activity activity = ((Activity) context);
                Intent intent_next = new Intent(context, VideoPlayerActivity.class);
                intent_next.putExtra("ID", videoObject.ID);
                intent_next.putExtra("year", year);
                intent_next.putExtra("section", section);
                intent_next.putExtra("name", videoObject.name);
                intent_next.putExtra("desc", videoObject.desc);

                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                activity.startActivity(intent_next);
                activity.finish();
            }
        });
    }
}
