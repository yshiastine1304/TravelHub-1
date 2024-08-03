package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class Registration_view extends AppCompatActivity {

    FirebaseFirestore firestore; //firestore instantiation
    EditText fullName,email,password,contactNumber,travelInterest,travelStyle; //declaring edittexts
    Button register;
    TextView gotoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_view);

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

    }
}