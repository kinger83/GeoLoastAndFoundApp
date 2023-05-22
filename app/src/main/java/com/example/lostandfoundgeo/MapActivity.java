package com.example.lostandfoundgeo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.lostandfoundgeo.databinding.ActivityMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    ActivityMapBinding binding;
    ArrayList<ItemModel> items = new ArrayList<>();
    GoogleMap gMap;
    FrameLayout map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set up binding
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        map = findViewById(R.id.map);
        // get item/items to show
        items = (ArrayList<ItemModel>) getIntent().getSerializableExtra("items");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // create varaibles to add lats and longs
        Double lat = Double.valueOf(0);
        Double longg =Double.valueOf(0);
        LatLng pos;

        this.gMap = googleMap;
        // loop through items and add to map + add co-ords
        for (int i = 0; i < items.size(); i++) {
            LatLng loc = new LatLng(Double.valueOf(items.get(i).getLatitude()), Double.valueOf(items.get(i).getLongitude()));
            lat += Double.valueOf(items.get(i).getLatitude());
            longg += Double.valueOf(items.get(i).getLongitude());
            this.gMap.addMarker(new MarkerOptions().position(loc).title(items.get(i).getDescription()));
        }
        //average out lat and long
        lat = lat / items.size();
        longg = longg / items.size();
        pos = new LatLng(lat,longg);

        // move camera to position
        float zoomLevel = 8.5f;
        this.gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, zoomLevel));


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}