package com.liakot.easytransaction;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ClassDatabaseHelper extends SQLiteOpenHelper {

private Context context;
private final static String databaseName = "EasyTransaction.db";
private final static int databaseVersion = 1;
private static String tableName;

private final static String customerDetailsTable = "CustomerDetails";

private final static String customerPhone = "Phone_Number";
private final static String customerName = "Name";
private final static String customerAddress = "Address";
private final static String customerPicture = "Picture";
private final static String customerAmount = "Amount";

private final static String toPayDetailsTable = "ToPayDetails";

private final static String toPayPhone = "Phone_Number";
private final static String toPayName = "Name";
private final static String toPayAddress = "Address";
private final static String toPayPicture = "Picture";
private final static String toPayAmount = "Amount";

private final static String shopDetailsTable = "ShopDetails";

private final static String shopName = "Shop_Name";
private final static String shopOwnerName = "Owner_Name";
private final static String shopCategory = "Category";
private final static String shopPhone = "Phone";
private final static String shopAddress = "Address";
private final static String shopPicture = "Picture";
private final static String shopPassword = "Password";
private final static String shopTotalRemain = "Total_Remain";
private final static String shopTotalPayble = "Total_Payble";
private final static String shopPaybleNumber = "Payble_Number";
private final static String shopCustomerNumber = "Customer_Number";

private final static String allTransactionTable = "AllTransaction";

private final static String transTransactionNo = "Transaction_No";
private final static String transDate = "Date";
private final static String transCustomerPhone = "Customer_Phone";
private final static String transExplanation = "Explanation";
private final static String transTotalExpense = "Total_Expense";
private final static String transGetMoney = "Get_Money";
private final static String transRemain = "Remain";

public boolean customerAdd = true;
public boolean toPayAdd = true;


public ClassDatabaseHelper(@Nullable Context context) {
        super(context, databaseName, null, databaseVersion);
        this.context = context;
        }

@Override
public void onCreate(SQLiteDatabase db) {
        String shopDetailsTableQuery = "CREATE TABLE " + shopDetailsTable + " (" + shopName + " TEXT NOT NULL, " + shopOwnerName + " TEXT, " +
        shopCategory + " TEXT, " + shopPhone + " INTEGER NOT NULL, " + shopAddress + " TEXT, " + shopPicture + " BOLB, " + shopPassword +
        " TEXT, " + shopTotalRemain + " INTEGER, " + shopTotalPayble + " INTEGER, " + shopCustomerNumber + " INTEGER, " +
        shopPaybleNumber + " INTEGER);";

        String customerDetailsTableQuery = "CREATE TABLE " + customerDetailsTable + " (" + customerPhone + " INTEGER PRIMARY KEY, " +
        customerName + " TEXT, " + customerAddress + " TEXT, " + customerPicture + " BLOB, " + customerAmount + " INTEGER);";

        String toPayDetailsTableQuery = "CREATE TABLE " + toPayDetailsTable + " (" + toPayPhone + " INTEGER PRIMARY KEY, " +
        toPayName + " TEXT, " + toPayAddress + " TEXT, " + toPayPicture + " BLOB, " + toPayAmount + " INTEGER);";

        String allTransactionTableQuery = "CREATE TABLE " + allTransactionTable + " (" + transTransactionNo + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        transDate + " TEXT, " + transCustomerPhone + " INTEGER, " + transExplanation + " TEXT, " + transTotalExpense + " INTEGER, " +
        transGetMoney + " INTEGER, " + transRemain + " INTEGER);";

        db.execSQL(shopDetailsTableQuery);
        db.execSQL(customerDetailsTableQuery);
        db.execSQL(toPayDetailsTableQuery);
        db.execSQL(allTransactionTableQuery);
        }

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String table1 = "DROP TABLE IF EXISTS " + shopDetailsTable;
        String table2 = "DROP TABLE IF EXISTS " + customerDetailsTable;
        String table3 = "DROP TABLE IF EXISTS " + toPayDetailsTable;
        String table4 = "DROP TABLE IF EXISTS " + allTransactionTable;

        db.execSQL(table1);
        db.execSQL(table2);
        db.execSQL(table3);
        db.execSQL(table4);
        onCreate(db);
        }

//--------add Customer Type customer-------
public void AddNewCustomer(ClassAddCustomer customer) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(customerPhone, customer.getPhone());
        cv.put(customerName, customer.getName());
        cv.put(customerAddress, customer.getAddress());
        cv.put(customerPicture, customer.getPicture());
        cv.put(customerAmount, customer.getAmount());

        long result = database.insert(customerDetailsTable, null, cv);
        if (result == -1) {
        Toast.makeText(context, "Customer not added", Toast.LENGTH_SHORT).show();
        customerAdd = false;
        } else {
        Toast.makeText(context, "New Customer added", Toast.LENGTH_SHORT).show();
        customerAdd = true;
        }
        }

//------------show all Customer type customer----------
public Cursor showCustomer() {
        Cursor cursor = null;
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + customerDetailsTable;
        if (database != null) {
        cursor = database.rawQuery(query, null);
        }
        return cursor;
        }

//----------add To Pay type customer---------
public void AddNewToPay(ClassAddCustomer customer) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(customerPhone, customer.getPhone());
        cv.put(customerName, customer.getName());
        cv.put(customerAddress, customer.getAddress());
        cv.put(customerPicture, customer.getPicture());
        cv.put(customerAmount, customer.getAmount());

        long result = database.insert(toPayDetailsTable, null, cv);
        if (result == -1) {
        Toast.makeText(context, "Customer not added", Toast.LENGTH_SHORT).show();
        toPayAdd = false;
        } else {
        Toast.makeText(context, "To Pay type Customer added", Toast.LENGTH_SHORT).show();
        toPayAdd = true;
        }
        }

//-----------show all To Pay type customer---------------
public Cursor showToPay() {
        Cursor cursor = null;
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + toPayDetailsTable;
        if (database != null) {
        cursor = database.rawQuery(query, null);
        }
        return cursor;
        }
        }
//--------To Check whether the entered user exist in the database or not
public Boolean checkusername(String username){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("Select * where username = ?",new String[]{username});
        if(cursor.getCount() > 0)
        return true;
        else
        return false;
        }

public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("Select * from users where username = ?",new String[] {username,password});
        if(cursor.getCount() > 0)
        return true;
        else
        return false;
        } }