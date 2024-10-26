package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        db = FirebaseFirestore.getInstance();  // Ensure Firestore is initialized here

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
                        if (user != null && user.isEmailVerified()) {
                            // Retrieve user data from Firestore
                            DocumentReference docRef = db.collection("users").document(user.getUid());
                            docRef.get().addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String access = documentSnapshot.getString("access");
                                    access_type = access;
                                    if (access != null) {
                                        if (access.equals("admin")) {
                                            startActivity(new Intent(this, AdminDashboardActivity.class));
                                            finish();
                                        } else if (access.equals("user")) {
                                            startActivity(new Intent(this, Dashboard.class));
                                            finish();
                                        }
                                    }
                                } else {
                                    Toast.makeText(LogInActivity.this, "User data not found", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(e -> {
                                Toast.makeText(LogInActivity.this, "Error fetching user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                        } else {
                            // Email not verified
                            Toast.makeText(LogInActivity.this, "Please verify your email address", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                        }
                    } else {
                        // Login failed
                        Toast.makeText(LogInActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userUid = user.getUid();
            db.collection("users").document(userUid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            access_type = documentSnapshot.getString("access");
                            if (user.isEmailVerified()) {
                                if ("admin".equals(access_type)) {
                                    startActivity(new Intent(LogInActivity.this, AdminDashboardActivity.class));
                                } else if ("user".equals(access_type)) {
                                    startActivity(new Intent(LogInActivity.this, Dashboard.class));
                                }
                                finish();
                            } else {
                                Toast.makeText(LogInActivity.this, "Please verify your email address", Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                            }
                        } else {
                            Toast.makeText(LogInActivity.this, "Cannot identify user", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(LogInActivity.this, "Error fetching user data: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
        }
    }
}
