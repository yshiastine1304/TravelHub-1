package com.personal.development.travelhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.personal.development.travelhub.adapters.ItineraryAdapter;
import com.personal.development.travelhub.models.ItineraryDestinationModel;

import java.util.ArrayList;
import java.util.List;

public class ItineraryActivity extends AppCompatActivity implements ItineraryAdapter.OnItemClickListener {

    private static final String TAG = "ItineraryActivity";
    private RecyclerView itineraryRecyclerView;
    private ItineraryAdapter adapter;
    private List<ItineraryDestinationModel> itineraryList;
    private FirebaseFirestore db;
    private String tourUID, destinationName, formStatus;

    private MaterialButton departureCommuteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itinerary_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Itinerary");

        Intent intent = getIntent();
        tourUID = intent.getStringExtra("tour_uid");
        destinationName = intent.getStringExtra("destination_name");
        formStatus = intent.getStringExtra("form_status");

        itineraryRecyclerView = findViewById(R.id.day1_recycler);
        itineraryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        itineraryList = new ArrayList<>();
        adapter = new ItineraryAdapter(itineraryList);
        adapter.setOnItemClickListener(this);
        itineraryRecyclerView.setAdapter(adapter);

        departureCommuteButton = findViewById(R.id.departure_commute_button);

        departureCommuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!itineraryList.isEmpty()) {
                    String firstDestination = itineraryList.get(0).getDestinationName();
                    startCommuteActivity("Current Location", firstDestination);
                }
            }
        });

        if (formStatus.equals("0")) {
            fetchItineraryData();
        }
    }

    private void fetchItineraryData() {
        db.collection("tour_package").document(tourUID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String documentUID = documentSnapshot.getId();
                        String tourName = documentSnapshot.getString("tourName");
                        String imgLink1 = documentSnapshot.getString("image_link_1");

                        db.collection("tour_package").document(documentUID)
                                .collection("destination_list")
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        for (QueryDocumentSnapshot destinationDocument : queryDocumentSnapshots) {
                                            String activity = destinationDocument.getString("activity");
                                            String day = destinationDocument.getString("day");
                                            String destinationCounter = destinationDocument.getString("destination_counter");
                                            String destinationName = destinationDocument.getString("destination_name");
                                            String startTime = destinationDocument.getString("start_time");

                                            ItineraryDestinationModel tour = new ItineraryDestinationModel(activity, day, destinationCounter, destinationName, startTime, imgLink1);
                                            itineraryList.add(tour);
                                        }
                                        adapter.notifyDataSetChanged();
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

    private void startCommuteActivity(String origin, String destination) {
        Intent intent = new Intent(ItineraryActivity.this, CommuteActivity.class);
        intent.putExtra("origin", origin);
        intent.putExtra("destination", destination);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        ItineraryDestinationModel selectedDestination = itineraryList.get(position);
        startCommuteActivity("Current Location", selectedDestination.getDestinationName());
    }
}

