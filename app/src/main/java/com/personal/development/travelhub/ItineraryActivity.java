package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.personal.development.travelhub.adapters.ItineraryAdapter;
import com.personal.development.travelhub.models.ItineraryDestinationModel;

import java.util.ArrayList;
import java.util.List;

public class ItineraryActivity extends AppCompatActivity {

    private static final String TAG = "ItineraryActivity";
    private RecyclerView itineraryRecyclerView;
    private ItineraryAdapter adapter;
    private List<ItineraryDestinationModel> itineraryList;
    private FirebaseFirestore db;
    String tourUID,destinationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itinerary_layout);

        Intent intent = getIntent();
        tourUID = intent.getStringExtra("tour_uid");
        destinationName = intent.getStringExtra("destination_name");
        // Initialize RecyclerView
        itineraryRecyclerView = findViewById(R.id.day1_recycler);
        itineraryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize data list and adapter
        itineraryList = new ArrayList<>();
        adapter = new ItineraryAdapter(itineraryList);
        itineraryRecyclerView.setAdapter(adapter);

        // Fetch data from Firestore
        fetchItineraryData();
    }

    private void fetchItineraryData() {
        db.collection("tour_package").document(tourUID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Fetch the document ID and tourName
                        String documentUID = documentSnapshot.getId();
                        String tourName = documentSnapshot.getString("tourName");

                        // Now, fetch the 'destination_list' subcollection of the specific tour_package
                        db.collection("tour_package").document(documentUID)
                                .collection("destination_list")
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        for (QueryDocumentSnapshot destinationDocument : queryDocumentSnapshots) {
                                            // Convert the destination document to the model
                                            ItineraryDestinationModel itinerary = destinationDocument.toObject(ItineraryDestinationModel.class);
                                            itineraryList.add(itinerary); // Add to the itinerary list
                                        }
                                        adapter.notifyDataSetChanged(); // Refresh RecyclerView after data is added
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Error fetching destination data", e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching tour package data", e);
                });
    }

}
