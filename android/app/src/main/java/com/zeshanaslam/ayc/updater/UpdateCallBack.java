package com.zeshanaslam.ayc.updater;

public abstract class UpdateCallBack {

    public enum UpdateType {
        Years, Sections, Videos
    }

    public abstract void onUpdateComplete(UpdateType updateType);
}
