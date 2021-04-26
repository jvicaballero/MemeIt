package com.example.memeit;

public class Meme {
    private static final String KEY_TITLE= "title";
    private static final String KEY_IMAGE_URL= "url";
    private static final String KEY_CREATED= "created_at";

    public static String getKeyImageUrl() {
        return KEY_IMAGE_URL;
    }

    public static String getKeyCreated() {
        return KEY_CREATED;
    }

    public static String getKeyTitle() {
        return KEY_TITLE;
    }
}
