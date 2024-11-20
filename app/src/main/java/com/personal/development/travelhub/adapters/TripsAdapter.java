package com.personal.development.travelhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.personal.development.travelhub.R;
import com.personal.development.travelhub.models.TourSaveModel;

import java.util.HashSet;
import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TourViewHolder> {

    private List<TourSaveModel> tourList;
    private final LayoutInflater inflater;
    private final HashSet<String> displayedDateRanges = new HashSet<>(); // To track displayed date ranges

    public TripsAdapter(Context context, List<TourSaveModel> tourList) {
        this.tourList = tourList;
        this.inflater = LayoutInflater.from(context);
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

        // Check if this dateRange has already been displayed
        if (displayedDateRanges.contains(dateRange)) {
            holder.tourDateRange.setVisibility(View.GONE); // Hide dateRange TextView
        } else {
            holder.tourDateRange.setVisibility(View.VISIBLE); // Show dateRange TextView
            holder.tourDateRange.setText(dateRange); // Set the date range text
            displayedDateRanges.add(dateRange); // Mark this dateRange as displayed
        }

        holder.tourName.setText(tourName); // Set the tour name
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public static class TourViewHolder extends RecyclerView.ViewHolder {
        TextView tourDateRange;
        TextView tourName;

        public TourViewHolder(View itemView) {
            super(itemView);
            tourDateRange = itemView.findViewById(R.id.tour_text);
            tourName = itemView.findViewById(R.id.tour_name);
        }
    }

    // Reset method for the adapter to handle fresh data
    public void resetDisplayedDateRanges() {
        displayedDateRanges.clear();
    }
}

