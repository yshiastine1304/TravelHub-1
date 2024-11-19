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

import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TourViewHolder> {

    private List<TourSaveModel> tourList;
    private final LayoutInflater inflater;

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
        holder.tourDateRange.setText(tour.getDateRange());
        holder.tourName.setText(tour.getTourName());
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
}
