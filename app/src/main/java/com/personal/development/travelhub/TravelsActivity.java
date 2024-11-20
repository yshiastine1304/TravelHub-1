package com.personal.development.travelhub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class TravelsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView tourName_txtV, description_txtV,goBack,duration_txtV,otherDetails,
            inclusion_details_txtV,price_txtV,minimumAge_txtV,pricePer,location_txtV;
    private Button viewItinerary,saveTripBtn;
    String tourName_,destination_name_,tourUID;
    String imgLink;
    private ImageView tourImg;
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
        duration_txtV = findViewById(R.id.duration_hour);
        otherDetails = findViewById(R.id.travel_other_details);
        inclusion_details_txtV = findViewById(R.id.inclusion_details);
        price_txtV = findViewById(R.id.price_travel);
        minimumAge_txtV = findViewById(R.id.min_age);
        pricePer = findViewById(R.id.price_per);
        location_txtV = findViewById(R.id.location_tour);
        saveTripBtn = findViewById(R.id.save_tripBtn);
        tourImg = findViewById(R.id.place_image);

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

        saveTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showSelectDate();
                saveTrip();
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

    public void showSelectDate(String dateRange){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.select_date_dialog_layout, null);

        TextView selectDateTextView = dialogView.findViewById(R.id.selectDate);

        selectDateTextView.setText(dateRange);
        selectDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrip();
            }
        });

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Select Date");
        builder.setPositiveButton("Save", (dialog, which) -> {
            saveDataIntDB(dateRange);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void saveTrip(){
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a Date Range");

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());
        builder.setCalendarConstraints(constraintsBuilder.build());

        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener(
                (MaterialPickerOnPositiveButtonClickListener<? super Pair<Long, Long>>) selection -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

                    Calendar calendarStart = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendarStart.setTimeInMillis(selection.first);
                    Calendar calendarEnd = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendarEnd.setTimeInMillis(selection.second);

                    String startDate = sdf.format(calendarStart.getTime());
                    String endDate = sdf.format(calendarEnd.getTime());

                    showSelectDate("from " + startDate + " to " + endDate);
                });
    }

    public void saveDataIntDB(String dateRange) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Reference to the "saved_tour" collection
        CollectionReference saveTourRef = db.collection("users")
                .document(uid)
                .collection("trips");

        // Create a new document with an auto-generated ID
        DocumentReference newTripRef = saveTourRef.document(); // This generates a new document ID

        Map<String, Object> tourData = new HashMap<>();
        tourData.put("tourName", tourName_txtV.getText().toString());
        tourData.put("dateRange", dateRange);
        tourData.put("image_link_1", imgLink);

        newTripRef.set(tourData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Trip saved successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error saving trip: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
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
                                String description = document.getString("description");
                                String duration = document.getString("duration");
                                String otherDetails_data = document.getString("otherDetails");
                                String inclusionDetails = document.getString("inclusionDetails");
                                String price = document.getString("price");
                                String minAge = document.getString("minimumAge");
                                String pricePer_ = document.getString("pricePer");
                                String location = document.getString("location");
                                imgLink = document.getString("image_link_1");

                                tourUID = document.getId();
                                description_txtV.setText(description);
                                duration_txtV.setText(duration);
                                otherDetails.setText(otherDetails_data);
                                inclusion_details_txtV.setText(inclusionDetails);
                                price_txtV.setText(price);
                                minimumAge_txtV.setText(minAge);
                                pricePer.setText(pricePer_);
                                location_txtV.setText(location);

                                // Ensure imgLink is not null and valid before loading
                                if (imgLink != null && !imgLink.isEmpty()) {
                                    Glide.with(TravelsActivity.this) // Corrected context
                                            .load(imgLink)
                                            .placeholder(R.drawable.default_picture) // Placeholder image
                                            .error(R.drawable.error_icon) // Error image
                                            .into(tourImg); // Replace `tourImg` with your ImageView ID
                                } else {
                                    Toast.makeText(TravelsActivity.this, "Image link is missing or invalid", Toast.LENGTH_SHORT).show();
                                }

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