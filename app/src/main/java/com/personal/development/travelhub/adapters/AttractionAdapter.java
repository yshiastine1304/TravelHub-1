package com.personal.development.travelhub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.personal.development.travelhub.R;
import com.personal.development.travelhub.models.AttractionsModel;

import java.util.List;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.ViewHolder> {
    private List<AttractionsModel> dataList;

    // Constructor that takes the list
    public AttractionAdapter(List<AttractionsModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public AttractionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attraction_item, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttractionAdapter.ViewHolder holder, int position) {
        AttractionsModel attractionsModel = dataList.get(position);
        // For the first image and caption
        Glide.with(holder.itemView.getContext())
                .load(attractionsModel.getImageUrl_1()) // Corrected to getImageUrl1
                .placeholder(R.drawable.default_picture)
                .into(holder.picture_location_1);
        holder.title_caption_1.setText(attractionsModel.getCaption_1()); // Corrected to getCaption1

        // For the second image and caption
        Glide.with(holder.itemView.getContext())
                .load(attractionsModel.getImageUrl_2()) // Corrected to getImageUrl2
                .placeholder(R.drawable.default_picture)
                .into(holder.picture_location_2);
        holder.title_caption_2.setText(attractionsModel.getCaption_2()); // Corrected to getCaption2
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture_location_1;
        TextView title_caption_1;

        ImageView picture_location_2;
        TextView title_caption_2;

        public ViewHolder(View itemView){
            super(itemView);
            picture_location_1 = itemView.findViewById(R.id.titleImageView_1);
            title_caption_1 = itemView.findViewById(R.id.descriptionTextView_1);

            picture_location_2 = itemView.findViewById(R.id.titleImageView_2);
            title_caption_2 = itemView.findViewById(R.id.descriptionTextView_2);

        }
    }
}
