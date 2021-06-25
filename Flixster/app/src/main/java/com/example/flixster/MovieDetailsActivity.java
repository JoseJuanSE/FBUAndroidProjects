package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;

    TextView tvdTitle;
    TextView tvdDescription;
    ImageView ivdPoster;
    RatingBar rbStars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        tvdDescription = findViewById(R.id.tvdDescription);
        tvdTitle = findViewById(R.id.tvdTitle);
        ivdPoster = findViewById(R.id.ivdPoster);
        rbStars = findViewById(R.id.rbStars);

        tvdDescription.setText(movie.getOverview());
        tvdTitle.setText(movie.getTitle());
        rbStars.setRating((float) (movie.getNote() / 2.0f));
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Glide.with(this).load(movie.getBackdropPath()).placeholder(R.drawable.flicks_backdrop_placeholder).error(R.drawable.flicks_backdrop_placeholder).into(ivdPoster);
        }else {
            Glide.with(this).load(movie.getPosterPath()).placeholder(R.drawable.flicks_movie_placeholder).error(R.drawable.flicks_movie_placeholder).into(ivdPoster);
        }
        Log.i("MovieDetailsActivity","Results: "+movie.toString());
    }
}