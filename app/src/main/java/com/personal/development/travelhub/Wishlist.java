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

        bottomNavigationView.setSelectedItemId(R.id.nav_wishlist);

        // Initially hide the empty state message
        noWishlist.setVisibility(View.GONE);


        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(Wishlist.this, Dashboard.class));
                return true;
            } else if (itemId == R.id.nav_wishlist) {
                return true;
            } else if (itemId == R.id.nav_trip) {
                startActivity(new Intent(Wishlist.this, TripsActivity.class));
                return true;
            } else if (itemId == R.id.nav_account) {
                startActivity(new Intent(Wishlist.this, Profile.class));
                return true;
            } else {
                return false;
            }
        });
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