package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AdminDashboardActivity extends AppCompatActivity {
    private CardView cardDestination, cardTotalUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard_layout);

        cardDestination = findViewById(R.id.card_total_destinations);
        cardTotalUsers =  findViewById(R.id.card_total_users);

        cardTotalUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this, AdminTotalUserActivity.class));
            }
        });

        cardDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this, activity_admin.class));
            }
        });
    }
}