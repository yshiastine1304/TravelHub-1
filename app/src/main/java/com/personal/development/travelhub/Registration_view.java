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
import com.google.firebase.firestore.FirebaseFirestore;
import com.personal.development.travelhub.models.User;

public class Registration_view extends AppCompatActivity {

    Intent intent;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    EditText fullName,email,password,contactNumber,travelInterest,travelStyle; //declaring edittexts
    Button register; // declaring button
    TextView gotoLogin; // declaring Textviews


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_view);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //EditText for Registration
        fullName = (EditText) findViewById(R.id.fullname_edittext);
        email = (EditText) findViewById(R.id.email_edittext);
        password = (EditText) findViewById(R.id.password_edittext);
        contactNumber = (EditText) findViewById(R.id.contactNumber_edittext);
        travelInterest = (EditText) findViewById(R.id.travelInterest_edittext);
        travelStyle = (EditText) findViewById(R.id.travelStyle_edittext);
        //Button for Registration
        register = (Button) findViewById(R.id.Register_button);
        //TextView for Registration
        gotoLogin = (TextView) findViewById(R.id.goToLogIn);


        //Onclick event listeners
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
                registerUser(fullName.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString(),
                        contactNumber.getText().toString(),
                        travelInterest.getText().toString(),
                        travelStyle.getText().toString());
            }
        });
    }

    private void registerUser(String fullname, String email, String password, String contactNumber, String interest, String travelStyle){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        // Sign in success, get the user
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null){
                            user.sendEmailVerification()
                                    .addOnCompleteListener(this, emailTask -> {
                                       if (emailTask.isSuccessful()){
                                           Toast.makeText(this, "Verification email sent to " + user.getEmail() , Toast.LENGTH_SHORT).show();
                                           //Create a new User
                                           User userData = new User (fullname, email, contactNumber, interest, travelStyle, "user");
                                           db.collection("users").document(user.getUid())
                                                   .set(userData)
                                                   .addOnSuccessListener(aVoid ->{
                                                       Toast.makeText(this, "User Registered!", Toast.LENGTH_SHORT).show();
                                                   })
                                                   .addOnFailureListener(e -> {
                                                       Toast.makeText(this, "Error, Not registered" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                   });
                                       }
                                    });
                        }

                    } else {
                        // If sign in fails
                        Toast.makeText(this, "Authentication Failed: "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}