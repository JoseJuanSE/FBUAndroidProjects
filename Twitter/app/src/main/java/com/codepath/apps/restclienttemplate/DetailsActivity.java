package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.codepath.apps.restclienttemplate.Databaseandnetworking.TwitterApp;
import com.codepath.apps.restclienttemplate.Databaseandnetworking.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.snackbar.Snackbar;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

import static androidx.core.content.ContextCompat.startActivity;

public class DetailsActivity extends AppCompatActivity {

    public static final String TAG = "DetailsActivity";

    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvScreenName;
    TextView tvAtName;
    TextView tvHour;
    TextView tvDate;
    ImageView ivContent;
    TextView countRetweets;
    TextView countLikes;
    TextView tvLikes;
    TextView tvRetweet;
    ImageView ivRetweet;
    ImageView ivHeart;
    ImageView ivReply;
    RelativeLayout rvMain;
    Tweet tweet;

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        client = TwitterApp.getRestClient(this);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvBody = findViewById(R.id.tvBody);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvAtName = findViewById(R.id.tvAtName);
        tvHour = findViewById(R.id.tvHour);
        tvDate = findViewById(R.id.tvDate);
        ivContent = findViewById(R.id.ivContent);
        countLikes = findViewById(R.id.countLikes);
        countRetweets = findViewById(R.id.countRetweets);
        ivRetweet = findViewById(R.id.ivRetweet);
        ivHeart = findViewById(R.id.ivHeart);
        ivReply = findViewById(R.id.ivReply);
        rvMain = findViewById(R.id.main_content);
        tvLikes = findViewById(R.id.tvLikes);
        tvRetweet = findViewById(R.id.tvRetweet);
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        tvBody.setText(tweet.body);
        Glide.with(DetailsActivity.this).load(tweet.user.publicImageUrl).into(ivProfileImage);
        tvHour.setText(tweet.hour+"  ·  ");
        tvDate.setText(tweet.day+"  ·  ");
        String userName = tweet.user.name;
        String userScreenName = "@"+tweet.user.screenName;
        int maxCharInView = 32;
        if (userName.length() > maxCharInView) {
            userName = userName.substring(0,maxCharInView)+"...";
            userScreenName = "";
        } else if(userName.length() + userScreenName.length() > maxCharInView) {
            userScreenName = userScreenName.substring(0,maxCharInView-userName.length()-3)+"...";
        }
        tvScreenName.setText(userName);
        tvAtName.setText(userScreenName);
        if (!tweet.embedUrl.isEmpty()) {
            Glide.with(DetailsActivity.this)
                    .load(tweet.embedUrl)
                    .transform(new MultiTransformation(new FitCenter(), new RoundedCornersTransformation(40,5)))
                    .into(ivContent);
            ivContent.setVisibility(View.VISIBLE);
        }else{
            ivContent.setMaxHeight(1);
            ivContent.setVisibility(View.GONE);
        }
        countLikes.setText(String.valueOf(tweet.favorite_count));
        if (tweet.favorite_count > 0) {
            countLikes.setVisibility(View.VISIBLE);
            tvLikes.setVisibility(View.VISIBLE);
            if (tweet.favorited == true) {
                ivHeart.setColorFilter(ContextCompat.getColor(DetailsActivity.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                ivHeart.setColorFilter(ContextCompat.getColor(DetailsActivity.this, R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        } else {
            countLikes.setVisibility(View.GONE);
            tvLikes.setVisibility(View.GONE);
        }
        countRetweets.setText(String.valueOf(tweet.retweet_count));
        if (tweet.retweet_count > 0) {
            countRetweets.setVisibility(View.VISIBLE);
            tvRetweet.setVisibility(View.VISIBLE);
            if (tweet.retweeted == true) {
                ivRetweet.setColorFilter(ContextCompat.getColor(DetailsActivity.this, R.color.correct), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                ivRetweet.setColorFilter(ContextCompat.getColor(DetailsActivity.this, R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        } else {
            countRetweets.setVisibility(View.GONE);
            tvRetweet.setVisibility(View.GONE);
        }
        //The following two functions are so similar, basically both does the same but with a different item.
        //We set a click listener for every retweet and favorite
        //In these, what we ar doing, is a request of post for twitter's servers
        //for do or undo any retweet or add to favorites.
        //These request needs tweet id and a handler
        //In these handlers we set up what to do in case of success and on failure
        //on failure easily we just log the error
        //In case of success we modify the color of icons, the count of any and the status of that in the tweet
        ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tweet.favorited) {
                    client.like(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {

                            tweet.favorite_count++;
                            countLikes.setText(String.valueOf(tweet.favorite_count));
                            countLikes.setVisibility(View.VISIBLE);
                            tvLikes.setVisibility(View.VISIBLE);
                            tweet.favorited = true;
                            ivHeart.setColorFilter(ContextCompat.getColor(DetailsActivity.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                            Snackbar.make(view,"Favorite!", Snackbar.LENGTH_LONG).show();
                        }
                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "error in favorite "+response );
                        }
                    });
                }
                else {
                    client.disLike(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {

                            tweet.favorite_count--;
                            countLikes.setText(String.valueOf(tweet.favorite_count));
                            tweet.favorited = false;
                            if (tweet.favorite_count == 0) {
                                countLikes.setVisibility(View.GONE);
                                tvLikes.setVisibility(View.GONE);
                            }
                            ivHeart.setColorFilter(ContextCompat.getColor(DetailsActivity.this, R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
                            Snackbar.make(view,"tweet removed of favorites", Snackbar.LENGTH_LONG).show();
                        }
                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "error in favorite " + response);
                        }
                    });
                }
            }
        });

        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tweet.retweeted) {
                    client.retweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {

                            tweet.retweet_count++;
                            countRetweets.setVisibility(View.VISIBLE);
                            tvRetweet.setVisibility(View.VISIBLE);
                            countRetweets.setText(String.valueOf(tweet.retweet_count));
                            tweet.retweeted = true;
                            ivRetweet.setColorFilter(ContextCompat.getColor(DetailsActivity.this, R.color.correct), android.graphics.PorterDuff.Mode.SRC_IN);
                            Snackbar.make(view,"Retweet!", Snackbar.LENGTH_LONG).show();
                        }
                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "failure " + response);
                        }
                    });
                }else{
                    client.unRetweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {

                            tweet.retweet_count--;
                            countRetweets.setText(String.valueOf(tweet.retweet_count));
                            if(tweet.retweet_count == 0){
                                countRetweets.setVisibility(View.GONE);
                                tvRetweet.setVisibility(View.GONE);
                            }
                            tweet.retweeted = false;
                            ivRetweet.setColorFilter(ContextCompat.getColor(DetailsActivity.this, R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
                            Snackbar.make(view,"Unretweeted", Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "failure" + response);
                        }
                    });
                }
            }
        });
        //TODO: solve this bug
        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, ComposeActivity.class);
                intent.putExtra("tweet", Parcels.wrap(tweet));
                startActivity(DetailsActivity.this,intent,new Bundle());
            }
        });
    }
}