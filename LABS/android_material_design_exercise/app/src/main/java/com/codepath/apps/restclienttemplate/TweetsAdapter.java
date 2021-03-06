package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

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

        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvAtName = itemView.findViewById(R.id.tvAtName);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
        }
        //Extra: names fit inside of tweet
        //Extra: timestamp and @name with twitter design
        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            Glide.with(context).load(tweet.user.publicImageUrl).into(ivProfileImage);
            tvTimeStamp.setText( "??  "+tweet.timeStamp);
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
        }
    }
}
