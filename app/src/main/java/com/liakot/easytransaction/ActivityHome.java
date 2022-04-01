package com.liakot.easytransaction;
//---------Liakot Ali Liton, ID : 1802035-----------


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityHome extends AppCompatActivity {

    int selectColor, deselectColor, amountSelectColor, amountDeselectColor;
    LinearLayout customerLay, toPayLay;
    Toolbar toolbar;
    FloatingActionButton homeFab;
    CircleImageView shopPicture;
    TextView shopName, shopAmount;
    ImageView searchBar;

    String name = null;
    long totalRemain = 0, totalPayble = 0 , customerNo = 0 , payableNo = 0;
    byte[] picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Initialize();

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHome.this, ActivitySearch.class));
//                Toast.makeText(ActivityHome.this, "Under construction", Toast.LENGTH_SHORT).show();
            }
        });

        customerLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerLay.setBackgroundColor(selectColor);
                toPayLay.setBackgroundColor(deselectColor);
                shopAmount.setTextColor(amountSelectColor);
                shopAmount.setText("Remain: " + '\u09F3' + totalRemain);
                setFragment(new FragmentCustomer());
            }
        });

        toPayLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPayLay.setBackgroundColor(selectColor);
                customerLay.setBackgroundColor(deselectColor);
                shopAmount.setTextColor(amountDeselectColor);
                shopAmount.setText("Pay: " + '\u09F3' + totalPayble);
                setFragment(new FragmentToPay());
            }
        });

        homeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityHome.this, ActivityAddCustomer.class);
               startActivity(intent);
            }
        });

        shopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassDatabaseHelper databaseHelper = new ClassDatabaseHelper(ActivityHome.this);
                Cursor cursor = databaseHelper.showShopInfo();
                cursor.moveToFirst();
                Intent intent = new Intent(ActivityHome.this, ActivityShopProfile.class);

                intent.putExtra("name", cursor.getString(0));
                intent.putExtra("owner", cursor.getString(1));
                intent.putExtra("category", cursor.getString(2));
                intent.putExtra("password", cursor.getString(6));
                intent.putExtra("address", cursor.getString(4));

                intent.putExtra("phone", cursor.getLong(3));
                intent.putExtra("totalRemain", cursor.getLong(7));
                intent.putExtra("totalPayble", cursor.getLong(8));
                intent.putExtra("customerNo", cursor.getLong(9));
                intent.putExtra("paybleNo", cursor.getLong(10));

                intent.putExtra("picture", cursor.getBlob(5));
                startActivity(intent);
            }
        });
        shopPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassDatabaseHelper databaseHelper = new ClassDatabaseHelper(ActivityHome.this);
                Cursor cursor = databaseHelper.showShopInfo();
                cursor.moveToFirst();
                Intent intent = new Intent(ActivityHome.this, ActivityShopProfile.class);

                intent.putExtra("name", cursor.getString(0));
                intent.putExtra("owner", cursor.getString(1));
                intent.putExtra("category", cursor.getString(2));
                intent.putExtra("password", cursor.getString(6));
                intent.putExtra("address", cursor.getString(4));

                intent.putExtra("phone", cursor.getLong(3));
                intent.putExtra("totalRemain", cursor.getLong(7));
                intent.putExtra("totalPayble", cursor.getLong(8));
                intent.putExtra("customerNo", cursor.getLong(9));
                intent.putExtra("paybleNo", cursor.getLong(10));

                intent.putExtra("picture", cursor.getBlob(5));
                startActivity(intent);
            }
        });
    }

    //-----Out of main section--------

    private void Initialize() {

        toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle("");

        customerLay = findViewById(R.id.homeCustomerLayout);
        toPayLay = findViewById(R.id.homeToPayLayout);
        homeFab = findViewById(R.id.homeFab);
        shopPicture = findViewById(R.id.homeShopPicture);
        shopName = findViewById(R.id.homeShopName);
        shopAmount = findViewById(R.id.homeTotalAmount);
        searchBar = findViewById(R.id.homeSearchBar);

//        phone = getIntent().getLongExtra("phone", 0);

        //--------get shopDetails from database and initialize the field---------
        ClassDatabaseHelper databaseHelper = new ClassDatabaseHelper(ActivityHome.this);
        Cursor cursor = databaseHelper.showShopInfo();
        if(cursor!=null) {
            cursor.moveToFirst();
            name = cursor.getString(0);
            totalRemain = cursor.getLong(7);
            totalPayble = cursor.getLong(8);
            customerNo = cursor.getLong(9);
            payableNo = cursor.getLong(10);
            picture = cursor.getBlob(5);
        }

        //------if there are no customer----------
        Cursor cusCursor = databaseHelper.showCustomer();
        Cursor toPayCursor = databaseHelper.showToPay();
        if(cusCursor.getCount() == 0){
            totalRemain = 0;
            customerNo = 0;
            ClassShop upShop = new ClassShop("", "", "", "", "", 0, totalRemain, totalPayble, customerNo, payableNo, picture);
            databaseHelper.updateShopAmount(upShop);
        }
        if(toPayCursor.getCount() == 0){
            totalPayble = 0;
            payableNo = 0;
            ClassShop upShop = new ClassShop("", "", "", "", "", 0, totalRemain, totalPayble, customerNo, payableNo, picture);
            databaseHelper.updateShopAmount(upShop);
        }

        shopName.setText(name);
        shopAmount.setText("Remain: " + '\u09F3' + totalRemain);
        if(picture != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            shopPicture.setImageBitmap(bitmap);
        }else{
            shopPicture.setImageResource(R.drawable.icon_profile_24);
        }


        selectColor = getResources().getColor(R.color.purple_200);
        deselectColor = getResources().getColor(R.color.white_gray);

        amountSelectColor = getResources().getColor(R.color.green);
        amountDeselectColor = getResources().getColor(R.color.red);

        customerLay.setBackgroundColor(selectColor);
        toPayLay.setBackgroundColor(deselectColor);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.homeFrameLayout, new FragmentCustomer());
        transaction.commit();
    }

    //---------for replace fragment------------
    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.homeFrameLayout, fragment);
        transaction.commit();
    }

    //-----------For three dotted home menu-----------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    //------------For menu item-------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        else if(item.getItemId() == R.id.menu_help)
        {
            Toast.makeText(ActivityHome.this, "Help Clicked", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.menu_about_us)
        {
//            Toast.makeText(ActivityHome.this, "About Us Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ActivityHome.this, ActivityAboutUs.class));
        }
        else if(item.getItemId() == R.id.menu_log_out)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityHome.this);
            dialog.setTitle("Are you sure?");
            dialog.setMessage("Do you want to log out?");
            dialog.setIcon(R.drawable.icon_logout_black);
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences sharedPreferences = getSharedPreferences("LogIn", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("loggedIn", false);
                    editor.apply();
                    Intent intent = new Intent(ActivityHome.this, ActivitySignIn.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(ActivityHome.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setNegativeButton("No", null);
            dialog.setCancelable(true);
            dialog.show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(ActivityHome.this, ActivityHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    long mBackPressed;
    @Override
    public void onBackPressed() {
        if (mBackPressed + 2000 > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(ActivityHome.this, "Press back button again to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }
}