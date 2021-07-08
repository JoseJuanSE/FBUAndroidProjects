package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

// In this class we will be calling and getting all the necessary resources to take a photo send that data to someplace
// In order to be store in our data base
public class TakePhotoActivity extends AppCompatActivity {

    public static final String TAG = "TakePhotoActivity";

    private EditText etDescription;
    private Button btnPicture;
    private Button btnSubmit;
    private ImageView ivPostImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        etDescription = findViewById(R.id.etDescription);
        btnPicture = findViewById(R.id.btnTakePicture);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivPostImage = findViewById(R.id.ivPostImage);
        ivPostImage.setVisibility(View.INVISIBLE);

        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}