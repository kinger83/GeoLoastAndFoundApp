package com.example.lostandfoundgeo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final Context context;
    private static ArrayList<ItemModel> itemList = new ArrayList<>();
    private static Activity activity;

    public ItemAdapter (Context context, ArrayList<ItemModel> itemList, Activity activity){
        this.context = context;
        this.itemList = itemList;
        this.activity = activity;
    }


    @NonNull
    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_layout, parent, false);
        return new ItemAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, int position) {
        ItemModel item = itemList.get(position);
        holder.isLost.setText(item.getIsLost().toUpperCase());
        holder.item.setText(item.getDescription());
        holder.location.setText(item.getLocation());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView isLost;
        private final TextView item;
        private final TextView location;


        public ItemViewHolder(@NonNull View itemView){
            super(itemView);
            isLost = itemView.findViewById(R.id.itemLostOrFoundText);
            item = itemView.findViewById(R.id.itemLostText);
            location = itemView.findViewById(R.id.locationLostText);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            Toast.makeText(itemView.getContext(), String.valueOf(pos), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, ItemDetailActivity.class);
            intent.putExtra("item", itemList.get(pos));
            activity.startActivity(intent);

        }
    }
}

