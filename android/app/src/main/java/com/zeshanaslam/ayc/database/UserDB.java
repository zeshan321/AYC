package com.zeshanaslam.ayc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDB extends SQLiteOpenHelper {

    public UserDB(Context context) {
        super(context, "UserDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE User ("
                + "ID INTEGER PRIMARY KEY   AUTOINCREMENT," + "Username TEXT," + "Password TEXT,"
                + "Videos TEXT" + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addUser(String username, String password, String videos) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Username", username);
        values.put("Password", password);
        values.put("Videos", videos);

        db.insert("User", null, values);
        db.close();
    }

    public boolean isSet() {
        boolean isSet = false;

        try {
            String SQL = "SELECT * FROM User";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(SQL, null);

            if (cursor != null && cursor.moveToFirst()) {
                isSet = true;
            }

            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return isSet;
    }

    public String getUsername() {
        String username = null;

        try {
            String SQL = "SELECT * FROM User";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(SQL, null);

            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    username = cursor.getString(cursor.getColumnIndex("Username"));

                    cursor.moveToNext();
                }
            }

            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        return username;
    }

    public String getVideos() {
        String videos = null;

        try {
            String SQL = "SELECT * FROM User";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(SQL, null);

            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    videos = cursor.getString(cursor.getColumnIndex("Videos"));

                    cursor.moveToNext();
                }
            }

            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        return videos;
    }
}
