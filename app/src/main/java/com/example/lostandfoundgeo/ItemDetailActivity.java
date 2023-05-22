package com.example.lostandfoundgeo;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.lostandfoundgeo.databinding.ActivityItemDetailBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ItemDetailActivity extends AppCompatActivity {
    ItemModel item;
    ActivityItemDetailBinding binding;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        item = (ItemModel) getIntent().getSerializableExtra("item");

        binding.itemisFoundText.setText(item.getIsLost().toUpperCase());
        binding.itemDescText.setText(item.getDescription());
        binding.itemDateText.setText(item.getDate());
        binding.itemLocationText.setText(item.getLocation());
        binding.itemNameText.setText(item.getName());
        binding.itemPhoneText.setText(item.getPhone());

        // show item on map button
        binding.showOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                ArrayList<ItemModel> items = new ArrayList<>();
                items.add(item);
                intent.putExtra("items", items);
                startActivity(intent);

            }
        });

        // remove item from database
        binding.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemID = String.valueOf(item.getId());
                db.collection("items").document(itemID).delete();



                Intent intent = new Intent(getApplicationContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });
    }
}