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

    private final static String customerId = "Customer_Id";
    private final static String customerPhone = "Phone_Number";
    private final static String customerName = "Name";
    private final static String customerAddress = "Address";
    private final static String customerPicture = "Picture";
    private final static String customerAmount = "Amount";

    private final static String toPayDetailsTable = "ToPayDetails";

    private final static String toPayId = "ToPay_Id";
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

    private final static String transTransactionNo = "Transaction_Id";
    private final static String transDate = "Date";
    private final static String transCustomerId = "ID";
    private final static String transExplanation = "Explanation";
    private final static String transTotalExpense = "Total_Expense";
    private final static String transGetMoney = "Get_Money";
    private final static String transRemain = "Remain";
    private final static String transType = "Type";

    public boolean customerAdd = true;
    public boolean toPayAdd = true;
    public boolean transactionAdd = true;

    public boolean customerUpdated = true;
    public boolean toPayUpdated = true;

    public boolean shopInfoAdd = true;
    public boolean shopInfoUpdate = true;
    public boolean shopAmountUpdate = true;



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

        String customerDetailsTableQuery = "CREATE TABLE " + customerDetailsTable + " (" + customerId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + customerPhone + " INTEGER UNIQUE, " +
                customerName + " TEXT, " + customerAddress + " TEXT, " + customerPicture + " BLOB, " + customerAmount + " INTEGER);";

        String toPayDetailsTableQuery = "CREATE TABLE " + toPayDetailsTable + " (" + toPayId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + toPayPhone + " INTEGER UNIQUE, " +
                toPayName + " TEXT, " + toPayAddress + " TEXT, " + toPayPicture + " BLOB, " + toPayAmount + " INTEGER);";

        String allTransactionTableQuery = "CREATE TABLE " + allTransactionTable + " (" + transTransactionNo + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                transDate + " TEXT, " + transCustomerId + " INTEGER, " + transExplanation + " TEXT, " + transTotalExpense + " INTEGER, " +
                transGetMoney + " INTEGER, " + transRemain + " INTEGER, " + transType + " TEXT);";

        db.execSQL(shopDetailsTableQuery);
        db.execSQL(customerDetailsTableQuery);
        db.execSQL(toPayDetailsTableQuery);
        db.execSQL(allTransactionTableQuery);


        ContentValues cv = new ContentValues();
        cv.put(shopName, "Shop Name");
        cv.put(shopOwnerName, "Owner Name");
        cv.put(shopCategory, "Category");
        cv.put(shopPhone, 123456789);
        cv.put(shopPassword, "123456789");
        cv.put(shopAddress, "Address");
        cv.put(shopPicture, (byte[]) null);
        cv.put(shopTotalRemain, 0);
        cv.put(shopTotalPayble, 0);
        cv.put(shopCustomerNumber, 0);
        cv.put(shopPaybleNumber, 0);
        db.insert(shopDetailsTable, null, cv);
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

    //-------show specific customer info-----------
    public Cursor showCustomerInfo(long id){
        Cursor cursor = null;
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + customerDetailsTable + " WHERE " + customerId + " = " + id + ";";
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    //------check customer phone is already exist or not------------
    public boolean customerPhoneExist(long phone) {
        boolean exist = false;
        Cursor cursor = showCustomer();
        while (cursor.moveToNext()) {
            if (phone == cursor.getInt(0)) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    //-------for update customer data----------
    public void updateCustomer(ClassAddCustomer upCustomer, long id) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if (upCustomer.name.isEmpty() && upCustomer.phone == 0 && upCustomer.address.isEmpty() && upCustomer.picture == null) {
            cv.put(customerAmount, upCustomer.getAmount());
        } else {
            cv.put(customerName, upCustomer.getName());
            cv.put(customerPhone, upCustomer.getPhone());
            cv.put(customerAddress, upCustomer.getAddress());
            cv.put(customerPicture, upCustomer.getPicture());
        }

        long result = database.update(customerDetailsTable, cv, customerId + "=" + id, null);
        customerUpdated = result != -1;
    }

    //----------delete a specific customer/toPay type---------
    public boolean DeleteCustomer(long id, String type){
        type = "'" + type + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        String query, query1;
        query1 = "DELETE FROM " + allTransactionTable + " WHERE " + transCustomerId + " = " + id + " AND " + transType + " = " + type + ";";
        if(type.equals("'Customer'")){
            query = "DELETE FROM " + customerDetailsTable + " WHERE " + customerId + " = " + id + ";";
        }else{
            query = "DELETE FROM " + toPayDetailsTable + " WHERE " + toPayId + " = " + id + ";";
        }
        if(database!=null)
        {
            database.execSQL(query);
            database.execSQL(query1);
            return true;
        }else{
            return false;
        }
    }


    //----------add To Pay type customer---------
    public void AddNewToPay(ClassAddCustomer customer) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(toPayPhone, customer.getPhone());
        cv.put(toPayName, customer.getName());
        cv.put(toPayAddress, customer.getAddress());
        cv.put(toPayPicture, customer.getPicture());
        cv.put(toPayAmount, customer.getAmount());

        long result = database.insert(toPayDetailsTable, null, cv);
        toPayAdd = result != -1;
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

    //------show all data of a specific customer---------
    public Cursor showTopayInfo(long id){
        Cursor cursor = null;
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + toPayDetailsTable + " WHERE " + toPayId + " = " + id + ";";
        if(database != null){
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    //------check customer phone is already exist or not------------
    public boolean toPayPhoneExist(long phone) {
        boolean exist = false;
        Cursor cursor = showToPay();
        while (cursor.moveToNext()) {
            if (phone == cursor.getInt(0)) {
                exist = true;
            }
        }
        return exist;
    }

    //-------for updated toPay data-----------
    public void updateToPay(ClassAddCustomer upToPay, long id) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if (upToPay.name.isEmpty() && upToPay.phone == 0 && upToPay.address.isEmpty() && upToPay.picture == null) {
            cv.put(toPayAmount, upToPay.getAmount());
        } else {
            cv.put(toPayName, upToPay.getName());
            cv.put(toPayPhone, upToPay.getPhone());
            cv.put(toPayAddress, upToPay.getAddress());
            cv.put(toPayPicture, upToPay.getPicture());
        }

        long result = database.update(toPayDetailsTable, cv, toPayId + "=" + id, null);
        toPayUpdated = result != -1;
    }

    //------for add the transaction to transaction table------------
    public void addTransaction(ClassAddTransaction transaction) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(transDate, transaction.getDate());
        cv.put(transCustomerId, transaction.getCustomerId());
        cv.put(transExplanation, transaction.getExplanation());
        cv.put(transTotalExpense, transaction.getExpense());
        cv.put(transGetMoney, transaction.getGetMoney());
        cv.put(transRemain, transaction.getRemain());
        cv.put(transType, transaction.getType());
        long result = database.insert(allTransactionTable, null, cv);
        transactionAdd = result != -1;
    }

    //---------show all transaction of a customer-----------
    public Cursor showTransaction(long id, String type) {
        type = "'" + type + "'";
        Cursor cursor = null;
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + allTransactionTable + " WHERE " + transCustomerId + " = " + id + " AND " + transType + " = " + type + ";";

        if (database != null) {
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    //-------delete specific transaction---------
    public boolean deleteTransaction(long id, String type){
        type = "'" + type + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "DELETE FROM " + allTransactionTable + " WHERE " + transTransactionNo + " = " + id + " AND " + transType + " = " + type + ";";
        if(database != null){
            database.execSQL(query);
            return true;
        }else{
            return false;
        }
    }

    //-----------update existing shop--------
    public  void updateShop(ClassShop newShop){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(shopName, newShop.getName());
        cv.put(shopOwnerName, newShop.getOwner());
        cv.put(shopCategory, newShop.getCategory());
        cv.put(shopPhone, newShop.getPhone());
        cv.put(shopPassword, newShop.getPassword());
        cv.put(shopAddress, newShop.getAddress());
        cv.put(shopPicture, newShop.getPicture());
        cv.put(shopTotalRemain, newShop.getTotalRemain());
        cv.put(shopTotalPayble, newShop.getTotalPayble());
        cv.put(shopCustomerNumber, newShop.getCustomerNo());
        cv.put(shopPaybleNumber, newShop.getPaybleNo());

        long result = database.update(shopDetailsTable, cv, null, null);
        if(result == -1){
            shopInfoAdd = false;
        }else{
            Toast.makeText(context, "Information added successfully", Toast.LENGTH_SHORT).show();
            String query1 = "DELETE FROM " + customerDetailsTable + ";";
            String query2 = "DELETE FROM " + toPayDetailsTable + ";";
            String query3 = "DELETE FROM " + allTransactionTable + ";";
            database.execSQL(query1);
            database.execSQL(query2);
            database.execSQL(query3);
            shopInfoAdd = true;
        }
    }

    public void updateShopInfo(ClassShop newShop){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(shopName, newShop.getName());
        cv.put(shopOwnerName, newShop.getOwner());
        cv.put(shopCategory, newShop.getCategory());
        cv.put(shopPhone, newShop.getPhone());
        cv.put(shopPassword, newShop.getPassword());
        cv.put(shopAddress, newShop.getAddress());
        cv.put(shopPicture, newShop.getPicture());
        cv.put(shopTotalRemain, newShop.getTotalRemain());
        cv.put(shopTotalPayble, newShop.getTotalPayble());
        cv.put(shopCustomerNumber, newShop.getCustomerNo());
        cv.put(shopPaybleNumber, newShop.getPaybleNo());

        long result = database.update(shopDetailsTable, cv, null, null);
        shopInfoUpdate = result != -1;
    }

    public  void updateShopAmount(ClassShop newShop){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(shopTotalRemain, newShop.getTotalRemain());
        cv.put(shopTotalPayble, newShop.getTotalPayble());
        cv.put(shopCustomerNumber, newShop.getCustomerNo());
        cv.put(shopPaybleNumber, newShop.getPaybleNo());

        long result = database.update(shopDetailsTable, cv, null, null);
        shopAmountUpdate = result != -1;
    }

    public Cursor showShopInfo() {
        Cursor cursor = null;
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + shopDetailsTable + ";";

        if(database!=null){
           cursor = database.rawQuery(query, null);
        }
        return cursor;
    }
}
