package com.liakot.easytransaction;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityTransaction extends AppCompatActivity {

    TextView nameTV, amountTV;
    CircleImageView pictureCV;
    Button detailsBtn, addTransactionBtn;


    String name, address;
    long phone, amount;
    byte[] pictureByte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        InitializeAll();

        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityTransaction.this, "Details button clicked", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void InitializeAll() {

        Bitmap bitmap;

        nameTV = findViewById(R.id.transCustomerName);
        amountTV = findViewById(R.id.transRemainingAmount);
        pictureCV = findViewById(R.id.transPicture);
        detailsBtn = findViewById(R.id.transDetailsBtn);
        addTransactionBtn = findViewById(R.id.transAddTransactionBtn);

        name = getIntent().getStringExtra("Name");
        phone = getIntent().getLongExtra("Phone", 0);
        address = getIntent().getStringExtra("Address");
        amount = getIntent().getLongExtra("Amount", 0);
        pictureByte = getIntent().getByteArrayExtra("Picture");

        if(pictureByte != null) {
            bitmap = BitmapFactory.decodeByteArray(pictureByte, 0, pictureByte.length);
            pictureCV.setImageBitmap(bitmap);
        }
        else{
            pictureCV.setImageResource(R.drawable.icon_profile_24);
        }

        nameTV.setText(name);
        amountTV.setText("Amount: " + amount);
    }
}