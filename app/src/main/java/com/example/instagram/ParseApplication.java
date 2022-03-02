package com.example.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;


public class ParseApplication extends Application {
    public static final String PARSE_APPID = BuildConfig.PARSE_APPID;
    public static final String PARSE_CLIENTID = BuildConfig.PARSE_CLIENTID;
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(PARSE_APPID)
                .clientKey(PARSE_CLIENTID)
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
