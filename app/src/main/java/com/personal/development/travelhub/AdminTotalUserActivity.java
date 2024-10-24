package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.personal.development.travelhub.adapters.AdminTotalListAdapter;

public class AdminTotalUserActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdminTotalListAdapter adapter;
    private EditText searchEditext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_total_users);

        searchEditext = findViewById(R.id.search_places);
        recyclerView = findViewById(R.id.RecyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdminTotalListAdapter(this);
        recyclerView.setAdapter(adapter);

        searchEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}