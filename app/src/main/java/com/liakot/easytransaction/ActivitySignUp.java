package com.liakot.easytransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivitySignUp extends AppCompatActivity {

    Button signUpBtn;
    TextView signInBtn;
    TextInputEditText name, phone, password, conPassword;
    TextInputLayout nameLay, phoneLay, passwordLay, conPasswordLay;
    CircleImageView logo;

    ProgressBar progressBar;

    FirebaseAuth newShop;

    public PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        InputMethodManager imm = (InputMethodManager)getSystemService(ActivityAddCustomer.INPUT_METHOD_SERVICE);
        InitializeAll();


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameSt, phoneSt, passwordSt, conPasswordSt;
                nameSt = name.getText().toString();
                phoneSt = phone.getText().toString();
                passwordSt = password.getText().toString();
                conPasswordSt = conPassword.getText().toString();

                long number = 2000000000;
                if (!phoneSt.isEmpty() && !phoneSt.contains("%") && !phoneSt.contains(":") && !phoneSt.contains(" ") && !phoneSt.contains(".")) {
                    number = Long.parseLong(phoneSt);
                }

                if (nameSt.isEmpty()) {
                    nameLay.setError("Name is empty");
                    phoneLay.setErrorEnabled(false);
                    passwordLay.setErrorEnabled(false);
                    conPasswordLay.setErrorEnabled(false);
                } else if (phoneSt.isEmpty()) {
                    phoneLay.setError("Phone is empty");
                    nameLay.setErrorEnabled(false);
                    passwordLay.setErrorEnabled(false);
                    conPasswordLay.setErrorEnabled(false);
                } else if ((phone.length() < 10  && phone.length() > 11) || number > 1999999999 || number < 999999999) {
                    phoneLay.setError("Invalid phone number");
                    nameLay.setErrorEnabled(false);
                    passwordLay.setErrorEnabled(false);
                    conPasswordLay.setErrorEnabled(false);
                } else if (passwordSt.isEmpty()) {
                    passwordLay.setError("Password is empty");
                    phoneLay.setErrorEnabled(false);
                    nameLay.setErrorEnabled(false);
                    conPasswordLay.setErrorEnabled(false);
                } else if (passwordSt.length()<=6) {
                    passwordLay.setError("Password must contain more than 6 characters");
                    phoneLay.setErrorEnabled(false);
                    nameLay.setErrorEnabled(false);
                    conPasswordLay.setErrorEnabled(false);
                } else if (!passwordSt.matches(conPasswordSt)) {
                    conPasswordLay.setError("password and confirm password not matched");
                    phoneLay.setErrorEnabled(false);
                    nameLay.setErrorEnabled(false);
                    passwordLay.setErrorEnabled(false);
                }else{
                    signUpBtn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    nameLay.setErrorEnabled(false);
                    phoneLay.setErrorEnabled(false);
                    passwordLay.setErrorEnabled(false);
                    conPasswordLay.setErrorEnabled(false);
                    nameLay.clearFocus();
                    phoneLay.clearFocus();
                    passwordLay.clearFocus();
                    conPasswordLay.clearFocus();

                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    if(phoneSt.length() == 10){
                        phoneSt = "+880" + phoneSt;
                    }else{
                        phoneSt = "+88" + phoneSt;
                    }

                    String finalPhoneSt = phoneSt;

                    long finalNumber = number;
                    mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            Toast.makeText(ActivitySignUp.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                            signUpBtn.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            Toast.makeText(ActivitySignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            signUpBtn.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            verificationCode = s;
                            Toast.makeText(ActivitySignUp.this, "Code sent successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ActivitySignUp.this, ActivityOTPCheck.class);
                            intent.putExtra("name", nameSt);
                            intent.putExtra("phone", finalPhoneSt);
                            intent.putExtra("number", finalNumber);
                            intent.putExtra("password", passwordSt);
                            intent.putExtra("OTP", verificationCode);
                            intent.putExtra("status", "new");
                            startActivity(intent);
                            finish();
                        }
                    };

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneSt, 60, TimeUnit.SECONDS, ActivitySignUp.this, mCallback);

                }
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                Intent intent = new Intent(ActivitySignUp.this, ActivitySignIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void InitializeAll() {

        newShop = FirebaseAuth.getInstance();

        signUpBtn = findViewById(R.id.signUpBtn);
        signInBtn = findViewById(R.id.signUpSignInBtn);
        logo = findViewById(R.id.signUpAppLogo);

        name = findViewById(R.id.signUpShopName);
        phone = findViewById(R.id.signUpPhone);
        password = findViewById(R.id.signUpPassword);
        conPassword = findViewById(R.id.signUpConfirmPassword);

        nameLay = findViewById(R.id.signUpShopNameLayout);
        phoneLay = findViewById(R.id.signUpPhoneLayout);
        passwordLay = findViewById(R.id.signUpPasswordLayout);
        conPasswordLay = findViewById(R.id.signUpConfirmPasswordLayout);

        progressBar = findViewById(R.id.signUpProgressBar);

    }
}