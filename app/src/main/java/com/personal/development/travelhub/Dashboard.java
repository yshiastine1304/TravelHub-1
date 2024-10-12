package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.personal.development.travelhub.adapters.AttractionAdapter;
import com.personal.development.travelhub.adapters.HomeAdapter;
import com.personal.development.travelhub.models.AttractionsModel;
import com.personal.development.travelhub.models.CardModel;
import com.personal.development.travelhub.R;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
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

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recommended_recycler_view);
        reco_recyclerView = findViewById(R.id.attractions_recyclerView);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_wishlist) {
                startActivity(new Intent(this, Wishlist.class));
                return true;
            } else if (itemId == R.id.nav_trip) {
                startActivity(new Intent(this, TripsActivity.class));
                return true;
            } else if (itemId == R.id.nav_account) {
                return true;
            } else {
                return false;
            }
        });

        // Create separate layout managers for each RecyclerView
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false); // Vertical layout

        // Set the layout managers to different RecyclerViews
        reco_recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setLayoutManager(layoutManager1);
//        reco_recyclerView.setLayoutManager(layoutManager2);

        // Set up data and adapters
        dataList = new ArrayList<>();
//        dataList.add(new CardModel("https://drive.google.com/uc?export=view&id=1OgDYv9jxQcKwJmTQUDzQLZ1nJLUZ4I9-", "Airplane"));
//        dataList.add(new CardModel("https://drive.google.com/uc?export=view&id=1_jK0fXLw-zqnYeiqrS09PEDMbddh47ab", "Magpayong Rock"));

        dataList2 = new ArrayList<>();
//        dataList2.add(new AttractionsModel("https://drive.google.com/uc?export=view&id=1Xwr2iJTFxsV7xnGGxl6Xi4irwLzAsnPx", "WHITE BEACH, SAAVEDRA",
//                "https://drive.google.com/uc?export=view&id=1g79NkbSd6gVCR-OJH0syP9PY9qcLlu9h", "PANAGSAMA BEACH, BASDIOT"));

        adapter = new HomeAdapter(this,dataList);
        adapter2 = new AttractionAdapter(this,dataList2);

        recyclerView.setAdapter(adapter);
        reco_recyclerView.setAdapter(adapter2);

        fetchRecommendedData();
        fetchAttractionsData();
    }

    private void fetchRecommendedData() {
        db.collection("attractions")
                .get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful()){
//                       dataList.clear();
                      for (QueryDocumentSnapshot document : task.getResult()) {
                          String interest = document.getString("recommend_interest");
                          String imageUrl = document.getString("image_link_1");
                          String title = document.getString("destination_name");
                          String documentId = document.getId();

                          if (interest.equals("Beach")){
                              dataList.add(new CardModel(imageUrl,title, documentId));
                          }
                      }
                       adapter.notifyDataSetChanged();
                   }
                });
    }

    private void fetchAttractionsData(){
        db.collection("attractions")
                .get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful()){
//                       dataList2.clear();
                       for (QueryDocumentSnapshot document : task.getResult()){
                           String imageUrl = document.getString("image_link_1");
                           String title = document.getString("destination_name");
                           String documentId = document.getId();

                           dataList2.add(new AttractionsModel(imageUrl,title,documentId));
                       }
                       adapter2.notifyDataSetChanged();
                   }
                });
    }

}