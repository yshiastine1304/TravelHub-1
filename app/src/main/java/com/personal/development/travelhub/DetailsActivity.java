package com.personal.development.travelhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailsActivity extends AppCompatActivity {
    TextView backBtn, placeName, highlight, time_open;
    ImageView placeImage;
    FirebaseFirestore db;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        backBtn = findViewById(R.id.back_btn);
        placeName = findViewById(R.id.place_name);
        placeImage = findViewById(R.id.place_image);
        highlight = findViewById(R.id.highlight_details);
        time_open = findViewById(R.id.open_time_details);

        db = FirebaseFirestore.getInstance();

        String documentId = getIntent().getStringExtra("DOCUMENT_ID");

        if (documentId != null) {
            fetchPlaceDetails(documentId);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), Dashboard.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    private void fetchPlaceDetails(String documentId){
        db.collection("attractions").document(documentId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("destination_name");
                            String imageUrl = documentSnapshot.getString("image_link_1");
                            String highlight_data= documentSnapshot.getString("highlight");
                            String time = documentSnapshot.getString("time");

                            placeName.setText(name);
                            highlight.setText(highlight_data);
                            time_open.setText(time);
                            Glide.with(DetailsActivity.this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.default_picture)
                                    .into(placeImage);
                        }else {
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
}