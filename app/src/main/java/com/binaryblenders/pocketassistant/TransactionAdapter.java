package com.binaryblenders.pocketassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewholder> {


    private Context context;
    private ArrayList<TransactionItems> list;

    public TransactionAdapter(Context context, ArrayList<TransactionItems> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public TransactionViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View item= inflater.inflate(R.layout.transaction_item,parent,false);
        TransactionViewholder holder=new TransactionViewholder(item);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewholder holder, int position) {

        holder.rupees.setText(list.get(position).getAmount());
        holder.time.setText(list.get(position).getTime());
        holder.reason.setText(list.get(position).getPurpose());
        holder.transactiontype.setText(list.get(position).getTransaction_type());
        holder.purpose.setText(list.get(position).getSource());

        if (position==list.size()-1)
            holder.line.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TransactionViewholder extends RecyclerView.ViewHolder {

        TextView rupees;
        TextView time;
        TextView reason;
        TextView transactiontype;
        TextView purpose;
        View line;

        public TransactionViewholder(@NonNull View itemView) {
            super(itemView);
            rupees=itemView.findViewById(R.id.rupees);
            time=itemView.findViewById(R.id.timedate);
            reason=itemView.findViewById(R.id.reason);
            transactiontype=itemView.findViewById(R.id.transactiontype);
            purpose=itemView.findViewById(R.id.purpose);
            line=itemView.findViewById(R.id.line);

        }


    }


}
