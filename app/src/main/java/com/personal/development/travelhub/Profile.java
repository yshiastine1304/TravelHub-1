package com.personal.development.travelhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreKtxRegistrar;
import com.google.firebase.firestore.QuerySnapshot;
import com.personal.development.travelhub.adapters.ProfileAdapter;
import com.personal.development.travelhub.models.User;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {
    private RecyclerView profile_recycler;
    private ProfileAdapter adapter;
    private List<User> users;
    private String currentUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile_recycler = findViewById(R.id.profile_recycler);
        profile_recycler.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUID = currentUser.getUid();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        List<User> users = querySnapshot.toObjects(User.class);
                        adapter = new ProfileAdapter(Profile.this, users, new ProfileAdapter.OnItemClickListener() {
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
                        });
                        profile_recycler.setAdapter(adapter);
                    } else {
                        Log.d("User", "No user data found");
                    }
                } else {
                    Log.e("Error", "Error fetching user data", task.getException());
                }
            }
        });
    }
}