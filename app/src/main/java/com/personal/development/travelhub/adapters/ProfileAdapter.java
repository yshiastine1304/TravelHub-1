package com.personal.development.travelhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.personal.development.travelhub.R;
import com.personal.development.travelhub.models.User;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private Context context;
    private List<User> users;
    private FirebaseFirestore firestore;
    private OnItemClickListener listener;

    public ProfileAdapter(Context context, List<User> users, OnItemClickListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.bindUser(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fullName, email, contactNumber, interest;
        private ImageView profileImage;
        private View saveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.full_name);
            email = itemView.findViewById(R.id.your_email);
            contactNumber = itemView.findViewById(R.id.your_contact);
            interest = itemView.findViewById(R.id.select_interest);
            profileImage = itemView.findViewById(R.id.icon);
            saveButton = itemView.findViewById(R.id.save_admin_btn);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onUpdateClicked(position, fullName.getText().toString(), email.getText().toString(),
                                contactNumber.getText().toString(), interest.getText().toString());
                    }
                }
            });
        }

        public void bindUser(User user) {
            fullName.setText(user.getFullName());
            email.setText(user.getEmail());
            contactNumber.setText(user.getContactNumber());
            interest.setText(user.getInterest());

            // Load profile image using Glide
            Glide.with(context)
                    .load(user.getProfilePictureLink())
                    .placeholder(R.drawable.plus_icon_img)
                    .into(profileImage);
        }
    }

    public interface OnItemClickListener {
        void onUpdateClicked(int position, String fullName, String email, String contactNumber, String interest);
    }
}