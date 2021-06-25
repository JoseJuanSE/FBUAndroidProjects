package com.example.flixster.models;

import com.facebook.stetho.inspector.jsonrpc.JsonRpcException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {
    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Double note;

    public Movie(){

    }

    public Movie(JSONObject jsonObject) throws JSONException{
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        note = jsonObject.getDouble("vote_average");
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }
    //TODO: obtener los valores de tamaños con la api de configuracion del video uno
    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/%s/%s","w342",posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/%s/%s","w342",backdropPath);
    }

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
