package com.example.memeit;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;


public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Memes.class);



        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        //use below code only if your getting networking error.
        /*
        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See https://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);
       */

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("kzzYiSAxpbJ6OLfu7HM5q9HetFUzFWshoaJm3UFh")
                .clientKey("2JN6psKEOl0CPQx6AKTdF2UAUWQr8O8MOshBKSzl")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
