package com.liakot.easytransaction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class ActivityAddCustomer extends AppCompatActivity {

    AutoCompleteTextView customerType;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        customerType = findViewById(R.id.addCustomerType);

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new ArrayAdapter<String>(ActivityAddCustomer.this, R.layout.customer_type_dropdown_item, getResources().getStringArray(R.array.CustomerType));
        customerType.setAdapter(adapter);
    }
}