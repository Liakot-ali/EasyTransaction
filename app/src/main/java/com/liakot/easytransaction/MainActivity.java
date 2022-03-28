package com.liakot.easytransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button homeBtn;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Test");

        EditText text = findViewById(R.id.firebaseText);
        Button add = findViewById(R.id.addToFirebase);

        homeBtn = findViewById(R.id.homeBtn);
       homeBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(MainActivity.this, ActivitySignIn.class));
           }
       });

       add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String st = text.getText().toString();
               ref.setValue(st).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       Toast.makeText(MainActivity.this, "Sucessfully upload", Toast.LENGTH_SHORT).show();
                       text.setText("");
                   }
               });
           }
       });
    }
}