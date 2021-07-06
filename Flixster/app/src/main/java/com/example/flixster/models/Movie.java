package com.example.flixster.models;

import com.facebook.stetho.inspector.jsonrpc.JsonRpcException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

//We added this Parcel tag and the empty class to make the objects of this class parcelables

//This class is our model for a movie.

@Parcel
public class Movie {

    private String backdropPath;
    private String posterPath;
    private String title;
    private String overview;
    private Double note;

    public Movie(){

    }

    // Here we get the data given from the jsonObject given from the API.
    // and we put that data into the attributes of this class
    public Movie(JSONObject jsonObject) throws JSONException{
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        note = jsonObject.getDouble("vote_average");
    }

    //Due that the API can return to us a JsonArray we can use this array to fill our list.
    //But to do this we need to convert each object. For that we use the function above this one.
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }
    //Here we return the poster path with the given data and a static size value
    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/%s/%s","w342",posterPath);
    }

    //Here we return the backdrop path with the given data and a static size value
    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/%s/%s","w342",backdropPath);
    }

    //The 3 functions below, are basically just getters.
    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Double getNote() {
        return note;
    }
}
