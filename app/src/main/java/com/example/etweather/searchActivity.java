package com.example.etweather;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class searchActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        databaseHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.searchedCities);

        loadCities();

        Button homeButton = findViewById(R.id.homeButton);
        Button historyButton = findViewById(R.id.historyButton);

        Button clearDataButton = findViewById(R.id.cleardata);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(searchActivity.this, weatherActivity.class);
                startActivity(homeIntent);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(searchActivity.this, searchActivity.class);
                startActivity(historyIntent);
            }
        });

        clearDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });
    }

    private void loadCities() {
        Cursor cursor = databaseHelper.getAllCities();
        ArrayList<HashMap<String, String>> cityList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String cityName = cursor.getString(cursor.getColumnIndex("cityName"));
                @SuppressLint("Range") String condition = cursor.getString(cursor.getColumnIndex("condition"));

                HashMap<String, String> cityMap = new HashMap<>();
                cityMap.put("cityName", cityName);
                cityMap.put("condition", condition);
                cityList.add(cityMap);
            } while (cursor.moveToNext());
        }

        cursor.close();

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                cityList,
                android.R.layout.simple_list_item_2,
                new String[]{"cityName", "condition"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        listView.setAdapter(adapter);
    }

    private void clearData() {
        databaseHelper.clearDatabase();

        loadCities();
    }
}
