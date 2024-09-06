package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.personal.development.travelhub.adapters.HomeAdapter;
import com.personal.development.travelhub.models.CardModel;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private List<CardModel> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerView = findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataList = new ArrayList<>();
        dataList.add(new CardModel("","1 Airplane Wreck, Saavedra"));

        adapter = new HomeAdapter(dataList);
        recyclerView.setAdapter(adapter);
    }

}