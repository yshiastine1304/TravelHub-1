package com.personal.development.travelhub;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.util.Pair;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class activity_admin extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST_1 = 1;
    private static final int PICK_IMAGE_REQUEST_2 = 2;
    private Uri imageUri1 , imageUri2;

    private FirebaseFirestore db;
    private StorageReference storageRef;

    private TextView btnOpenTimePicker, uploadImgBtn1, uploadImgBtn2;
    private EditText destinationNameAdmin, busFareAdmin, entranceFeeAdmin, locationAdmin, whatToExpectAdmin, highlightAdmin, other_detailsAdmin;
    private Button saveAdminBtn;
    private Spinner recommendedSpinner;
    private String selectedStartTime; // Add this line
    private String selectedEndTime;
    private String selectedTimeRange;
    private String selectedRecommended;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        db=FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        btnOpenTimePicker = findViewById(R.id.time_admin_btn);
        uploadImgBtn1 = findViewById(R.id.upload_img_btn_1);
        uploadImgBtn2 = findViewById(R.id.upload_img_btn_2);
        destinationNameAdmin = findViewById(R.id.destination_admin);
       other_detailsAdmin = findViewById(R.id.other_details_admin);
        busFareAdmin = findViewById(R.id.bus_fare_admin);
        entranceFeeAdmin = findViewById(R.id.entrance_fee_admin);
        locationAdmin = findViewById(R.id.location_admin);
        whatToExpectAdmin = findViewById(R.id.what_to_expect_admin);
        highlightAdmin = findViewById(R.id.highlight_admin);
        saveAdminBtn = findViewById(R.id.save_admin_btn);
        recommendedSpinner = findViewById(R.id.recommended_spinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.recommended_spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        recommendedSpinner.setAdapter(adapter);

        recommendedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRecommended = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnOpenTimePicker.setOnClickListener(v -> {openStartTimePicker();});

        // Set image upload button listeners
        uploadImgBtn1.setOnClickListener(v -> openImagePicker(PICK_IMAGE_REQUEST_1));
        uploadImgBtn2.setOnClickListener(v -> openImagePicker(PICK_IMAGE_REQUEST_2));

        // Set save button listener
        saveAdminBtn.setOnClickListener(v -> submitData());

    }

    private void openImagePicker(int requestCode){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data!=null && data.getData() != null){
            if (requestCode == PICK_IMAGE_REQUEST_1){
                imageUri1 = data.getData();
                uploadImgBtn1.setText("Uploaded Successfully");
            }else if (requestCode == PICK_IMAGE_REQUEST_2){
                imageUri2 = data.getData();
                uploadImgBtn2.setText("Uploaded Successfully");
            }
        }
    }

    private void uploadImage(Uri imageUri, String filename, OnSuccessListener<Uri> onSuccessListener){
        if (imageUri != null){
            StorageReference imgRef = storageRef.child("images/"+ filename);
            imgRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl().addOnSuccessListener(onSuccessListener))
                    .addOnFailureListener(e -> Toast.makeText(activity_admin.this, "Image Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void submitData() {
        String destinationName = destinationNameAdmin.getText().toString();
        String busFare = busFareAdmin.getText().toString();
        String entranceFee = entranceFeeAdmin.getText().toString();
        String location = locationAdmin.getText().toString();
        String whatToExpect = whatToExpectAdmin.getText().toString();
        String highlight = highlightAdmin.getText().toString();
        String otherDetails = other_detailsAdmin.getText().toString();
        String selectedTimeRange = btnOpenTimePicker.getText().toString(); // Updated variable name

        if (destinationName.isEmpty() || highlight.isEmpty() || location.isEmpty() || whatToExpect.isEmpty() || selectedTimeRange == null) {
            Toast.makeText(this, "Please fill all mandatory fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("destination_name", destinationName);
        dataMap.put("bus_fare", busFare);
        dataMap.put("entrance_fee", entranceFee);
        dataMap.put("location", location);
        dataMap.put("what_to_expect", whatToExpect);
        dataMap.put("highlight", highlight);
        dataMap.put("other_details", otherDetails);
        dataMap.put("time", selectedTimeRange);
        dataMap.put("recommend_interest", selectedRecommended);

        // Upload images and store URLs in Firestore
        if (imageUri1 != null) {
            uploadImage(imageUri1, "image1.jpg", uri -> {
                dataMap.put("image_link_1", uri.toString());
                if (imageUri2 == null) {
                    saveToFirestore(dataMap); // Save if only image 1 is selected
                }
            });
        }

        if (imageUri2 != null) {
            uploadImage(imageUri2, "image2.jpg", uri -> {
                dataMap.put("image_link_2", uri.toString());
                saveToFirestore(dataMap); // Save after both images are uploaded
            });
        }

        if (imageUri1 == null && imageUri2 == null) {
            saveToFirestore(dataMap); // Save if no images are selected
        }
    }


    private void saveToFirestore(Map<String, Object> dataMap) {
        db.collection("attractions")
                .add(dataMap)
                .addOnSuccessListener(documentReference -> Toast.makeText(activity_admin.this, "Data submitted successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(activity_admin.this, "Error submitting data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
    private void openStartTimePicker() {
        MaterialTimePicker startTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H) // or TimeFormat.CLOCK_12H
                .setTitleText("Select Start Time")
                .build();

        startTimePicker.show(getSupportFragmentManager(), "START_TIME_PICKER");

        startTimePicker.addOnPositiveButtonClickListener(v -> {
            selectedStartTime = String.format(Locale.getDefault(), "%02d:%02d", startTimePicker.getHour(), startTimePicker.getMinute());
            // Open end time picker after selecting start time
            openEndTimePicker();
        });
    }

    private void openEndTimePicker() {
        MaterialTimePicker endTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H) // or TimeFormat.CLOCK_12H
                .setTitleText("Select End Time")
                .build();

        endTimePicker.show(getSupportFragmentManager(), "END_TIME_PICKER");

        endTimePicker.addOnPositiveButtonClickListener(v -> {
            selectedEndTime = String.format(Locale.getDefault(), "%02d:%02d", endTimePicker.getHour(), endTimePicker.getMinute());
            // Set the selected time range in the TextView
            btnOpenTimePicker.setText("daily " + selectedStartTime + " am to " + selectedEndTime+ " pm " );
        });
    }


//
//    private void openDateRangePicker(){
//        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
//
//        builder.setTitleText("Select a Date Range");
//
//        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
//        constraintsBuilder.setValidator(DateValidatorPointForward.now());
//        builder.setCalendarConstraints(constraintsBuilder.build());
//
//        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();
//
//        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
//
//        materialDatePicker.addOnPositiveButtonClickListener(
//                (MaterialPickerOnPositiveButtonClickListener<? super Pair<Long, Long>>) selection -> {
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
//                    Calendar calendarStart = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//                    calendarStart.setTimeInMillis(selection.first);
//
//                    Calendar calendarEnd = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//                    calendarEnd.setTimeInMillis(selection.second);
//
//                    String startDate = sdf.format(calendarStart.getTime());
//                    String endDate = sdf.format(calendarEnd.getTime());
//
//                    btnOpenDatePicker.setText("daily "+ startDate + " to " + endDate);
//                });
//
//    }
}