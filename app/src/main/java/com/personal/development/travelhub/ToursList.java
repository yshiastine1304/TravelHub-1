package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;
import com.personal.development.travelhub.adapters.AdminTourListAdapter;

public class ToursList extends AppCompatActivity {
    private Button addTour;
    private RecyclerView tourListRecycler;
    private AdminTourListAdapter adapter;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_tours_list);

        addTour = findViewById(R.id.add_btn_tour);
        tourListRecycler = findViewById(R.id.tours_list_recycler);
        db = FirebaseFirestore.getInstance();

        adapter = new AdminTourListAdapter(ToursList.this);
        tourListRecycler.setLayoutManager(new LinearLayoutManager(ToursList.this));
        tourListRecycler.setAdapter(adapter);

        addTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ToursList.this, AddToursActivity.class));
            }
        });
    }
}