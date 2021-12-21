package com.liakot.easytransaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ClassDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private final static String databaseName = "EasyTransaction.db";
    private final static int databaseVersion = 1;
    private  static String tableName;

    public ClassDatabaseHelper(@Nullable Context context) {
        super(context, databaseName, null, databaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE CustomerDetails(Phone_Number INTEGER PRIMARY KEY, " +
                "Name TEXT NOT NULL, Address TEXT NOT NULL, Picture BOLB, Amount INTEGER);" ;
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS CustomerDetails";
        db.execSQL(query);
        onCreate(db);
    }

    public void AddNewCustomer(ClassAddCustomer customer)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();


    }
}
