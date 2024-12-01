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

public class AgencyDashboard extends AppCompatActivity {

    private CardView tourList, destinationList;
    private Button signOut;
    private TextView welcomeText, destinationTotalTxt, tourTotalTxt;
    private FirebaseFirestore db;
    String agencyName;
    int destinationTotal,tourTotal;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour_agency_dashboard);

        db = FirebaseFirestore.getInstance();

        destinationList = findViewById(R.id.card_total_agency_destination);
        tourList = findViewById(R.id.card_total_tour_agency);
        welcomeText = findViewById(R.id.tour_welcome_text);
        signOut = findViewById(R.id.sign_out);
        destinationTotalTxt = findViewById(R.id.total_destinations_count_);
        tourTotalTxt = findViewById(R.id.total_tour_count);

        intent = getIntent();
        agencyName = intent.getStringExtra("agency_name");
        if (agencyName.isEmpty()){
            welcomeText.setVisibility(View.GONE);
        }else {
            welcomeText.setText("Welcome "+agencyName);
        }


        getCount();

         signOut.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 FirebaseAuth.getInstance().signOut();
                 intent = new Intent(AgencyDashboard.this, LogInActivity.class);
                 startActivity(intent);
             }
         });

         tourList.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 intent = new Intent(AgencyDashboard.this, ToursList.class);
                 intent.putExtra("access", "agency");
                 intent.putExtra("name", agencyName);
                 startActivity(intent);
             }
         });
         destinationList.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 intent = new Intent(AgencyDashboard.this, DestinationList.class);
                 intent.putExtra("access", "agency");
                 intent.putExtra("name", agencyName);
                 startActivity(intent);
             }
         });
    }

    public void getCount(){
        Query destinationQuery = db.collection("attractions");

        destinationQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    destinationTotal = task.getResult().size();
                    destinationTotalTxt.setText(String.valueOf(destinationTotal));
                }
            }
        });

        Query tourQuery = db.collection("tour_package");

        tourQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    tourTotal = task.getResult().size();
                    tourTotalTxt.setText(String.valueOf(tourTotal));
                }
            }
        });
    }

}