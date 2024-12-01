package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.personal.development.travelhub.adapters.AdminTotalListAdapter;

public class AdminTotalUserActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdminTotalListAdapter adapter;
    private EditText searchEditext;
    private TextView addNewAgency;
    String Indicator,access;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_total_users);

        // Initialize views
        searchEditext = findViewById(R.id.search_places);
        recyclerView = findViewById(R.id.RecyclerViewUsers);
        addNewAgency = findViewById(R.id.add_new_agency);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        intent = getIntent();
        access=intent.getStringExtra("getAccess");

        if ("user".equals(access)){
            addNewAgency.setVisibility(View.GONE);
        }

        // Initialize adapter
        adapter = new AdminTotalListAdapter(this);

       // Retrieve the intent and extras
        intent = getIntent();
        Indicator = intent.getStringExtra("indicator");
        searchEditext.setText(Indicator);
        // Null check for Indicator

            recyclerView.setAdapter(adapter);

        addNewAgency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(new Intent(AdminTotalUserActivity.this, Registration_view.class));
                intent.putExtra("access_type","agency");
                startActivity(intent);
            }
        });


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
