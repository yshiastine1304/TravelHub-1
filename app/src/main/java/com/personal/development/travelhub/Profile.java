package com.personal.development.travelhub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.personal.development.travelhub.adapters.ProfileAdapter;
import com.personal.development.travelhub.models.User;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity implements ProfileAdapter.OnItemClickListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    private RecyclerView profile_recycler;
    private ProfileAdapter adapter;
    private List<User> users;
    private String currentUID;
    private BottomNavigationView bottomNavigationView;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri imageUri;
    private int currentSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase services
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUID = currentUser.getUid();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        // Set up Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        setupBottomNavigation();

        // Set up RecyclerView
        profile_recycler = findViewById(R.id.profile_recycler);
        profile_recycler.setLayoutManager(new LinearLayoutManager(this));
        users = new ArrayList<>();

        // Fetch user data from Firestore
        fetchUserData();
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.nav_account);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                navigateToActivity(Dashboard.class);
                return true;
            } else if (itemId == R.id.nav_wishlist) {
                navigateToActivity(Wishlist.class);
                return true;
            } else if (itemId == R.id.nav_trip) {
                navigateToActivity(TripsActivity.class);
                return true;
            }
            else if (itemId == R.id.nav_account) {
                return true;
            }

            return false;
        });
    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.nav_account);
    }

    private void fetchUserData() {
        firestore.collection("users").document(currentUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            users.add(user);

                            // Set up adapter
                            adapter = new ProfileAdapter(Profile.this, users, Profile.this);
                            profile_recycler.setAdapter(adapter);
                        }
                    } else {
                        Log.d("User", "No user data found");
                    }
                } else {
                    Log.e("Error", "Error fetching user data", task.getException());
                }
            }
        });
    }

    @Override
    public void onUpdateClicked(int position, String fullName, String email, String contactNumber, String interest) {
        // Update user data in Firestore
        User user = users.get(position);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setContactNumber(contactNumber);
        user.setInterest(interest);

        firestore.collection("users").document(currentUID).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Profile.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Error", "Error updating user", task.getException());
                }
            }
        });
    }

    @Override
    public void onProfileImageClicked(int position) {
        currentSelectedPosition = position;
        // Launch image picker
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            // Upload image to Firebase Storage
            if (imageUri != null) {
                StorageReference fileRef = storageRef.child("profile_pictures/" + System.currentTimeMillis() + ".jpg");

                fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Get the download URL and update Firestore and RecyclerView
                                String downloadUrl = uri.toString();
                                updateProfilePictureInFirestore(currentSelectedPosition, downloadUrl);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(Profile.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void updateProfilePictureInFirestore(int position, String imageUrl) {
        // Update Firestore for the selected user
        User user = users.get(position);
        user.setProfilePictureLink(imageUrl);

        firestore.collection("users").document(currentUID)
                .update("profilePictureLink", imageUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Update the RecyclerView after successful upload
                        adapter.notifyItemChanged(position);
                        Toast.makeText(Profile.this, "Profile picture updated!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "Failed to update profile picture: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}