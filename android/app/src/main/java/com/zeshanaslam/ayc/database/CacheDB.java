package com.zeshanaslam.ayc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.zeshanaslam.ayc.listviews.section.SectionObject;

import java.util.ArrayList;
import java.util.List;

public class CacheDB extends SQLiteOpenHelper {

    public CacheDB(Context context) {
        super(context, "CacheDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String yearsTable = "CREATE TABLE Year ("
                + "ID INTEGER PRIMARY KEY   AUTOINCREMENT, Year TEXT )";

        db.execSQL(yearsTable);

        String sectionTable = "CREATE TABLE Section ("
                + "ID INTEGER PRIMARY KEY, Year TEXT, Name TEXT)";

        db.execSQL(sectionTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*
    Years
     */
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

    public void clearYears() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Year", null, null);
        db.close();
    }

    /*
    Sections
     */

    public void addSection(String ID, String year, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ID", Integer.parseInt(ID));
        values.put("Year", year);
        values.put("Name", name);

        db.insert("Section", null, values);
        db.close();
    }

    public List<SectionObject> getSections() {
        List<SectionObject> sectionsList = new ArrayList<>();

        try {
            String SQL = "SELECT * FROM Section";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(SQL, null);

            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    sectionsList.add(new SectionObject(cursor.getInt(cursor.getColumnIndex("ID")), cursor.getString(cursor.getColumnIndex("Year")), cursor.getString(cursor.getColumnIndex("Name"))));

                    cursor.moveToNext();
                }
            }

            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        return sectionsList;
    }

    public void clearSections() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Section", null, null);
        db.close();
    }

    public boolean isSectionEmpty() {
        boolean empty = true;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Section", null);

        if (cursor != null && cursor.moveToFirst()) {
            empty = (cursor.getInt (0) == 0);
        }

        cursor.close();
        db.close();

        return empty;
    }

}
