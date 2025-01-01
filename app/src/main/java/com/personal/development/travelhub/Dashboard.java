package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.personal.development.travelhub.adapters.AttractionAdapter;
import com.personal.development.travelhub.adapters.HomeAdapter;
import com.personal.development.travelhub.models.AttractionsModel;
import com.personal.development.travelhub.models.CardModel;

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
    private String user_interest;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recommended_recycler_view);
        reco_recyclerView = findViewById(R.id.attractions_recyclerView);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        setupBottomNavigation();

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager1);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reco_recyclerView.setLayoutManager(layoutManager2);

        dataList = new ArrayList<>();
        dataList2 = new ArrayList<>();

        adapter = new HomeAdapter(this, dataList);
        adapter2 = new AttractionAdapter(this, dataList2);

        recyclerView.setAdapter(adapter);
        reco_recyclerView.setAdapter(adapter2);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        fetchInterest();
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_wishlist) {
                navigateToActivity(Wishlist.class);
                return true;

            } else if (itemId == R.id.nav_trip) {
                navigateToActivity(TripsActivity.class);
                return true;
            }
            else if (itemId == R.id.nav_account) {
                navigateToActivity(Profile.class);
                return true;
            }

            return false;
        });
    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        refreshData();
    }

    private void refreshData() {
        fetchInterest();
    }

    public void fetchInterest() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userUid = user.getUid();
            db.collection("users")
                    .document(userUid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                user_interest = documentSnapshot.getString("interest");
                                fetchRecommendedData();
                            }
                        }
                    });
        }
    }

    private void fetchRecommendedData() {
        db.collection("attractions")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dataList.clear();
                        dataList2.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String interest = document.getString("recommend_interest");
                            String imageUrl = document.getString("image_link_1");
                            String title = document.getString("destination_name");
                            String documentId = document.getId();

                            if (interest != null && interest.equals(user_interest)) {
                                dataList.add(new CardModel(imageUrl, title, documentId));
                            } else {
                                dataList2.add(new AttractionsModel(imageUrl, title, documentId, interest));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        adapter2.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }
}