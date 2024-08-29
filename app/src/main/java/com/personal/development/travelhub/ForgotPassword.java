package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextView backToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        Button resetPasswordButton = findViewById(R.id.ResetBtn);
        backToLogin = (TextView) findViewById(R.id.back_to_login);
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) findViewById(R.id.email_edittext_forgot)).getText().toString().trim();

                if (email.isEmpty()) {
                    ((EditText) findViewById(R.id.email_edittext_forgot)).setError("Email is required");
                    return;
                }

                resetPassword(email);
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LogInActivity.class);
                v.getContext().startActivity(intent);

            }
        });
    }

    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPassword.this,
                                "Password reset email sent.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ForgotPassword.this,
                                "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

}