package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.personal.development.travelhub.adapters.AttractionAdapter;
import com.personal.development.travelhub.adapters.HomeAdapter;
import com.personal.development.travelhub.models.AttractionsModel;
import com.personal.development.travelhub.models.CardModel;
import com.personal.development.travelhub.R;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private RecyclerView reco_recyclerView;
    private HomeAdapter adapter;
    private AttractionAdapter adapter2;
    private List<CardModel> dataList;
    private List<AttractionsModel> dataList2;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerView = findViewById(R.id.recommended_recycler_view);
        reco_recyclerView = findViewById(R.id.attractions_recyclerView);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_wishlist) {
                startActivity(new Intent(this, Wishlist.class));
                return true;
            } else if (itemId == R.id.nav_trip) {
                return true;
            } else if (itemId == R.id.nav_account) {
                return true;
            } else {
                return false;
            }
        });

        // Create separate layout managers for each RecyclerView
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false); // Vertical layout

        // Set the layout managers to different RecyclerViews
        recyclerView.setLayoutManager(layoutManager1);
        reco_recyclerView.setLayoutManager(layoutManager2);

        // Set up data and adapters
        dataList = new ArrayList<>();
        dataList.add(new CardModel("https://drive.google.com/uc?export=view&id=1OgDYv9jxQcKwJmTQUDzQLZ1nJLUZ4I9-", "1 Airplane"));
        dataList.add(new CardModel("https://drive.google.com/uc?export=view&id=1_jK0fXLw-zqnYeiqrS09PEDMbddh47ab", "2 Magpayong Rock"));

        dataList2 = new ArrayList<>();
        dataList2.add(new AttractionsModel("https://drive.google.com/file/d/1Xwr2iJTFxsV7xnGGxl6Xi4irwLzAsnPx", "3 WHITE BEACH, SAAVEDRA",
                "https://drive.google.com/file/d/1g79NkbSd6gVCR-OJH0syP9PY9qcLlu9h", "4 PANAGSAMA BEACH, BASDIOT"));

        adapter = new HomeAdapter(dataList);
        adapter2 = new AttractionAdapter(dataList2);

        recyclerView.setAdapter(adapter);
        reco_recyclerView.setAdapter(adapter2);

    }

}