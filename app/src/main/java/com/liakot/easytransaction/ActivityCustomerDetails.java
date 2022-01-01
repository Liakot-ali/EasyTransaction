package com.liakot.easytransaction;

//---------Liakot Ali Liton, ID : 1802035---------

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityCustomerDetails extends AppCompatActivity {

    TextView detailsName, detailsPhone;
    CircleImageView toolbarPicture;
    ImageView detailsDelete;
    LinearLayout fieldLayout;


    RecyclerView recyclerView;
    AdapterTransactionList adapter;
    ArrayList<ClassAddTransaction> arrayList;
    RecyclerView.LayoutManager layoutManager;
    TextView emptyText;

    String name, address, type;
    long phone, amount;
    byte[] picture;

    Cursor cursor = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        InitializeAll();


        ClassDatabaseHelper helper = new ClassDatabaseHelper(ActivityCustomerDetails.this);
        cursor = helper.showTransaction(phone, type);
        if(cursor.getCount() == 0)
        {
            fieldLayout.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
        else {
            fieldLayout.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
            while (cursor.moveToNext())
            {
                String date, explanation;
                long expense, getMoney, remain;

                date = cursor.getString(0);
                explanation = cursor.getString(1);
                expense = cursor.getInt(2);
                getMoney = cursor.getInt(3);
                remain = cursor.getInt(4);

                ClassAddTransaction transaction = new ClassAddTransaction(date, explanation, phone, expense, getMoney, remain, type);
                arrayList.add(transaction);
            }

            adapter = new AdapterTransactionList(ActivityCustomerDetails.this, arrayList);
            recyclerView.setAdapter(adapter);
        }
    }

    private void InitializeAll() {


        name = getIntent().getStringExtra("Name");
        address = getIntent().getStringExtra("Address");
        phone = getIntent().getLongExtra("Phone", 0);
        amount = getIntent().getLongExtra("Amount", 0);
        picture = getIntent().getByteArrayExtra("Picture");
        type = getIntent().getStringExtra("Type");

        layoutManager = new LinearLayoutManager(ActivityCustomerDetails.this);

        recyclerView = findViewById(R.id.transDetailsRecyclerView);
        arrayList =  new ArrayList<>();
        detailsName = findViewById(R.id.detailsCustomerName);
        detailsPhone = findViewById(R.id.detailsPhoneNumber);
        detailsDelete = findViewById(R.id.detailsDelete);

        emptyText = findViewById(R.id.detailsEmptyText);
        fieldLayout = findViewById(R.id.detailsFieldHeadlineLayout);

        toolbarPicture = findViewById(R.id.detailsCustomerPicture);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //------ set customer profile----
        detailsName.setText(name);
        detailsPhone.setText(String.valueOf(phone));
        //--------set picture in circle image view--------------
        Bitmap bitmap;
        if(picture != null) {
            bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            toolbarPicture.setImageBitmap(bitmap);
        }
        else{
            toolbarPicture.setImageResource(R.drawable.icon_profile_24);
        }
    }
}