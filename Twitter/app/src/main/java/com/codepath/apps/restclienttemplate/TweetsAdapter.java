package com.codepath.apps.restclienttemplate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    Context context;
    List<Tweet> tweets;

    //Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
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
        @SuppressLint("ResourceAsColor")
        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            Log.e("TweetAdapter","I'm in bind");
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
                int radius = 30; // corner radius, higher value = more rounded
                int margin = 10; // crop margin, set to 0 for corners with no crop
                Glide.with(context)
                        .load(tweet.embedUrl)
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
