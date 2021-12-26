package com.liakot.easytransaction;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

                //TODO--get Picture url ---------
                picture = customerPicture.getResources().toString();

                long number = 2000000000;
                if (!phone.isEmpty() && !phone.contains("%") && !phone.contains(":") && !phone.contains(" ") && !phone.contains(".")) {
                    number = Long.parseLong(phone);
                }

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
                } else if (phone.length() != 10 || number > 1999999999 || number < 999999999) {
                    phoneLayout.setError("Invalid phone number");
                    nameLayout.setErrorEnabled(false);
                    addressLayout.setErrorEnabled(false);
                    typeLayout.setErrorEnabled(false);
                } else if (address.isEmpty()) {
                    addressLayout.setError("Address is empty");
                    phoneLayout.setErrorEnabled(false);
                    nameLayout.setErrorEnabled(false);
                    typeLayout.setErrorEnabled(false);
                } else {
                    ClassDatabaseHelper helper = new ClassDatabaseHelper(ActivityAddCustomer.this);
                    nameLayout.setErrorEnabled(false);
                    phoneLayout.setErrorEnabled(false);
                    addressLayout.setErrorEnabled(false);
                    typeLayout.setErrorEnabled(false);

                    if (type.equals("Customer")) {
                        //------------for customer type--------
                        Cursor cursor = helper.showCustomer();

                        if (cursor.getCount() == 0) {
                            //---------add the customer to database--------
                            ClassAddCustomer newCustomer = new ClassAddCustomer(name, number, address, picture, 0);
                            helper.AddNewCustomer(newCustomer);
                            if (helper.customerAdd) {
                                customerName.setText("");
                                customerPhone.setText("");
                                customerAddress.setText("");
                                customerType.setText("Select Type", false);
                                //TODO----set default picture------

                            }
                        } else {
                            boolean exist = false;
                            while (cursor.moveToNext()) {
                                if (number == cursor.getInt(0)) {
                                    phoneLayout.setError("This number is already exist in Customer type");
                                    exist = true;
                                    break;
                                }
                            }
                            if (!exist) {
                                //------Add the customer to database--------
                                ClassAddCustomer newCustomer = new ClassAddCustomer(name, number, address, picture, 0);
                                helper.AddNewCustomer(newCustomer);
                                if (helper.customerAdd) {
                                    customerName.setText("");
                                    customerPhone.setText("");
                                    customerAddress.setText("");
                                    customerType.setText("Select Type", false);
                                    //TODO----set default picture------

                                }
                            }
                        }

                    } else {
                        //------for ToPay type------
                        Cursor cursor = helper.showToPay();
                        if (cursor.getCount() == 0) {
                            //------Add the To Pay to database--------
                            ClassAddCustomer newCustomer = new ClassAddCustomer(name, number, address, picture, 0);
                            helper.AddNewToPay(newCustomer);
                            if (helper.toPayAdd) {
                                customerName.setText("");
                                customerPhone.setText("");
                                customerAddress.setText("");
                                customerType.setText("Select Type", false);
                                //TODO----set default picture------

                            }
                        } else {
                            boolean exist = false;
                            while (cursor.moveToNext()) {
                                if (number == cursor.getInt(0)) {
                                    phoneLayout.setError("This number is already exist in To Pay type");
                                    exist = true;
                                    break;
                                }
                            }
                            if (!exist) {
                                //------Add the customer to database--------
                                ClassAddCustomer newCustomer = new ClassAddCustomer(name, number, address, picture, 0);
                                helper.AddNewToPay(newCustomer);
                                if (helper.toPayAdd) {
                                    customerName.setText("");
                                    customerPhone.setText("");
                                    customerAddress.setText("");
                                    customerType.setText("Select Type", false);
                                    //TODO----set default picture------

                                }
                            }
                        }

                    }

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
        adapter = new ArrayAdapter<>(ActivityAddCustomer.this, R.layout.item_customer_type_dropdown, getResources().getStringArray(R.array.CustomerType));
        customerType.setAdapter(adapter);
        customerType.setText("Select Type", false);
    }
}