package com.liakot.easytransaction;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityAddCustomer extends AppCompatActivity {

    public final int PICK_IMAGE = 1;

    AutoCompleteTextView customerType;
    CircleImageView customerPicture, addPicture;
    TextInputEditText customerName, customerPhone, customerAddress;
    TextInputLayout nameLayout, phoneLayout, addressLayout, typeLayout;
    Button recordBtn;
    ArrayAdapter<String> adapter;
    Uri imageUri = null;
    byte[] imageByte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        InitializeAll();

        //-------for dropDown menu------------
        adapter = new ArrayAdapter<>(ActivityAddCustomer.this, R.layout.item_customer_type_dropdown, getResources().getStringArray(R.array.CustomerType));
        customerType.setAdapter(adapter);
        customerType.setText("Select Type", false);

        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type, name, phone, address, picture;

                type = customerType.getText().toString();
                name = customerName.getText().toString();
                phone = customerPhone.getText().toString();
                address = customerAddress.getText().toString();

                //----------convert imageUri to byte array to store as BLOB---------
                if(imageUri!=null)
                {
                    try{
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        imageByte = getByte(inputStream);
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

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
                            ClassAddCustomer newCustomer = new ClassAddCustomer(name, number, address, imageByte, 0);
                            helper.AddNewCustomer(newCustomer);
                            if (helper.customerAdd) {
                                customerName.setText("");
                                customerPhone.setText("");
                                customerAddress.setText("");
                                customerType.setText("Select Type", false);
                                customerPicture.setImageResource(R.drawable.icon_profile_24);
//                                Picasso.get().load(R.drawable.icon_profile_24).into(customerPicture);

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
                                ClassAddCustomer newCustomer = new ClassAddCustomer(name, number, address, imageByte, 0);
                                helper.AddNewCustomer(newCustomer);
                                if (helper.customerAdd) {
                                    customerName.setText("");
                                    customerPhone.setText("");
                                    customerAddress.setText("");
                                    customerType.setText("Select Type", false);
                                    customerPicture.setImageResource(R.drawable.icon_profile_24);
//                                    Picasso.get().load(R.drawable.icon_profile_24).into(customerPicture);

                                }
                            }
                        }

                    } else {
                        //------for ToPay type------
                        Cursor cursor = helper.showToPay();
                        if (cursor.getCount() == 0) {
                            //------Add the To Pay to database--------
                            ClassAddCustomer newCustomer = new ClassAddCustomer(name, number, address, imageByte, 0);
                            helper.AddNewToPay(newCustomer);
                            if (helper.toPayAdd) {
                                customerName.setText("");
                                customerPhone.setText("");
                                customerAddress.setText("");
                                customerType.setText("Select Type", false);
                                customerPicture.setImageResource(R.drawable.icon_profile_24);
//                                Picasso.get().load(R.drawable.icon_profile_24).into(customerPicture);

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
                                ClassAddCustomer newCustomer = new ClassAddCustomer(name, number, address, imageByte, 0);
                                helper.AddNewToPay(newCustomer);
                                if (helper.toPayAdd) {
                                    customerName.setText("");
                                    customerPhone.setText("");
                                    customerAddress.setText("");
                                    customerType.setText("Select Type", false);
                                    customerPicture.setImageResource(R.drawable.icon_profile_24);
//                                    Picasso.get().load(R.drawable.icon_profile_24).into(customerPicture);

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

    //------convert Uri to byte array--------
    private byte[] getByte(InputStream inputStream) throws IOException {

        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(customerPicture);
        }
        else{
            imageUri = null;
        }
    }

    //-------For drop down menu---------
    @Override
    protected void onResume() {
        super.onResume();

    }
}