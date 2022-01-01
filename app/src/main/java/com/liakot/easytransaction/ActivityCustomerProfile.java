package com.liakot.easytransaction;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityCustomerProfile extends AppCompatActivity {
    CircleImageView cusPicture, cusAddPicture;
    TextInputEditText cusName, cusPhone, cusAddress;
    Button updateProfile;

    TextInputLayout nameLay, phoneLay, addressLay;

    String upName, upAddress, upPhone;

    String preName, preAddress, type;
    long prePhone, amount;
    byte[] pictureByte;

    private static final int PICK_IMAGE = 1;

    Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        InitializeAll();

        cusAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-----open gallery-------
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upName = cusName.getText().toString();
                upAddress = cusAddress.getText().toString();
                upPhone = cusPhone.getText().toString();

                if (upName.equals(preName) && upAddress.equals(preAddress) && upPhone.equals(String.valueOf(prePhone)) && imageUri == null) {
                    Toast.makeText(ActivityCustomerProfile.this, "No Changes", Toast.LENGTH_SHORT).show();
                } else {

                    //--------convert image to byteArray-------------
                    if (imageUri != null) {
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            pictureByte = getByte(inputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    //--------------convert phone number to long type---------
                    long upNumber = 2000000000;
                    if (!upPhone.isEmpty() && !upPhone.contains("%") && !upPhone.contains(":") && !upPhone.contains(" ") && !upPhone.contains(".")) {
                        upNumber = Long.parseLong(upPhone);
                    }

                    if (upName.isEmpty()) {
                        nameLay.setError("Name is empty");
                        phoneLay.setErrorEnabled(false);
                        addressLay.setErrorEnabled(false);
                    } else if (upPhone.isEmpty()) {
                        phoneLay.setError("Phone is empty");
                        nameLay.setErrorEnabled(false);
                        addressLay.setErrorEnabled(false);
                    } else if (upPhone.length() != 10 || upNumber > 1999999999 || upNumber < 999999999) {
                        phoneLay.setError("Invalid phone number");
                        nameLay.setErrorEnabled(false);
                        addressLay.setErrorEnabled(false);
                    } else if (upAddress.isEmpty()) {
                        addressLay.setError("Address is empty");
                        phoneLay.setErrorEnabled(false);
                        nameLay.setErrorEnabled(false);
                    } else {
                        //-----------update customerDetails/ToPayDetails table-----------
                        ClassDatabaseHelper helper = new ClassDatabaseHelper(ActivityCustomerProfile.this);
                        ClassAddCustomer upCustomer = new ClassAddCustomer(upName, upNumber, upAddress, pictureByte, amount);
                        if (type.equals("Customer")) {
                            //----------check update number is exist or not----------
                            if (prePhone != upNumber && helper.customerPhoneExist(upNumber)) {
                                Toast.makeText(ActivityCustomerProfile.this, "This phone is already exist in Customer", Toast.LENGTH_SHORT).show();
                            } else {
                                helper.updateCustomer(upCustomer, prePhone);
                                if (helper.customerUpdated) {
                                    Toast.makeText(ActivityCustomerProfile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ActivityCustomerProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            //----------check update number is exist or not----------
                            if (prePhone != upNumber && helper.toPayPhoneExist(upNumber)) {
                                Toast.makeText(ActivityCustomerProfile.this, "This phone is already exist in To Pay", Toast.LENGTH_SHORT).show();
                            } else {
                                helper.updateToPay(upCustomer, prePhone);
                                if (helper.toPayUpdated) {
                                    Toast.makeText(ActivityCustomerProfile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ActivityCustomerProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void InitializeAll() {

        preName = getIntent().getStringExtra("Name");
        preAddress = getIntent().getStringExtra("Address");
        type = getIntent().getStringExtra("Type");
        prePhone = getIntent().getLongExtra("Phone", 0);
        amount = getIntent().getLongExtra("Amount", 0);
        pictureByte = getIntent().getByteArrayExtra("Picture");

        cusPicture = findViewById(R.id.customerProfilePicture);
        cusAddPicture = findViewById(R.id.customerProfileAddImage);
        cusName = findViewById(R.id.customerProfileName);
        cusPhone = findViewById(R.id.customerProfilePhone);
        cusAddress = findViewById(R.id.customerProfileAddress);
        updateProfile = findViewById(R.id.customerProfileUpdateBtn);
        nameLay = findViewById(R.id.customerProfileNameLayout);
        phoneLay = findViewById(R.id.customerProfilePhoneLayout);
        addressLay = findViewById(R.id.customerProfileAddressLayout);

        cusName.setText(preName);
        cusAddress.setText(preAddress);
        cusPhone.setText(String.valueOf(prePhone));

        //--------set picture in circle image view--------------
        Bitmap bitmap;
        if(pictureByte != null) {
            bitmap = BitmapFactory.decodeByteArray(pictureByte, 0, pictureByte.length);
            cusPicture.setImageBitmap(bitmap);
        }
        else{
            cusPicture.setImageResource(R.drawable.icon_profile_24);
        }

    }

    //----for load image into CircleImageView----------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(cusPicture);
        } else {
            imageUri = null;
        }
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

}