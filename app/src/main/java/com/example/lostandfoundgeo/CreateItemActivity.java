package com.example.lostandfoundgeo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lostandfoundgeo.databinding.ActivityCreateItemBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateItemActivity extends AppCompatActivity {
    FirebaseFirestore db;
    ActivityCreateItemBinding binding;
    String name, phone, description, date, location, radio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set up binding
        binding = ActivityCreateItemBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.createProgressBar.setVisibility(View.GONE);

        // setup db
        db = FirebaseFirestore.getInstance();

        // set lost or found radio click
        binding.lostFoundRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.lostRadioButton){
                    radio = "lost";
                }
                if(checkedId == R.id.foundRadioButton){
                    radio = "found";
                }
            }
        });

        // get current location button
        binding.getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // save button
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save inputs to strings;
               setInputs();
               // check inputs are valid
                if(!validateInput()){
                    return;
                }

                // save to database
                addToDB();

            }
        });
    }// end onCreate

    // set user inputs to strings
    private void setInputs(){
        name = binding.nameEditText.getText().toString();
        phone = binding.phoneEditText.getText().toString();
        description = binding.descriptioneditText.getText().toString();
        date = binding.dateEditText.getText().toString();
        location = binding.locationEditText.getText().toString();
    }

    // Validate user inputs
    private Boolean validateInput(){
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter you phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(description)){
            Toast.makeText(this, "Please describe the item", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(date)){
            Toast.makeText(this, "Please enter the date you lost/found the item", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(location)){
            Toast.makeText(this, "Please enter the location you lost/found the item", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(radio)){
            Toast.makeText(this, "Please click if item is lost or found", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // add item to database
    private void addToDB(){
        binding.createProgressBar.setVisibility(View.VISIBLE);
        // create item map to send to db
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("name", name);
        itemMap.put("phone", phone);
        itemMap.put("description", description);
        itemMap.put("date", date);
        itemMap.put("location", location);
        itemMap.put("radio", radio);

        // send to db
        db.collection("items").add(itemMap)
                // If add successful.
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // alert user successful add
                        Toast.makeText(CreateItemActivity.this, "Item added", Toast.LENGTH_SHORT).show();
                        binding.createProgressBar.setVisibility(View.GONE);
                        // return user to main page
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                })
                // if failed
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // alert user that add failed
                        binding.createProgressBar.setVisibility(View.GONE);
                        Toast.makeText(CreateItemActivity.this, "ERROR ADDING ITEM", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}