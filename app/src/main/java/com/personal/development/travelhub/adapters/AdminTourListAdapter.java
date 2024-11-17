package com.personal.development.travelhub.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.personal.development.travelhub.R;
import com.personal.development.travelhub.models.AddTourModel;

import java.util.ArrayList;
import java.util.List;

public class AdminTourListAdapter extends RecyclerView.Adapter<AdminTourListAdapter.TourViewHolder> {

    private Context context;
    private List<AddTourModel> tourList;
    private FirebaseFirestore db;

    public AdminTourListAdapter(Context context){
        this.context = context;
        this.tourList = new ArrayList<>();
        this.db = FirebaseFirestore.getInstance();
        fetchActivitiesFromFirestore();
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(context).inflate(R.layout.tour_list_items, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminTourListAdapter.TourViewHolder holder, int position) {
        AddTourModel currentItem = tourList.get(position);
        holder.tourName.setText(currentItem.getTourName());  // Display the tour name
        holder.listDestination.setText(""); // Clear destination text initially

        // Call fetchDestinationList with holder and tourName
        fetchDestinationList(holder, currentItem.getTourName());
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    private void fetchActivitiesFromFirestore(){
        CollectionReference tourPackageList = db.collection("tour_package");

        tourPackageList.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        List<AddTourModel> fetchedItems = queryDocumentSnapshots.toObjects(AddTourModel.class);
                        tourList.clear();
                        tourList.addAll(fetchedItems);
                        notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Modify the method to accept TourViewHolder and tourName
    private void fetchDestinationList(TourViewHolder holder, String tourName) {
        // Get all tour package documents
        CollectionReference tourPackageListRef = db.collection("tour_package");

        // Query the collection
        tourPackageListRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        StringBuilder destinationNames = new StringBuilder();

                        // Iterate over each document in the query result
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Get the document ID (which is the tourName in this case)
                            String documentId = document.getId(); // This is the document ID

                            // If the documentId matches the tourName, fetch the destination_list subcollection
                            if (documentId.equals(tourName)) {
                                // Fetch the destination_list subcollection for this specific document
                                CollectionReference destinationListRef = document.getReference().collection("destination_list");

                                // Query the destination_list subcollection
                                destinationListRef.get()
                                        .addOnSuccessListener(destinationSnapshots -> {
                                            if (!destinationSnapshots.isEmpty()) {
                                                // Iterate through each destination document
                                                for (QueryDocumentSnapshot destinationDocument : destinationSnapshots) {
                                                    // Get the destination_name field
                                                    String destinationName = destinationDocument.getString("destination_name");
                                                    if (destinationName != null) {
                                                        // Append destination name to the StringBuilder
                                                        destinationNames.append(destinationName).append("\n");
                                                    } else {
                                                        // Log if destination_name is null
                                                        Log.d("destination list", "destination_name is null for document: " + destinationDocument.getId());
                                                    }
                                                }
                                            } else {
                                                // If no destinations are found
                                                destinationNames.append("No destinations available.").append("\n");
                                            }
                                            // Update the TextView with the destination names
                                            holder.listDestination.setText(destinationNames.toString());
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("fetchDestinationList", "Error fetching destinations: " + e.getMessage());
                                            Toast.makeText(context, "Error fetching destinations: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }
                    } else {
                        Log.d("fetchDestinationList", "No tour packages found.");
                        Toast.makeText(context, "No tour packages found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("fetchDestinationList", "Error fetching tour packages: " + e.getMessage());
                    Toast.makeText(context, "Error fetching tour packages: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }




    public static class TourViewHolder extends RecyclerView.ViewHolder {
        TextView tourName, listDestination;
        Button removeBtn, viewBtn;

        public TourViewHolder(View itemView){
            super(itemView);
            // Correct view binding
            tourName = itemView.findViewById(R.id.tour_name);
            listDestination = itemView.findViewById(R.id.list_destination);
        }
    }
}
