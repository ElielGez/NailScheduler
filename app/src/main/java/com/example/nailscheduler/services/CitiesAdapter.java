package com.example.nailscheduler.services;

import android.content.Context;

import com.example.nailscheduler.models.CityJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CitiesAdapter {
    private static String JSON_PATH = "cities.json";

    public static ArrayList<CityJSON> readJsonCities(Context context) {
        String json;
        ArrayList<CityJSON> cities = new ArrayList<CityJSON>();

        try {
            InputStream is = context.getAssets().open(JSON_PATH);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer,"UTF-8");
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject jo = arr.getJSONObject(i);
                cities.add(new CityJSON(jo.getInt("key"), jo.getString("value")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cities;
    }
}
