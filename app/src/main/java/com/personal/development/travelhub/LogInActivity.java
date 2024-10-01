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

    Intent intent;
    private FirebaseAuth mAuth;

    EditText email,password;
    TextView forgotPassword,gotoSignUp;
    Button logIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Edittext
        email = (EditText) findViewById(R.id.email_edittext_login);
        password = (EditText) findViewById(R.id.password_edittext_login);
        //TextViews
        forgotPassword = (TextView) findViewById(R.id.forgotPassword_textView);
        gotoSignUp = (TextView) findViewById(R.id.SignUp);
        logIn = (Button) findViewById(R.id.logIn_button);

        gotoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), Registration_view.class);
                v.getContext().startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), ForgotPassword.class);
                v.getContext().startActivity(intent);
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(email.getText().toString(),password.getText().toString());
            }
        });

    }

    private void loginUser(String email_, String password_){
        mAuth.signInWithEmailAndPassword(email_, password_)
                .addOnCompleteListener(this, task -> {
                   if (task.isSuccessful()){
                       // Login success
                       FirebaseUser user = mAuth.getCurrentUser();
                       if (user != null && user.isEmailVerified()) {

                           // Retrieve
                           FirebaseFirestore db = FirebaseFirestore.getInstance();
                           DocumentReference docRef = db.collection("users").document(user.getUid());

                           docRef.get().addOnSuccessListener(documentSnapshot -> {
                               if (documentSnapshot.exists()) {
                               String access = documentSnapshot.getString("access");

                               if (access != null){
                                   if (access.equals("admin")) {
                                       startActivity(new Intent(this, activity_admin.class));
                                       finish();
                                   }else if (access.equals("user")){
                                       startActivity(new Intent(LogInActivity.this, Dashboard.class));
                                       finish();
                                   }
                               }
                               } else {
                                   Toast.makeText(LogInActivity.this, "User data not found", Toast.LENGTH_LONG).show();
                               }
                           }).addOnFailureListener(e -> {
                               Toast.makeText(LogInActivity.this, "Error fetching user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                           });


                       }else {
                           // Email not verified, notify the user
                           Toast.makeText(LogInActivity.this,
                                   "Please verify your email address",
                                   Toast.LENGTH_LONG).show();
                           mAuth.signOut();
                       }
                   } else {
                       // Login failed, show error message
                       Toast.makeText(LogInActivity.this,
                               "Authentication failed: "+ task.getException().getMessage(),
                               Toast.LENGTH_LONG).show();
                   }
                });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null && user.isEmailVerified()){
//            Intent intent = new Intent(LogInActivity.this, Dashboard.class);
//            startActivity(intent);
//            finish();
//        }
//    }
}