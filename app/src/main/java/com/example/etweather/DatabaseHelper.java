package com.example.etweather;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WeatherSearch.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "SearchHistory";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CITY_NAME = "cityName";
    private static final String COLUMN_CONDITION = "condition";
    private static final String COLUMN_TEMPERATURE = "temperature";
    private static final String COLUMN_HUMIDITY = "humidity";
    private static final String COLUMN_WIND_SPEED = "windSpeed";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CITY_NAME + " TEXT, " +
                COLUMN_CONDITION + " TEXT, " +
                COLUMN_TEMPERATURE + " REAL, " +
                COLUMN_HUMIDITY + " INTEGER, " +
                COLUMN_WIND_SPEED + " REAL )" ;
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertSearchHistory(String cityName, String condition, double temperature, int humidity, double windSpeed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CITY_NAME, cityName);
        cv.put(COLUMN_CONDITION, condition);
        cv.put(COLUMN_TEMPERATURE, temperature);
        cv.put(COLUMN_HUMIDITY, humidity);
        cv.put(COLUMN_WIND_SPEED, windSpeed);

        long insert = db.insert(TABLE_NAME, null, cv);
        db.close();
        return insert != -1;
    }

    public Cursor getAllCities() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM SearchHistory";
        return db.rawQuery(query, null);
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SearchHistory");
        db.close();
    }



}

