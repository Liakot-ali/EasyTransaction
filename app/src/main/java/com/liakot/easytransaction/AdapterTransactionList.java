package com.liakot.easytransaction;

//---------Liakot Ali Liton, ID : 1802035----------

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
                Toast.makeText(activityContext, "Under Construction", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
