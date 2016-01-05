package com.zeshanaslam.ayc.requet;

import java.io.InputStream;

public abstract class DownloadCallBack {
    public abstract void onRequestComplete();

    public abstract void onProgress(int progress);

    public abstract void onRequestFailed();
}
