package com.codepath.apps.restclienttemplate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.Databaseandnetworking.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    public static final String TAG = "TweetsAdapter";

    Context context;
    List<Tweet> tweets;
    TwitterClient client;


    //Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets, TwitterClient client) {
        this.context = context;
        this.tweets = tweets;
        this.client = client;
    }


    // For each row, inflate the layout
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent,false);
        return new ViewHolder(view);
    }
    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);
        //Bind the tweet with view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvAtName;
        TextView tvTimeStamp;
        ImageView ivContent;
        TextView countRetweets;
        TextView countLikes;
        ImageView ivRetweet;
        ImageView ivHeart;

        //Here I got all the items that I need from layout

        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvAtName = itemView.findViewById(R.id.tvAtName);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            ivContent = itemView.findViewById(R.id.ivContent);
            countLikes = itemView.findViewById(R.id.countLikes);
            countRetweets = itemView.findViewById(R.id.countRetweets);
            ivRetweet = itemView.findViewById(R.id.ivRetweet);
            ivHeart = itemView.findViewById(R.id.ivHeart);
        }
        //Extra: names fit inside of tweet
        //Extra: timestamp and @name with twitter design
        // TODO: if there is time, add color blue to # and @

        //In this huge function what I am doing in a nutshell is to get all the data from the tweet
        // to push them into the tweetview, and I also make the respective modifications to each
        //item with base of tweet's status. Also we set the click listener for the retweet and favorite actions.

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            Glide.with(context).load(tweet.user.publicImageUrl).into(ivProfileImage);
            tvTimeStamp.setText( "·  "+tweet.timeStamp);
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
                Glide.with(context)
                        .load(tweet.embedUrl)
                        .transform(new MultiTransformation(new FitCenter(), new RoundedCornersTransformation(40,10)))
                        .into(ivContent);
                ivContent.setVisibility(View.VISIBLE);
            }else{
                ivContent.setMaxHeight(1);
                ivContent.setVisibility(View.GONE);
            }
            if (tweet.favorite_count > 0) {
                countLikes.setVisibility(View.VISIBLE);
                countLikes.setText(String.valueOf(tweet.favorite_count));
                if (tweet.favorited == true) {
                    ivHeart.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                    countLikes.setTextColor(ContextCompat.getColor(context, R.color.red));
                } else {
                    ivHeart.setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
                    countLikes.setTextColor(ContextCompat.getColor(context, R.color.gray));
                }
            } else {
                countLikes.setVisibility(View.INVISIBLE);
            }
            if (tweet.retweet_count > 0) {
                countRetweets.setVisibility(View.VISIBLE);
                countRetweets.setText(String.valueOf(tweet.retweet_count));
                if (tweet.retweeted == true) {
                    ivRetweet.setColorFilter(ContextCompat.getColor(context, R.color.correct), android.graphics.PorterDuff.Mode.SRC_IN);
                    countRetweets.setTextColor(ContextCompat.getColor(context, R.color.correct));
                } else {
                    ivRetweet.setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
                    countRetweets.setTextColor(ContextCompat.getColor(context, R.color.gray));
                }
            } else {
                countRetweets.setVisibility(View.INVISIBLE);
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

                                int nLikes = Integer.valueOf(countLikes.getText().toString());
                                countLikes.setText(String.valueOf(nLikes+1));
                                tweet.favorited = true;
                                tweet.favorite_count++;
                                ivHeart.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                                countLikes.setTextColor(ContextCompat.getColor(context, R.color.red));
                                View parentLayout;
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

                                int nLikes = Integer.valueOf(countLikes.getText().toString());
                                countLikes.setText(String.valueOf(nLikes-1));
                                tweet.favorited = false;
                                tweet.favorite_count--;
                                ivHeart.setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
                                countLikes.setTextColor(ContextCompat.getColor(context, R.color.gray));
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

                                int retweets = Integer.valueOf(countRetweets.getText().toString());
                                countRetweets.setText(String.valueOf(retweets+1));
                                tweet.retweet_count++;
                                tweet.retweeted = true;
                                ivRetweet.setColorFilter(ContextCompat.getColor(context, R.color.correct), android.graphics.PorterDuff.Mode.SRC_IN);
                                countRetweets.setTextColor(ContextCompat.getColor(context, R.color.correct));
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

                                int retweets = Integer.valueOf(countRetweets.getText().toString());
                                countRetweets.setText(String.valueOf(retweets-1));
                                tweet.retweet_count--;
                                tweet.retweeted = false;
                                ivRetweet.setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
                                countRetweets.setTextColor(ContextCompat.getColor(context, R.color.gray));
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

        }
    }
    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }
}
