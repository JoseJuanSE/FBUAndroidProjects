package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

//We use this class to model a user and keep in this class all the data that we need of a user.
@Parcel
public class User {

    public String name;
    public String screenName;
    public String publicImageUrl;

    //This constructor is just needed because of parcel(used to make serializable a custom object and used it in intents)
    public User(){

    }

    public User(String x){
        name = x;
        screenName = x;
        publicImageUrl = "";
    }
    //Here we just populete our properties with the data given in the jsonObject that API gave us
    public static User fromJson(JSONObject jsonObject) throws JSONException {

        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.publicImageUrl = jsonObject.getString("profile_image_url_https");

        return user;
    }
}
