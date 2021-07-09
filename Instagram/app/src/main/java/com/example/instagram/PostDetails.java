package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PostDetails extends AppCompatActivity {

    public static final String TAG = "PostDetails";

    TextView tvUsername;
    TextView tvUsername2;
    ImageView ivImage;
    TextView tvDescription;
    TextView tvDate;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        tvUsername = findViewById(R.id.tvUser);
        tvUsername2 = findViewById(R.id.tvUser2);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvDate = findViewById(R.id.tvDate);
        post = Parcels.unwrap(getIntent().getParcelableExtra("Post"));

        tvDescription.setText(post.getDescription());
        ParseFile image = post.getImage();
        if(image != null){
            Glide.with(PostDetails.this).load(image.getUrl()).into(ivImage);
        }
        tvUsername.setText(post.getUser().getUsername());
        tvUsername2.setText(post.getUser().getUsername());
        tvDate.setText(post.getDatetime());
    }
}