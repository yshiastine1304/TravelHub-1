package com.personal.development.travelhub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.personal.development.travelhub.R;
import com.personal.development.travelhub.models.ItineraryDestinationModel;

import java.util.List;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ViewHolder> {

    private final List<ItineraryDestinationModel> itineraryList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ItineraryAdapter(List<ItineraryDestinationModel> itineraryList) {
        this.itineraryList = itineraryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.destination_list_itinerary, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItineraryDestinationModel itinerary = itineraryList.get(position);
        holder.dayText.setText(itinerary.getDay());
        holder.destinationName.setText(itinerary.getDestinationName());
        holder.startTime.setText(itinerary.getStartTime());
        holder.activity.setText(itinerary.getActivity());
        holder.destinationCounter.setText(itinerary.getDestinationCounter());
    }

    @Override
    public int getItemCount() {
        return itineraryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayText, destinationName, startTime, activity, destinationCounter;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            dayText = itemView.findViewById(R.id.day_text);
            destinationName = itemView.findViewById(R.id.destinationName);
            startTime = itemView.findViewById(R.id.startTime);
            activity = itemView.findViewById(R.id.activities);
            destinationCounter = itemView.findViewById(R.id.destination_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}

