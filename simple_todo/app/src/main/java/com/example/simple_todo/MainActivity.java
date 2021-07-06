package com.example.simple_todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

// In this class we handle the main activity (include the button, edit text and the recycler view)
// First we get the items that we are going to use, we also set our items adapter,
// as well as set the actions for every item
public class MainActivity extends AppCompatActivity {

    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItem;

    ItemsAdapter itemsAdapter;

    //This method is the first that is going to run when this activity begins.
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItem = findViewById(R.id.rvItem);

        //Here we get all the items that are in memory, we load those items to the list
        //And those are what we are going to display using the recycler view through adarter
        loadItems();

        //Here we set what will happen when we press for a long time an item.
        //Basically we erase this item from list and we no longer display it.
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                //Delete the item from the model
                items.remove(position);
                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Item was removed",Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rvItem.setAdapter(itemsAdapter);
        rvItem.setLayoutManager(new LinearLayoutManager(this));

        //Here we set what our button will do when we press it.
        //basically will add the value of editview to the task list
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                //Add item to the model
                items.add(todoItem);
                //Notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size()-1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(),"Item was added",Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }
    //Here we get the file to store the list in memory in order that our application has persistence (store the data even if the app is close)
    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");
    }
    //This function will load items by reading every line of the data file
    private void loadItems(){
        try {
            items = new ArrayList<>(org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items",e);
            items = new ArrayList<>();
        }
    }
    //This function saves items by writing them into the data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writting items",e);
        }
    }
}