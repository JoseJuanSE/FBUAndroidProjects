package com.codepath.debuggingchallenges.activities;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.debuggingchallenges.R;

public class ToolbarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        Log.e("Error Finder","FIRST");
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Log.e("Error Finder","Second");
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        //setActionBar(toolbar);
        setSupportActionBar(toolbar);
        Log.e("Error Finder","third");
        TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvDescription.setText(R.string.hello);
        Log.e("Error Finder","four");
    }
}
