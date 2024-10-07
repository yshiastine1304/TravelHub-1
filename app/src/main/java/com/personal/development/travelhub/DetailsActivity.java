package com.personal.development.travelhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DetailsActivity extends AppCompatActivity {
    // Declare UI elements
    TextView backBtn, placeName, highlight, time_open, btnAllDatePicker, reviews;
    ImageView placeImage;
    FirebaseFirestore db;
    Intent intent;

    private FirebaseAuth auth;
    private String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);

        auth = FirebaseAuth.getInstance();
        userUid = auth.getCurrentUser().getUid();
        // Initialize views
        backBtn = findViewById(R.id.back_btn);
        btnAllDatePicker = findViewById(R.id.allDate);
        placeName = findViewById(R.id.place_name);
        placeImage = findViewById(R.id.place_image);
        highlight = findViewById(R.id.highlight_details);
        time_open = findViewById(R.id.open_time_details);
        reviews = findViewById(R.id.reviews_txt);

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();

        // Get document ID passed from the previous activity
        String documentId = getIntent().getStringExtra("DOCUMENT_ID");
        String imageString = getIntent().getStringExtra("IMAGE_URL");

        // Fetch place details from Firestore if document ID exists
        if (documentId != null) {
            fetchPlaceDetails(documentId);
        }

        // Set back button to return to Dashboard
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), Dashboard.class);
                v.getContext().startActivity(intent);
            }
        });

        placeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWishlist(placeName.getText().toString(),imageString,reviews.getText().toString(),userUid);
            }
        });

        // Open the date picker on button click
        btnAllDatePicker.setOnClickListener(v -> openDateRangePicker());
    }

    private void createWishlist(String place_name, String image_url, String reviews, String userId){
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("tripName;", place_name);
        dataMap.put("imageUrl", image_url);
        dataMap.put("reviews", reviews);

        saveWishlistData(userId, dataMap);
    }

    private void saveWishlistData(String userId,Map<String, Object> dataMap){
        CollectionReference wishlistRef = db.collection("users").document(userId).collection("wishlist");

        wishlistRef.add(dataMap)
                .addOnSuccessListener(documentReference -> Toast.makeText(DetailsActivity.this, "Saved to wishlist!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(DetailsActivity.this,  "Error submitting data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Fetch place details from Firestore using document ID
    private void fetchPlaceDetails(String documentId) {
        db.collection("attractions").document(documentId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Extract data from Firestore document
                            String name = documentSnapshot.getString("destination_name");
                            String imageUrl = documentSnapshot.getString("image_link_1");
                            String highlightData = documentSnapshot.getString("highlight");
                            String time = documentSnapshot.getString("time");

                            // Populate views with the extracted data
                            placeName.setText(name);
                            highlight.setText(highlightData);
                            time_open.setText(time);

                            // Load the image using Glide
                            Glide.with(DetailsActivity.this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.default_picture)  // Default image placeholder
                                    .into(placeImage);
                        } else {
                            Toast.makeText(DetailsActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailsActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Open a Date Range Picker
    private void openDateRangePicker() {
        // Build the DateRangePicker with constraints to block past dates
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a Date Range");

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());  // Only future dates allowed
        builder.setCalendarConstraints(constraintsBuilder.build());

        // Build and show the date picker dialog
        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");

        // Handle the result when a date range is selected
        materialDatePicker.addOnPositiveButtonClickListener(
                (MaterialPickerOnPositiveButtonClickListener<? super Pair<Long, Long>>) selection -> {

                    // Format the selected dates
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                    Calendar calendarStart = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendarStart.setTimeInMillis(selection.first);

                    Calendar calendarEnd = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendarEnd.setTimeInMillis(selection.second);

                    // Display the formatted date range
                    String startDate = sdf.format(calendarStart.getTime());
                    String endDate = sdf.format(calendarEnd.getTime());

                    // Set the text on the button to show selected date range
                    btnAllDatePicker.setText("from " + startDate + " to " + endDate);
                });
    }
}
