package com.liakot.easytransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivitySignIn extends AppCompatActivity {

    CircleImageView logo;
    TextInputEditText signin_phonenumber,signin_password;
    TextInputLayout phonenumberlayout,passwordlayout;
    TextView signin_forgotpassword,signin_signup,signin_donthaveaccount;
    Button signin_signin;
    CheckBox rememberme;
    ClassDatabaseHelper DB;


    private void InitializeAll() {
        logo = findViewById(R.id.signinlogo);

        signin_phonenumber = findViewById(R.id.signinPhoneNumber);
        signin_password = findViewById(R.id.signInPassword);
        rememberme = findViewById(R.id.rememberme_sigin);

        phonenumberlayout = findViewById(R.id.SignInPhoneNumberLayout);
        passwordlayout = findViewById(R.id.signInPasswordLayout);

        signin_forgotpassword = findViewById(R.id.forgetpassword_signin);
        signin_signup= findViewById(R.id.signup_signin);
        signin_donthaveaccount = findViewById(R.id.haveaccount_signin);

        signin_signin = findViewById(R.id.signinBtn);
        DB = new ClassDatabaseHelper(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //For remember me
        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox = preferences.getString("remember","");
        if (checkbox.equals("true")){
            Intent intent = new Intent(ActivitySignIn.this,ActivityHome.class);
            startActivity(intent);

        }else if(checkbox.equals("false"))
        {
            Toast.makeText(this,"Please sign  in.",Toast.LENGTH_SHORT).show();
        }

        InitializeAll();
        signin_signup .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = signin_phonenumber.getText().toString();
                String pass = signin_password.getText().toString();

                if(user.equals("")||pass.equals(""))
                    Toast.makeText( ActivitySignIn.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                 else {
                    Boolean checkuserpass = DB.checkphonepassword(user, pass);
                    if (checkuserpass) {
                        Toast.makeText(ActivitySignIn.this, "Sign in Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ActivityHome.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(ActivitySignIn.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }

                }

                startActivity(new Intent(ActivitySignIn.this, ActivitySignUp.class));

            }
        });
        //For remember me box
        rememberme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rememberme.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                   Toast.makeText(ActivitySignIn.this, "checked",Toast.LENGTH_SHORT).show();

                }else if (!rememberme.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(ActivitySignIn.this, "Unchecked",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

}