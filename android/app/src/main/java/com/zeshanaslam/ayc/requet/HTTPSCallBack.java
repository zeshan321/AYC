package com.zeshanaslam.ayc.requet;

public abstract class HTTPSCallBack {
    public abstract void onRequestComplete(String response);

    public abstract void onRequestFailed();
}
