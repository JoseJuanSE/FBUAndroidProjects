package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private RecyclerView rvPosts;
    protected  PostAdapter adapter;
    protected List<Post> allPosts;
    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Support swipecontainer
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        rvPosts = findViewById(R.id.rvPosts);

        // initialize the array that will hold posts and create a PostsAdapter
        allPosts = new ArrayList<>();
        adapter = new PostAdapter(this, allPosts);

        rvPosts.setAdapter(adapter);

        rvPosts.setLayoutManager(new LinearLayoutManager(this));

        queryPosts();

    }

    private void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts: " + e);
                    return ;
                }
                //For debugging purposes
                for (Post post : objects){
                    Log.i(TAG, "Post added with des: " + post.getDescription()+ " username: " + post.getUser().getUsername());
                }
                allPosts.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }

    //Here we display  the the logout button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activitymain,menu);
        MenuItem logoutItem = menu.findItem(R.id.id_Logout);
        MenuItem photoItem = menu.findItem(R.id.id_Photo);
        logoutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            // Here we logout the user, and go again to login activity
            public boolean onMenuItemClick(MenuItem menuItem) {
                ParseUser.logOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return false;
            }
        });
        //Here we go to TakePhoto activity
        photoItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, TakePhotoActivity.class);
                startActivity(intent);
                return false;
            }
        });
        return true;
    }

    // TODO: create just one functio for duplicated code
    public void fetchTimelineAsync(int page) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts: " + e);
                    return ;
                }
                allPosts.clear();
                allPosts.addAll(objects);
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
    }

}