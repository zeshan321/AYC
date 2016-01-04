package com.zeshanaslam.ayc.updater;

public abstract class UpdateCallBack {

    public enum UpdateType {
        Years, Sections
    }

    public abstract void onUpdateComplete(UpdateType updateType);
}
