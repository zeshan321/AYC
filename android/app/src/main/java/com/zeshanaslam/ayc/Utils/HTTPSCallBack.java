package com.zeshanaslam.ayc.utils;

import java.io.InputStream;

public abstract class HTTPSCallBack {
    public abstract void onRequestComplete(String response);
    public abstract void onRequestComplete(InputStream inputStream);

    public abstract void onRequestFailed();
}
