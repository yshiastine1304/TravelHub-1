package com.personal.development.travelhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.personal.development.travelhub.R;
import com.personal.development.travelhub.models.DestinationModels;

import java.util.List;

public class DestinationListAdapter extends RecyclerView.Adapter<DestinationListAdapter.ViewHolder> {

    private Context context;
    private List<DestinationModels> destinationModelsList;
    private OnItemClickListener listener;

    // Constructor takes Context, List of DestinationModels, and the OnItemClickListener
    public DestinationListAdapter(Context context, List<DestinationModels> destinationModelsList, OnItemClickListener listener) {
        this.context = context;
        this.destinationModelsList = destinationModelsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the list
        View view = LayoutInflater.from(context).inflate(R.layout.admin_destinations, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind the destination data to the ViewHolder
        DestinationModels model = destinationModelsList.get(position);
        holder.bindDestination(model);
    }

    @Override
    public int getItemCount() {
        return destinationModelsList.size();
    }

    // ViewHolder class to hold each item view
    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView destinationCardView;
        ImageView destinationImageView;
        TextView destinationNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationCardView = itemView.findViewById(R.id.destinationcardview);  // Ensure this matches the ID in your layout file
            destinationImageView = itemView.findViewById(R.id.tripsImageView);
            destinationNameTextView = itemView.findViewById(R.id.tripsDescriptionTextView);

            // Set the onClickListener for each item in the RecyclerView
            destinationCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }

        public void bindDestination(DestinationModels destination) {
            // Set the destination name and image
            destinationNameTextView.setText(destination.getDestination_name());
            Glide.with(context)
                    .load(destination.getImage_link_1())
                    .placeholder(R.drawable.default_picture)
                    .into(destinationImageView);
        }
    }

    // Interface for item click events
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
