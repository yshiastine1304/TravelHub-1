package com.personal.development.travelhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.personal.development.travelhub.ItineraryActivity;
import com.personal.development.travelhub.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.personal.development.travelhub.models.TourSaveModel;

import java.util.HashSet;
import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TourViewHolder> {

    private List<TourSaveModel> tourList;
    private final LayoutInflater inflater;
    private final HashSet<String> displayedDateRanges = new HashSet<>(); // To track displayed date ranges
    private final Context context;

    public TripsAdapter(Context context, List<TourSaveModel> tourList) {
        this.tourList = tourList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_tour_save, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        TourSaveModel tour = tourList.get(position);
        String dateRange = tour.getDateRange();
        String tourName = tour.getTourName();
        String imgLink = tour.getImage_link_1();

        // Check if this dateRange has already been displayed
        if (displayedDateRanges.contains(dateRange)) {
            holder.tourDateRange.setVisibility(View.GONE); // Hide dateRange TextView
        } else {
            holder.tourDateRange.setVisibility(View.VISIBLE); // Show dateRange TextView
            holder.tourDateRange.setText(dateRange); // Set the date range text
            displayedDateRanges.add(dateRange); // Mark this dateRange as displayed
        }

        holder.tourName.setText(tourName); // Set the tour name
        Glide.with(context)
                .load(imgLink)
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.error_icon)
                .into(holder.imgView);

        holder.viewDetail.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("tour_package")
                    .whereEqualTo("tourName", tourName)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            String tourUID = document.getId();

                            if (tourUID != null) {
                                Intent intent = new Intent(context, ItineraryActivity.class);
                                intent.putExtra("destination_name", tourName);
                                intent.putExtra("tour_uid", tourUID);
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context, "Tour UID not found!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Tour details not found!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to fetch tour details!", Toast.LENGTH_SHORT).show();
                    });
        });

        holder.removeBTN.setOnClickListener(v -> {
            // Get the current user's UID
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Reference to the "trips" collection of the current user
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference saveTourRef = db.collection("users")
                    .document(uid)
                    .collection("trips");

            Query query = saveTourRef.whereEqualTo("destination_name", tourName);

            // Get documents matching the query
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Delete the document
                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Handle success
                                    Toast.makeText(context.getApplicationContext(), "Document successfully deleted!", Toast.LENGTH_SHORT).show();
                                    // Optionally, remove the item from the RecyclerView
                                    tourList.remove(position); // Remove item from the list
                                    notifyItemRemoved(position); // Notify adapter about the change
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure
                                    Toast.makeText(context.getApplicationContext(), "Error deleting document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    // Handle error
                    Toast.makeText(context.getApplicationContext(), "Error getting documents: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });



    }



    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public static class TourViewHolder extends RecyclerView.ViewHolder {
        TextView tourDateRange;
        TextView tourName;
        ImageView imgView;
        Button removeBTN;
        Button viewDetail;

        public TourViewHolder(View itemView) {
            super(itemView);
            tourDateRange = itemView.findViewById(R.id.tour_text);
            tourName = itemView.findViewById(R.id.tour_name);
            imgView = itemView.findViewById(R.id.tourImageView_saved);
            viewDetail = itemView.findViewById(R.id.view_details_saveData);
            removeBTN = itemView.findViewById(R.id.remove_btn_saveTrip);
        }
    }

    // Reset method for the adapter to handle fresh data
    public void resetDisplayedDateRanges() {
        displayedDateRanges.clear();
    }
}
