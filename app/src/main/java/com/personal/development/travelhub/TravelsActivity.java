package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class TravelsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView tourName_txtV, description_txtV,goBack;
    private Button viewItinerary;
    String tourName_,destination_name_,tourUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_details_layout);

        Intent intent = getIntent();
        tourName_ = intent.getStringExtra("tour_name");
        destination_name_ = intent.getStringExtra("destination_name");


        db = FirebaseFirestore.getInstance();
        viewItinerary = findViewById(R.id.view_itinerary_btn);
        tourName_txtV = findViewById(R.id.place_name);
        description_txtV = findViewById(R.id.description_txtV);
        goBack = findViewById(R.id.goback);

        tourName_txtV.setText(tourName_);

        viewItinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TravelsActivity.this, ItineraryActivity.class));
                Intent intent = new Intent(TravelsActivity.this, ItineraryActivity.class);

                // Add extra data to the intent
                intent.putExtra("destination_name", destination_name_); // Example of a string
                intent.putExtra("tour_uid", tourUID);
                // Start the activity
                startActivity(intent);
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TravelsActivity.this, TravelPlanActivity.class);

                // Add extra data to the intent
                intent.putExtra("destination_name", destination_name_); // Example of a string

                // Start the activity
                startActivity(intent);

            }
        });

        retrievedData();
    }

    public void retrievedData() {
        db.collection("tour_package").whereEqualTo("tourName", tourName_)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                String tourname = document.getString("tourName");
                                tourUID = document.getId().toString();
                                // Update your UI or handle the retrieved data here
                                Toast.makeText(TravelsActivity.this, "Data found: " + tourname, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(TravelsActivity.this, "Document not Exist", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TravelsActivity.this, "Error fetching data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}