package com.example.simple_todo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

//Responsible for displaying data from the model into a row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    //We define this interface to be able of implement this method in the main activity
    //in that particular object.
    public interface OnLongClickListener{
        void onItemLongClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;

    //We initialized our object's attributes using this constructor
    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
    }

    //We return the view that we just created to hold all the items in that recycler view
    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup viewGroup, int i) {
        //Use layout inflater to inflate a view
        View todoView = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_1,viewGroup,false);
        //We return the view that we inflated
        return new ViewHolder(todoView);
    }
    //responsible for binding data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull ItemsAdapter.ViewHolder viewHolder, int i) {
        //Grab the item at the position
        String item = items.get(i);
        // Bind the item into the specified view holder
        viewHolder.bind(item);
    }

    //Tells the RV how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvItem;

        public ViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }
        //Update the view inside of the view holder with this data
        public void bind(String item){
            tvItem.setText(item);
            tvItem.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    // Notify adapter that an item is inserted
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
