package com.example.bank_o_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class cash_out_history extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ArrayList<cash_out_data> cash_out_history_list;
    private cash_out_adapter cashOutHistoryAdapter;
    private FirebaseUser user;

    private ImageView back_btn, delete_cash_out_history_btn;
    private RecyclerView cash_out_history_recycle_view;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_out_history);

        _init_();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        DatabaseReference cashOutHistoryReference = databaseReference.child("Users").child(userID).child("Cash_out_history");
        cash_out_history_list = new ArrayList<>();
        cash_out_history_recycle_view.setLayoutManager(new LinearLayoutManager(this));
        cashOutHistoryAdapter = new cash_out_adapter(this, cash_out_history_list);
        cash_out_history_recycle_view.setAdapter(cashOutHistoryAdapter);

        cashOutHistoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cash_out_history_list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    cash_out_data obj_cash_out_data = dataSnapshot.getValue(cash_out_data.class);
                    cash_out_history_list.add(obj_cash_out_data);
                }
                cashOutHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        delete_cash_out_history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(cash_out_history.this);
                builder.setTitle("Delete cash out History");
                builder.setMessage("Are you sure you want to delete cash out history?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cashOutHistoryReference.removeValue();
                        Toast.makeText(cash_out_history.this, "Cash Out history deleted!", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.secondary_color));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.red));
                    }
                });
                dialog.show();
            }
        });
    }

    public void _init_() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        back_btn = findViewById(R.id.back_btn);
        cash_out_history_recycle_view = findViewById(R.id.cash_in_history_recycle_view);
        delete_cash_out_history_btn = findViewById(R.id.delete_cash_out_history_btn);
    }
}