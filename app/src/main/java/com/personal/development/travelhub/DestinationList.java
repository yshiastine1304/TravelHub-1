// DestinationList.java
package com.personal.development.travelhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.personal.development.travelhub.adapters.DestinationListAdapter;
import com.personal.development.travelhub.models.DestinationModels;

import java.util.ArrayList;
import java.util.List;

public class DestinationList extends AppCompatActivity implements DestinationListAdapter.OnItemClickListener {

    private RecyclerView destinationRecyclerView;
    private DestinationListAdapter adapter;
    private List<DestinationModels> destinationModelsList;
    private FirebaseFirestore firestore;
    private TextView back_btn;
    private Button addDestination;
    private Intent intent;
    private String accessVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_destination_list);

        destinationRecyclerView = findViewById(R.id.destination_recycler);
        destinationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        destinationModelsList = new ArrayList<>();
        adapter = new DestinationListAdapter(this, destinationModelsList, this); // Pass 'this' as OnItemClickListener
        destinationRecyclerView.setAdapter(adapter);

        addDestination = findViewById(R.id.add_btn_destination);
        back_btn = findViewById(R.id.back_btn_destination);

        firestore = FirebaseFirestore.getInstance();

        // Retrieve access value from the Intent
        intent = getIntent();
        accessVal = intent.getStringExtra("access");

        // Check access value and set up the corresponding behavior
        if ("agency".equals(accessVal)) {
            addDestination.setVisibility(View.GONE); // Hide the "Add Destination" button for agency
            back_btn.setOnClickListener(v -> {
                Intent intentCharlie = new Intent(DestinationList.this, AgencyDashboard.class);
                intentCharlie.putExtra("agency_name", intent.getStringExtra("name"));
                startActivity(intentCharlie);
            });
        } else {
            back_btn.setOnClickListener(v -> {
                Intent intentBeta = new Intent(DestinationList.this, activity_admin.class);
                startActivity(intentBeta);
            });
        }

        // Handle the add destination button click
        addDestination.setOnClickListener(v -> {
            if ("agency".equals(accessVal)) {
                // No action for agency when the add button is clicked
                return;
            } else {
                Intent intentBeta = new Intent(DestinationList.this, activity_admin.class);
                startActivity(intentBeta);
            }
        });

        loadDestinations();
    }

    // Fetch the list of destinations from Firestore
    private void loadDestinations() {
        CollectionReference attractionsRef = firestore.collection("attractions");

        attractionsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult() != null) {
                    Log.d("Firestore", "Successfully retrieved attractions collection");

                    destinationModelsList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DestinationModels model = document.toObject(DestinationModels.class);
                        destinationModelsList.add(model);
                    }
                    Log.d("Firestore", "Added " + destinationModelsList.size() + " items to the destination models list");

                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("Firestore", "No documents found in attractions collection");
                }
            } else {
                Log.e("Firestore", "Error retrieving attractions collection: ", task.getException());
            }
        });
    }

    // Handle item click based on the access value
    @Override
    public void onItemClick(int position) {
        // Get the clicked item (DestinationModels object) at the position
        DestinationModels destinationModel = destinationModelsList.get(position);

        // Fetch the unique destination ID
        String destinationId = destinationModel.getId();  // Fetch the ID of the clicked item

        // Check the access value to determine which activity to open
        if ("agency".equals(accessVal)) {
            Intent intent = new Intent(DestinationList.this, DetailsActivity.class);
            // Pass the destination ID to DetailsActivity
            intent.putExtra("DOCUMENT_ID", destinationId);  // Pass the unique destination ID
            intent.putExtra("IMAGE_URL", destinationModel.getImageUrl());  // Pass the image URL
            intent.putExtra("access", "agency");  // Pass the access value
            startActivity(intent);
        } else {
            // If access is not "agency", redirect to AdminUpdateDestinationActivity
            Intent intent = new Intent(DestinationList.this, AdminUpdateDestinationActivity.class);
            // Pass the destination ID to AdminUpdateDestinationActivity
            intent.putExtra("destination_id", destinationId);  // Pass the unique destination ID
            startActivity(intent);
        }
    }

}
