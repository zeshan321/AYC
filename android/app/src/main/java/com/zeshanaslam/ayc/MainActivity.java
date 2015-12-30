package com.zeshanaslam.ayc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zeshanaslam.ayc.R;
import com.zeshanaslam.ayc.activity.LoginActivity;
import com.zeshanaslam.ayc.database.UserDB;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.textView)
    TextView _textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the user is already registered
        UserDB userDB = new UserDB(this);
        if (!userDB.isSet()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            finish();
            return;
        }

        // Bind views
        ButterKnife.bind(this);

        _textView.setText(userDB.getUsername() + "\n" + userDB.getVideos());
    }
}