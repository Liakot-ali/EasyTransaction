package com.liakot.easytransaction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityHome extends AppCompatActivity {

    int selectColor, deselectColor;
    LinearLayout customerLay, toPayLay;
    Toolbar toolbar;
    FloatingActionButton homeFab;
    CircleImageView shopPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Initialize();

        customerLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerLay.setBackgroundColor(selectColor);
                toPayLay.setBackgroundColor(deselectColor);
                setFragment(new FragmentCustomer());
            }
        });

        toPayLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPayLay.setBackgroundColor(selectColor);
                customerLay.setBackgroundColor(deselectColor);
                setFragment(new FragmentToPay());
            }
        });

        homeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityHome.this, ActivityAddCustomer.class);
                ActivityHome.this.startActivityForResult(intent, 1);
            }
        });

        shopPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHome.this, ActivityShopProfile.class));
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

        selectColor = getResources().getColor(R.color.purple_200);
        deselectColor = getResources().getColor(R.color.white_gray);

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
            Toast.makeText(ActivityHome.this, "About Us Clicked", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.menu_log_out)
        {
            Toast.makeText(ActivityHome.this, "Log Out Clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1) {
            startActivity(new Intent(ActivityHome.this, ActivityHome.class));
        }
    }
}