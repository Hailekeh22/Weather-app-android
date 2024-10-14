package com.example.etweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import org.json.JSONObject;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;


public class weatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather2);

        Button homeButton = findViewById(R.id.homeButton);
        Button historyButton = findViewById(R.id.historyButton);
        Button searchButton = findViewById(R.id.searchbtn);
        EditText queryTxt = findViewById(R.id.querytxt);
        Button addis = findViewById(R.id.addis);
        Button bahirdar = findViewById(R.id.bahirdar);
        Button gondar = findViewById(R.id.gondar);
        Button hawassa = findViewById(R.id.hawassa);
        Button adama = findViewById(R.id.adama);
        Button jimma = findViewById(R.id.jimma);

        final OkHttpClient client = new OkHttpClient();

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(weatherActivity.this, weatherActivity.class);
                startActivity(homeIntent);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(weatherActivity.this, searchActivity.class);
                startActivity(historyIntent);
            }
        });


        addis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    fetchWeatherData("addis ababa", client);
            }
        });

        jimma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchWeatherData("jimma", client);
            }
        });

        adama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchWeatherData("adama", client);
            }
        });

        hawassa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchWeatherData("hawassa", client);
            }
        });

        gondar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchWeatherData("gondar", client);
            }
        });

        bahirdar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchWeatherData("bahir dar", client);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = queryTxt.getText().toString().trim();
                if (!city.isEmpty()) {
                    fetchWeatherData(city, client);

                } else {
                    Toast.makeText(weatherActivity.this, "Please enter a city", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    DatabaseHelper dbHelper = new DatabaseHelper(this);


    private void fetchWeatherData(String city, OkHttpClient client) {
        String url = "https://weather-app-back-end-hbn3.onrender.com/searchcity";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("city", city);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder().url(url).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();

                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        final String name = jsonResponse.getString("name");
                        final String condition = jsonResponse.getString("condition");
                        final double temperature = jsonResponse.getDouble("tempreture");
                        final int humidity = jsonResponse.getInt("humidity");
                        final double windSpeed = jsonResponse.getDouble("windSpeed");

                        dbHelper.insertSearchHistory(name, condition, temperature, humidity, windSpeed);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateXML(name, condition, temperature, humidity, windSpeed);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateXML(String name, String condition, double temperature, int humidity, double windSpeed) {
        TextView cityName = findViewById(R.id.cityName);
        TextView weatherCondition = findViewById(R.id.condition);
        TextView temp = findViewById(R.id.tempreture);
        TextView humid = findViewById(R.id.humidity);
        TextView wind = findViewById(R.id.windspeed);

        cityName.setText(name);
        weatherCondition.setText(condition);
        temp.setText(String.valueOf(temperature) + " °C");
        humid.setText("Humidity: " + humidity + "%");
        wind.setText("Wind Speed: " + windSpeed + " m/s");
    }


}
