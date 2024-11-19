package com.personal.development.travelhub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.personal.development.travelhub.R;
import com.personal.development.travelhub.models.TourSaveModel;

import java.util.List;

public class TourSavedAdapter extends RecyclerView.Adapter<TourSavedAdapter.TourSavedViewHolder> {

    private List<TourSaveModel> tourList;

    public TourSavedAdapter(List<TourSaveModel> tourList) {
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
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public static class TourSavedViewHolder extends RecyclerView.ViewHolder {

        TextView tourDateRange;
        TextView tourName;

        public TourSavedViewHolder(View itemView) {
            super(itemView);
            // Find the views by their IDs
            tourDateRange = itemView.findViewById(R.id.tour_text);
            tourName = itemView.findViewById(R.id.tour_name);
        }
    }
}
