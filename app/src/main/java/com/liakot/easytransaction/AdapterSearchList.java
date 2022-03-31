package com.liakot.easytransaction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterSearchList extends RecyclerView.Adapter<AdapterSearchList.ViewHolder> {

    ArrayList<ClassAddCustomer> arrayList;
    Context context;

    public AdapterSearchList(ArrayList<ClassAddCustomer> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, phone, amount;
        CircleImageView picture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.searchName);
            phone = itemView.findViewById(R.id.searchPhone);
            picture = itemView.findViewById(R.id.searchPicture);
            amount = itemView.findViewById(R.id.searchAmount);
        }
    }


    @NonNull
    @Override
    public AdapterSearchList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_search, parent, false);
        return new AdapterSearchList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchList.ViewHolder holder, int position) {
        byte[] pictureByte;

        holder.itemView.setTag(arrayList.get(position));
        Bitmap bitmap = null;
        pictureByte = arrayList.get(position).getPicture();
        if(pictureByte!=null) {
            bitmap = BitmapFactory.decodeByteArray(pictureByte, 0, pictureByte.length);
            holder.picture.setImageBitmap(bitmap);
        }else{
            holder.picture.setImageResource(R.drawable.icon_profile_24);
        }
        if(arrayList.get(position).getType() == "Customer"){
            holder.amount.setTextColor(context.getResources().getColor(R.color.green));
        }else{
            holder.amount.setTextColor(context.getResources().getColor(R.color.red));
        }
        holder.name.setText(arrayList.get(position).getName());
        holder.phone.setText("0" + arrayList.get(position).getPhone());
        holder.amount.setText(context.getResources().getString(R.string.tk_sign) + arrayList.get(position).getAmount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Under construction", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void filterList(ArrayList<ClassAddCustomer> list){
        arrayList = list;
        notifyDataSetChanged();
    }
}
