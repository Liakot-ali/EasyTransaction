package com.liakot.easytransaction;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivitySignIn extends AppCompatActivity {

    CircleImageView logo;
    TextInputEditText signInPhoneNumber,signInPassword;
    TextInputLayout phoneNumberLayout,passwordLayout;
    TextView signInForgotPassword,signInSignUp;
    Button signInBtn;
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        InputMethodManager imm = (InputMethodManager)getSystemService(ActivityAddCustomer.INPUT_METHOD_SERVICE);

        InitializeAll();
        rememberMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumberLayout.clearFocus();
                passwordLayout.clearFocus();
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPhone, userPassword, savePhone, savePassword;
                userPhone = signInPhoneNumber.getText().toString();
                userPassword = signInPassword.getText().toString();

                long number = 2000000000;
                if (!userPhone.isEmpty() && !userPhone.contains("%") && !userPhone.contains(":") && !userPhone.contains(" ") && !userPhone.contains(".")) {
                    number = Long.parseLong(userPhone);
                }

                if(userPhone.isEmpty()){
                    phoneNumberLayout.setError("Phone number is empty");
                    phoneNumberLayout.setErrorEnabled(true);
                    passwordLayout.setErrorEnabled(false);
                }else if(userPhone.length()!=10 || number > 1999999999 || number < 999999999){
                    phoneNumberLayout.setError("Invalid phone number");
                    phoneNumberLayout.setErrorEnabled(true);
                    passwordLayout.setErrorEnabled(false);
                }else if(userPassword.isEmpty()){
                    passwordLayout.setError("Password is empty");
                    phoneNumberLayout.setErrorEnabled(false);
                    passwordLayout.setErrorEnabled(true);
                }else {
                    passwordLayout.setErrorEnabled(false);
                    phoneNumberLayout.setErrorEnabled(false);
                    phoneNumberLayout.clearFocus();
                    passwordLayout.clearFocus();

                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    ClassDatabaseHelper helper = new ClassDatabaseHelper(ActivitySignIn.this);
                    Cursor cursor = helper.showShopInfo();
                    cursor.moveToFirst();
                    savePhone = String.valueOf(cursor.getLong(3));
                    savePassword = cursor.getString(6);

                    if (userPhone.equals(savePhone) && userPassword.equals(savePassword)) {
                        if(rememberMe.isChecked()){
                            SharedPreferences sharedPreferences = getSharedPreferences("LogIn", MODE_PRIVATE);
                            SharedPreferences.Editor  editor = sharedPreferences.edit();
                            editor.putString("phone", userPhone);
                            editor.putString("password", userPassword);
                            editor.putBoolean("loggedIn", true);
                            editor.apply();
                        }
                        Toast.makeText(ActivitySignIn.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivitySignIn.this, ActivityHome.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        signInPhoneNumber.setText("");
                        signInPassword.setText("");
                    } else if (!userPhone.equals(savePhone)) {
                        Toast.makeText(ActivitySignIn.this, "Phone number not matched", Toast.LENGTH_SHORT).show();
                        phoneNumberLayout.setError("Not registered yet");
                    } else {
                        passwordLayout.setError("Password not matched");
//                        Toast.makeText(ActivitySignIn.this, "Password not matched", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        signInSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivitySignIn.this, ActivitySignUp.class));
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
        signInForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySignIn.this, ActivityPhoneVerify.class);
                startActivity(intent);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
        rememberMe = findViewById(R.id.signInRememberMe);
        signInSignUp= findViewById(R.id.signInSignUp);
        signInBtn = findViewById(R.id.signInBtn);
    }
}