    package com.personal.development.travelhub;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;
    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;
    import java.util.List;

    public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.AttractionViewHolder> {

        private List<Attraction> attractions;
        private OnAttractionClickListener listener;

        public AttractionAdapter(List<Attraction> attractions, OnAttractionClickListener listener) {
            this.attractions = attractions;
            this.listener = listener;
        }

        @NonNull
        @Override
        public AttractionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attraction_card, parent, false);
            return new AttractionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AttractionViewHolder holder, int position) {
            Attraction attraction = attractions.get(position);
            holder.bind(attraction);
        }

        @Override
        public int getItemCount() {
            return attractions.size();
        }

        class AttractionViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView nameTextView;
            TextView descriptionTextView;
            TextView categoryTextView;

            AttractionViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.attraction_image);
                nameTextView = itemView.findViewById(R.id.attraction_name);
                descriptionTextView = itemView.findViewById(R.id.attraction_description);
                categoryTextView = itemView.findViewById(R.id.attraction_category);

                itemView.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onAttractionClick(attractions.get(position));
                    }
                });
            }

            void bind(Attraction attraction) {
                imageView.setImageResource(attraction.getImageResourceId());
                nameTextView.setText(attraction.getName());
                descriptionTextView.setText(attraction.getDescription());
                categoryTextView.setText(attraction.getCategory());
            }
        }

        public interface OnAttractionClickListener {
            void onAttractionClick(Attraction attraction);
        }
    }

