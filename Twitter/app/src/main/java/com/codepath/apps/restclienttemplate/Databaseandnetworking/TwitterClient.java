package com.codepath.apps.restclienttemplate.Databaseandnetworking;

import android.content.Context;
import android.util.Log;

import com.codepath.apps.restclienttemplate.BuildConfig;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;

//In this class we will  doing all the necessary things to handle
//the connection with the twitter's API
public class TwitterClient extends OAuthBaseClient {
	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance();
	public static final String REST_URL = "https://api.twitter.com/1.1";
	//We have this in a file call apikey.properties of course this one is exclude of git with .gitignore for security of our keys
	public static final String REST_CONSUMER_KEY = BuildConfig.CONSUMER_KEY;
	public static final String REST_CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET;

	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				null,  // OAuth2 scope, null for OAuth1
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}

	//Here we ask to api for the home timeline
	public void getHomeTimeline(JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("count",25);
		params.put("since_id", 1);
		client.get(apiUrl, params, handler);
	}

	//Here we ask to api for public a tweet, but also I use this to reply a tweet as well.
	//if we want to publish a tweet we have to send a -1 in id
	public void publishTweet(long id, String tweetContent, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("status", tweetContent);
		if (id != -1) {
			params.put("in_reply_to_status_id", id);
			Log.e("Client","REPLY!");
		}
		client.post(apiUrl, params, "",handler);
	}

	//Here we like a tweet
	public void like(Long tweetId, JsonHttpResponseHandler handler)
	{
		String apiUrl = getApiUrl("favorites/create.json");
		RequestParams params = new RequestParams();
		params.put("id",tweetId);
		client.post(apiUrl, params, "", handler);
	}

	//Here we disLike a tweet
	public void disLike(Long tweetId, JsonHttpResponseHandler handler)
	{
		String apiUrl = getApiUrl("favorites/destroy.json");
		RequestParams params = new RequestParams();
		params.put("id",tweetId);
		client.post(apiUrl, params, "", handler);
	}

	//Here we request a retweet
	public void retweet(Long tweetId, JsonHttpResponseHandler handler)
	{
		String apiUrl = getApiUrl("statuses/retweet/"+tweetId+".json");
		RequestParams params = new RequestParams();
		params.put("id",tweetId);
		client.post(apiUrl, params, "", handler);
	}

	//Here we request a unretweet
	public void unRetweet(long tweetId, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/unretweet/"+tweetId+".json");
		RequestParams params = new RequestParams();
		params.put("id",tweetId);
		client.post(apiUrl, params, "", handler);
	}

}
