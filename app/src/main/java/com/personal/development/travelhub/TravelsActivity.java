package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class TravelsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView tourName_txtV, description_txtV,goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_details_layout);

        db = FirebaseFirestore.getInstance();
        tourName_txtV = findViewById(R.id.place_name);
        description_txtV = findViewById(R.id.description_txtV);
        goBack = findViewById(R.id.goback);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TravelsActivity.this, Dashboard.class));
            }
        });

        retrievedData();
    }

    public void retrievedData(){
        db.collection("tour_package").document("KAYjS0gELU4LcDwH3neb")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String tourname = document.getString("tourName");
                            tourName_txtV.setText(tourname);

                            Toast.makeText(TravelsActivity.this, "Data found", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TravelsActivity.this, "Document not Exist", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(TravelsActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}