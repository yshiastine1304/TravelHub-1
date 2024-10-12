package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.personal.development.travelhub.adapters.TripsAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TripsActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private TextView noTripsAdded;
    private RecyclerView trip_recyclerview;
    private TripsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        trip_recyclerview = findViewById(R.id.trips_recycler);
        noTripsAdded = findViewById(R.id.no_trips_text_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        // Initially hide the empty state message
        noTripsAdded.setVisibility(View.GONE);

        trip_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TripsAdapter(this);
        trip_recyclerview.setAdapter(adapter);

        bottomNavigationView.setSelectedItemId(R.id.nav_trip);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(TripsActivity.this, Dashboard.class));
                return true;
            } else if (itemId == R.id.nav_wishlist) {
                startActivity(new Intent(TripsActivity.this, Wishlist.class));
                return true;
            } else if (itemId == R.id.nav_trip) {
                return true;
            } else if (itemId == R.id.nav_account) {
                return true;
            } else {
                return false;
            }
        });
    }

    // Method to show or hide the empty state
    public void toggleEmptyState(boolean isEmpty) {
        if (isEmpty) {
            noTripsAdded.setVisibility(View.VISIBLE);
            trip_recyclerview.setVisibility(View.GONE);
        } else {
            noTripsAdded.setVisibility(View.GONE);
            trip_recyclerview.setVisibility(View.VISIBLE);
        }
    }
}