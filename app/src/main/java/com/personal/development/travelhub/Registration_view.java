package com.personal.development.travelhub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
    private EditText fullName, email, password, contactNumber; // declaring edittexts
    private Spinner interestSpinner;
    private Button register; // declaring button
    private TextView gotoLogin,lbl_1; // declaring TextViews
    private String selectedInterest;
    String accessType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_view);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        intent = getIntent();
        accessType = intent.getStringExtra("access_type");

        // EditText for Registration
        fullName = findViewById(R.id.fullname_edittext);
        email = findViewById(R.id.email_edittext);
        password = findViewById(R.id.password_edittext);
        contactNumber = findViewById(R.id.contactNumber_edittext);
        interestSpinner = findViewById(R.id.travelInterest_spinner);
        lbl_1 = findViewById(R.id.Labeled1);
        // Button for Registration
        register = findViewById(R.id.Register_button);
        // TextView for Registration
        gotoLogin = findViewById(R.id.goToLogIn);

        if (accessType.equals("agency")){
            fullName.setHint("Enter agency name");
            interestSpinner.setVisibility(View.GONE);
            lbl_1.setVisibility(View.GONE);
            gotoLogin.setVisibility(View.GONE);
        }



        // Set up the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.interest_spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interestSpinner.setAdapter(adapter);

        // OnItemSelectedListener for the spinner
        interestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedInterest = parent.getItemAtPosition(position).toString();
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
                if (accessType.equals("users")){
                    registerUser(fullName.getText().toString(),
                            email.getText().toString(),
                            password.getText().toString(),
                            contactNumber.getText().toString(),
                            selectedInterest);
                }else if (accessType.equals("agency")){
                    registerUser(fullName.getText().toString(),
                            email.getText().toString(),
                            password.getText().toString(),
                            contactNumber.getText().toString(),"N/A");
                }

            }
        });
    }

    private void showDiaglogBox(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setPositiveButton("I understand", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void registerUser(String fullname, String email, String password, String contactNumber, String interest) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (fullname.isEmpty() && email.isEmpty() && password.isEmpty() && contactNumber.isEmpty() && interest.isEmpty()){
                            if (fullname.isEmpty()){
                                showDiaglogBox("Fullname is empty!");
                            }else if (email.isEmpty()){
                                showDiaglogBox("Email is empty!");
                            }else if (password.isEmpty()){
                                showDiaglogBox("Password is empty!");
                            }else if (contactNumber.isEmpty()){
                                showDiaglogBox("Contact number is empty!");
                            }else if (interest.isEmpty()){
                                showDiaglogBox("Kindly choose your interest!");
                            }
                        }else{
                            if (user != null) {
                                user.sendEmailVerification()
                                        .addOnCompleteListener(this, emailTask -> {
                                            if (emailTask.isSuccessful()) {
                                                Toast.makeText(this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                                // Create a new User
                                                User userData = new User(fullname, email, contactNumber, interest, accessType,"");
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
                        }

                    } else {
                        // If sign in fails
                        Toast.makeText(this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
