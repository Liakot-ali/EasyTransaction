package com.liakot.easytransaction;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
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
    LinearLayout fieldLayout, totalLayout;
    TextView totalExpense, totalGet, totalRemain;


    RecyclerView recyclerView;
    AdapterTransactionList adapter;
    ArrayList<ClassAddTransaction> arrayList;
    RecyclerView.LayoutManager layoutManager;
    TextView emptyText;

    String name, address, type;
    long phone, amount, id;
    byte[] picture;

    Cursor cursor = null;

    long expenseTotal = 0, getTotal = 0, remainTotal = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        InitializeAll();

        ClassDatabaseHelper helper = new ClassDatabaseHelper(ActivityCustomerDetails.this);
        cursor = helper.showTransaction(id, type);
        if(cursor.getCount() == 0)
        {
            fieldLayout.setVisibility(View.GONE);
            totalLayout.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
        else {
            fieldLayout.setVisibility(View.VISIBLE);
            totalLayout.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
            while (cursor.moveToNext())
            {
                String date, explanation;
                long transId, expense, getMoney, remain;

                transId = cursor.getLong(0);
                date = cursor.getString(1);
                explanation = cursor.getString(3);
                expense = cursor.getInt(4);
                getMoney = cursor.getInt(5);
                remain = cursor.getInt(6);

                expenseTotal += expense;
                getTotal += getMoney;
                remainTotal += remain;

                ClassAddTransaction transaction = new ClassAddTransaction(transId, date, explanation, id, expense, getMoney, remain, type);
                arrayList.add(transaction);
            }

            adapter = new AdapterTransactionList(ActivityCustomerDetails.this, arrayList);
            recyclerView.setAdapter(adapter);

            totalExpense.setText(getResources().getString(R.string.tk_sign) + expenseTotal);
            totalGet.setText(getResources().getString(R.string.tk_sign) + getTotal);
            totalRemain.setText(getResources().getString(R.string.tk_sign) + remainTotal);

        }


        detailsName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCustomerDetails.this, ActivityCustomerProfile.class);
                intent.putExtra("Id", id);
                intent.putExtra("Name", name);
                intent.putExtra("Phone", phone);
                intent.putExtra("Address", address);
                intent.putExtra("Picture", picture);
                intent.putExtra("Amount", amount);
                intent.putExtra("Type", type);
                startActivity(intent);
            }
        });
        toolbarPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCustomerDetails.this, ActivityCustomerProfile.class);
                intent.putExtra("Id", id);
                intent.putExtra("Name", name);
                intent.putExtra("Phone", phone);
                intent.putExtra("Address", address);
                intent.putExtra("Picture", picture);
                intent.putExtra("Amount", amount);
                intent.putExtra("Type", type);
                startActivity(intent);
            }
        });

        detailsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityCustomerDetails.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Do you want to delete this customer?");
                dialog.setIcon(R.drawable.icon_delete_black);
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClassDatabaseHelper helper = new ClassDatabaseHelper(ActivityCustomerDetails.this);
                        if(helper.DeleteCustomer(id, type)){
                            long totalRemain, totalPayable, customerNo, toPayNo;
                            Cursor shopCursor = helper.showShopInfo();
                            shopCursor.moveToFirst();
                            totalRemain = shopCursor.getLong(7);
                            totalPayable = shopCursor.getLong(8);
                            customerNo = shopCursor.getLong(9);
                            toPayNo = shopCursor.getLong(10);
                            if(type.equals("Customer")){
                                totalRemain -= remainTotal;
                                customerNo--;
                            }else{
                                totalPayable -= remainTotal;
                                toPayNo--;
                            }
                            expenseTotal = 0;
                            getTotal = 0;
                            ClassShop upShop = new ClassShop("", "", "", "", "", 0, totalRemain, totalPayable, customerNo, toPayNo, null);
                            helper.updateShopAmount(upShop);
                            Toast.makeText(ActivityCustomerDetails.this, "Customer deleted successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ActivityCustomerDetails.this, ActivityHome.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                dialog.setNegativeButton("No", null);
                dialog.setCancelable(true);
                dialog.show();
//                Toast.makeText(ActivityCustomerDetails.this, "Delete button clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InitializeAll() {

        id = getIntent().getLongExtra("Id", -1);
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
        totalLayout = findViewById(R.id.detailsTotalLayout);

        totalExpense = findViewById(R.id.detailsTotalExpense);
        totalGet = findViewById(R.id.detailsTotalGet);
        totalRemain = findViewById(R.id.detailsTotalRemain);

        toolbarPicture = findViewById(R.id.detailsCustomerPicture);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //------ set customer profile----
        detailsName.setText(name);
        detailsPhone.setText("+880" + String.valueOf(phone));
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