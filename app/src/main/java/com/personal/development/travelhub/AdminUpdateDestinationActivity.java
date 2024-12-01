package com.personal.development.travelhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.personal.development.travelhub.models.DestinationModels;

public class AdminUpdateDestinationActivity extends AppCompatActivity {

    // UI components
    private EditText destinationName, location, highlight, busFare, entranceFee, whatToExpect, otherDetails;
    private ImageView destinationImage;
    private Button updateButton;

    // Firestore instance
    private FirebaseFirestore firestore;

    // Destination object
    private DestinationModels destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_update_destination);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI components
        destinationName = findViewById(R.id.tour_name);
        location = findViewById(R.id.location_admin);
        highlight = findViewById(R.id.highlight_admin);
        busFare = findViewById(R.id.bus_fare_admin);
        entranceFee = findViewById(R.id.entrance_fee_admin);
        whatToExpect = findViewById(R.id.what_to_expect_admin);
        otherDetails = findViewById(R.id.other_details_admin);
        destinationImage = findViewById(R.id.upload_img_btn_tour);
        updateButton = findViewById(R.id.save_admin_btn);

        // Get the destination_id passed from the adapter
        Intent intent = getIntent();
        String destinationIdValue = intent.getStringExtra("destination_id"); // Get the destination_id

// Fetch the destination data using destination_id
        fetchDestinationData(destinationIdValue);


        // Update button listener
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDestination();
            }
        });
    }

    private void fetchDestinationData(String destinationId) {
        firestore.collection("attractions")
                .whereEqualTo("destination_id", destinationId)  // Use destination_id here
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            destination = document.toObject(DestinationModels.class); // Map document to model
                            Log.d("AdminUpdate", "Data fetched successfully: " + destination);
                            if (destination != null) {
                                populateFields(destination); // Populate the fields with the destination data
                            } else {
                                Log.d("AdminUpdate", "Destination object is null.");
                            }
                        } else {
                            Log.d("AdminUpdate", "No matching destination found.");
                        }
                    } else {
                        Log.e("AdminUpdate", "Query failed: ", task.getException());
                    }
                });
    }

    private void populateFields(DestinationModels destination) {
        if (destination != null) {
            destinationName.setText(destination.getDestination_name());
            location.setText(destination.getLocation());
            highlight.setText(destination.getHighlight());
            busFare.setText(destination.getBus_fare());
            entranceFee.setText(destination.getEntrance_fee());
            whatToExpect.setText(destination.getWhat_to_expect());
            otherDetails.setText(destination.getOther_details());

            // Load the image into the ImageView using Glide
            Glide.with(this)
                    .load(destination.getImage_link_1()) // Assuming the image URL is in the model
                    .placeholder(R.drawable.default_picture) // Optional placeholder image
                    .into(destinationImage);
        } else {
            Log.d("AdminUpdate", "Destination data is null, can't populate fields.");
        }
    }


    private void updateDestination() {
        // Get updated data from fields
        String updatedDestinationName = destinationName.getText().toString();
        String updatedLocation = location.getText().toString();
        String updatedHighlight = highlight.getText().toString();
        String updatedBusFare = busFare.getText().toString();
        String updatedEntranceFee = entranceFee.getText().toString();
        String updatedWhatToExpect = whatToExpect.getText().toString();
        String updatedOtherDetails = otherDetails.getText().toString();

        // Validate fields
        if (updatedDestinationName.isEmpty() || updatedLocation.isEmpty() || updatedHighlight.isEmpty() ||
                updatedBusFare.isEmpty() || updatedEntranceFee.isEmpty() ||
                updatedWhatToExpect.isEmpty() || updatedOtherDetails.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update destination object
        destination.setDestination_name(updatedDestinationName);
        destination.setLocation(updatedLocation);
        destination.setHighlight(updatedHighlight);
        destination.setBus_fare(updatedBusFare);
        destination.setEntrance_fee(updatedEntranceFee);
        destination.setWhat_to_expect(updatedWhatToExpect);
        destination.setOther_details(updatedOtherDetails);

        // Update Firestore
        firestore.collection("attractions")
                .whereEqualTo("destination_name", destination.getDestination_name())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        String documentId = task.getResult().getDocuments().get(0).getId();

                        firestore.collection("destinations").document(documentId)
                                .set(destination)
                                .addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        Toast.makeText(this, "Destination updated successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Error updating destination", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Error finding destination to update", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
