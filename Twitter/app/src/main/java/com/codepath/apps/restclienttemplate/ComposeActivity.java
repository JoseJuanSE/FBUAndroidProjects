package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.Databaseandnetworking.TwitterApp;
import com.codepath.apps.restclienttemplate.Databaseandnetworking.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

//In this class we mainly control what we use while we are making a tweet
//We get all the necessary items to get the tweet data that we will send
//As well as do the necessary to send them.
public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGHT = 140;
    public static final String TAG = "ComposeActivity";

    EditText tvTweet;
    Button btnTweet;
    TwitterClient client;

    //In this function, we do a lot of things, we start the activity choosing the layout
    //we get the necessary items and we set the click listener to upload the tweet
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        tvTweet = findViewById(R.id.tvTweet);
        btnTweet = findViewById(R.id.btnTweet);
        client = TwitterApp.getRestClient(this);

        //Set click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = tvTweet.getText().toString();
                //Make an Api call to Twitter to publish the tweet
                if (content.isEmpty()) {
                    Toast.makeText(ComposeActivity.this,"Sorry, your Tweet cannot be empty",Toast.LENGTH_LONG).show();
                } else if(content.length() > MAX_TWEET_LENGHT) {
                    Toast.makeText(ComposeActivity.this,"Sorry, your Tweet is too long",Toast.LENGTH_LONG).show();
                } else {
                    client.publishTweet(content, new JsonHttpResponseHandler() {
                        //In this function if we succeed, then we will return the data that we got here to timeline,
                        //and the program returns to timeline too. From there, we sent the post request we the API
                        //through intents, to pass to timelime, we finish this activity
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG,"onSuccess to publish tweet");
                            try{
                                Tweet tweet = Tweet.fromJson(json.jsonObject);
                                Log.i(TAG, "Published tweet says: " + tweet);
                                Intent intent = new Intent();
                                intent.putExtra("tweet", Parcels.wrap(tweet));
                                setResult(RESULT_OK, intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure to publish tweet", throwable);
                        }
                    });
                }
            }
        });
    }
}