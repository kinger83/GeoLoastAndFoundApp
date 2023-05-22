package com.example.lostandfoundgeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lostandfoundgeo.databinding.ActivityMainBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayList<ItemModel> items = new ArrayList<>();
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.mainProgressBar.setVisibility(View.GONE);

        // setup db
        db = FirebaseFirestore.getInstance();

        // Create lost or found button
        binding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateItemActivity.class);
                startActivity(intent);
            }
        });

        // show all button
        binding.showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });

        // Show map button
        binding.showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get items to show on map
                populateItems();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("items", items);
                startActivity(intent);
            }
        });
    }

    // populates item list from db
    private void populateItems(){
        binding.mainProgressBar.setVisibility(View.VISIBLE);
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
                        binding.mainProgressBar.setVisibility(View.GONE);
                    }else {
                            // Handle the error
                            Toast.makeText(getApplicationContext(), "Error retrieving items", Toast.LENGTH_SHORT).show();
                            binding.mainProgressBar.setVisibility(View.GONE);
                        }
                    });

    }
}

