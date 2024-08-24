package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LogInActivity extends AppCompatActivity {

    Intent intent;

    EditText email,password;
    TextView forgotPassword,gotoSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Edittext
        email = (EditText) findViewById(R.id.email_edittext_login);
        password = (EditText) findViewById(R.id.password_edittext_login);
        //TextViews
        forgotPassword = (TextView) findViewById(R.id.forgotPassword_textView);
        gotoSignUp = (TextView) findViewById(R.id.SignUp);

        gotoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), Registration_view.class);
                v.getContext().startActivity(intent);
            }
        });
    }
}