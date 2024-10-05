package com.personal.development.travelhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.bumptech.glide.Glide;
import com.personal.development.travelhub.DetailsActivity;
import com.personal.development.travelhub.R;
import com.personal.development.travelhub.models.CardModel;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context context;
    private List<CardModel> dataList;

    public HomeAdapter(Context context,List<CardModel> dataList) {

        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        CardModel cardModel = dataList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(cardModel.getImageUrl())
                .placeholder(R.drawable.default_picture)
                .into(holder.picture_location);

        holder.picture_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("DOCUMENT_ID", cardModel.getDocumentUID()); // Pass the documentId
                context.startActivity(intent);
            }
        });

        holder.title_caption.setText(cardModel.getCaption());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture_location;
        TextView title_caption;
        public ViewHolder(View itemView){
            super(itemView);
            picture_location = itemView.findViewById(R.id.titleImageView);
            title_caption = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
