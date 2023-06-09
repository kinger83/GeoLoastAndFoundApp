package com.example.lostandfoundgeo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lostandfoundgeo.databinding.ActivityShowAllBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ShowAllActivity extends AppCompatActivity {
    ActivityShowAllBinding binding;
    FirebaseFirestore db;
    ArrayList<ItemModel> items = new ArrayList<>();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set up binding
        binding = ActivityShowAllBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;
        binding.listProgressBar.setVisibility(View.GONE);
        // setup database
        db = FirebaseFirestore.getInstance();

        // set add new item button
        binding.addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateItemActivity.class);
                startActivity(intent);
            }
        });

        // add all items from db to list
        populateItems();
    }

    private void populateItems(){
// set progress bar visible
        binding.listProgressBar.setVisibility(View.VISIBLE);
        // retrieve all urls from user
        db.collection("items")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // if successful, load urls into url list for recycler view
                        items = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String name = document.getString("name");
                            String phone = document.getString("phone");
                            String desc = document.getString("description");
                            String date = document.getString("date");
                            String location = document.getString("location");
                            String isLost = document.getString("radio");
                            String lat = document.getString("lat");
                            String longg = document.getString("long");
                            ItemModel item = new ItemModel(id, name, phone, desc, date, location, isLost, lat, longg);
                            items.add(item);
                        }

                        // setup and display recycler view
                        binding.itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                        ItemAdapter itemAdapter = new ItemAdapter(this, items, ShowAllActivity.this);
                        binding.itemRecyclerView.setAdapter(itemAdapter);
                        binding.listProgressBar.setVisibility(View.GONE);
                    } else {
                        // Handle the error
                        Toast.makeText(getApplicationContext(), "Error retrieving items", Toast.LENGTH_SHORT).show();
                        binding.listProgressBar.setVisibility(View.GONE);
                    }
                });

    }
}
