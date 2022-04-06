package com.liakot.easytransaction;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterTransactionList extends RecyclerView.Adapter<AdapterTransactionList.ViewHolder> {

    ArrayList<ClassAddTransaction> arrayList;
    Context activityContext;

    public AdapterTransactionList(Context context, ArrayList<ClassAddTransaction> list) {

        activityContext = context;
        arrayList = list;
    }

    //------To hold every list item view------
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, explanation, expense, getMoney, remain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.detailsDate);
            explanation = itemView.findViewById(R.id.detailsExplanation);
            expense = itemView.findViewById(R.id.detailsExpenseMoney);
            getMoney = itemView.findViewById(R.id.detailsGetMoney);
            remain = itemView.findViewById(R.id.detailsRemain);

        }
    }

    @NonNull
    @Override
    public AdapterTransactionList.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(R.layout.item_transaction_details, viewGroup, false);

        return new AdapterTransactionList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTransactionList.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.itemView.setTag(arrayList.get(position));

        holder.date.setText(arrayList.get(position).getDate());
        holder.explanation.setText(arrayList.get(position).getExplanation());
        holder.expense.setText(String.valueOf(arrayList.get(position).getExpense()));
        holder.getMoney.setText(String.valueOf(arrayList.get(position).getGetMoney()));
        holder.remain.setText(String.valueOf(arrayList.get(position).getRemain()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activityContext, "Long press to delete", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activityContext);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Do you want to delete this transaction?");
                dialog.setIcon(activityContext.getResources().getDrawable(R.drawable.icon_delete_black));

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cusType;
                        long transId, cusId, remain, amount;
                        transId = arrayList.get(position).getTransactionNo();
                        cusType = arrayList.get(position).getType();
                        cusId = arrayList.get(position).getCustomerId();
                        remain = arrayList.get(position).getRemain();


                        ClassDatabaseHelper helper = new ClassDatabaseHelper(activityContext);
                        Cursor shopCursor = helper.showShopInfo();
                        shopCursor.moveToFirst();

                        long totalPayable, totalRemain, customerNo, payableNo;
                        totalRemain = shopCursor.getLong(7);
                        totalPayable = shopCursor.getLong(8);
                        customerNo = shopCursor.getLong(9);
                        payableNo = shopCursor.getLong(10);

                        if(helper.deleteTransaction(transId, cusType)) {

                            if (cusType.equals("Customer")) {
                                Cursor cursor = helper.showCustomerInfo(cusId);
                                cursor.moveToFirst();
                                amount = cursor.getLong(5);
                                amount -= remain;
                                totalRemain -= remain;
                                ClassAddCustomer upCus = new ClassAddCustomer("", 0, "", null, amount);
                                helper.updateCustomer(upCus, cusId);

                                ClassShop updateShop = new ClassShop("", "", "", "", "", 0, totalRemain, totalPayable, customerNo, payableNo, null);
                                helper.updateShopAmount(updateShop);

                                Toast.makeText(activityContext, "Transaction deleted successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activityContext, ActivityCustomerDetails.class);
                                intent.putExtra("Id", cusId);
                                intent.putExtra("Name", cursor.getString(2));
                                intent.putExtra("Phone", cursor.getLong(1));
                                intent.putExtra("Address", cursor.getString(3));
                                intent.putExtra("Amount", amount);
                                intent.putExtra("Picture", cursor.getBlob(4));
                                intent.putExtra("Type", cusType);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activityContext.startActivity(intent);
                            } else {
                                Cursor cursor = helper.showTopayInfo(cusId);
                                cursor.moveToFirst();
                                amount = cursor.getLong(5);
                                amount -= remain;
                                totalPayable -= remain;
                                ClassAddCustomer upCus = new ClassAddCustomer("", 0, "", null, amount);
                                helper.updateToPay(upCus, cusId);

                                ClassShop updateShop = new ClassShop("", "", "", "", "", 0, totalRemain, totalPayable, customerNo, payableNo, null);
                                helper.updateShopAmount(updateShop);

                                Toast.makeText(activityContext, "Transaction deleted successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activityContext, ActivityCustomerDetails.class);
                                intent.putExtra("Id", cusId);
                                intent.putExtra("Name", cursor.getString(2));
                                intent.putExtra("Phone", cursor.getLong(1));
                                intent.putExtra("Address", cursor.getString(3));
                                intent.putExtra("Amount", amount);
                                intent.putExtra("Picture", cursor.getBlob(4));
                                intent.putExtra("Type", cusType);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activityContext.startActivity(intent);
                            }
                        }else{
                            Toast.makeText(activityContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.setNegativeButton("No", null);
                dialog.setCancelable(true);
                dialog.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
