package com.personal.development.travelhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.personal.development.travelhub.LogInActivity;
import com.personal.development.travelhub.R;
import com.personal.development.travelhub.models.User;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private Context context;
    private List<User> users;
    private OnItemClickListener listener;
    private ArrayAdapter<String> interestAdapter;

    public ProfileAdapter(Context context, List<User> users, OnItemClickListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;

        interestAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                context.getResources().getStringArray(R.array.interest_spinner_items));
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
        private TextView fullName, email, contactNumber;
        private AutoCompleteTextView interest;
        private ImageView profileImage;
        private Button saveButton, signoutBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.full_name);
            email = itemView.findViewById(R.id.your_email);
            contactNumber = itemView.findViewById(R.id.your_contact);
            interest = itemView.findViewById(R.id.select_interest);
            profileImage = itemView.findViewById(R.id.profile_image);
            saveButton = itemView.findViewById(R.id.save_admin_btn);
            signoutBtn = itemView.findViewById(R.id.sign_outBtn);

            interest.setAdapter(interestAdapter);

            profileImage.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onProfileImageClicked(position);
                }
            });

            signoutBtn.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(context, LogInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            });

            saveButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onUpdateClicked(position, fullName.getText().toString(), email.getText().toString(),
                            contactNumber.getText().toString(), interest.getText().toString());
                }
            });
        }

        public void bindUser(User user) {
            fullName.setText(user.getFullName());
            email.setText(user.getEmail());
            contactNumber.setText(user.getContactNumber());

            if (user.getInterest() != null) {
                interest.setText(user.getInterest(), false);
            }

            Glide.with(context)
                    .load(user.getProfilePictureLink())
                    .placeholder(R.drawable.baseline_person_24)
                    .circleCrop()
                    .into(profileImage);
        }
    }

    public interface OnItemClickListener {
        void onUpdateClicked(int position, String fullName, String email, String contactNumber, String interest);
        void onProfileImageClicked(int position);
    }
}

