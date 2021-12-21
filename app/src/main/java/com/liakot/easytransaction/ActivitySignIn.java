package com.liakot.easytransaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivitySignIn extends AppCompatActivity {

    CircleImageView logo;
    TextInputEditText signin_phonenumber,signin_password;
    TextInputLayout phonenumberlayout,passwordlayout;
    TextView signin_forgotpassword,signin_signup,signin_donthaveaccount;
    Button signin_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        InitializeAll();
        signin_signup .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivitySignIn.this, ActivitySignUp.class));
            }
        });

    }
    private void InitializeAll() {
        logo = findViewById(R.id.signinlogo);

        signin_phonenumber = findViewById(R.id.signinPhoneNumber);
        signin_password = findViewById(R.id.signInPassword);

        phonenumberlayout = findViewById(R.id.SignInPhoneNumberLayout);
        passwordlayout = findViewById(R.id.signInPasswordLayout);

        signin_forgotpassword = findViewById(R.id.forgetpassword_signin);
        signin_signup= findViewById(R.id.signup_signin);
        signin_donthaveaccount = findViewById(R.id.haveaccount_signin);

        signin_signin = findViewById(R.id.signinBtn);
    }
}