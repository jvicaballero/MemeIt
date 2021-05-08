package com.example.memeit;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("memes")
public class Memes extends ParseObject {
//get and set memes
    public static final String memeURL= "memeURL";
    public static final String memeName= "memeName";
    public static final String voteVal= "votes";

    public int voteVal() {
        return getInt(voteVal);
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
    public void setvoteVal(Integer upvote) {
        put(voteVal,upvote);
    }

}
