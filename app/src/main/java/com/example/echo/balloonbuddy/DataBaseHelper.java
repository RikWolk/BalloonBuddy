package com.example.echo.balloonbuddy;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private static final String NUMBER = "number";
    private static final String CREATED_AT = "created_at";

    // SCORES Table - column nmaes
    private static final String SCORE = "score";
    private static final String MISTAKES = "mistakes";

    // SETTINGS Table - column names
    private static final String REMINDER = "reminder";

    // ACHIEVEMENTS Table - column names
    private static final String UNLOCKED = "unlocked";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";

    // SCORES Create Statements
    private static final String CREATE_TABLE_SCORES = "CREATE TABLE " + TABLE_SCORES
            + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SCORE
            + " INTEGER, " + MISTAKES + " INTEGER," + CREATED_AT + " DATETIME)";

    // SETTINGS Create Statement
    private static final String CREATE_TABLE_SETTINGS = "CREATE TABLE " + TABLE_SETTINGS
            + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REMINDER + " INTEGER, "
            + NUMBER + " INTEGER, " + CREATED_AT + " DATETIME)";

    // ACHIEVEMENTS Create Statement
    private static final String CREATE_TABLE_ACHIEVEMENTS = "CREATE TABLE " + TABLE_ACHIEVEMENTS
            + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + UNLOCKED + " INTEGER, "
            + NAME + " TEXT, " + DESCRIPTION + " TEXT, " + NUMBER + " INTEGER, "
            + CREATED_AT + " DATETIME)";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_SCORES);
        db.execSQL(CREATE_TABLE_SETTINGS);
        db.execSQL(CREATE_TABLE_ACHIEVEMENTS);

        insertSettings(db);
        insertAchievements(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACHIEVEMENTS);

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

    public void insertSettings(SQLiteDatabase db) {
        ContentValues settingsValues = new ContentValues();
        settingsValues.put(REMINDER, 1);
        settingsValues.put(NUMBER, 1);
        settingsValues.put(CREATED_AT, getDateTime());
        db.insert(TABLE_SETTINGS, null, settingsValues);
    }

    public void insertAchievements(SQLiteDatabase db) {
        ContentValues achievementsValues = new ContentValues();

        achievementsValues.put(UNLOCKED, 0);
        achievementsValues.put(NAME, "flights1");
        achievementsValues.put(DESCRIPTION, "Maak de eerste vlucht");
        achievementsValues.put(NUMBER, 1);
        achievementsValues.put(CREATED_AT, getDateTime());

        db.insert(TABLE_ACHIEVEMENTS, null, achievementsValues);

        achievementsValues.put(UNLOCKED, 0);
        achievementsValues.put(NAME, "flights100");
        achievementsValues.put(DESCRIPTION, "Maak 100 vluchten");
        achievementsValues.put(NUMBER, 2);
        achievementsValues.put(CREATED_AT, getDateTime());

        db.insert(TABLE_ACHIEVEMENTS, null, achievementsValues);

        achievementsValues.put(UNLOCKED, 0);
        achievementsValues.put(NAME, "flights250");
        achievementsValues.put(DESCRIPTION, "Maak 250 vluchten");
        achievementsValues.put(NUMBER, 3);
        achievementsValues.put(CREATED_AT, getDateTime());

        db.insert(TABLE_ACHIEVEMENTS, null, achievementsValues);

        achievementsValues.put(UNLOCKED, 0);
        achievementsValues.put(NAME, "streak7");
        achievementsValues.put(DESCRIPTION, "Vlieg 7 dagen achter elkaar");
        achievementsValues.put(NUMBER, 4);
        achievementsValues.put(CREATED_AT, getDateTime());

        db.insert(TABLE_ACHIEVEMENTS, null, achievementsValues);

        achievementsValues.put(UNLOCKED, 0);
        achievementsValues.put(NAME, "streak14");
        achievementsValues.put(DESCRIPTION, "Vlieg 14 dagen achter elkaar");
        achievementsValues.put(NUMBER, 5);
        achievementsValues.put(CREATED_AT, getDateTime());

        db.insert(TABLE_ACHIEVEMENTS, null, achievementsValues);

        achievementsValues.put(UNLOCKED, 0);
        achievementsValues.put(NAME, "streak28");
        achievementsValues.put(DESCRIPTION, "Vlieg 28 dagen achter elkaar");
        achievementsValues.put(NUMBER, 6);
        achievementsValues.put(CREATED_AT, getDateTime());

        db.insert(TABLE_ACHIEVEMENTS, null, achievementsValues);

        achievementsValues.put(UNLOCKED, 0);
        achievementsValues.put(NAME, "score1000");
        achievementsValues.put(DESCRIPTION, "Haal een score van 1000");
        achievementsValues.put(NUMBER, 7);
        achievementsValues.put(CREATED_AT, getDateTime());

        db.insert(TABLE_ACHIEVEMENTS, null, achievementsValues);

        achievementsValues.put(UNLOCKED, 0);
        achievementsValues.put(NAME, "score2500");
        achievementsValues.put(DESCRIPTION, "Haal een score van 2500");
        achievementsValues.put(NUMBER, 8);
        achievementsValues.put(CREATED_AT, getDateTime());

        db.insert(TABLE_ACHIEVEMENTS, null, achievementsValues);

        achievementsValues.put(UNLOCKED, 0);
        achievementsValues.put(NAME, "score5000");
        achievementsValues.put(DESCRIPTION, "Haal een score van 5000");
        achievementsValues.put(NUMBER, 9);
        achievementsValues.put(CREATED_AT, getDateTime());

        db.insert(TABLE_ACHIEVEMENTS, null, achievementsValues);
    }

    public Cursor getAllData(String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + table_name;
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public int getReminderSetting() {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT reminder FROM " + TABLE_SETTINGS + " WHERE number = 1";
        Cursor data = db.rawQuery(query, null);
        data.moveToFirst();
        int reminderState =  data.getInt(0);

        return reminderState;
    }

    public void updateAchievements() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT COUNT(*) FROM scores";
        Cursor data = db.rawQuery(query, null);
        data.moveToFirst();
        int count =  data.getInt(0);

        query = "SELECT MAX(score) FROM scores";
        data = db.rawQuery(query, null);
        data.moveToFirst();
        int highest =  data.getInt(0);

        query = "SELECT MIN(mistakes) FROM scores";
        data = db.rawQuery(query, null);
        data.moveToFirst();
        int lowest =  data.getInt(0);

        if(count > 0 && count < 20) {
            updateAchievement(1, 1);
        } else if(count >= 20 && count < 50) {
            updateAchievement(1, 1);
            updateAchievement(2, 1);
        } else if(count >= 50) {
            updateAchievement(1, 1);
            updateAchievement(2, 1);
            updateAchievement(3, 1);
        } else {
            Log.d("TableData", "Count van scores is nul");
        }

        if(lowest >= 0 && lowest <= 5) {
            updateAchievement(4, 1);
        } else if(lowest > 5 && lowest < 10) {
            updateAchievement(4, 1);
            updateAchievement(5, 1);
        } else if(lowest < 25) {
            updateAchievement(4, 1);
            updateAchievement(5, 1);
            updateAchievement(6, 1);
        } else {
            Log.d("TableData", "Laagste mistakes hoger dan 24");
        }

        if(highest >= 50 && highest < 150) {
            updateAchievement(7, 1);
        } else if(highest >= 150 && highest < 300) {
            updateAchievement(7, 1);
            updateAchievement(8, 1);
        } else if(highest >= 300) {
            updateAchievement(7, 1);
            updateAchievement(8, 1);
            updateAchievement(9, 1);
        } else {
            Log.d("TableData", "Hoogste score lager dan 50");
        }
    }

    public void updateAchievement(int number, int unlocked) {
        SQLiteDatabase db = this.getWritableDatabase();

        String UPDATE_SETTINGS = "UPDATE " + TABLE_ACHIEVEMENTS
                + " SET " + UNLOCKED + " = " + unlocked
                + " WHERE " + NUMBER + " = " + number;

        db.execSQL(UPDATE_SETTINGS);
    }

    public void updateSettings(int number, int reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        String UPDATE_SETTINGS = "UPDATE " + TABLE_SETTINGS
                + " SET " + REMINDER + " = " + reminder
                + " WHERE " + NUMBER + " = " + number;

        db.execSQL(UPDATE_SETTINGS);

        getReminderSetting();
    }

    public void deleteAllData(String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + table_name;
        Cursor data = db.rawQuery(query, null);

        while(data.moveToNext()) {
            String DELETE = "DELETE FROM " + table_name + " WHERE " + ID + " = " + data.getString(0);
            db.execSQL(DELETE);
        }
    }

    public void dropTable(String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DROP TABLE " + table_name;
        db.execSQL(query);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}