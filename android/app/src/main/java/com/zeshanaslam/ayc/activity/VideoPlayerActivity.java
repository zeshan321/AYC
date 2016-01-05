package com.zeshanaslam.ayc.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.zeshanaslam.ayc.R;
import com.zeshanaslam.ayc.database.CacheDB;
import com.zeshanaslam.ayc.database.UserDB;
import com.zeshanaslam.ayc.utils.HTTPSCallBack;
import com.zeshanaslam.ayc.utils.HTTPSManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

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
    @Bind(R.id.videoView)
    VideoView _videoView;
    @Bind(R.id.videoDesc)
    TextView _videoDesc;
    @BindString(R.string.server_url)
    String _serverURL;

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

            _videoDesc.setText(desc);
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

        // Testing downloading
        File videoFile = new File(appPath + "/videos/" + ID + ".mp4");
        if (videoFile.exists()) {
            playVideo(appPath + "/videos/" + ID + ".mp4");
        } else {
            System.out.println("VIDEO DOWNLOADING!");
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

        HTTPSManager httpsManager = new HTTPSManager();
        httpsManager.runConnectionDownload(_serverURL + "/download?user=" + userDB.getUsername() + "&pass=" + userDB.getPassword() + "&ID=1", new HTTPSCallBack() {

            @Override
            public void onRequestComplete(InputStream inputStream) {
                try {
                    if (videoFile.exists()) {
                        videoFile.delete();
                    }

                    byte[] byteArray = ByteStreams.toByteArray(inputStream);
                    Files.write(byteArray, videoFile);

                    playVideo(videoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestComplete(String response) {}

            @Override
            public void onRequestFailed() {
                videoFile.delete();
            }
        });
    }

    private void playVideo(final String path) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _videoView.setVideoPath(path);
                _videoView.seekTo(2);
            }
        });
    }
}
