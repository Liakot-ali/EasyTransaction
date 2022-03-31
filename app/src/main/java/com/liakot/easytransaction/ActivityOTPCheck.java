package com.liakot.easytransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ActivityOTPCheck extends AppCompatActivity {

    EditText otp;
    Button verifyBtn;
    TextView resendBtn, phone;

    String nameSt, phoneSt, passwordSt, verificationCode, status = null;
    long phoneLong;
    FirebaseAuth mAuth;
    ProgressBar progressBar, resentProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_check);
        InputMethodManager imm = (InputMethodManager)getSystemService(ActivityAddCustomer.INPUT_METHOD_SERVICE);
        InitializeAll();

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput;
                userInput = otp.getText().toString().trim();
                if (userInput.equals("")) {
                    verifyBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ActivityOTPCheck.this, "Enter the verification code", Toast.LENGTH_SHORT).show();
                } else {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    verifyBtn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthCredential phoneAuth = PhoneAuthProvider.getCredential(verificationCode, userInput);

                    mAuth.signInWithCredential(phoneAuth).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                verifyBtn.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);

                                ClassDatabaseHelper addShopValue = new ClassDatabaseHelper(ActivityOTPCheck.this);
                                //----for forgot password---------
                                if (status.equals("update")) {
                                    Intent intent = new Intent(ActivityOTPCheck.this, ActivitySetPassword.class);
                                    intent.putExtra("phone", phoneSt);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    verifyBtn.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    resentProgressbar.setVisibility(View.GONE);
                                    resendBtn.setVisibility(View.VISIBLE);

                                } else {
                                    //--------for create new shop--------
                                    ClassShop newShop = new ClassShop(nameSt, "", "", passwordSt, "", phoneLong, 0, 0, 0, 0, null);
                                    addShopValue.updateShop(newShop);
                                    if (addShopValue.shopInfoAdd) {
                                        Intent intent = new Intent(ActivityOTPCheck.this, ActivityHome.class);
//                                intent.putExtra("phone", phoneLong);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        verifyBtn.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(ActivityOTPCheck.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(ActivityOTPCheck.this, "Verification code is not matched", Toast.LENGTH_SHORT).show();
                                otp.requestFocus();
                                verifyBtn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendBtn.setVisibility(View.GONE);
                resentProgressbar.setVisibility(View.VISIBLE);
                verifyBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneSt, 60, TimeUnit.SECONDS, ActivityOTPCheck.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Toast.makeText(ActivityOTPCheck.this, "Verification completed", Toast.LENGTH_SHORT).show();
                        resendBtn.setVisibility(View.VISIBLE);
                        resentProgressbar.setVisibility(View.GONE);
                        verifyBtn.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(ActivityOTPCheck.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                        resendBtn.setVisibility(View.VISIBLE);
                        resentProgressbar.setVisibility(View.GONE);
                        verifyBtn.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        verificationCode = s;
                        Toast.makeText(ActivityOTPCheck.this, "Code sent successfully", Toast.LENGTH_SHORT).show();
                        resendBtn.setVisibility(View.VISIBLE);
                        resentProgressbar.setVisibility(View.GONE);
                        verifyBtn.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void InitializeAll() {

        mAuth = FirebaseAuth.getInstance();

        nameSt = getIntent().getStringExtra("name");
        phoneSt = getIntent().getStringExtra("phone");
        phoneLong = getIntent().getLongExtra("number", 0);
        passwordSt = getIntent().getStringExtra("password");
        verificationCode = getIntent().getStringExtra("OTP");
        status = getIntent().getStringExtra("status");

        otp = findViewById(R.id.otpCheckOTP);
        resendBtn = findViewById(R.id.otpCheckResendBtn);
        verifyBtn = findViewById(R.id.otpCheckVerifyBtn);
        phone = findViewById(R.id.otpCheckPhone);
        phone.setText(phoneSt);
        progressBar = findViewById(R.id.otpCheckProgressBar);
        resentProgressbar = findViewById(R.id.resentProgressBar);

    }
}