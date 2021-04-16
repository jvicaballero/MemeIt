package com.example.memeit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.memeit.models.DailyMeme;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String TRENDING_MEME_URL = "https://api.giphy.com/v1/gifs/trending?api_key=85yQsmwttEKrF5w5R3AAPMd3UpbVwKsC&limit=25&rating=g";
    public static final String TAG = "MainActivity";

    Context context;
    Button button;
    Button showPopup;

    TextView dailyMemeTitle;
    ImageView dailyMemeImage;

    DailyMeme dailyMeme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = findViewById(R.id.button);
        showPopup = findViewById(R.id.dailyMemeButton);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        showPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDailyMemePopup();
            }
        });

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(TRENDING_MEME_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray data = jsonObject.getJSONArray("data");
                    Log.i(TAG, "Results: " + data.toString());
                    dailyMeme = DailyMeme.fromJsonArray(data);

                    Log.i(TAG, "the dailyMeme:" + dailyMeme);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    public void createDailyMemePopup(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dailyMemePopup = inflater.inflate(R.layout.daily_meme_popup_page, null);

        dailyMemeTitle = dailyMemePopup.findViewById(R.id.tvDailyMemeTitle);
        dailyMemeImage = dailyMemePopup.findViewById(R.id.ivShowDailyMeme);

        dailyMemeTitle.setText(dailyMeme.getDailyMemeTitle());
        Glide.with(this).asGif().load(dailyMeme.getDailyMemePath()).into(dailyMemeImage);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(dailyMemePopup, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(dailyMemePopup, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        dailyMemePopup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }



}