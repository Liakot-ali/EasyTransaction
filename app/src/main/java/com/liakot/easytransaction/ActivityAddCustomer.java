package com.liakot.easytransaction;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityAddCustomer extends AppCompatActivity {

    public final int PICK_IMAGE = 1;

    AutoCompleteTextView customerType;
    CircleImageView customerPicture, addPicture;
    TextInputEditText customerName, customerPhone, customerAddress;
    TextInputLayout nameLayout, phoneLayout, addressLayout, typeLayout;
    TextView imageSizeTv;
    Button recordBtn;
    ArrayAdapter<String> adapter;
    Uri imageUri = null;
    byte[] imageByte = null;

    long totalRemain, totalPayable, customerNo, payableNo, imageSize = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        InputMethodManager imm = (InputMethodManager)getSystemService(ActivityAddCustomer.INPUT_METHOD_SERVICE);

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
                    imageSizeTv.setVisibility(View.GONE);
                } else if(imageSize >= 500){
                    imageSizeTv.setVisibility(View.VISIBLE);
                    Toast.makeText(ActivityAddCustomer.this, "This image size is " + imageSize + "Kb", Toast.LENGTH_SHORT).show();
                }
                else if (name.isEmpty()) {
                    nameLayout.setError("Name is empty");
                    nameLayout.requestFocus();
                    imm.toggleSoftInput(1, 1);
                    phoneLayout.setErrorEnabled(false);
                    addressLayout.setErrorEnabled(false);
                    typeLayout.setErrorEnabled(false);
                    imageSizeTv.setVisibility(View.GONE);
                } else if (phone.isEmpty()) {
                    phoneLayout.setError("Phone is empty");
                    phoneLayout.requestFocus();
                    imm.toggleSoftInput(1, 1);
                    nameLayout.setErrorEnabled(false);
                    addressLayout.setErrorEnabled(false);
                    typeLayout.setErrorEnabled(false);
                    imageSizeTv.setVisibility(View.GONE);
                } else if (phone.length() != 10 || number > 1999999999 || number < 999999999) {
                    phoneLayout.setError("Invalid phone number");
                    phoneLayout.requestFocus();
                    imm.toggleSoftInput(1, 1);
                    nameLayout.setErrorEnabled(false);
                    addressLayout.setErrorEnabled(false);
                    typeLayout.setErrorEnabled(false);
                    imageSizeTv.setVisibility(View.GONE);
                } else if (address.isEmpty()) {
                    addressLayout.setError("Address is empty");
                    addressLayout.requestFocus();
                    imm.toggleSoftInput(1, 1);
                    phoneLayout.setErrorEnabled(false);
                    nameLayout.setErrorEnabled(false);
                    typeLayout.setErrorEnabled(false);
                    imageSizeTv.setVisibility(View.GONE);
                } else {
                    ClassDatabaseHelper helper = new ClassDatabaseHelper(ActivityAddCustomer.this);
                    nameLayout.setErrorEnabled(false);
                    phoneLayout.setErrorEnabled(false);
                    addressLayout.setErrorEnabled(false);
                    typeLayout.setErrorEnabled(false);
                    imageSizeTv.setVisibility(View.GONE);

                    //-------hide keyboard--------
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    nameLayout.clearFocus();
                    phoneLayout.clearFocus();
                    addressLayout.clearFocus();

                    if (type.equals("Customer")) {
                        //------------for customer type--------
                        Cursor cursor = helper.showCustomer();

                        if (cursor.getCount() == 0) {
                            //---------add the customer to database--------
                            ClassAddCustomer newCustomer = new ClassAddCustomer(name, number, address, imageByte, 0);
                            helper.AddNewCustomer(newCustomer);
                            if (helper.customerAdd) {
                                customerNo++;
                                Toast.makeText(ActivityAddCustomer.this, "New customer added", Toast.LENGTH_SHORT).show();
                                customerName.setText("");
                                customerPhone.setText("");
                                customerAddress.setText("");
                                customerType.setText("Select Type", false);
                                customerPicture.setImageResource(R.drawable.icon_profile_24);
                            }else{
                                Toast.makeText(ActivityAddCustomer.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                                    customerNo++;
                                    Toast.makeText(ActivityAddCustomer.this, "New customer added", Toast.LENGTH_SHORT).show();
                                    customerName.setText("");
                                    customerPhone.setText("");
                                    customerAddress.setText("");
                                    customerType.setText("Select Type", false);
                                    customerPicture.setImageResource(R.drawable.icon_profile_24);
                                }else{
                                    Toast.makeText(ActivityAddCustomer.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                                payableNo++;
                                Toast.makeText(ActivityAddCustomer.this, "New customer added", Toast.LENGTH_SHORT).show();
                                customerName.setText("");
                                customerPhone.setText("");
                                customerAddress.setText("");
                                customerType.setText("Select Type", false);
                                customerPicture.setImageResource(R.drawable.icon_profile_24);
                            }else{
                                Toast.makeText(ActivityAddCustomer.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                                    payableNo++;
                                    Toast.makeText(ActivityAddCustomer.this, "New customer added", Toast.LENGTH_SHORT).show();
                                    customerName.setText("");
                                    customerPhone.setText("");
                                    customerAddress.setText("");
                                    customerType.setText("Select Type", false);
                                    customerPicture.setImageResource(R.drawable.icon_profile_24);
                                }else{
                                    Toast.makeText(ActivityAddCustomer.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }

                    //---------update customer no in shop details--------------
                    ClassShop upShop = new ClassShop("", "", "", "", "", 0, totalRemain, totalPayable, customerNo, payableNo, null);
                    helper.updateShopAmount(upShop);
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
        imageSizeTv = findViewById(R.id.addCustomerImageSize);

        typeLayout = findViewById(R.id.addCustomerTypeLayout);
        nameLayout = findViewById(R.id.addCustomerNameLayout);
        phoneLayout = findViewById(R.id.addCustomerPhoneLayout);
        addressLayout = findViewById(R.id.addCustomerAddressLayout);

        //---------get the shop info---------
        ClassDatabaseHelper helper = new ClassDatabaseHelper(ActivityAddCustomer.this);
        Cursor cursor = null;
        cursor = helper.showShopInfo();
        cursor.moveToFirst();

        totalRemain = cursor.getLong(7);
        totalPayable = cursor.getLong(8);
        customerNo = cursor.getLong(9);
        payableNo = cursor.getInt(10);

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

            //------for calculate the image size----------
            Cursor returnCursor = this.getContentResolver().query(imageUri, null, null, null, null);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            imageSize = (returnCursor.getLong(sizeIndex)) / 1024;
            if(imageSize > 500){
                imageSizeTv.setVisibility(View.VISIBLE);
                imageSizeTv.setText("Image size is greater than 500 KB");
                Toast.makeText(this, "Upload a new image", Toast.LENGTH_SHORT).show();
            }else{
                imageSizeTv.setVisibility(View.GONE);
            }
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