package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.personal.development.travelhub.adapters.AdminTotalListAdapter;

public class AdminTotalUserActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdminTotalListAdapter adapter;
    private EditText searchEditext;
    String Indicator;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_total_users);

        // Initialize views
        searchEditext = findViewById(R.id.search_places);
        recyclerView = findViewById(R.id.RecyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter
        adapter = new AdminTotalListAdapter(this);

       // Retrieve the intent and extras
        intent = getIntent();
        Indicator = intent.getStringExtra("indicator");
        searchEditext.setText(Indicator);
        // Null check for Indicator

            recyclerView.setAdapter(adapter);


        // Set up search functionality
        searchEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter adapter data
                if (adapter != null && adapter.getFilter() != null) {
                    adapter.getFilter().filter(s);
                } else {
                    Log.e("AdminTotalUserActivity", "Adapter or filter not initialized");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
