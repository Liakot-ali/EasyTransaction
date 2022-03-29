package com.liakot.easytransaction;

//--------------Liakot Ali Liton, ID : 1802035------------

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class ActivityShopProfile extends AppCompatActivity {

    public final int PICK_IMAGE = 10;

    Toolbar toolbar;
    TextInputEditText shopName, shopOwner, shopCategory, shopPhone, shopAddress;
    TextInputLayout shopNameLay, shopPhoneLay, shopOwnerLay, shopCategoryLay, shopAddressLay;
    Button updateBtn;
    CircleImageView shopPicture, addPicture;

    String name, owner, category, password, address;
    long phone, totalRemain, totalPayble, customerNo, paybleNo;
    byte[] picture = null;

    Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_profile);
        InitializeAll();


        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameSt, ownerSt, categorySt, addressSt, phoneSt;
                nameSt = shopName.getText().toString();
                ownerSt = shopOwner.getText().toString();
                categorySt = shopCategory.getText().toString();
                addressSt = shopAddress.getText().toString();
                phoneSt = shopPhone.getText().toString();

                //----------convert imageUri to byte array to store as BLOB---------
                if(imageUri!=null)
                {
                    try{
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        picture = getByte(inputStream);
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                long number = 2000000000;
                if (!phoneSt.isEmpty() && !phoneSt.contains("%") && !phoneSt.contains(":") && !phoneSt.contains(" ") && !phoneSt.contains(".")) {
                    number = Long.parseLong(phoneSt);
                }
                if(nameSt.isEmpty()){
                    shopNameLay.setError("Name is empty");
                    shopNameLay.setErrorEnabled(true);
                    shopPhoneLay.setErrorEnabled(false);

                } else if (phoneSt.isEmpty()) {
                    shopPhoneLay.setError("Phone is empty");
                    shopPhoneLay.setErrorEnabled(true);
                    shopNameLay.setErrorEnabled(false);

                } else if (phoneSt.length()!=10 || number > 1999999999 || number < 999999999) {
                    shopPhoneLay.setError("Invalid phone number");
                    shopNameLay.setErrorEnabled(false);
                    shopPhoneLay.setErrorEnabled(true);
                }else{
                    shopPhoneLay.setErrorEnabled(false);
                    shopNameLay.setErrorEnabled(false);

                    ClassShop updateShop = new ClassShop(nameSt, ownerSt, categorySt, password, addressSt, number, totalRemain, totalPayble, customerNo, paybleNo, picture);
                    ClassDatabaseHelper helper = new ClassDatabaseHelper(ActivityShopProfile.this);
                    helper.updateShopInfo(updateShop);
                    if(helper.shopInfoUpdate){
                        Toast.makeText(ActivityShopProfile.this, "Shop profile updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityShopProfile.this, ActivityHome.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(ActivityShopProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void InitializeAll() {
        name = getIntent().getStringExtra("name");
        owner = getIntent().getStringExtra("owner");
        category = getIntent().getStringExtra("category");
        password = getIntent().getStringExtra("password");
        address = getIntent().getStringExtra("address");

        phone = getIntent().getLongExtra("phone", 0);
        totalRemain = getIntent().getLongExtra("totalRemain", 0);
        totalPayble = getIntent().getLongExtra("totalPayble",0);
        customerNo = getIntent().getLongExtra("customerNo", 0);
        paybleNo = getIntent().getLongExtra("paybleNo", 0);

        picture = getIntent().getByteArrayExtra("picture");


        //------------find the field--------------
        toolbar = findViewById(R.id.shopProfileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        shopName = findViewById(R.id.shopProfileShopName);
        shopCategory = findViewById(R.id.shopProfileCategory);
        shopPhone = findViewById(R.id.shopProfilePhone);
        shopOwner = findViewById(R.id.shopProfileOwnerName);
        shopAddress = findViewById(R.id.shopProfileAddress);

        shopNameLay = findViewById(R.id.shopProfileShopNameLayout);
        shopCategoryLay = findViewById(R.id.shopProfileCategoryLayout);
        shopPhoneLay = findViewById(R.id.shopProfilePhoneLayout);
        shopOwnerLay = findViewById(R.id.shopProfileOwnerNameLayout);
        shopAddressLay = findViewById(R.id.shopProfileAddressLayout);

        shopPicture = findViewById(R.id.shopProfilePicture);
        addPicture = findViewById(R.id.shopProfileAddImage);

        updateBtn = findViewById(R.id.shopProfileUpdate);

        //-------assign the value to the field--------
        shopName.setText(name);
        shopOwner.setText(owner);
        shopCategory.setText(category);
        shopPhone.setText(String.valueOf(phone));
        shopAddress.setText(address);
        if(picture!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            shopPicture.setImageBitmap(bitmap);
        }else{
            shopPicture.setImageResource(R.drawable.icon_profile_24);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(shopPicture);
        }
        else{
            imageUri = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return true;
    }
}