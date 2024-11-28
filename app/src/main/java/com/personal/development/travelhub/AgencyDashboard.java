package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class AgencyDashboard extends AppCompatActivity {

    private CardView tourList, destinationList;
    private Button signOut;
    private TextView welcomeText;
    String agencyName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour_agency_dashboard);

        destinationList = findViewById(R.id.card_total_agency_destination);
        tourList = findViewById(R.id.card_total_tour_agency);
        welcomeText = findViewById(R.id.tour_welcome_text);
        signOut = findViewById(R.id.sign_out);

        Intent intent = getIntent();
        agencyName = intent.getStringExtra("agency_name");
        welcomeText.setText("Welcome "+agencyName);

         signOut.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 FirebaseAuth.getInstance().signOut();
             }
         });

         tourList.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                startActivity(new Intent(AgencyDashboard.this, ToursList.class));
             }
         });
         destinationList.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(AgencyDashboard.this, DestinationList.class));
             }
         });
    }


}