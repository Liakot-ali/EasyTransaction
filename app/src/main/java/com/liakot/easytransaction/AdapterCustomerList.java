package com.liakot.easytransaction;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterCustomerList extends RecyclerView.Adapter<AdapterCustomerList.ViewHolder> {
    ArrayList<ClassAddCustomer> arrayList;
    Context activityContext;
    Animation recyclerAnim;
    byte[] pictureByte = null;

    public AdapterCustomerList(Context context, ArrayList<ClassAddCustomer> list) {

        activityContext = context;
        arrayList = list;
    }

    //------To hold every list item view------
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, amount;
        CircleImageView picture;
        LinearLayout mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.customerName);
            address = itemView.findViewById(R.id.customerAddress);
            picture = itemView.findViewById(R.id.customerPicture);
            amount = itemView.findViewById(R.id.customerTotalAmount);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(R.layout.item_customer, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Bitmap bitmap;
        holder.itemView.setTag(arrayList.get(position));

        //------convert byte to string------
        pictureByte = arrayList.get(position).getPicture();
        if (pictureByte != null) {
            bitmap = BitmapFactory.decodeByteArray(pictureByte, 0, pictureByte.length);
            holder.picture.setImageBitmap(bitmap);
        } else {
            holder.picture.setImageResource(R.drawable.icon_profile_24);
        }

        holder.name.setText(arrayList.get(position).getName());
        holder.address.setText(arrayList.get(position).getAddress());
        holder.amount.setText(activityContext.getResources().getString(R.string.tk_sign) + String.valueOf(arrayList.get(position).getAmount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityContext, ActivityTransaction.class);
                intent.putExtra("Id", arrayList.get(position).getId());
                intent.putExtra("Name", arrayList.get(position).getName());
                intent.putExtra("Phone", arrayList.get(position).getPhone());
                intent.putExtra("Address", arrayList.get(position).getAddress());
                intent.putExtra("Amount", arrayList.get(position).getAmount());
                intent.putExtra("Picture", arrayList.get(position).getPicture());
                intent.putExtra("Type", "Customer");

                activityContext.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activityContext);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Do you want to delete this customer?");
                dialog.setIcon(activityContext.getResources().getDrawable(R.drawable.icon_delete_black));
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClassDatabaseHelper helper = new ClassDatabaseHelper(activityContext);
                        long cusId = arrayList.get(position).getId();
                        long cusRemain = arrayList.get(position).getAmount();
                        String cusType = "Customer";
                        if (helper.DeleteCustomer(cusId, cusType)) {
                            long totalRemain, totalPayable, customerNo, toPayNo;
                            Cursor shopCursor = helper.showShopInfo();
                            shopCursor.moveToFirst();
                            totalRemain = shopCursor.getLong(7);
                            totalPayable = shopCursor.getLong(8);
                            customerNo = shopCursor.getLong(9);
                            toPayNo = shopCursor.getLong(10);
                            totalRemain -= cusRemain;
                            customerNo--;

                            ClassShop upShop = new ClassShop("", "", "", "", "", 0, totalRemain, totalPayable, customerNo, toPayNo, null);
                            helper.updateShopAmount(upShop);
                            Toast.makeText(activityContext, "Customer deleted successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(activityContext, ActivityHome.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activityContext.startActivity(intent);
                        }
                    }
                });
                dialog.setNegativeButton("No", null);
                dialog.setCancelable(true);
                dialog.show();

                return  true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
