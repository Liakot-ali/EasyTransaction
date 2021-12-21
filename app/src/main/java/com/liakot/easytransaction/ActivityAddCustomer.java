package com.liakot.easytransaction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityAddCustomer extends AppCompatActivity {

    AutoCompleteTextView customerType;
    CircleImageView customerPicture, addPicture;
    TextInputEditText customerName, customerPhone, customerAddress;
    TextInputLayout nameLayout, phoneLayout, addressLayout, typeLayout;
    Button recordBtn;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        InitializeAll();

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type, name, phone, address, picture;
                type = customerType.getText().toString();
                name = customerName.getText().toString();
                phone = customerPhone.getText().toString();
                address = customerAddress.getText().toString();
                picture = customerPicture.getResources().toString();

                if (type.equals("Select Type")) {
                    typeLayout.setError("Select customer type");
                    nameLayout.setErrorEnabled(false);
                    phoneLayout.setErrorEnabled(false);
                    addressLayout.setErrorEnabled(false);
                } else if (name.isEmpty()) {
                    nameLayout.setError("Name is empty");
                    phoneLayout.setErrorEnabled(false);
                    addressLayout.setErrorEnabled(false);
                    typeLayout.setErrorEnabled(false);
                } else if (phone.isEmpty()) {
                    phoneLayout.setError("Phone is empty");
                    nameLayout.setErrorEnabled(false);
                    addressLayout.setErrorEnabled(false);
                    typeLayout.setErrorEnabled(false);
                } else if (phone.length() != 10) {
                    phoneLayout.setError("Invalid phone number");
                    nameLayout.setErrorEnabled(false);
                    addressLayout.setErrorEnabled(false);
                    typeLayout.setErrorEnabled(false);

                }
                else if (address.isEmpty()) {
                    addressLayout.setError("Address is empty");
                    phoneLayout.setErrorEnabled(false);
                    nameLayout.setErrorEnabled(false);
                    typeLayout.setErrorEnabled(false);
                } else {
                    //------Add the data to database--------
                    nameLayout.setErrorEnabled(false);
                    phoneLayout.setErrorEnabled(false);
                    addressLayout.setErrorEnabled(false);
                    typeLayout.setErrorEnabled(false);
                    ClassAddCustomer newCustomer = new ClassAddCustomer(type, name, phone, address, picture);

                    ClassDatabaseHelper helper = new ClassDatabaseHelper(ActivityAddCustomer.this);
                    helper.AddNewCustomer(newCustomer);




                    Toast.makeText(ActivityAddCustomer.this, "New customer Added", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
//-----Out of main section-----------
    private void InitializeAll() {

        customerType = findViewById(R.id.addCustomerType);
        customerPicture = findViewById(R.id.addCustomerPicture);
        customerName = findViewById(R.id.addCustomerName);
        customerPhone = findViewById(R.id.addCustomerPhone);
        customerAddress = findViewById(R.id.addCustomerAddress);
        addPicture = findViewById(R.id.addCustomerAddPicture);
        recordBtn = findViewById(R.id.addCustomerRecordBtn);

        typeLayout = findViewById(R.id.addCustomerTypeLayout);
        nameLayout = findViewById(R.id.addCustomerNameLayout);
        phoneLayout = findViewById(R.id.addCustomerPhoneLayout);
        addressLayout = findViewById(R.id.addCustomerAddressLayout);


    }


    //-------For drop down menu---------
    @Override
    protected void onResume() {
        super.onResume();
        adapter = new ArrayAdapter<>(ActivityAddCustomer.this, R.layout.customer_type_dropdown_item, getResources().getStringArray(R.array.CustomerType));
        customerType.setAdapter(adapter);
    }
}