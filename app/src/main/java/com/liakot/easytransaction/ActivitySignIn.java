package com.liakot.easytransaction;

//-------------Jannatul ferdouse,  ID : 1702010------------

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
    TextInputEditText signInPhoneNumber,signInPassword;
    TextInputLayout phoneNumberLayout,passwordLayout;
    TextView signInForgotPassword,signInSignUp;
    Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        InitializeAll();
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySignIn.this, ActivityHome.class);
                startActivity(intent);
                finish();
            }
        });
        signInSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivitySignIn.this, ActivitySignUp.class));
            }
        });
    }
    private void InitializeAll() {

        logo = findViewById(R.id.signInLogo);
        signInPhoneNumber = findViewById(R.id.signInPhoneNumber);
        signInPassword = findViewById(R.id.signInPassword);
        phoneNumberLayout = findViewById(R.id.SignInPhoneNumberLayout);
        passwordLayout = findViewById(R.id.signInPasswordLayout);
        signInForgotPassword = findViewById(R.id.signInForgetPassword);
        signInSignUp= findViewById(R.id.signInSignUp);
        signInBtn = findViewById(R.id.signInBtn);
    }
}