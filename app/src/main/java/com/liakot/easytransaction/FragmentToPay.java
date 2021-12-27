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

public class FragmentToPay extends Fragment {
    View view;
    ArrayList<ClassAddCustomer> toPayList;
    RecyclerView toPayListView;
    RecyclerView.LayoutManager layoutManager;
    AdapterToPayList adapter;

    TextView emptyText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_to_pay, container, false);

        layoutManager = new LinearLayoutManager(getContext());


        toPayListView = view.findViewById(R.id.toPayRecyclerView);
        emptyText = view.findViewById(R.id.toPayEmptyText);
        toPayList = new ArrayList<>();

        toPayListView.setHasFixedSize(true);
        toPayListView.setLayoutManager(layoutManager);


        ClassDatabaseHelper helper = new ClassDatabaseHelper(getContext());

        Cursor toPayCursor = helper.showToPay();
        if(toPayCursor.getCount() == 0)
        {
            emptyText.setVisibility(View.VISIBLE);
        }
        else{
            emptyText.setVisibility(View.GONE);

            while (toPayCursor.moveToNext())
            {
                String name, address;
                long phone, amount;
                byte[] picture;

                name = toPayCursor.getString(1);
                address = toPayCursor.getString(2);
                phone = toPayCursor.getInt(0);
                amount = toPayCursor.getInt(4);
                picture = toPayCursor.getBlob(3);

                ClassAddCustomer toPay = new ClassAddCustomer(name, phone, address, picture, amount);
                toPayList.add(toPay);
            }

            adapter = new AdapterToPayList(getContext(), toPayList);
            toPayListView.setAdapter(adapter);
        }

        return view;
    }
}