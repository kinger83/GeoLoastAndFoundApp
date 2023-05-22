package com.example.lostandfoundgeo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lostandfoundgeo.databinding.ActivityCreateItemBinding;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import android.location.Location;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.Manifest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

public class CreateItemActivity extends AppCompatActivity {
    FirebaseFirestore db;
    ActivityCreateItemBinding binding;
    String name, phone, description, date, location, radio, latitude, longitude;

    public static final int REQUEST_LOCATION_PERMISSION = 1;
    FusedLocationProviderClient fusedLocationProviderClient;

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

        //setup location client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // set lost or found radio click
        binding.lostFoundRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.lostRadioButton) {
                    radio = "lost";
                }
                if (checkedId == R.id.foundRadioButton) {
                    radio = "found";
                }
            }
        });

        // get current location button
        binding.getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for location permissions
                if (ActivityCompat.checkSelfPermission(CreateItemActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(CreateItemActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    // Request location permissions
                    ActivityCompat.requestPermissions(CreateItemActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_LOCATION_PERMISSION);
                    return;
                }
                // get location
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            // if location found
                            @Override
                            public void onSuccess(Location location) {
                                if(location != null){
                                    // setup geocoder and create address list
                                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                    List<Address> addresses = null;
                                    try {
                                        //add geocoded address to address list
                                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    // set lat/long/userview from address
                                    latitude = String.valueOf(addresses.get(0).getLatitude());
                                    longitude = String.valueOf(addresses.get(0).getLongitude());
                                    binding.locationEditText.setText(addresses.get(0).getAddressLine(0));

                                }
                            }
                        });


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
        itemMap.put("lat", latitude);
        itemMap.put("long", longitude);

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
