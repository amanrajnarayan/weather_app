package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView tempTextView;
    TextView cityTextView;
    TextView weatherDescTextView;
    TextView date;
    ImageView weatherImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempTextView = (TextView) findViewById(R.id.tempTextView);
//        tempTextView.setText("16.3");
        date = (TextView) findViewById(R.id.date);
        weatherDescTextView = (TextView) findViewById(R.id.weatherDescTextView);
        cityTextView = (TextView) findViewById((R.id.cityTextView));
        weatherImageView = (ImageView) findViewById(R.id.weatherImageView);
//        weatherImageView.setImageResource(R.drawable.icon_clearsky);


        date.setText(getCurrentDate());

        String url = "https://api.openweathermap.org/data/2.5/weather?id=1260086&appid=81cbddc591d72992014df0f2f769ced6&units=Metric";
//        String url = "https://api.openweathermap.org/data/2.5/weather?id=2643743&appid=81cbddc591d72992014df0f2f769ced6&units=Metric";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject responseObject) {
//                        tempTextView.setText("Response: " + response.toString());
                        Log.v("WEATHER","Response: " + responseObject.toString());

                        try {
                            JSONObject mainJSONObject = responseObject.getJSONObject("main");
                            JSONArray weatherArray = responseObject.getJSONArray("weather");
                            JSONObject firstWeatherObject = weatherArray.getJSONObject(0);

                            String temp = Integer.toString((int)Math.round(mainJSONObject.getDouble("temp")));
                            String weatherDescription=firstWeatherObject.getString("description");
                            String iconDescription=firstWeatherObject.getString("icon");
                            String city = responseObject.getString("name");

                            tempTextView.setText(temp);
                            weatherDescTextView.setText(weatherDescription);
                            cityTextView.setText(city);

//                            int iconResourceId = getResources().getIdentifier("icon_"+ weatherDescription.replace(" ","") "drawable",getPackageName());
                            int iconResourceId = getResources().getIdentifier("icon_" + iconDescription.replace(" ", ""), "drawable", getPackageName());
                            weatherImageView.setImageResource(iconResourceId);


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

// Access the RequestQueue through your singleton class.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE,MMM dd");
        String formattedDate = dateFormat.format(calendar.getTime());

        return formattedDate;
    }
}