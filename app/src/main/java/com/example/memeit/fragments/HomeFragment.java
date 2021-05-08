package com.example.memeit.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.memeit.Auth.Login;
import com.example.memeit.Memes;
import com.example.memeit.R;
import com.example.memeit.models.DailyMeme;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Headers;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class HomeFragment extends Fragment {
    ParseUser parseUser = ParseUser.getCurrentUser();
    final String TAG = "MainActivity";
    Button button;


    final String TRENDING_MEME_URL = "https://api.giphy.com/v1/gifs/trending?api_key=85yQsmwttEKrF5w5R3AAPMd3UpbVwKsC&limit=25&rating=g";

    Context context;
    BottomNavigationView bottomNav;

    Button showPopup;

    TextView dailyMemeTitle;
    ImageView dailyMemeImage;

    DailyMeme dailyMeme;

    AsyncHttpClient client = new AsyncHttpClient();

    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showPopup = view.findViewById(R.id.dailyMemeButton);

        showPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDailyMemePopup();
            }
        });

        client.get(TRENDING_MEME_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JsonHttpResponseHandler.JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray data = jsonObject.getJSONArray("data");
//                    Log.i(TAG, "Results: " + data.toString());
                    dailyMeme = DailyMeme.fromJsonArray(data);

//                    Log.i(TAG, "the dailyMeme:" + dailyMeme);
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

    public void createDailyMemePopup() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dailyMemePopup = inflater.inflate(R.layout.daily_meme_popup_page, null);

        dailyMemeTitle = dailyMemePopup.findViewById(R.id.tvDailyMemeTitle);
        dailyMemeImage = dailyMemePopup.findViewById(R.id.ivShowDailyMeme);

        //in here, call the function to add the queried meme
        addMeme(dailyMeme.getDailyMemeTitle(), dailyMeme.getDailyMemePath());

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

    void addMeme (String title, String URL){
        //Query the db first to check if record already exists.
        //can just look for if the title string already exists in db
        ParseQuery<ParseObject> query = ParseQuery.getQuery("memes");
        query.whereStartsWith("memeName", title);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() == 0) {
                    //If meme record does not yet exist, add it to db.
                    //upvote downvote values should start as 0 in new records
                    Memes meme = new Memes();
                    meme.setMemeURL(URL);
                    meme.setmemeName(title);
                    meme.setvoteVal(0);

                    meme.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Error while saving meme to db", e);
                            } else
                                Log.i(TAG, "new meme has been added! ");
                        }
                    });
                }
                Log.d(TAG, "Parse query search results " + objects);
            }
        });
//        Log.d(TAG, "Parse query search results " + results);

    }

}


