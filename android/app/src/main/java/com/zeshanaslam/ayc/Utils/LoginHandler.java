package com.zeshanaslam.ayc.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginHandler {

    String JSON;

    public LoginHandler(String JSON) {
        this.JSON = JSON;
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
}
