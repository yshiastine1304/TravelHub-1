package com.personal.development.travelhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private CardView cardDestination, cardTotalUsers, cardTours, agencyTotal;
    private TextView totalUser, totalDestination, totalTourCount,totalAgency;
    private Button signOut;
    private FirebaseFirestore db;
    int userTotal, adminTotal, destinationTotal, toursPackageTotal,agencyCount;


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
        totalTourCount = findViewById(R.id.total_tour_count);
        agencyTotal = findViewById(R.id.card_total_agency);
        totalAgency = findViewById(R.id.total_agency_count);


        getUserandAdminCount();


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AdminDashboardActivity.this, LogInActivity.class);
                intent.putExtra("indicator","user");
                startActivity(intent);

            }
        });

        cardTotalUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("access_type",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("getAcces", "users");
                editor.apply();
                Intent intent = new Intent(AdminDashboardActivity.this, AdminTotalUserActivity.class);
                intent.putExtra("getAccess","user");
                startActivity(intent);
            }
        });
        cardTours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, ToursList.class);
                intent.putExtra("access","admin");
                startActivity(intent);
            }
        });

        cardDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, DestinationList.class);
                intent.putExtra("access", "admin");
                startActivity(intent);
            }
        });
        agencyTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SharedPreferences sharedPreferences = getSharedPreferences("access_type",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("getAcces", "agency");
                editor.apply();

                Intent intent = new Intent(AdminDashboardActivity.this, AdminTotalUserActivity.class);
                startActivity(intent);

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

        Query toursQuery = db.collection("tour_package");
        toursQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                   toursPackageTotal = task.getResult().size();
                    totalTourCount.setText(String.valueOf(toursPackageTotal));
                }
            }
        });
        Query agencyQuery = db.collection("users").whereEqualTo("access", "agency");
        agencyQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    agencyCount = task.getResult().size();
                    totalAgency.setText(String.valueOf(agencyCount));
                }
            }
        });
    }
}