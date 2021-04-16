package com.example.memeit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
        
        queryMemes();
    }
//testing to see if the databse connection is working properly -- Akbar Haider 4/16/2021
    //you can see the queries in logcat with the tag MainActivity. The meme names will show up that is in our database right now
    private void queryMemes() {
        ParseQuery<Memes> query = ParseQuery.getQuery(Memes.class);
        query.include(Memes.memeURL);
        query.include(Memes.UpvoteVal);
        query.include(Memes.DownvoteVal);
        query.findInBackground(new FindCallback<Memes>() {
            @Override
            public void done(List<Memes> memes, ParseException e) {
                if(e != null){
                    Log.e(TAG,"Issue with Getting Memes",e);
                    return;
                }
                for(Memes meme : memes) {
                    Log.i(TAG,"Meme Name :  " + meme.getmemeName() + ". " +  "MEME URL: " + meme.getMemeURL() + ". " + " Meme Upvote: " + meme.getUpvoteVal() + ". " + "Meme Downvote: " + meme.getDownvoteVal());


                }

            }
        });

    }
}