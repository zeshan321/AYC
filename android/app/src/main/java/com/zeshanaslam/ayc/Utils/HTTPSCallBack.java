package com.zeshanaslam.ayc.utils;

public abstract class HTTPSCallBack {
    public abstract void onRequestComplete(String response);

    public abstract void onRequestFailed();
}
