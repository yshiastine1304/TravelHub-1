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
import com.personal.development.travelhub.models.TripsModel;
import com.personal.development.travelhub.models.WishlistModels;


import java.util.ArrayList;
import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripsViewHolder> {
    private List<TripsModel> tripsModel;
    private Context context;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public TripsAdapter(Context context){
        this.context = context;
        this.tripsModel = tripsModel != null ? tripsModel : new ArrayList<>();
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        fetchTripsFromFirestore();
    }

    @Override
    public void onBindViewHolder(@NonNull TripsAdapter.TripsViewHolder holder, int position) {
        TripsModel model = tripsModel.get(position);
        holder.tripDescriptionTxtView.setText(model.getTripDescription());
        holder.destination_highlight.setText(model.getTripHighlight());
        holder.dateTxtView.setText(model.getTripDateFromAndTo());
        holder.statusTxt.setText(model.getTripStatus());

        Glide.with(context)
                .load(model.getTripImgUrl())
                .apply(new RequestOptions().placeholder(R.drawable.default_picture).error(R.drawable.error_icon))
                .into(holder.tripImageView);

    }

    public TripsAdapter(List<TripsModel> tripsModel){
        this.tripsModel = tripsModel != null ? tripsModel : new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return tripsModel != null ? tripsModel.size() : 0;
    }

    @NonNull
    @Override
    public TripsAdapter.TripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_trips_layout, parent, false);
       return new TripsViewHolder(view);
    }

    public class TripsViewHolder extends RecyclerView.ViewHolder {
        TextView tripDescriptionTxtView, dateTxtView,destination_count,destination_highlight,statusTxt;
        ImageView tripImageView;

        public TripsViewHolder(@NonNull View itemView){
            super(itemView);
            tripDescriptionTxtView = itemView.findViewById(R.id.tripsDescriptionTextView);
            tripImageView = itemView.findViewById(R.id.tripsImageView);
            dateTxtView = itemView.findViewById(R.id.dateTextView);
            destination_count = itemView.findViewById(R.id.destinations_count);
            destination_highlight = itemView.findViewById(R.id.trips_highlight_txt);
            statusTxt = itemView.findViewById(R.id.status);
        }

    }

    // In the fetchTripsFromFirestore method
    // In the fetchTripsFromFirestore method
    public void fetchTripsFromFirestore() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            CollectionReference tripsRef = db.collection("users").document(userId).collection("trips");

            // Order by tripDateFromAndTo for chronological ordering
            tripsRef.orderBy("tripDateFromAndTo", Query.Direction.ASCENDING)
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshots = task.getResult();
                            tripsModel.clear();

                            if (snapshots != null && !snapshots.isEmpty()) {
                                for (QueryDocumentSnapshot document : snapshots) {
                                    TripsModel models = document.toObject(TripsModel.class);
                                    tripsModel.add(models);
                                }
                                notifyDataSetChanged();
                                // Hide empty state
                                ((TripsActivity) context).toggleEmptyState(false);
                            } else {
                                // No trips found, show empty state
                                ((TripsActivity) context).toggleEmptyState(true);
                            }
                        } else {
                            Log.e("Firestore", "Error getting trip documents: ", task.getException());
                            // Show empty state in case of error
                            ((TripsActivity) context).toggleEmptyState(true);
                        }
                    });
        } else {
            Log.e("Firestore", "User not logged in");
            ((TripsActivity) context).toggleEmptyState(true);
        }
    }
}
