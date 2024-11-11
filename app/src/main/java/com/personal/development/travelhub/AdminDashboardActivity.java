package com.personal.development.travelhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminDashboardActivity extends AppCompatActivity {
    private CardView cardDestination, cardTotalUsers, cardTours;
    private TextView totalUser, totalDestination;
    private Button signOut;
    private FirebaseFirestore db;
    int userTotal, adminTotal, destinationTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard_layout);

        db = FirebaseFirestore.getInstance();

        cardDestination = findViewById(R.id.card_total_destinations);
        cardTotalUsers =  findViewById(R.id.card_total_users);
        totalUser = findViewById(R.id.total_users_count);
        totalDestination = findViewById(R.id.total_destinations_count);
        cardTours = findViewById(R.id.card_total_tour_offers);
        signOut = findViewById(R.id.sign_out);

        getUserandAdminCount();


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminDashboardActivity.this, LogInActivity.class));
            }
        });

        cardTotalUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this, AdminTotalUserActivity.class));
            }
        });
        cardTours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this, AddToursActivity.class));
            }
        });

        cardDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this, DestinationList.class));
            }
        });


    }

    public void getUserandAdminCount(){
        Query userQuery = db.collection("users").whereEqualTo("access", "user");
        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    userTotal = task.getResult().size();
                    totalUser.setText(String.valueOf(userTotal));
                }
            }
        });
        Query adminQuery = db.collection("users").whereEqualTo("access", "admin");
        adminQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    adminTotal = task.getResult().size();
                }
            }
        });
        Query destinationQuery = db.collection("attractions");
        destinationQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    destinationTotal = task.getResult().size();
                    totalDestination.setText(String.valueOf(destinationTotal));
                }
            }
        });
    }
}