package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.personal.development.travelhub.models.User;

public class Registration_view extends AppCompatActivity {

    Intent intent;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    EditText fullName, email, password, contactNumber, travelStyle; // declaring edittexts
    private Spinner interestSpinner;
    Button register; // declaring button
    TextView gotoLogin; // declaring TextViews

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_view);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // EditText for Registration
        fullName = findViewById(R.id.fullname_edittext);
        email = findViewById(R.id.email_edittext);
        password = findViewById(R.id.password_edittext);
        contactNumber = findViewById(R.id.contactNumber_edittext);
        travelStyle = findViewById(R.id.travelStyle_edittext);
        interestSpinner = findViewById(R.id.travelInterest_spinner);

        // Button for Registration
        register = findViewById(R.id.Register_button);
        // TextView for Registration
        gotoLogin = findViewById(R.id.goToLogIn);

        // Set up the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.interest_spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interestSpinner.setAdapter(adapter);

        // OnItemSelectedListener for the spinner
        interestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // You can handle item selection if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected if needed
            }
        });

        // Onclick event listeners
        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), LogInActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedInterest = interestSpinner.getSelectedItem().toString(); // Get selected item from Spinner
                registerUser(fullName.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString(),
                        contactNumber.getText().toString(),
                        selectedInterest, // Use selected interest here
                        travelStyle.getText().toString());
            }
        });
    }

    private void registerUser(String fullname, String email, String password, String contactNumber, String interest, String travelStyle) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, get the user
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(this, emailTask -> {
                                        if (emailTask.isSuccessful()) {
                                            Toast.makeText(this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                            // Create a new User
                                            User userData = new User(fullname, email, contactNumber, interest, travelStyle, "user");
                                            db.collection("users").document(user.getUid())
                                                    .set(userData)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(this, "User Registered!", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(this, "Error, Not registered: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    });
                        }

                    } else {
                        // If sign in fails
                        Toast.makeText(this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
