package com.example.memeit.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DailyMeme {

    public static final String TAG = "DailyMemeModel";

    String dailyMemePath;
    String dailyMemeTitle;


    public DailyMeme(JSONObject jsonObject) throws JSONException {
        dailyMemePath = jsonObject.getJSONObject("images").getJSONObject("original").getString("url");
        dailyMemeTitle = jsonObject.getString("title");

        Log.i(TAG, "dailyMemeImages: " + dailyMemePath);
        Log.i(TAG, "dailyMemeTitle: " + dailyMemeTitle);

    }

    public static DailyMeme fromJsonArray(JSONArray dailyMemeJSONArray) throws JSONException {
        DailyMeme featuredMeme = new DailyMeme(dailyMemeJSONArray.getJSONObject(0));//.getJSONObject("images").getJSONObject("original").getString("url"));
//        Log.i(TAG, "featuredMemeImages: " + featuredMeme);

        return featuredMeme;
    }

    public String getDailyMemePath() {
//        return String.format("https://image.tmdb.org/t/p/w342%s", dailyMemePath);
        return dailyMemePath;
    }

    public String getDailyMemeTitle() {
        return dailyMemeTitle;
    }
}

