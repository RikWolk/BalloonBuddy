package com.example.echo.balloonbuddy;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DataBaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "balloonbuddy";

    // Table Names
    private static final String TABLE_SCORES = "scores";
    private static final String TABLE_SETTINGS = "settings";
    private static final String TABLE_ACHIEVEMENTS = "achievements";

    // Common column names
    private static final String ID = "id";
    private static final String CREATED_AT = "created_at";

    // SCORES Table - column nmaes
    private static final String SCORE = "score";
    private static final String MISTAKES = "mistakes";

    // SETTINGS Table - column names
    private static final String REMINDER = "reminder";

    // ACHIEVEMENTS Table - column names
    private static final String UNLOCKED = "unlocked";
    private static final String NAME = "unlocked";
    private static final String DESCRIPTION = "description";

    // SCORES Create Statements
    private static final String CREATE_TABLE_SCORES = "CREATE TABLE "
            + TABLE_SCORES + "(" + ID + " INTEGER PRIMARY KEY," + SCORE
            + " INTEGER," + MISTAKES + " INTEGER," + CREATED_AT
            + " DATETIME" + ")";

    // SETTINGS Create Statement
    private static final String CREATE_TABLE_SETTINGS = "CREATE TABLE " + TABLE_SETTINGS
            + "(" + ID + " INTEGER PRIMARY KEY," + REMINDER + " INTEGER,"
            + CREATED_AT + " DATETIME" + ")";

    // ACHIEVEMENTS Create Statement
//    private static final String CREATE_TABLE_ACHIEVEMENTS = "CREATE TABLE " + TABLE_ACHIEVEMENTS
//            + "(" + ID + " INTEGER PRIMARY KEY," + UNLOCKED + " INTEGER,"
//            + NAME + " TEXT," + DESCRIPTION + " TEXT,"
//            + CREATED_AT + " DATETIME" + ")";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_SCORES);
        db.execSQL(CREATE_TABLE_SETTINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);

        // create new tables
        onCreate(db);
    }

    public void insertScore(int score, int mistakes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SCORE, score);
        values.put(MISTAKES, mistakes);
        values.put(CREATED_AT, getDateTime());

        // insert row
        db.insert(TABLE_SCORES, null, values);
    }

    public void insertSetting(int reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(REMINDER, reminder);
        values.put(CREATED_AT, getDateTime());

        // insert row
        db.insert(TABLE_SETTINGS, null, values);
    }

    public Cursor getAllData(String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + table_name;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public void updateSettings(int id, int reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(REMINDER, reminder);

        String UPDATE_SETTINGS = "UPDATE " + TABLE_SETTINGS
                + " SET " + "'" + REMINDER + "' = " + reminder
                + " WHERE '" + ID + "' = " + id;

        db.execSQL(UPDATE_SETTINGS);

        // updating row
//         return db.update(TABLE_SETTINGS, values, ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void deleteAllData(String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + table_name;
        Cursor data = db.rawQuery(query, null);

        while(data.moveToNext()) {
            String DELETE = "DELETE FROM " + table_name + " WHERE " + ID + " = " + data.getString(0);
            db.execSQL(DELETE);
        }

        query = "SELECT * FROM " + table_name;
        data = db.rawQuery(query, null);

        while(data.moveToNext()) {
            Log.d("Table Data", "Row: " + data.getString(0));
        }
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}