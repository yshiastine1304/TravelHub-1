package com.personal.development.travelhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.personal.development.travelhub.R;
import com.personal.development.travelhub.models.DestinationModels;

import java.util.List;

public class DestinationListAdapter extends RecyclerView.Adapter<DestinationListAdapter.ViewHolder> {
    private Context context;
    private List<DestinationModels> destinationModelsList;

    public DestinationListAdapter(Context context, List<DestinationModels> destinationModelsList) {
        this.context = context;
        this.destinationModelsList = destinationModelsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wishlist_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DestinationModels model = destinationModelsList.get(position);

        // Set the destination name
        holder.destinationNameTextView.setText(model.getDestination_name());

        // Load the image using Glide
        Glide.with(context)
                .load(model.getImage_link_1())
                .placeholder(R.drawable.default_picture) // Placeholder image
                .into(holder.destinationImageView);
    }

    @Override
    public int getItemCount() {
        return destinationModelsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView destinationImageView;
        TextView destinationNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationImageView = itemView.findViewById(R.id.tripsImageView); // Adjust ID if necessary
            destinationNameTextView = itemView.findViewById(R.id.tripsDescriptionTextView); // Adjust ID if necessary
        }
    }
}

