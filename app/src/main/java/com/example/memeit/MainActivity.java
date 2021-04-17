package com.example.memeit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.memeit.Auth.Login;
import com.example.memeit.models.DailyMeme;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String TRENDING_MEME_URL = "https://api.giphy.com/v1/gifs/trending?api_key=85yQsmwttEKrF5w5R3AAPMd3UpbVwKsC&limit=25&rating=g";
    public static final String TAG = "MainActivity";

    Context context;
    private BottomNavigationView bottomNav;

    Button showPopup;

    TextView dailyMemeTitle;
    ImageView dailyMemeImage;

    DailyMeme dailyMeme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav= findViewById(R.id.botton_navigation);
        showPopup = findViewById(R.id.dailyMemeButton);



        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.postings:
//                        Toast.makeText(MainActivity.this,"Posts",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
//                        Toast.makeText(MainActivity.this,"Settings",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        break;
                    case R.id.signout:
                        Toast.makeText(MainActivity.this,"Signed Out",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                        break;
                    default:
                        break;
                }
                return true;
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