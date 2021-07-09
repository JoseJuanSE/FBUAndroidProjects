package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;

import static androidx.core.content.ContextCompat.startActivity;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public static final String TAG = "PostAdapter";

    Context context;
    List<Post> posts;

    //Pass in the context and list of posts
    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }


    // For each row, inflate the layout
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent,false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // Get the data at position
        Post post = posts.get(position);
        //Bind the tweet with view holder
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        TextView tvUsername2;
        ImageView ivImage;
        TextView tvDescription;
        RelativeLayout rlMain;

        //Here I got all the items that I need from layout

        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUser);
            tvUsername2 = itemView.findViewById(R.id.tvUser2);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            rlMain = itemView.findViewById(R.id.rlMain);
        }
        //In this huge function what I am doing in a nutshell is to get all the data from the post
        // to push them into the item layout, and I also make the respective modifications to each
        //item with base of post's status. Also we set the click listener for the retweet and favorite actions.
        //Also here we make that the user's names, @ and timestamps fit in one line
        public void bind(Post post) {
            tvDescription.setText(post.getDescription());
            ParseFile image = post.getImage();
            if(image != null){
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
            tvUsername.setText(post.getUser().getUsername());
            tvUsername2.setText(post.getUser().getUsername());
            rlMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostDetails.class);
                    intent.putExtra("Post", Parcels.wrap(post));
                    startActivity(context, intent, new Bundle());
                }
            });
        }
    }
    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
}
