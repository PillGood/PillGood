package com.example.pc.pillgood;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2017-06-22.
 */

public class EntryDatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "entryManager";
    private static final String TABLE_ENTRIES = "entries";
    // Entry Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_HOSPITAL = "hospital";
    private static final String KEY_DATE = "date";
    private static final String KEY_DONE = "done";
    private static final String KEY_SUB = "sub";
    private static EntryDatabaseHandler entryDatabaseHandler;


    private EntryDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized EntryDatabaseHandler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (entryDatabaseHandler == null) {
            entryDatabaseHandler = new EntryDatabaseHandler(context.getApplicationContext());
        }
        return entryDatabaseHandler;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_ENTRIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_HOSPITAL + " TEXT,"
                + KEY_DATE + " INTEGER,"
                + KEY_DONE + " INTEGER,"
                + KEY_SUB + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);

        // Create tables again
        onCreate(db);
    }

    public void addEntry(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, entry.getTitle());
        values.put(KEY_HOSPITAL, entry.getHospital());
        values.put(KEY_DATE, entry.getDate().toString());
        values.put(KEY_DONE, entry.getDone());
        JSONArray jsArray = new JSONArray(entry.getSubEntries());
        values.put(KEY_SUB, jsArray.toString());

        // Inserting Row
        db.insert(TABLE_ENTRIES, null, values);
        db.close(); // Closing database connection
    }

    public Entry getEntry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ENTRIES,
                new String[]{KEY_ID, KEY_TITLE, KEY_HOSPITAL, KEY_DATE, KEY_DONE, KEY_SUB},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Entry entry = new Entry(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Long.parseLong(cursor.getString(3)));
        entry.setDone(Integer.parseInt(cursor.getString(4)));
        ArrayList<String> listdata = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(cursor.getString(5));
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
                    listdata.add(jArray.getString(i));
                }
            }
            entry.setSubEntries(listdata);
        }catch (JSONException e){

        }
        cursor.close();
        return entry;
    }

    public List<Entry> getAllEntries() {
        List<Entry> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ENTRIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Entry entry = new Entry();
                entry.setId(Integer.parseInt(cursor.getString(0)));
                entry.setTitle(cursor.getString(1));
                entry.setHospital(cursor.getString(2));
                entry.setDate(Long.parseLong(cursor.getString(3)));
                entry.setDone(Integer.parseInt(cursor.getString(4)));
                contactList.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }

    public List<Entry> getDoneEntries() {
        List<Entry> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ENTRIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if (Integer.parseInt(cursor.getString(4)) == 1) {
                    Entry entry = new Entry();
                    entry.setId(Integer.parseInt(cursor.getString(0)));
                    entry.setTitle(cursor.getString(1));
                    entry.setHospital(cursor.getString(2));
                    entry.setDate(Long.parseLong(cursor.getString(3)));
                    entry.setDone(Integer.parseInt(cursor.getString(4)));
                    ArrayList<String> listdata = new ArrayList<>();
                    try {
                        JSONArray jArray = new JSONArray(cursor.getString(5));
                        if (jArray != null) {
                            for (int i=0;i<jArray.length();i++){
                                listdata.add(jArray.getString(i));
                            }
                        }
                        entry.setSubEntries(listdata);
                    }catch (JSONException e){

                    }
                    contactList.add(entry);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }

    public List<Entry> getUndoneEntries() {
        List<Entry> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ENTRIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if (Integer.parseInt(cursor.getString(4)) == 0) {
                    Entry entry = new Entry();
                    entry.setId(Integer.parseInt(cursor.getString(0)));
                    entry.setTitle(cursor.getString(1));
                    entry.setHospital(cursor.getString(2));
                    entry.setDate(Long.parseLong(cursor.getString(3)));
                    entry.setDone(Integer.parseInt(cursor.getString(4)));
                    ArrayList<String> listdata = new ArrayList<>();
                    try {
                        JSONArray jArray = new JSONArray(cursor.getString(5));
                        if (jArray != null) {
                            for (int i=0;i<jArray.length();i++){
                                listdata.add(jArray.getString(i));
                            }
                        }
                        entry.setSubEntries(listdata);
                    }catch (JSONException e){

                    }
                    contactList.add(entry);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }

    public int getEntryCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ENTRIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateEntry(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, entry.getTitle());
        values.put(KEY_HOSPITAL, entry.getHospital());
        values.put(KEY_DATE, entry.getDate().toString());
        values.put(KEY_DONE, entry.getDone());
        JSONArray jsArray = new JSONArray(entry.getSubEntries());
        values.put(KEY_SUB, jsArray.toString());

        // updating row
        return db.update(TABLE_ENTRIES, values, KEY_ID + " = ?", new String[]{String.valueOf(entry.getId())});
    }

    public void deleteEntry(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENTRIES, KEY_ID + " = ?", new String[]{String.valueOf(entry.getId())});
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENTRIES, null, null);
    }
}
