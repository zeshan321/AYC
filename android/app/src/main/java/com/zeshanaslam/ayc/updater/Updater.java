package com.zeshanaslam.ayc.updater;

import android.content.Context;

import com.zeshanaslam.ayc.database.CacheDB;
import com.zeshanaslam.ayc.requet.HTTPSCallBack;
import com.zeshanaslam.ayc.requet.HTTPSManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Updater {

    Context context;
    String URL;

    public Updater(Context context, String URL) {
        this.context = context;
        this.URL = URL;
    }

    public void updateYears(final UpdateCallBack callBack) {
        final CacheDB cacheDB = new CacheDB(context);

        HTTPSManager httpsManager = new HTTPSManager();
        httpsManager.runConnection(URL + "/year", new HTTPSCallBack() {

            @Override
            public void onRequestComplete(String response) {
                // Clear previous years
                cacheDB.clearYears();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("info");

                    for (int n = 0; n < jsonArray.length(); n++) {
                        cacheDB.addYear(jsonArray.getString(n));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                callBack.onUpdateComplete();
            }

            @Override
            public void onRequestFailed() {

            }
        });
    }

    public void updateSections(final String year, final UpdateCallBack callBack) {
        final CacheDB cacheDB = new CacheDB(context);

        HTTPSManager httpsManager = new HTTPSManager();
        httpsManager.runConnection(URL + "/section?year=" + year, new HTTPSCallBack() {

            @Override
            public void onRequestComplete(String response) {
                // Clear previous sections
                cacheDB.clearSections();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("info");

                    for (int n = 0; n < jsonArray.length(); n++) {
                        JSONObject jsonObject1 = new JSONObject(jsonArray.getString(n));
                        cacheDB.addSection(jsonObject1.get("ID").toString(), year, jsonObject1.get("name").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                callBack.onUpdateComplete();
            }

            @Override
            public void onRequestFailed() {

            }
        });
    }

    public void updateVideos(final String year, final String section, final UpdateCallBack callBack) {
        final CacheDB cacheDB = new CacheDB(context);

        HTTPSManager httpsManager = new HTTPSManager();
        httpsManager.runConnection(URL + "/videos?year=" + year + "&section=" + section.replace(" ", "%20"), new HTTPSCallBack() {

            @Override
            public void onRequestComplete(String response) {
                // Clear previous videos
                cacheDB.clearVideos();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("info");

                    for (int n = 0; n < jsonArray.length(); n++) {
                        JSONObject jsonObject1 = new JSONObject(jsonArray.getString(n));
                        cacheDB.addVideo(jsonObject1.get("ID").toString(), year, section.replace("%20", " "), jsonObject1.get("name").toString(), jsonObject1.get("desc").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                callBack.onUpdateComplete();
            }

            @Override
            public void onRequestFailed() {

            }
        });
    }
}
