package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGHT = 140;
    EditText tvTweet;
    Button btnTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        tvTweet = findViewById(R.id.tvTweet);
        btnTweet = findViewById(R.id.btnTweet);

        //Set click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = tvTweet.getText().toString();
                //Make an Api call to Twitter  publish the tweet
                if (content.isEmpty()) {
                    Toast.makeText(ComposeActivity.this,"Sorry, your Tweet cannot be empty",Toast.LENGTH_LONG).show();
                } else if(content.length() > MAX_TWEET_LENGHT) {
                    Toast.makeText(ComposeActivity.this,"Sorry, your Tweet is too long",Toast.LENGTH_LONG).show();
                } else {

                }
            }
        });
    }
}