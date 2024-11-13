package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddToursActivity extends AppCompatActivity {

    private EditText tourName_txtV,
            description_txtV,
            location_txtV,
            inclusionDetails_txtV,
            destinationDetails_txtV,
            activityDetails_txtV,
            duration_txtV,
            price_txtV,
            minimumAge_txtV,
            pricePer_txtV,
            otherDetails_txtV;
    private Button saveBtn;
    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tours);

        // Initialize FirebaseFirestore instance
        db = FirebaseFirestore  .getInstance();

        tourName_txtV = findViewById(R.id.tour_name);
        description_txtV = findViewById(R.id.description_admin_tour);
        location_txtV = findViewById(R.id.location_admin);
        inclusionDetails_txtV = findViewById(R.id.inclusion_details_admin);
        destinationDetails_txtV = findViewById(R.id.destinations_tour_admin);
        activityDetails_txtV = findViewById(R.id.activity_details_tour_admin);
        duration_txtV = findViewById(R.id.duration_tour_admin);
        price_txtV = findViewById(R.id.price_tour_admin);
        minimumAge_txtV = findViewById(R.id.min_age_tour_admin);
        pricePer_txtV = findViewById(R.id.price_per_tour_admin);
        otherDetails_txtV = findViewById(R.id.other_details_tour_admin);
        saveBtn = findViewById(R.id.save_admin_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTour();
            }
        });
    }

    public void saveTour(){
        String tourname = tourName_txtV.getText().toString();
        String description = description_txtV.getText().toString();
        String location = location_txtV.getText().toString();
        String inclusionDetails = inclusionDetails_txtV.getText().toString();
        String destinationDetails = destinationDetails_txtV.getText().toString();
        String activityDetails = activityDetails_txtV.getText().toString();
        String duration = duration_txtV.getText().toString();
        String price = price_txtV.getText().toString();
        String minimumAge = minimumAge_txtV.getText().toString();
        String pricePer = pricePer_txtV.getText().toString();
        String otherDetails = otherDetails_txtV.getText().toString();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("tourName", tourname);
        dataMap.put("description", description);
        dataMap.put("location", location);
        dataMap.put("inclusionDetails", inclusionDetails);
        dataMap.put("destinationDetails", destinationDetails);
        dataMap.put("activityDetails", activityDetails);
        dataMap.put("duration", duration);
        dataMap.put("price", price);
        dataMap.put("minimumAge", minimumAge);
        dataMap.put("pricePer", pricePer);
        dataMap.put("otherDetails", otherDetails);

        saveToFirestore(dataMap);
    }

    public void saveToFirestore(Map<String, Object> dataMap){
        db.collection("tour_package")
                .add(dataMap)
                .addOnSuccessListener(documentReference -> Toast.makeText(AddToursActivity.this, "Tour Save Successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(documentReference -> Toast.makeText(AddToursActivity.this, "Something went wrong, data not saved.", Toast.LENGTH_SHORT).show());
    }
}