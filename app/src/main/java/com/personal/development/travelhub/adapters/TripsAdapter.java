package com.personal.development.travelhub.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.personal.development.travelhub.R;
import com.personal.development.travelhub.TripsActivity;
import com.personal.development.travelhub.models.Destination;
import com.personal.development.travelhub.models.TripsModel;
import com.personal.development.travelhub.models.WishlistModels;


import java.util.ArrayList;
import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripsViewHolder> {
    private List<TripsModel> tripsModel;
    private Context context;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public TripsAdapter(Context context) {
        this.context = context;
        this.tripsModel = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        fetchTripsFromFirestore();
    }

    @NonNull
    @Override
    public TripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_trips_layout, parent, false);
        return new TripsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsViewHolder holder, int position) {
        TripsModel model = tripsModel.get(position);

        // Set date range and other trip info
        holder.dateRangeTextView.setText(model.getTripDateFromAndTo());
        List<Destination> destinations = model.getDestinations();


        // Populate destination data up to 5
        for (int i = 0; i < destinations.size() && i < holder.destinationViews.length; i++) {
            Destination destination = destinations.get(i);
            CardView destinationView = holder.destinationViews[i];
            destinationView.setVisibility(View.VISIBLE);

            TextView description = destinationView.findViewById(R.id.tripsDescriptionTextView);
            TextView highlight = destinationView.findViewById(R.id.trips_highlight_txt);
            TextView status = destinationView.findViewById(R.id.status);
            ImageView imageView = destinationView.findViewById(R.id.tripsImageView);

            description.setText(destination.getDescription());
            highlight.setText(destination.getHighlight());
            status.setText("Status: " + destination.getStatus());

            // Load image using Glide or other image loader
            Glide.with(context)
                    .load(destination.getImageUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.default_picture).error(R.drawable.error_icon))
                    .into(imageView);

            Log.d("ImageString", "Image Path: " + destination.getImageUrl());

        }

        // Hide unused destination views
        for (int i = destinations.size(); i < holder.destinationViews.length; i++) {
            holder.destinationViews[i].setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tripsModel.size();
    }

    public class TripsViewHolder extends RecyclerView.ViewHolder {
        TextView dateRangeTextView;
        CardView[] destinationViews = new CardView[5];

        public TripsViewHolder(@NonNull View itemView) {
            super(itemView);

            dateRangeTextView = itemView.findViewById(R.id.dateTextView);

            // Initialize CardViews for up to 5 destinations
            destinationViews[0] = itemView.findViewById(R.id.destination_1);
            destinationViews[1] = itemView.findViewById(R.id.destination_2);
            destinationViews[2] = itemView.findViewById(R.id.destination_3);
            destinationViews[3] = itemView.findViewById(R.id.destination_4);
            destinationViews[4] = itemView.findViewById(R.id.destination_5);
        }
    }

    private void fetchTripsFromFirestore() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            CollectionReference tripsRef = db.collection("users").document(userId).collection("trips");

            // Order by tripDateFromAndTo
            tripsRef.orderBy("tripDateFromAndTo", Query.Direction.ASCENDING)
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            tripsModel.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TripsModel model = document.toObject(TripsModel.class);
                                tripsModel.add(model);
                            }
                            notifyDataSetChanged();
                            ((TripsActivity) context).toggleEmptyState(tripsModel.isEmpty());
                        } else {
                            Log.e("Firestore", "Error getting documents: ", task.getException());
                            ((TripsActivity) context).toggleEmptyState(true);
                        }
                    });
        } else {
            Log.e("Firestore", "User not logged in");
            ((TripsActivity) context).toggleEmptyState(true);
        }
    }
}
