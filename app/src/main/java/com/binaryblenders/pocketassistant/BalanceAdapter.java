package com.binaryblenders.pocketassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.BalanceViewHolder> {

    private Context context;
    private ArrayList<BalanceDataModel> list;

    public BalanceAdapter(Context context, ArrayList<BalanceDataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BalanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root=inflater.inflate(R.layout.balance_item,parent,false);
        BalanceViewHolder balanceViewHolder=new BalanceViewHolder(root);
        return balanceViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BalanceViewHolder holder, int position) {
        holder.added.setText( holder.added.getText().toString() + String.valueOf(list.get(position).getAdded()));
        holder.balance.setText(holder.balance.getText().toString()+ String.valueOf(list.get(position).getBalance()));
        holder.source.setText(list.get(position).getSource());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BalanceViewHolder extends RecyclerView.ViewHolder{

        TextView added;
        TextView balance,source;

        public BalanceViewHolder(@NonNull View itemView) {
            super(itemView);
            added=itemView.findViewById(R.id.added);
            balance=itemView.findViewById(R.id.balance);
            source = itemView.findViewById(R.id.src);

        }
    }
}
