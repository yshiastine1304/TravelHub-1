package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String access_type, userUid;

    EditText email, password;
    TextView forgotPassword, gotoSignUp;
    Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // EditTexts
        email = findViewById(R.id.email_edittext_login);
        password = findViewById(R.id.password_edittext_login);
        // TextViews
        forgotPassword = findViewById(R.id.forgotPassword_textView);
        gotoSignUp = findViewById(R.id.SignUp);
        logIn = findViewById(R.id.logIn_button);

        // Sign up and forgot password redirections
        gotoSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Registration_view.class);
            intent.putExtra("access_type", "users");
            startActivity(intent);
        });

        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ForgotPassword.class);
            startActivity(intent);
        });

        // Log in button action
        logIn.setOnClickListener(v -> loginUser(email.getText().toString(), password.getText().toString()));
    }

    private void loginUser(String email_, String password_) {
        mAuth.signInWithEmailAndPassword(email_, password_)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login success
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Retrieve user data from Firestore
                            checkUserAccessAndRedirect(user.getUid());
                        }
                    } else {
                        // Login failed
                        Log.e("LogInActivity", "Authentication failed", task.getException());
                        Toast.makeText(LogInActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LogInActivity", "onStart called");

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userUid = user.getUid();
            checkUserAccessAndRedirect(userUid);
        }
    }

    private void checkUserAccessAndRedirect(String userUid) {
        db.collection("users").document(userUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        access_type = documentSnapshot.getString("access");
                        String name = documentSnapshot.getString("fullName");
                        redirectUserBasedOnAccess(access_type, name);
                    } else {
                        Log.w("LogInActivity", "User document does not exist");
                        Toast.makeText(LogInActivity.this, "User data not found. Please log in again.", Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("LogInActivity", "Error fetching user data", e);
                    Toast.makeText(LogInActivity.this, "Error fetching user data. Please try again.", Toast.LENGTH_LONG).show();
                    mAuth.signOut();
                });
    }

    private void redirectUserBasedOnAccess(String accessType, String name) {
        Intent intent;
        switch (accessType) {
            case "admin":
                intent = new Intent(LogInActivity.this, AdminDashboardActivity.class);
                break;
            case "users":
                intent = new Intent(LogInActivity.this, Dashboard.class);
                break;
            case "agency":
                intent = new Intent(LogInActivity.this, AgencyDashboard.class);
                intent.putExtra("agency_name", name);
                break;
            default:
                Log.w("LogInActivity", "Unknown access type: " + accessType);
                Toast.makeText(LogInActivity.this, "Invalid user type. Please contact support.", Toast.LENGTH_LONG).show();
                mAuth.signOut();
                return;
        }
        startActivity(intent);
        finish();
    }
}

