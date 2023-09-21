package com.example.bank_o_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class cash_in_adapter extends RecyclerView.Adapter<cash_in_adapter.MyViewHolder> {
    Context context;
    ArrayList<cash_in_data> cash_in_history_list;

    public cash_in_adapter(Context context, ArrayList<cash_in_data> cash_in_history_list) {
        this.context = context;
        this.cash_in_history_list = cash_in_history_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cash_in_data_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cash_in_adapter.MyViewHolder holder, int position) {
        DecimalFormat df;
        df = new DecimalFormat("###,##0.00");
        cash_in_data obj_cash_in_data = cash_in_history_list.get(position);
        holder.date_and_time_holder.setText(obj_cash_in_data.getDate_and_time());
        holder.amount_holder.setText("+₱" + String.valueOf(df.format(obj_cash_in_data.getAmount())));
    }

    @Override
    public int getItemCount() {
        return cash_in_history_list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView date_and_time_holder, amount_holder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            date_and_time_holder = itemView.findViewById(R.id.date_and_time_holder);
            amount_holder = itemView.findViewById(R.id.amount_holder);
        }
    }
}
