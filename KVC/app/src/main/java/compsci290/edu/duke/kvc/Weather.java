package compsci290.edu.duke.kvc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Natalie on 4/21/17.
 *
 * // code borrowed from
 // http://androstock.com/tutorials/create-a-weather-app-on-android-android-studio.html
 //and https://code.tutsplus.com/tutorials/create-a-weather-app-on-android--cms-21587
 */

// DURHAM'S CITY ID IS 4464368
public class Weather{

    public static double getData(){
        double ans = 0;
        BufferedReader reader = null;
        StringBuffer json = new StringBuffer(1024);

            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?id=4464368&units=imperial&appid=54aa8efad13a44916ed53266ee0ab168");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                Log.d("WEATHER", "connection is being made");
                try {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String tmp = "";
                    try {
                        while ((tmp = reader.readLine()) != null)
                            json.append(tmp).append("\n");
                        reader.close();
                        try {
                            JSONObject data = new JSONObject(json.toString());
                            JSONObject m = data.getJSONObject("main");
                            ans = m.getDouble("temp");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return ans;
        }
    }


