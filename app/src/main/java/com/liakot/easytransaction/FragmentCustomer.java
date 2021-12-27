package com.liakot.easytransaction;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentCustomer extends Fragment {
    View view;
    RecyclerView recyclerView;
    ArrayList<ClassAddCustomer> customerList;
    AdapterCustomerList adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView emptyText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_customer, container, false);

        layoutManager = new LinearLayoutManager(getContext());

        recyclerView = view.findViewById(R.id.customerRecyclerView);
        emptyText = view.findViewById(R.id.customerEmptyText);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        customerList = new ArrayList<>();

        ClassDatabaseHelper helper = new ClassDatabaseHelper(getContext());
        Cursor customerCursor = helper.showCustomer();

        if(customerCursor.getCount() == 0)
        {
            emptyText.setVisibility(View.VISIBLE);
        }
        else{
            emptyText.setVisibility(View.GONE);
            while (customerCursor.moveToNext())
            {
                String name, address;
                long phone, amount;
                byte[] picture;

                name = customerCursor.getString(1);
                address = customerCursor.getString(2);
                phone = customerCursor.getInt(0);
                amount = customerCursor.getInt(4);
                picture = customerCursor.getBlob(3);

                ClassAddCustomer customer = new ClassAddCustomer(name, phone, address, picture, amount);
                customerList.add(customer);
            }
            adapter = new AdapterCustomerList(getContext(), customerList);
            recyclerView.setAdapter(adapter);

        }

        return view;
    }
}