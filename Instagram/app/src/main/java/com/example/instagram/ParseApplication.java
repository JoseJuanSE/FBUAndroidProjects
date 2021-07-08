package com.example.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Post.class);

        // set applicationId, and server server based on the values in the back4app setting.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with te Configuration Builder given this synth
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("0yXjwCkvTWkbx7EpRAU34jCX3SMYfttgX7gfmbWH")
                .clientKey("KS6asSUbSn50sZj7shJA3nrnEBdHpMpy5ijKzgXr")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
