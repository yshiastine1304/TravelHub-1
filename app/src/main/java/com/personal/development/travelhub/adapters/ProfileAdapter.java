package com.personal.development.travelhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.personal.development.travelhub.LogInActivity;
import com.personal.development.travelhub.R;
import com.personal.development.travelhub.models.User;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private Context context;
    private List<User> users;
    private OnItemClickListener listener;
    private ArrayAdapter<String> interestAdapter;
    private List<String> interestOptions;

    public ProfileAdapter(Context context, List<User> users, OnItemClickListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
        this.interestOptions = interestOptions;

        interestAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,
                context.getResources().getStringArray(R.array.interest_spinner_items));
        interestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
        private Spinner interest;
        private ImageView profileImage;
        private View saveButton;
        private Button signoutBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.full_name);
            email = itemView.findViewById(R.id.your_email);
            contactNumber = itemView.findViewById(R.id.your_contact);
            interest = itemView.findViewById(R.id.select_interest);
            profileImage = itemView.findViewById(R.id.icon);
            saveButton = itemView.findViewById(R.id.save_admin_btn);
            signoutBtn = itemView.findViewById(R.id.sign_outBtn);

            // Initialize the Spinner with options
//            interestAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, interestOptions);
//            interestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            interest.setAdapter(interestAdapter);
              interest.setAdapter(interestAdapter);

            // Handle profile image click for uploading
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onProfileImageClicked(position);
                    }
                }
            });

            signoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(context, LogInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            });
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onUpdateClicked(position, fullName.getText().toString(), email.getText().toString(),
                                contactNumber.getText().toString(),  interest.getSelectedItem().toString());
                    }
                }
            });
        }

        public void bindUser(User user) {
            fullName.setText(user.getFullName());
            email.setText(user.getEmail());
            contactNumber.setText(user.getContactNumber());


            // Set selected interest in Spinner
            if (user.getInterest() != null) {
                int spinnerPosition = interestAdapter.getPosition(user.getInterest());
                interest.setSelection(spinnerPosition);
            }

            // Load profile image using Glide
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