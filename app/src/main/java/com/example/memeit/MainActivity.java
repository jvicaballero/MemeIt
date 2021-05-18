package com.example.memeit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
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
import com.example.memeit.fragments.HomeFragment;
import com.example.memeit.fragments.MemeFragment;
import com.example.memeit.fragments.ProfileFragment;
import com.example.memeit.models.DailyMeme;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Button button;


    public static final String TRENDING_MEME_URL = "https://api.giphy.com/v1/gifs/trending?api_key=85yQsmwttEKrF5w5R3AAPMd3UpbVwKsC&limit=25&rating=g";

    private ChipNavigationBar bottomNav;
//    private BottomNavigationView bottomNav;
    private Fragment fragment = null;

    Button showPopup;

    TextView dailyMemeTitle;
    ImageView dailyMemeImage, appbarsinout;

    DailyMeme dailyMeme;

    final FragmentManager fragmentManager= getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        showPopup = findViewById(R.id.dailyMemeButton);
        bottomNav.setItemSelected(R.id.postings,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MemeFragment()).commit();

        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){
                    case R.id.postings:
                        fragment = new MemeFragment();
                        break;
                    case R.id.profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.saved:
                        fragment = new HomeFragment();
                        break;
                    default:
                        fragment = new MemeFragment();
                        break;
                }
                if(fragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                }
            }
        });

//        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment fragment;
//                switch (item.getItemId()) {
//                    case R.id.postings:
//                        fragment = new MemeFragment();
//                        break;
//                    case R.id.profile:
//                        fragment = new ProfileFragment();
//                        break;
//                    case R.id.saved:
//                        fragment = new HomeFragment();
//                        break;
//                    default:
//                        fragment = new MemeFragment();
//                        break;
//                }
//                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
//                return true;
//            }
//        });
//
//        bottomNav.setSelectedItemId(R.id.postings);
    }
}

