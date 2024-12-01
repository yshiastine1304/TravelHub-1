package com.personal.development.travelhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.personal.development.travelhub.R; // Make sure to replace this with your actual package name
import com.personal.development.travelhub.TravelsActivity;
import com.personal.development.travelhub.models.Tour;

import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {

    private List<Tour> tourList;
    private Context context;

    public TourAdapter(Context context, List<Tour> tourList) {
        this.context = context;
        this.tourList = tourList;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.travel_list_card, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        Tour tour = tourList.get(position);

        // Set text for TextViews
        holder.tourName.setText(tour.getTourName());
        holder.destinationList.setText(tour.getDestinationCounter());
        holder.destinationName.setText(tour.getDestinationName());

        // Set OnClickListener for CardView
        holder.cardView.setOnClickListener(v -> {
//            SharedPreferences sharedPreferences = context.getSharedPreferences("TripPreferences", Context.MODE_PRIVATE);
//            String destinationName = sharedPreferences.getString("destinationName","Unknown Destination");
            Intent intent = new Intent(context, TravelsActivity.class);
            intent.putExtra("tour_name", tour.getTourName());
            intent.putExtra("destination_name", tour.getDestinationName());
            context.startActivity(intent);
        });

        // Handle Glide loading
        String imageUrl = tour.getImage_link_1();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.default_picture)
                    .error(R.drawable.error_icon)
                    .into(holder.imgView);
        } else {
            // Set a default image if the URL is null or empty
            holder.imgView.setImageResource(R.drawable.default_picture);
        }
    }


    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public static class TourViewHolder extends RecyclerView.ViewHolder {
        TextView tourName, destinationList, destinationName;
        CardView cardView;
        ImageView imgView;

        public TourViewHolder(View itemView) {
            super(itemView);
            tourName = itemView.findViewById(R.id.tour_name);
            destinationList = itemView.findViewById(R.id.list_destination);
            destinationName = itemView.findViewById(R.id.destination_name);  // New field
            cardView = itemView.findViewById(R.id.view_tour_details);
            imgView = itemView.findViewById(R.id.tourImageView_saved);
        }
    }
}

