package com.example.flixster.adaptors;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

import static java.lang.Math.min;

public class MovieAdaptor extends RecyclerView.Adapter<MovieAdaptor.ViewHolder>{
    Context context;
    List<Movie> movies;

    public MovieAdaptor(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    //Usually involves inflating a layout from XML and returning a holder
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    //Involves population data into the item through the holder
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //Get the movie at the passed int position
        Movie movie = movies.get(position);
        //Bind the movie data into the VH
        holder.bind(movie);
    }

    //Return the total account of items that we have
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivdPoster);
            itemView.setOnClickListener(this);
        }


        public void bind(Movie movie) {
            String title = movie.getTitle();
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                title = getCorrectSize(title,80);
            }else {
                title = getCorrectSize(title,50);
            }
            tvTitle.setText(title);

            String overview = movie.getOverview();
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                overview = getCorrectSize(overview,400);
            }else {
                overview = getCorrectSize(overview,250);
            }
            tvOverview.setText(overview);

            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                Glide.with(context).load(movie.getBackdropPath()).placeholder(R.drawable.flicks_backdrop_placeholder).error(R.drawable.flicks_backdrop_placeholder).into(ivPoster);
            }else {
                Glide.with(context).load(movie.getPosterPath()).placeholder(R.drawable.flicks_movie_placeholder).error(R.drawable.flicks_movie_placeholder).into(ivPoster);
            }
        }
    }

    private String getCorrectSize(String overview, int chars) {
        String ans = "";
        for(int i=0;i<min(overview.length(),chars);i++){
            ans = ans + overview.charAt(i);
        }
        if(overview.length() > chars)ans+="...";
        return ans;
    }
}
