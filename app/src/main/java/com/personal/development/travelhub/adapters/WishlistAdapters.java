package com.personal.development.travelhub.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.personal.development.travelhub.R;
import com.personal.development.travelhub.TripsActivity;
import com.personal.development.travelhub.Wishlist;
import com.personal.development.travelhub.models.WishlistModels;

import java.util.ArrayList;
import java.util.List;

public class WishlistAdapters extends RecyclerView.Adapter<WishlistAdapters.WishlistViewHolder> {
    private List<WishlistModels> wishlistModels;
    private Context context;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public WishlistAdapters(Context context){
        this.context = context;
        this.wishlistModels = wishlistModels != null ? wishlistModels : new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        fetchWishlistFromFirestore();
    }

    @NonNull
    @Override
    public WishlistAdapters.WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_layout, parent, false);
       return new WishlistViewHolder(view);
    }

    public class WishlistViewHolder extends RecyclerView.ViewHolder {
        TextView tripNameTextView, reviewTxtview;
        ImageView tripImageView;
        public WishlistViewHolder(@NonNull View itemView){
            super(itemView);
            tripNameTextView = itemView.findViewById(R.id.tripsDescriptionTextView);
            reviewTxtview = itemView.findViewById(R.id.trips_reviews_txt);
            tripImageView = itemView.findViewById(R.id.tripsImageView);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull WishlistAdapters.WishlistViewHolder holder, int position) {
        WishlistModels model = wishlistModels.get(position);
        holder.tripNameTextView.setText(model.getTripName());
        holder.reviewTxtview.setText(model.getReviews());

        Glide.with(context)
                .load(model.getImageUrl())
                .apply(new RequestOptions().placeholder(R.drawable.default_picture).error(R.drawable.error_icon))
                .into(holder.tripImageView);
    }

    public WishlistAdapters(List<WishlistModels> wishlistModels) {
        // Ensure wishlistModels is not null
        this.wishlistModels = wishlistModels != null ? wishlistModels : new ArrayList<>();
    }
    @Override
    public int getItemCount() {
        return wishlistModels != null ? wishlistModels.size() : 0;
    }
    public void fetchWishlistFromFirestore() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            CollectionReference wishlistRef = db.collection("users").document(userId).collection("wishlist");
            wishlistRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshots = task.getResult();
                    wishlistModels.clear();

                    if (snapshots != null && !snapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : snapshots) {
                            WishlistModels models = document.toObject(WishlistModels.class);
                            wishlistModels.add(models);
                        }
                        notifyDataSetChanged();

                        // Hide empty state
                        ((Wishlist) context).toggleEmptyState(false);
                    } else {
                        // No trips found, show empty state
                        ((Wishlist) context).toggleEmptyState(true);
                    }
                } else {
                    Log.e("Firestore", "Error getting wishlist documents: ", task.getException());
                    // In case of an error, you may also want to show the empty state
                    ((Wishlist) context).toggleEmptyState(true);
                }
            });
        } else {
            Log.e("Firestore", "User not logged in");
            ((Wishlist) context).toggleEmptyState(true);
        }
    }
}
