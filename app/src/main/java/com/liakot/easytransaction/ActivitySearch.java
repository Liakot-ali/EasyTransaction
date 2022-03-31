package com.liakot.easytransaction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivitySearch extends AppCompatActivity {

    EditText search;
    RecyclerView searchView;
    ArrayList<ClassAddCustomer> arrayList, tempList;
    RecyclerView.LayoutManager manager;
    AdapterSearchList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        InitializeAll();

        tempList = arrayList;
        adapter = new AdapterSearchList(tempList, ActivitySearch.this);
        searchView.setAdapter(adapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Filter(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
//                Toast.makeText(ActivitySearch.this, s.toString(), Toast.LENGTH_SHORT).show();
                ArrayList<ClassAddCustomer> filterList;
                filterList = new ArrayList<>();
                for (ClassAddCustomer customer : arrayList) {
                    if (customer.getName().toLowerCase().contains(s.toString().toLowerCase()) ||
                            ("0" + customer.getPhone()).toLowerCase().contains(s.toString().toLowerCase()) ||
                    customer.getAddress().toLowerCase().contains(s.toString().toLowerCase())) {

                        filterList.add(customer);
                    }
                }
                adapter.filterList(filterList);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void Filter(String s) {

    }

    private void InitializeAll() {

        search = findViewById(R.id.searchView);
        searchView = findViewById(R.id.searchRecyclerView);
        manager = new LinearLayoutManager(ActivitySearch.this);

        searchView.setHasFixedSize(true);
        searchView.setLayoutManager(manager);

        arrayList = new ArrayList<>();
        tempList = new ArrayList<>();
        ClassDatabaseHelper helper = new ClassDatabaseHelper(ActivitySearch.this);
        Cursor cusCursor = helper.showCustomer();
        Cursor payCursor = helper.showToPay();

        if (cusCursor.getCount() != 0) {
            while (cusCursor.moveToNext()) {
                String name, address;
                byte[] picture;
                long phone, amount, id;
                name = cusCursor.getString(2);
                id = cusCursor.getLong(0);
                phone = cusCursor.getLong(1);
                amount = cusCursor.getLong(5);
                picture = cusCursor.getBlob(4);
                address = cusCursor.getString(3);
                ClassAddCustomer customer = new ClassAddCustomer(name, address, picture, id, phone, amount, "Customer");
                arrayList.add(customer);
            }
        }
        if (payCursor.getCount() != 0) {
            while (payCursor.moveToNext()) {
                String name, address;
                byte[] picture;
                long phone, amount, id;
                name = payCursor.getString(2);
                id = payCursor.getLong(0);
                phone = payCursor.getLong(1);
                amount = payCursor.getLong(5);
                picture = payCursor.getBlob(4);
                address = payCursor.getString(3);
                ClassAddCustomer customer = new ClassAddCustomer(name, address, picture, id, phone, amount, "ToPay");
                arrayList.add(customer);
            }
        }
    }
}