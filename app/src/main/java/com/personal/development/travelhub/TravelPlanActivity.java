package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import com.personal.development.travelhub.R; // Make sure to use the correct package for your resources
import com.personal.development.travelhub.adapters.TourAdapter;
import com.personal.development.travelhub.models.Tour;

import android.os.Bundle;

public class TravelPlanActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private TourAdapter adapter;
    private List<Tour> tourList;
    public String destinationNameToMatch;
    private TextView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_plan_layout);

        Intent intent = getIntent();
        destinationNameToMatch = intent.getStringExtra("destination_name");

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.travel_plan_recycler);
        backBtn = findViewById(R.id.back_btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tourList = new ArrayList<>();
        adapter = new TourAdapter(this, tourList);
        recyclerView.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TravelPlanActivity.this, Dashboard.class));

            }
        });

        fetchTourPackages();
    }

    private void fetchTourPackages() {

        // Query the 'destination_list' subcollection within 'tour_package' to find the document where destination_name matches
        db.collection("tour_package")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Fetch the document ID for each tour_package
                            String documentUID = document.getId();
                            String tourName = document.getString("tourName");

                            // Now, fetch the 'destination_list' subcollection of the specific tour_package
                            db.collection("tour_package").document(documentUID)
                                    .collection("destination_list")
                                    .whereEqualTo("destination_name", destinationNameToMatch)
                                    .get()
                                    .addOnSuccessListener(destinationQuery -> {
                                        if (!destinationQuery.isEmpty()) {
                                            for (QueryDocumentSnapshot destinationDocument : destinationQuery) {
                                                // Fetch the details for each destination that matches the destination_name
//                                                String activity = destinationDocument.getString("activity");
                                                String destinationCounter = destinationDocument.getString("destination_counter");
                                                String destinationName = destinationDocument.getString("destination_name");
                                                String startTime = destinationDocument.getString("start_time");

                                                // Create a Tour object and add to the list
                                                Tour tour = new Tour(tourName, destinationCounter, destinationName, startTime);
                                                tourList.add(tour);
                                            }
                                            adapter.notifyDataSetChanged();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(TravelPlanActivity.this, "Error fetching destination list: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(TravelPlanActivity.this, "Error fetching tour packages: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}