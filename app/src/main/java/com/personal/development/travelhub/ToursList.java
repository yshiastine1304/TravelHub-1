package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.personal.development.travelhub.adapters.AdminTourListAdapter;

public class ToursList extends AppCompatActivity {
    private Button addTour;
    private TextView backBtn;
    private RecyclerView tourListRecycler;
    private AdminTourListAdapter adapter;
    private FirebaseFirestore db;
    String accessType, name;
    private Intent intent,itineraryIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_tours_list);

        addTour = findViewById(R.id.add_btn_tour);
        tourListRecycler = findViewById(R.id.tours_list_recycler);
        backBtn = findViewById(R.id.back_btn_tours);
        db = FirebaseFirestore.getInstance();

        intent = getIntent();
        accessType =intent.getStringExtra("access");
        name = intent.getStringExtra("name");


        if ("admin".equals(accessType)) {
            addTour.setVisibility(View.GONE);
        }else if ("agency".equals(accessType)){
//            addTour.setVisibility(View.GONE);
            itineraryIntent = new Intent(ToursList.this, TravelsActivity.class);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    intent = new Intent(ToursList.this, AgencyDashboard.class);
                    intent.putExtra("agency_name",name);
                    startActivity(intent);
            }
        });


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