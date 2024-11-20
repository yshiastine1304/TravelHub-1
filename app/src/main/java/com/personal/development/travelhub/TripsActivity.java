package com.personal.development.travelhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.personal.development.travelhub.adapters.TripsAdapter;
import com.personal.development.travelhub.models.TourSaveModel;

import java.util.ArrayList;
import java.util.List;

public class TripsActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private TextView noTripsAdded;
    private RecyclerView trip_recyclerview;
    private TripsAdapter adapter;

    // List to store trips
    private List<TourSaveModel> tripList;

    // Firebase Firestore
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String uid;

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

        // Initialize the Firebase Firestore instance
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid(); // Get current user's UID

        // Initialize the trips list
        tripList = new ArrayList<>();

        // Initialize the adapter
        adapter = new TripsAdapter(this, tripList);
        trip_recyclerview.setAdapter(adapter);

        // Fetch trips data from Firestore
        fetchTripsData();

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
                startActivity(new Intent(TripsActivity.this, Profile.class));
                return true;
            } else {
                return false;
            }
        });
    }

    // Method to fetch trip data from Firestore
    private void fetchTripsData() {
        // Reference to the "saved_tour" collection
        CollectionReference saveTourRef = db.collection("users")
                .document(uid)
                .collection("trips");

        // Clear existing data to avoid duplicates
        tripList.clear();
        adapter.resetDisplayedDateRanges();

        // Fetch the trips
        saveTourRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    // Get the data from each document
                    String dateRange = document.getString("dateRange");
                    String tourName = document.getString("tourName");

                    // Create a TourSaveModel object with the retrieved data
                    TourSaveModel tour = new TourSaveModel(dateRange, tourName);

                    // Add the tour to the list
                    tripList.add(tour);
                }

                // Notify the adapter that the data has been updated
                adapter.notifyDataSetChanged();

                // Update the empty state visibility
                toggleEmptyState(tripList.isEmpty());
            } else {
                // Handle case when there are no trips
                toggleEmptyState(true);
            }
        }).addOnFailureListener(e -> {
            // Handle any errors
            toggleEmptyState(true);
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


