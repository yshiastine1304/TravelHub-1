package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.personal.development.travelhub.adapters.WishlistAdapters;

public class Wishlist extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private RecyclerView wishlistRecycler;
    private WishlistAdapters adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        wishlistRecycler = findViewById(R.id.wishlist_recycler);
        wishlistRecycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new WishlistAdapters(this);
        wishlistRecycler.setAdapter(adapter);

        bottomNavigationView.setSelectedItemId(R.id.nav_wishlist);


        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, Dashboard.class));
                return true;
            } else if (itemId == R.id.nav_wishlist) {
                return true;
            } else if (itemId == R.id.nav_trip) {
                return true;
            } else if (itemId == R.id.nav_account) {
                return true;
            } else {
                return false;
            }
        });
    }
}