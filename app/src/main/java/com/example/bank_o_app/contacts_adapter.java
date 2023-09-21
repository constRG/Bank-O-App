package com.example.bank_o_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class contacts_adapter extends RecyclerView.Adapter<contacts_adapter.MyViewHolder> {
    Context context;
    ArrayList<contact_data> contact_list;

    public contacts_adapter(Context context, ArrayList<contact_data> contact_list) {
        this.context = context;
        this.contact_list = contact_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_data_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull contacts_adapter.MyViewHolder holder, int position) {
        contact_data obj_contact_data = contact_list.get(position);
        holder.fullname_holder.setText(obj_contact_data.getFullname());
        holder.email_holder.setText(obj_contact_data.getEmail());
        holder.mobile_no_holder.setText(obj_contact_data.getMobile_no());

        holder.view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, view_contact_details.class);
                intent.putExtra("FULLNAME", holder.fullname_holder.getText());
                intent.putExtra("EMAIL", holder.email_holder.getText());
                intent.putExtra("MOBILE_NO", holder.mobile_no_holder.getText());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contact_list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView fullname_holder, email_holder, mobile_no_holder;
        private TextView view_details;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fullname_holder = itemView.findViewById(R.id.fullname_holder);
            email_holder = itemView.findViewById(R.id.email_holder);
            mobile_no_holder = itemView.findViewById(R.id.mobile_no_holder);

            view_details = itemView.findViewById(R.id.view_details);
        }
    }
}
