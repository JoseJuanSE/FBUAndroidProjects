package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.codepath.apps.restclienttemplate.Databaseandnetworking.TwitterApp;
import com.codepath.apps.restclienttemplate.Databaseandnetworking.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Headers;

// In this class

public class TimelineActivity extends AppCompatActivity implements OnProgressBarListener {

    public static final String TAG = "TimelineActivity";
    public static final int REQUEST_CODE = 20;
    private Timer timer;

    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private NumberProgressBar npb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateHomeTimeLine();
                swipeContainer.setRefreshing(false);
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = findViewById(R.id.addTweet);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
                Tweet tweet = new Tweet();
                tweet.id = -1;
                intent.putExtra("tweet", Parcels.wrap(tweet));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        client = TwitterApp.getRestClient(this);

        //Number progress bar
        npb = (NumberProgressBar)findViewById(R.id.number_progress_bar);
        npb.setOnProgressBarListener(this);
        // Find the recycler view
        rvTweets = findViewById(R.id.rvTweets);
        // Init the list of tweets and adapter
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this,tweets,client);
        // Recycler view setup: layout manager and the adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(adapter);
        populateHomeTimeLine();
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        getHundred();
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // Remember to CLEAR OUT old items before appending in the new ones
                adapter.clear();
                // ...the data has come back, add new items to your adapter...
                Log.i(TAG,"onSuccess!");
                JSONArray jsonArray = json.jsonArray;
                try{
                    tweets.addAll(Tweet.fromJsonArray(jsonArray));
                    adapter.notifyDataSetChanged();
                }catch (JSONException | ParseException e){
                    Log.e(TAG, "Json exception: "+e);
                }
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
                npb.setProgress(100);
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("DEBUG", "Fetch timeline error: " + throwable.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //Get data form intent (tweet)
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            //Update RV
            //Modify data source
            tweets.add(0, tweet);
            //Update adapter
            adapter.notifyItemInserted(0);
            rvTweets.smoothScrollToPosition(0);
            Snackbar.make(rvTweets,"Tweet posted!", Snackbar.LENGTH_LONG).setDuration(2000).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //In this we fill the recycler view with the tweets that we get from API
    private void populateHomeTimeLine() {
        // This function is for progressBar
        getHundred();
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG,"onSuccess!");
                JSONArray jsonArray = json.jsonArray;
                try{
                    tweets.addAll(Tweet.fromJsonArray(jsonArray));
                    adapter.notifyDataSetChanged();
                }catch (JSONException | ParseException e){
                    Log.e(TAG, "Json exception: "+e);
                }
                npb.setProgress(100);
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG,"onFailure!",throwable);
            }
        });
    }
    //Here we display  the the logout button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.timelime,menu);
        MenuItem logoutItem = menu.findItem(R.id.id_Logout);
        logoutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                client.clearAccessToken(); // forget who's logged in
                finish(); // navigate backwards to Login screen
                return false;
            }
        });
        return true;
    }
    //This function is call when the progress bar is done. Then here we hide the progress bar
    //and we left it ready for the next time, setting it to 0.
    @Override
    public void onProgressChange(int current, int max) {
        if(current == max){
            npb.setProgress(0);
            npb.setVisibility(View.GONE);
        }
    }
    // Here we show the progress bar in another thread while the data is being pull.
    // That information should be load before this finish. Then how the other process will
    // Finish first, there we set a progress of 100%, and that automatically call the function above this one
    //That one will hide the progress bar and set it to 0 to be ready for the next time.
    void getHundred(){
        npb.setVisibility(View.VISIBLE);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        npb.incrementProgressBy(1);
                    }
                });
            }
        }, 1000, 100);
    }
}