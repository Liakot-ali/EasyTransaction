package com.liakot.easytransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Currency;

public class MainActivity extends AppCompatActivity {
    Button homeBtn;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String phone, password;
                boolean loggedIn = false;
                SharedPreferences sharedPreferences = getSharedPreferences("LogIn", MODE_PRIVATE);
                phone = sharedPreferences.getString("phone", "1234");
                password = sharedPreferences.getString("password", "pass");
                loggedIn = sharedPreferences.getBoolean("loggedIn", false);
                if(loggedIn){
                    ClassDatabaseHelper helper = new ClassDatabaseHelper(MainActivity.this);
                    Cursor cursor = helper.showShopInfo();
                    cursor.moveToFirst();
                    String savePhone, savePassword;
                    savePhone = String.valueOf(cursor.getLong(3));
                    savePassword = cursor.getString(6);

                    Intent intent;
                    if(phone.equals(savePhone) && password.equals(savePassword)){
                        intent = new Intent(MainActivity.this, ActivityHome.class);
                    }else{
                        intent = new Intent(MainActivity.this, ActivitySignIn.class);
                    }
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MainActivity.this, ActivitySignIn.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 500);



//        homeBtn = findViewById(R.id.homeBtn);
//        homeBtn.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               startActivity(new Intent(MainActivity.this, ActivitySignIn.class));
//               finish();
//           }
//       });
    }
}