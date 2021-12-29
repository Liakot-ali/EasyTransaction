package com.liakot.easytransaction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Layout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityCustomerDetails extends AppCompatActivity {

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
            //TODO----show empty text-------
            Toast.makeText(ActivityCustomerDetails.this, "No transaction", Toast.LENGTH_SHORT).show();
        }
        else {
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

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //--TODO---- set customer profile----
    }
}