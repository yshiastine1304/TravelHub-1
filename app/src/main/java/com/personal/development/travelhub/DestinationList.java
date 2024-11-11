package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.personal.development.travelhub.adapters.DestinationListAdapter;
import com.personal.development.travelhub.models.DestinationModels;

import java.util.ArrayList;
import java.util.List;

public class DestinationList extends AppCompatActivity {
    private RecyclerView destinationRecyclerView;
    private DestinationListAdapter adapter;
    private List<DestinationModels> destinationModelsList;
    private FirebaseFirestore firestore;
    private Button addDestination;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_destination_list);

        destinationRecyclerView = findViewById(R.id.destination_recycler);
        destinationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        destinationModelsList = new ArrayList<>();
        adapter = new DestinationListAdapter(this, destinationModelsList);
        destinationRecyclerView.setAdapter(adapter);
        addDestination = findViewById(R.id.add_btn_destination);

        firestore = FirebaseFirestore.getInstance();

        addDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DestinationList.this, activity_admin.class));
            }
        });

        loadDestinations();
    }

    private void loadDestinations() {
        CollectionReference attractionsRef = firestore.collection("attractions");

        attractionsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult() != null) {
                    Log.d("Firestore", "Successfully retrieved attractions collection");

                    destinationModelsList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DestinationModels model = document.toObject(DestinationModels.class);
                        destinationModelsList.add(model);
                    }
                    Log.d("Firestore", "Added " + destinationModelsList.size() + " items to the destination models list");

                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("Firestore", "No documents found in attractions collection");
                }
            } else {
                Log.e("Firestore", "Error retrieving attractions collection: ", task.getException());
            }
        });
    }
}
