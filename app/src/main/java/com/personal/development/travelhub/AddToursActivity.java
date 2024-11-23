package com.personal.development.travelhub;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddToursActivity extends AppCompatActivity {

    private EditText tourName_txtV,
            description_txtV,
            location_txtV,
            inclusionDetails_txtV,
            activityDetails_txtV,
            numOfDays_txtV,
            duration_txtV,
            price_txtV,
            minimumAge_txtV,
            pricePer_txtV,
            otherDetails_txtV;
    private static final int PICK_IMAGE_REQUEST_1 = 1;
    private Spinner destinationDetails_spinner;
    private TextView backBtn,uploadImgBtn;
    private Button saveBtn;
    boolean isSpinnerInitialLoad = true, isAllDetailsEmpty= false;
    String tourID;
    int countDestination = 0;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private Uri imageUri1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tours);

        // Initialize FirebaseFirestore instance
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        tourName_txtV = findViewById(R.id.tour_name);
        description_txtV = findViewById(R.id.description_admin_tour);
        location_txtV = findViewById(R.id.location_admin);
        inclusionDetails_txtV = findViewById(R.id.inclusion_details_admin);
        destinationDetails_spinner = findViewById(R.id.destinations_tour_admin);
        activityDetails_txtV = findViewById(R.id.activity_details_tour_admin);
        numOfDays_txtV = findViewById(R.id.num_of_days_tour_admin);
        duration_txtV = findViewById(R.id.duration_tour_admin);
        price_txtV = findViewById(R.id.price_tour_admin);
        minimumAge_txtV = findViewById(R.id.min_age_tour_admin);
        pricePer_txtV = findViewById(R.id.price_per_tour_admin);
        otherDetails_txtV = findViewById(R.id.other_details_tour_admin);
        saveBtn = findViewById(R.id.save_admin_btn);
        backBtn = findViewById(R.id.add_new_trip);
        uploadImgBtn = findViewById(R.id.upload_img_btn_tour);

        destinationDetails_spinner.setEnabled(false);

        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker(PICK_IMAGE_REQUEST_1);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTour();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddToursActivity.this, ToursList.class));
            }
        });
        populateDestinationDetails();
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
                uploadImgBtn.setText("Uploaded Successfully");
            }
        }
    }

    private void uploadImage(Uri imageUri, String filename, OnSuccessListener<Uri> onSuccessListener){
        if (imageUri != null){
            StorageReference imgRef = storageRef.child("images/"+ filename);
            imgRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl().addOnSuccessListener(onSuccessListener))
                    .addOnFailureListener(e -> Toast.makeText(AddToursActivity.this, "Image Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    public void populateDestinationDetails() {
        List<String> destinationsList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddToursActivity.this, android.R.layout.simple_spinner_item, destinationsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationDetails_spinner.setAdapter(adapter);

        db.collection("attractions")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                String destinationName = document.getString("destination_name");
                                if (destinationName != null) {
                                    destinationsList.add(destinationName);
                                } else {
                                    Toast.makeText(AddToursActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AddToursActivity.this, "Failed to get information", Toast.LENGTH_SHORT).show();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AddToursActivity.this, "Error: "+ task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });

        destinationDetails_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDestination = destinationsList.get(position);

                if (isSpinnerInitialLoad){
                    isSpinnerInitialLoad = false;
                    return;
                }

                countDestination = countDestination + 1;
                openSaveDestination(selectedDestination, countDestination);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void openSaveDestination(String destinationName, int destinationCounter){
        LayoutInflater inflater = LayoutInflater.from(AddToursActivity.this);
        View viewSaveDestination = inflater.inflate(R.layout.add_destination_dialog_layout, null);

        EditText activity= viewSaveDestination.findViewById(R.id.activity_details_tour_admin2);
        EditText days = viewSaveDestination.findViewById(R.id.days);
        EditText startTime = viewSaveDestination.findViewById(R.id.startTime_edittext);
        TextView destinationName_txt = viewSaveDestination.findViewById(R.id.destinationName_dialogTxt);
        Button addToList = viewSaveDestination.findViewById(R.id.add_destinations_btn);

        Map<String, Object> dataMap = new HashMap<>();
        destinationName_txt.setText("Destination "+destinationCounter);

        AlertDialog.Builder builder = new AlertDialog.Builder(AddToursActivity.this);
        builder.setView(viewSaveDestination);

        builder.setCancelable(true)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataMap.put("day", days.getText().toString());
                dataMap.put("destination_counter", "Destination "+destinationCounter);
                dataMap.put("destination_name", destinationName);
                dataMap.put("start_time", startTime.getText().toString());
                dataMap.put("activity", activity.getText().toString());

                addDestinationToFirestore(dataMap);
            }
        });
    }

    public void saveTour(){
        String tourname = tourName_txtV.getText().toString();
        String description = description_txtV.getText().toString();
        String location = location_txtV.getText().toString();
        String inclusionDetails = inclusionDetails_txtV.getText().toString();
        String numOfDays = numOfDays_txtV.getText().toString();
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
        dataMap.put("numOfDays", numOfDays);
        dataMap.put("duration", duration);
        dataMap.put("price", price);
        dataMap.put("minimumAge", minimumAge);
        dataMap.put("pricePer", pricePer);
        dataMap.put("otherDetails", otherDetails);

        if (imageUri1 != null) {
            uploadImage(imageUri1, tourname +"_1.jpg", uri -> {
                dataMap.put("image_link_1", uri.toString());
                    saveToFirestore(dataMap); // Save if only image 1 is selected
            });
        }

//        saveToFirestore(dataMap);
    }

    public void addDestinationToFirestore(Map<String, Object> dataMap) {
        db.collection("tour_package")
                .document(tourID)
                .collection("destination_list")
                .add(dataMap)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(AddToursActivity.this, "Destination saved successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(AddToursActivity.this, "Something went wrong, data not saved.", Toast.LENGTH_SHORT).show());
    }

    public void saveToFirestore(Map<String, Object> dataMap) {
        db.collection("tour_package")
                .add(dataMap)
                .addOnSuccessListener(documentReference -> {
                    tourID = documentReference.getId();
                    destinationDetails_spinner.setEnabled(true);
                    saveBtn.setEnabled(false);
                    Toast.makeText(AddToursActivity.this, "Tour saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(AddToursActivity.this, "Something went wrong, data not saved.", Toast.LENGTH_SHORT).show());
    }

}