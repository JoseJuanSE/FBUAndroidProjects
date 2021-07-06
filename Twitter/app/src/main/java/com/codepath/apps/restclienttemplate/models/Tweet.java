package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// This class is the model of a tweet, here we are going to
// save all the data that we will need.
@Parcel
public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public String timeStamp;
    public String embedUrl;
    public int favorite_count;
    public int retweet_count;
    public long id;
    public boolean retweeted;
    public boolean favorite;
    public String hour;
    public String day;

    //In the following lines we will transform the time keep by tweeter, in what we see in the real tweeter, that's what
    //we are going to need

    //---get_time_stamp
    public static final int SECOND_MILLIS = 1000;
    public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    public static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    public static final String TAG = "Tweet";

    //This constructor is needed for Parcel (make serializable a custom element)
    public Tweet(){

    }

    //We return a timestamp such like 5m, 1h, 3d, just now.
    public String getRelativeTimeAgo(String rawJsonDate) {

        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        try {

            long time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 60 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + "m";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + "h";
            } else {
                return diff / DAY_MILLIS + "d";
            }

        } catch (ParseException e) {

            Log.i(TAG, "getRelativeTimeAgo failed");
            e.printStackTrace();

        }

        return "";
    }

    //The following to functions are performed just for the detail view of a tweet.

    //We return the hour and minutes when this tweet was created.
    public String getRelativeHour(String rawJsonDate) throws ParseException {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        Date date = new SimpleDateFormat(twitterFormat).parse(rawJsonDate);
        SimpleDateFormat f = new SimpleDateFormat("HH:mm");
        return f.format(date);
    }
    //We return the hour and minutes when this tweet was created.
    public String getRelativeDay(String rawJsonDate) throws ParseException {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        Date date = new SimpleDateFormat(twitterFormat).parse(rawJsonDate);
        SimpleDateFormat f = new SimpleDateFormat("MM/dd/yy");
        return f.format(date);
    }

    //Here we recive the jsonObject that API send us, and we push all the needed date
    //into the properties of this class (body, createdAt, user (another custom object), etc...)
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException, ParseException {

        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.timeStamp = tweet.getRelativeTimeAgo(jsonObject.getString("created_at"));
        tweet.hour = tweet.getRelativeHour(jsonObject.getString("created_at"));
        tweet.day = tweet.getRelativeDay(jsonObject.getString("created_at"));
        tweet.retweet_count = jsonObject.getInt("retweet_count");
        tweet.favorite_count = jsonObject.getInt("favorite_count");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.favorite = jsonObject.getBoolean("favorited");
        tweet.id = jsonObject.getLong("id");
        //Here we do this check to avoid received void objects parameter that could make
        //crash our program when pushing this data into our ImageView using Glade.
        if (!jsonObject.isNull("extended_entities")) {
            tweet.embedUrl = jsonObject
                    .getJSONObject("extended_entities")
                    .getJSONArray("media")
                    .getJSONObject(0)
                    .getString("media_url_https");
        } else {
            tweet.embedUrl = "";
        }

        return tweet;
    }
    //Here just easily traverse an array transforming each of its elements in a tweet
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException, ParseException {

        List<Tweet> tweets = new ArrayList<>();

        for (int i=0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }

        return tweets;
    }
}
