package com.example.memeit.fragments;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.memeit.Memes;
import com.example.memeit.MemesAdapter;
import com.example.memeit.R;
import com.example.memeit.models.DailyMeme;
import com.example.memeit.saveMemesAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class HomeFragment extends Fragment {
    ParseUser parseUser = ParseUser.getCurrentUser();
    final String TAG = "MainActivity";
    Button button;

    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;

    final String TRENDING_MEME_URL = "https://api.giphy.com/v1/gifs/trending?api_key=85yQsmwttEKrF5w5R3AAPMd3UpbVwKsC&limit=25&rating=g";

    Context context;
    BottomNavigationView bottomNav;

    Button showPopup;

    TextView dailyMemeTitle;
    ImageView dailyMemeImage;

    DailyMeme dailyMeme;

    AsyncHttpClient client = new AsyncHttpClient();

    private RecyclerView rvSaveMemes;
    protected List<Memes> memesList;
    protected saveMemesAdapter saveMemesAdapter;


    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rvSaveMemes = view.findViewById(R.id.rvSaveMemes);
        memesList= new ArrayList<>();
        saveMemesAdapter= new saveMemesAdapter(getContext(), memesList);
        rvSaveMemes.setAdapter(saveMemesAdapter);
        rvSaveMemes.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showPopup = view.findViewById(R.id.dailyMemeButton);

        //The reason for the error was because I was editing the same array list as the one binded to the adapter. So after adding the meme to the array list, i call clear which clears out the array.
        //Adding all to the array list would show empty because the same array was cleared after calling clear().

//        Memes newMeme = new Memes();
//
//        newMeme.setmemeName("Heart Love GIF by Hello All");
//        newMeme.setMemeURL("https://media1.giphy.com/media/wXLKuXiP2wWR71RhRe/giphy.gif?cid=f28d4bb7h0in6y6iq3jmwj071dxp2ry2o9vnzug5pn7q39fl&rid=giphy.gif&ct=g");
//
//        memesList.add(newMeme);
//        saveMemesAdapter.addAll(memesList);
//        saveMemesAdapter.notifyDataSetChanged();
//        rvSaveMemes = view.findViewById(R.id.rvSaveMemes);

//        saveMemesAdapter= new saveMemesAdapter(getContext(), memesList);
////
//        rvSaveMemes.setAdapter(saveMemesAdapter);
//        rvSaveMemes.setLayoutManager(new LinearLayoutManager(getContext()));

        getSavedMemes();

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

        dialogbuilder = new AlertDialog.Builder(getContext());
        final View conactPopupView = getLayoutInflater().inflate(R.layout.daily_meme_popup_page, null);
        dailyMemeTitle = (TextView) conactPopupView.findViewById(R.id.tvDailyMemeTitle);
        dailyMemeImage = (ImageView) conactPopupView.findViewById(R.id.ivShowDailyMeme);

        //in here, call the function to add the queried meme
        addMeme(dailyMeme.getDailyMemeTitle(), dailyMeme.getDailyMemePath());

        dailyMemeTitle.setText(dailyMeme.getDailyMemeTitle());
        Glide.with(this).asGif().load(dailyMeme.getDailyMemePath()).into(dailyMemeImage);
        dialogbuilder.setView(conactPopupView);
        dialog = dialogbuilder.create();
        dialog.show();
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View dailyMemePopup = inflater.inflate(R.layout.daily_meme_popup_page, null);
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
    //end of functions to getmemebutton

    private void getSavedMemes(){
//        ParseQuery<Memes> query = ParseQuery.getQuery(Memes.class);
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser savedMemesDB, ParseException e) {
                if(e == null){
                    //I THINK I FOUND THE ISSUE! THE MEME DATA SAVED ONTO BACK4APP IS ONLY A POINTER TO THE MEMES, DOES NOT ACTUALLY
                    //HAVE THE VALUES OF THE MEMES. MAYBE JUST CALL A SEARCH ON THE OBJECT IDS, REFER TO P2 MOCKSTAGRAM PROJ
                    JSONArray tempArray = savedMemesDB.getJSONArray("savedMemes");

                    //Have to create a new query to search for the content id and return back the meme.name, meme.url to display.
                    for(int i = 0; i < tempArray.length(); i++){
                        JSONObject row = null;
                        try {
                            row = tempArray.getJSONObject(i);
                            Log.i("SaveMemesDisplay", "content of save array " + row.get("objectId"));
                            Memes searchMeme = searchMeme(row.get("objectId"));
                            Log.i("SaveMemesComp", "What was returned from searchMeme: " + searchMeme);
                            Log.i("SaveMemesComp", "Taken from search query saved memes: " + searchMeme.getmemeName() + ' ' + searchMeme.getMemeURL());

//                            memesList.add(searchMeme);


                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }


                    }

//                    saveMemesAdapter.clear();
//                    saveMemesAdapter.addAll(memesList);
//                    saveMemesAdapter.notifyDataSetChanged();


                }
                else{
                    Log.e("SaveMemesDisplay" , "Something went wrong saving " + e + savedMemesDB);
                }

            }
        });

    }

    private Memes searchMeme(Object objectID){
        Memes resultMemeQuery = new Memes();

        Log.i("SaveMemesComp", "in function: " + objectID);
        ParseQuery<Memes> query = ParseQuery.getQuery("memes");
        query.whereEqualTo("objectId" , objectID);
        query.getFirstInBackground(new GetCallback<Memes>() {
            @Override
            public void done(Memes result, ParseException e) {
                if(e == null){
                    String memeName = result.getmemeName();
                    String memeURL = result.getMemeURL();

                    resultMemeQuery.setMemeURL(memeURL);
                    resultMemeQuery.setmemeName(memeName);
                    Log.i("SaveMemesComp", "In function search memes: " + resultMemeQuery.getmemeName() + ' ' + resultMemeQuery.getMemeURL());
                    memesList.add(resultMemeQuery);
                    Log.i("SaveMemesComp", "Lets check whats in memesList: " + memesList.toString());
//                    saveMemesAdapter.clearThis();
//
//
//                        Log.i("SaveMemesComp", memesList.toString());
//
//                    saveMemesAdapter.addAll(memesList);
                    saveMemesAdapter.notifyDataSetChanged();
                }
                else{
                    Log.e("SaveMemesComp", "Error retrieving saved memes: " + e);
                }
            }
        });

        return resultMemeQuery;
    }



}


