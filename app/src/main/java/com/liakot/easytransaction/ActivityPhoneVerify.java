package com.liakot.easytransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ActivityPhoneVerify extends AppCompatActivity {

    TextInputEditText phoneNumber;
    TextInputLayout phoneLay;
    Button verifyBtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    public PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);

        InputMethodManager imm = (InputMethodManager)getSystemService(ActivityAddCustomer.INPUT_METHOD_SERVICE);

        InitializeAll();
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneNumber.getText().toString();

                long number = 2000000000;
                if (!phone.isEmpty() && !phone.contains("%") && !phone.contains(":") && !phone.contains(" ") && !phone.contains(".")) {
                    number = Long.parseLong(phone);
                }

                if (phone.isEmpty()) {
                    phoneLay.setError("Phone is empty");
                    phoneLay.requestFocus();
                } else if (phone.length() < 10 || phone.length() > 11  || number > 1999999999 || number < 999999999) {
                    phoneLay.setError("Invalid phone number");
                    phoneLay.requestFocus();
                }else{
                    ClassDatabaseHelper helper =  new ClassDatabaseHelper(ActivityPhoneVerify.this);
                    Cursor cursor = helper.showShopInfo();
                    cursor.moveToFirst();
                    long savePhone = cursor.getLong(3);
                    if(savePhone != number){
                        phoneLay.setError("Number not matched");
                        phoneLay.requestFocus();
//                        Toast.makeText(ActivityPhoneVerify.this, String.valueOf(savePhone), Toast.LENGTH_SHORT).show();
                    }else{
                        phoneLay.clearFocus();
                        phoneLay.setErrorEnabled(false);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                        progressBar.setVisibility(View.VISIBLE);
                        verifyBtn.setVisibility(View.GONE);

                        if(phone.length() == 10){
                            phone = "+880" + phone;
                        }else{
                            phone = "+88" + phone;
                        }

                        String finalPhone = phone;
                        long finalNumber = number;
                        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast.makeText(ActivityPhoneVerify.this, "Verification completed", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                verifyBtn.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(ActivityPhoneVerify.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                verifyBtn.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                Toast.makeText(ActivityPhoneVerify.this, "Code sent successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ActivityPhoneVerify.this, ActivityOTPCheck.class);
                                intent.putExtra("phone", finalPhone);
                                intent.putExtra("number", finalNumber);
                                intent.putExtra("OTP", s);
                                intent.putExtra("status", "update");
                                startActivity(intent);
                                progressBar.setVisibility(View.GONE);
                                verifyBtn.setVisibility(View.VISIBLE);
                            }
                        };

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60, TimeUnit.SECONDS, ActivityPhoneVerify.this, mCallback);
                    }
                }
            }
        });

        InitializeAll();
    }

    private void InitializeAll() {
        mAuth = FirebaseAuth.getInstance();
        phoneNumber = findViewById(R.id.verifyPhone);
        phoneLay = findViewById(R.id.verifyPhoneLayout);
        verifyBtn = findViewById(R.id.verifyBtn);
        progressBar = findViewById(R.id.verifyProgresbar);
    }
}