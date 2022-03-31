package com.liakot.easytransaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ActivitySetPassword extends AppCompatActivity {

    TextInputLayout passwordLay, conPasswordLay;
    TextInputEditText password, conPassword;
    Button submitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        InputMethodManager imm = (InputMethodManager)getSystemService(ActivityAddCustomer.INPUT_METHOD_SERVICE);

        InitializeAll();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass, conPass;
                pass = password.getText().toString();
                conPass = conPassword.getText().toString();

                if (pass.isEmpty()) {
                    passwordLay.setError("Password is empty");
                    passwordLay.requestFocus();
                    conPasswordLay.setErrorEnabled(false);
                } else if (pass.length()<=6) {
                    passwordLay.setError("Password must contain more than 6 characters");
                    passwordLay.requestFocus();
                    conPasswordLay.setErrorEnabled(false);
                } else if (!pass.matches(conPass)) {
                    conPasswordLay.setError("password and confirm password not matched");
                    conPasswordLay.requestFocus();
                    passwordLay.setErrorEnabled(false);
                } else{
                    passwordLay.setErrorEnabled(false);
                    conPasswordLay.setErrorEnabled(false);
                    passwordLay.clearFocus();
                    conPasswordLay.clearFocus();

                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    ClassDatabaseHelper helper = new ClassDatabaseHelper(ActivitySetPassword.this);
                    Cursor cursor = helper.showShopInfo();
                    cursor.moveToFirst();

                    String name, owner, category, address;
                    long phone;
                    byte[] picture = null;

                    name = cursor.getString(0);
                    owner = cursor.getString(1);
                    category = cursor.getString(2);
                    phone = cursor.getLong(3);
                    address = cursor.getString(4);
                    picture = cursor.getBlob(5);

                    ClassShop upShop = new ClassShop(name, owner, category, pass, address, phone, 0, 0, 0, 0, picture);
                    helper.updateShopInfo(upShop);
                    if(helper.shopInfoUpdate){
                        Toast.makeText(ActivitySetPassword.this, "Reset password successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivitySetPassword.this, ActivityHome.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(ActivitySetPassword.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void InitializeAll() {
        password = findViewById(R.id.setPassword);
        conPassword = findViewById(R.id.setConfirmPassword);
        passwordLay = findViewById(R.id.setPasswordLay);
        conPasswordLay = findViewById(R.id.setConfirmPasswordLay);
        submitBtn = findViewById(R.id.setSubmitBtn);
    }
}