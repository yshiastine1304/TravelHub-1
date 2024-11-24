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
import com.personal.development.travelhub.adapters.TourSavedAdapter;
import com.personal.development.travelhub.models.TourSaveModel;

import java.util.ArrayList;
import java.util.List;

public class TripsActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private TextView noTripsAdded;
    private RecyclerView trip_recyclerview;
    private TourSavedAdapter adapter;

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

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid(); // Get current user's UID

        // Initialize the trips list
        tripList = new ArrayList<>();

        // Initialize the adapter with TourSavedAdapter
        adapter = new TourSavedAdapter(this, tripList, db, uid);
        trip_recyclerview.setAdapter(adapter);

        // Fetch trips data from Firestore
        fetchTripsData();

        // Set up bottom navigation actions
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

    private void fetchTripsData() {
        // Reference to the "trips" collection
        CollectionReference saveTourRef = db.collection("users")
                .document(uid)
                .collection("trips");

        // Clear existing data to avoid duplicates
        tripList.clear();

        // Fetch the trips
        saveTourRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                // Fetch tours and then fetch tour ID for each one
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    // Get the data from each document
                    String dateRange = document.getString("dateRange");
                    String tourName = document.getString("tourName");
                    String imgUrl = document.getString("image_link_1");
                    String documentId = document.getId();

                    // Asynchronous query to get the tour ID
                    Query getTourId = db.collection("tour_package").whereEqualTo("tourName", tourName);
                    getTourId.get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Assuming the tour ID is fetched correctly
                                    String tourID = "";
                                    for (DocumentSnapshot tourDoc : task.getResult()) {
                                        tourID = tourDoc.getId(); // Set tourID from the fetched document
                                    }

                                    // Now that the tourID is retrieved, create the TourSaveModel
                                    TourSaveModel tour = new TourSaveModel(dateRange, tourName, imgUrl, documentId, uid, tourID);

                                    // Add the tour to the list
                                    tripList.add(tour);

                                    // Notify the adapter that the data has been updated
                                    adapter.notifyDataSetChanged();

                                    // Update the empty state visibility
                                    toggleEmptyState(tripList.isEmpty());
                                }
                            })
                            .addOnFailureListener(e -> {
                                // Handle any errors during the fetch of tour ID
                                toggleEmptyState(true);
                            });
                }
            } else {
                // Handle case when there are no trips
                toggleEmptyState(true);
            }
        }).addOnFailureListener(e -> {
            // Handle any errors during the trips fetch
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
