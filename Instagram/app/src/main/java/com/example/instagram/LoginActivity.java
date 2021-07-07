package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

//Here we control the logIn activity
public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    private EditText etUser;
    private EditText etPassword;
    private Button btnLogin;


    //TODO: if there is time when keyboard displays, drag all above keyboard
    //TODO: if there is time, make button log in just available when both fields are not empty
    //TODO: if there is time copy the complete design
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // If we are already log in, we have to skip the log in.
        if( ParseUser.getCurrentUser() != null) {
            goToMainActivity();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUser.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(userName, password);
            }
        });
    }

    private void loginUser(String userName, String password) {
        Log.i(TAG, "User " + userName + " trying to login...");
        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Problem logIn ", e);
                    return;
                } else {
                    goToMainActivity();
                    Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                }
            }
            //While testing this, I found the error "Go to the documentation to learn how to Fix dependency resolution errors."
            //with a lot of classes duplicated, it was not easy to find the solution but the answer is
            //write android.useAndroidX=true
            //android.enableJetifier=true in grandle.properties.
        });
    }

    //In this function we just do an intent to call the MainActivity on success case
    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //We finish the activity here in order to avoid that user return to login once the user is logged
        finish();
    }
}