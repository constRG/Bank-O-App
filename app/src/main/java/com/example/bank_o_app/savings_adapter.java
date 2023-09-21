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

public class savings_adapter extends RecyclerView.Adapter<savings_adapter.MyViewHolder> {
    Context context;
    ArrayList<saving_data> savings_list;

    public savings_adapter(Context context, ArrayList<saving_data> savings_list) {
        this.context = context;
        this.savings_list = savings_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.saving_data_card, parent, false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull savings_adapter.MyViewHolder holder, int position) {
        DecimalFormat df;
        df = new DecimalFormat("###,##0.00");
        saving_data obj_saving_data = savings_list.get(position);
        holder.saving_title_holder.setText(obj_saving_data.getSaving_title());
        holder.saving_amount_holder.setText("₱ " + String.valueOf(df.format(obj_saving_data.getSaving_amount())));
    }

    @Override
    public int getItemCount() {
        return savings_list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView saving_title_holder, saving_amount_holder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            saving_title_holder = itemView.findViewById(R.id.saving_title_holder);
            saving_amount_holder = itemView.findViewById(R.id.saving_amount_holder);
        }
    }
}
