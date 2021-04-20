package com.example.memeit;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("memes")
public class Memes extends ParseObject {
//get and set memes
    public static final String memeURL= "memeURL";
    public static final String memeName= "memeName";
    public static final String UpvoteVal= "UpvoteVal";
    public static final String DownvoteVal= "DownvoteVal";

    public int getUpvoteVal() {
        return getInt(UpvoteVal);
    }
    public int getDownvoteVal() {
        return getInt(DownvoteVal);
    }
    public String getMemeURL(){
        return getString(memeURL);
    }
    public String getmemeName(){
        return getString(memeName);
    }


    public void setMemeURL(String link){
        put(memeURL,link);
    }
    public void setmemeName(String name) {
        put(memeName,name);
    }
    public void setUpvoteVal(String upvote) {
        put(memeName,upvote);
    }
    public void setDownvoteVal(String downvote) {
        put(memeName,downvote);
    }

}
