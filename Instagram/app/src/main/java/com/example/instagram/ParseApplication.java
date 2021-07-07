package com.example.instagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("0yXjwCkvTWkbx7EpRAU34jCX3SMYfttgX7gfmbWH")
                .clientKey("KS6asSUbSn50sZj7shJA3nrnEBdHpMpy5ijKzgXr")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
