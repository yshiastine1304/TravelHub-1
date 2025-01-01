package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.personal.development.travelhub.adapters.WishlistAdapters;

public class Wishlist extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private RecyclerView wishlistRecycler;
    private TextView noWishlist;
    private WishlistAdapters adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        noWishlist = findViewById(R.id.no_wishlist_text_view);

        wishlistRecycler = findViewById(R.id.triplist_recycler);
        wishlistRecycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new WishlistAdapters(this);
        wishlistRecycler.setAdapter(adapter);

        setupBottomNavigation();

        // Initially hide the empty state message
        noWishlist.setVisibility(View.GONE);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.nav_wishlist);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                navigateToActivity(Dashboard.class);
                return true;
            } else if (itemId == R.id.nav_wishlist) {
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
        bottomNavigationView.setSelectedItemId(R.id.nav_wishlist);
    }

    public void toggleEmptyState(boolean isEmpty) {
        if (isEmpty) {
            noWishlist.setVisibility(View.VISIBLE);
            wishlistRecycler.setVisibility(View.GONE);
        } else {
            noWishlist.setVisibility(View.GONE);
            wishlistRecycler.setVisibility(View.VISIBLE);
        }
    }
}