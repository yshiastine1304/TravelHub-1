package com.personal.development.travelhub.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.personal.development.travelhub.ItineraryActivity;
import com.personal.development.travelhub.R;
import com.personal.development.travelhub.models.TourSaveModel;

import java.util.List;

public class TourSavedAdapter extends RecyclerView.Adapter<TourSavedAdapter.TourSavedViewHolder> {
    private final Context context;
    private final List<TourSaveModel> tripList;
    private final FirebaseFirestore db;
    private final String userId;

    public TourSavedAdapter(Context context, List<TourSaveModel> tripList, FirebaseFirestore db, String userId) {
        this.context = context;
        this.tripList = tripList;
        this.db = db;
        this.userId = userId;
    }

    @NonNull
    @Override
    public TourSavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_tour_save, parent, false);
        return new TourSavedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourSavedViewHolder holder, int position) {
        TourSaveModel trip = tripList.get(position);

        // Set trip data
        holder.tourName.setText(trip.getTourName());
        holder.dateRange.setText(trip.getDateRange());
        Glide.with(context).load(trip.getImage_link_1()).into(holder.tourImage);

        holder.viewItinerary.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItineraryActivity.class);
            intent.putExtra("tour_uid", trip.getTourID());
            intent.putExtra("form_status", "0");
            context.startActivity(intent);
//            holder.tourName.setText(trip.getTourID());
        });

        // Handle delete button click
        holder.deleteButton.setOnClickListener(v -> {
            String documentId = trip.getDocumentId();

            // Confirm with the user before deletion
            new android.app.AlertDialog.Builder(context)
                    .setTitle("Delete Trip")
                    .setMessage("Are you sure you want to delete this trip?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Proceed to delete from Firestore
                        db.collection("users")
                                .document(userId)
                                .collection("trips")
                                .document(documentId)
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Remove trip from the list
                                    tripList.remove(position);

                                    // Notify adapter
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, tripList.size());

                                    // Show success message
                                    Toast.makeText(context, "Trip deleted successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    // Show error message
                                    Toast.makeText(context, "Failed to delete trip. Try again.", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class TourSavedViewHolder extends RecyclerView.ViewHolder {
        TextView tourName, dateRange;
        ImageView tourImage;
        Button deleteButton, viewItinerary;

        public TourSavedViewHolder(@NonNull View itemView) {
            super(itemView);

            tourName = itemView.findViewById(R.id.tour_name);
            dateRange = itemView.findViewById(R.id.tour_text);
            tourImage = itemView.findViewById(R.id.tourImageView_saved);
            deleteButton = itemView.findViewById(R.id.remove_btn_saveTrip);
            viewItinerary = itemView.findViewById(R.id.view_details_saveData);
        }
    }
}
