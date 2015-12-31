package com.zeshanaslam.ayc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CacheDB extends SQLiteOpenHelper {

    public CacheDB(Context context) {
        super(context, "CacheDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String yearsTable = "CREATE TABLE Year ("
                + "ID INTEGER PRIMARY KEY   AUTOINCREMENT," + "Year TEXT )";

        db.execSQL(yearsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addYear(String year) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Year", year);

        db.insert("Year", null, values);
        db.close();
    }

    public List<String> getYears() {
        List<String> yearsList = new ArrayList<>();

        try {
            String SQL = "SELECT * FROM Year";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(SQL, null);

            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    yearsList.add(cursor.getString(cursor.getColumnIndex("Year")));

                    cursor.moveToNext();
                }
            }

            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        return yearsList;
    }
}
