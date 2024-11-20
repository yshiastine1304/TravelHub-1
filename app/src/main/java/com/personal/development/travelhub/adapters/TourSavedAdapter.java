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
import com.personal.development.travelhub.models.TourSaveModel;

import java.util.List;

public class TourSavedAdapter extends RecyclerView.Adapter<TourSavedAdapter.TourSavedViewHolder> {

    private List<TourSaveModel> tourList;
    private Context context;

    public TourSavedAdapter(Context context, List<TourSaveModel> tourList ) {

        this.context = context;
        this.tourList = tourList;

    }

    @NonNull
    @Override
    public TourSavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout (tour_item.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_tour_save, parent, false);
        return new TourSavedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourSavedViewHolder holder, int position) {
        // Get the current tour item
        TourSaveModel currentTour = tourList.get(position);

        // Bind data to the TextViews
        holder.tourDateRange.setText(currentTour.getDateRange());
        holder.tourName.setText(currentTour.getTourName());

        Glide.with(context)
                .load(currentTour.getImage_link_1())
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.error_icon)
                .into(holder.tourImg);
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public static class TourSavedViewHolder extends RecyclerView.ViewHolder {

        TextView tourDateRange;
        TextView tourName;
        ImageView tourImg;

        public TourSavedViewHolder(View itemView) {
            super(itemView);
            // Find the views by their IDs
            tourDateRange = itemView.findViewById(R.id.tour_text);
            tourName = itemView.findViewById(R.id.tour_name);
            tourImg = itemView.findViewById(R.id.tourImageView_saved);
        }
    }
}
