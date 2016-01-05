package com.zeshanaslam.ayc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.zeshanaslam.ayc.MainActivity;
import com.zeshanaslam.ayc.R;
import com.zeshanaslam.ayc.database.CacheDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class LoginHandler {

    private String JSON;
    private Context context;

    public LoginHandler(String JSON, Context context) {
        this.JSON = JSON;
        this.context = context;
    }

    public boolean loginCheck() {
        boolean login = true;

        try {
            JSONObject jsonObject = new JSONObject(JSON);

            login = jsonObject.getBoolean("succeed");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return login;
    }

    public String getVideos() {
        String videos = null;

        try {
            JSONArray jsonArray = new JSONObject(JSON).getJSONArray("info");

            for (int n = 0; n < jsonArray.length(); n++) {
                JSONObject object = jsonArray.getJSONObject(n);
                videos = object.getString("videos");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return videos;
    }

    public void loadYears(String URL) {
        final CacheDB cacheDB = new CacheDB(context);

        HTTPSManager httpsManager = new HTTPSManager();
        httpsManager.runConnection(URL + "/year", new HTTPSCallBack() {

            @Override
            public void onRequestComplete(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("info");

                    for (int n = 0; n < jsonArray.length(); n++) {
                        cacheDB.addYear(jsonArray.getString(n));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Start next activity
                Activity activity = ((Activity) context);
                Intent intent_next = new Intent(context, MainActivity.class);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                activity.startActivity(intent_next);
                activity.finish();
            }

            @Override
            public void onRequestComplete(InputStream inputStream) {}

            @Override
            public void onRequestFailed() {}
        });
    }
}
