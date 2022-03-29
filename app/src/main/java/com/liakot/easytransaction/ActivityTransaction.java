package com.liakot.easytransaction;

//---------Liakot Ali Liton, ID : 1802035----------

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityTransaction extends AppCompatActivity {

    TextView nameTV, amountTV;
    TextInputEditText expenseET, getMoneyET, explanationET;
    TextInputLayout expenseLayout, getMonetLayout, explanationLayout;
    CircleImageView pictureCV;
    Button detailsBtn, addTransactionBtn;


    String name, address, type;
    long phone, amount, id;
    byte[] pictureByte;

    long totalCustomerRemain, totalToPayRemain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        InitializeAll();

        pictureCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTransaction.this, ActivityCustomerProfile.class);
                intent.putExtra("Id", id);
                intent.putExtra("Name", name);
                intent.putExtra("Phone", phone);
                intent.putExtra("Address", address);
                intent.putExtra("Picture", pictureByte);
                intent.putExtra("Amount", amount);
                intent.putExtra("Type", type);
                startActivity(intent);
            }
        });

        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTransaction.this, ActivityCustomerDetails.class);
                intent.putExtra("Id", id);
                intent.putExtra("Name", name);
                intent.putExtra("Phone", phone);
                intent.putExtra("Address", address);
                intent.putExtra("Picture", pictureByte);
                intent.putExtra("Amount", amount);
                intent.putExtra("Type", type);
                startActivity(intent);
                Toast.makeText(ActivityTransaction.this, "Details button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String expense, getMoney, explanation, date;

                Date d = new Date();
                date = DateFormat.format("dd-MM-yyyy hh:mm:ss a", d).toString();

                expense = expenseET.getText().toString();
                getMoney = getMoneyET.getText().toString();
                explanation = explanationET.getText().toString();

                if(expense.isEmpty())
                {
                    expenseLayout.setError("Invalid transaction");
                    getMonetLayout.setErrorEnabled(false);
                }
                else if(getMoney.isEmpty()){
                    getMonetLayout.setError("Invalid transaction");
                    expenseLayout.setErrorEnabled(false);
                }
                else{
                    expenseLayout.setErrorEnabled(false);
                    getMonetLayout.setErrorEnabled(false);

                    long totalExpense, totalGet, remain;
                    totalExpense = Long.parseLong(expense);
                    totalGet = Long.parseLong(getMoney);
                    remain = totalExpense - totalGet;

                    ClassDatabaseHelper helper = new ClassDatabaseHelper(ActivityTransaction.this);

                    ClassAddTransaction transaction = new ClassAddTransaction(date, explanation, id, totalExpense, totalGet, remain, type);
                    helper.addTransaction(transaction);
                    if(helper.transactionAdd)
                    {
                        Toast.makeText(ActivityTransaction.this, "New transaction added", Toast.LENGTH_SHORT).show();
                        expenseET.setText("");
                        getMoneyET.setText("");
                        explanationET.setText("");

                        expenseLayout.setSelected(false);
                        getMonetLayout.setSelected(false);
                        explanationLayout.setSelected(false);
                        amount = amount + remain;
                        amountTV.setText("Amount: " + amount);
                        //TODO-------- get the totalRemainAmount from shopDetails table-------
                        //----------add updated amount to database--------
                        if(type.equals("Customer")) {

                            //TODO----totalCustomerRemain amount will be changed-----
                            totalCustomerRemain += amount;
                            ClassAddCustomer upCustomer = new ClassAddCustomer("", 0, "", null, amount);
                            helper.updateCustomer(upCustomer, id);
                        }
                        else{
                            //TODO----totalToPayRemain amount will be changed-----
                            totalToPayRemain += amount;
                            ClassAddCustomer upToPay = new ClassAddCustomer("", 0, "", null, amount);
                            helper.updateToPay(upToPay, id);
                        }
                    }else{
                        Toast.makeText(ActivityTransaction.this, "Transaction failed", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    private void InitializeAll() {
        nameTV = findViewById(R.id.transCustomerName);
        amountTV = findViewById(R.id.transRemainingAmount);
        pictureCV = findViewById(R.id.transPicture);
        expenseET = findViewById(R.id.transExpense);
        getMoneyET = findViewById(R.id.transGetMoney);
        explanationET = findViewById(R.id.transExplanation);
        detailsBtn = findViewById(R.id.transDetailsBtn);
        addTransactionBtn = findViewById(R.id.transAddTransactionBtn);

        expenseLayout = findViewById(R.id.transExpenseLayout);
        getMonetLayout = findViewById(R.id.transGetMoneyLayout);
        explanationLayout = findViewById(R.id.transExplanationLayout);

        id = getIntent().getLongExtra("Id", -1);
        type = getIntent().getStringExtra("Type");
        ClassDatabaseHelper helper = new ClassDatabaseHelper(ActivityTransaction.this);
        Cursor cursor = null;
        if(type.equals("Customer")){
            cursor = helper.showCustomerInfo(id);
        }else{
            cursor = helper.showTopayInfo(id);
        }
        cursor.moveToFirst();

        name = cursor.getString(2);
        phone = cursor.getLong(1);
        address = cursor.getString(3);
        amount = cursor.getLong(5);
        pictureByte = cursor.getBlob(4);


        Bitmap bitmap;
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
    //TODO---------- updated customer profile ----------
}