package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class ComposeActivity extends AppCompatActivity {
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
                    Snackbar.make(view, "Sorry, your Tweet cannot be empty", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if(content.length() > 140) {
                    Snackbar.make(view, "Sorry, your Tweet is too long", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {

                }
            }
        });
    }
}