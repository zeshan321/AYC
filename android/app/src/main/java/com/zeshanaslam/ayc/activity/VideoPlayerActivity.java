package com.zeshanaslam.ayc.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.zeshanaslam.ayc.R;
import com.zeshanaslam.ayc.database.CacheDB;
import com.zeshanaslam.ayc.database.UserDB;
import com.zeshanaslam.ayc.requet.DownloadCallBack;
import com.zeshanaslam.ayc.requet.HTTPSDownload;

import java.io.File;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

public class VideoPlayerActivity extends AppCompatActivity {

    private int ID;
    private String year;
    private String section;
    private String name;
    private String desc;
    private String appPath;

    private CacheDB cacheDB;
    private MediaController mediaController;

    // Views
    @Bind(R.id.fragment_video_player)
    VideoView _videoView;
    @Bind(R.id.fragment_video_title)
    TextView _videoTitle;
    @Bind(R.id.fragment_video_desc)
    TextView _videoDesc;
    @Bind(R.id.fragment_video_download)
    TextView _videoDownload;
    @Bind(R.id.fragment_video_progress)
    ProgressBar _videoProgress;

    // Strings
    @BindString(R.string.server_url)
    String _serverURL;
    @BindString(R.string.video_error)
    String _videoError;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplayer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cacheDB = new CacheDB(this);
        appPath = this.getApplication().getApplicationContext().getFilesDir().getAbsolutePath();

        // Bind views
        ButterKnife.bind(this);

        // Get intent extras
        Bundle bundleExtras = getIntent().getExtras();
        if (bundleExtras != null) {
            ID = bundleExtras.getInt("ID");
            year = bundleExtras.getString("year");
            section = bundleExtras.getString("section");
            name = bundleExtras.getString("name");
            desc = bundleExtras.getString("desc");

            _videoTitle.setText(name);
            _videoDesc.setText(desc);

            // Set action bar to video title
            getSupportActionBar().setTitle(name);
        }

        mediaController = new MediaController(this);
        mediaController.setAnchorView(_videoView);

        _videoView.setMediaController(mediaController);

        _videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
                        mediaController = new MediaController(VideoPlayerActivity.this);
                        _videoView.setMediaController(mediaController);

                        mediaController.setAnchorView(_videoView);
                    }
                });
            }
        });

        _videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                _videoProgress.setVisibility(View.GONE);
                _videoDownload.setVisibility(View.VISIBLE);
                _videoDownload.setText(_videoError);

                // Delete corrupt file
                new File(appPath + "/videos/" + ID + ".mp4").delete();
                return true;
            }
        });

        // Video downloader
        File videoFile = new File(appPath + "/videos/" + ID + ".mp4");

        if (videoFile.exists()) {
            System.out.println("Video found");
            playVideo(appPath + "/videos/" + ID + ".mp4");
        } else {
            _videoDownload.setVisibility(View.VISIBLE);
            _videoProgress.setVisibility(View.VISIBLE);
            downloadVideo();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent_next = new Intent(this, VideoActivity.class);
                intent_next.putExtra("year", year);
                intent_next.putExtra("section", section);

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

    private void downloadVideo() {
        UserDB userDB = new UserDB(this);

        // Make videos dir if it does not exist
        new File(appPath + "/videos").mkdir();

        final File videoFile = new File(appPath + "/videos/" + ID + ".mp4");
        if (videoFile.exists()) {
            videoFile.delete();
        }

        HTTPSDownload httpsDownload = new HTTPSDownload();
        httpsDownload.runConnectionDownload(_serverURL + "/download?user=" + userDB.getUsername() + "&pass=" + userDB.getPassword() + "&ID=" + ID, videoFile, new DownloadCallBack() {

            @Override
            public void onRequestComplete() {
                playVideo(videoFile.getPath());
            }

            @Override
            public void onProgress(int progress) {
                _videoProgress.setProgress(progress);
            }

            @Override
            public void onRequestFailed() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _videoProgress.setVisibility(View.GONE);
                        _videoDownload.setVisibility(View.VISIBLE);
                        _videoDownload.setText(_videoError);
                    }
                });

                // Delete corrupt file
                new File(appPath + "/videos/" + ID + ".mp4").delete();
            }
        });
    }

    private void playVideo(final String path) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _videoDownload.setVisibility(View.GONE);
                _videoProgress.setVisibility(View.GONE);

                _videoView.setVideoPath(path);
                _videoView.start();
            }
        });
    }
}
