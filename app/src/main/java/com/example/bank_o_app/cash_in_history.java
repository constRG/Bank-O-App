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

public class cash_in_history extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private ArrayList<cash_in_data> cash_in_history_list;
    private cash_in_adapter cashInHistoryAdapter;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private ImageView back_btn, delete_cash_in_history_btn;
    private RecyclerView cash_in_history_recycle_view;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in_history);

        _init_();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        DatabaseReference cashInHistoryReference = databaseReference.child("Users").child(userID).child("Cash_in_history");
        cash_in_history_list = new ArrayList<>();
        cash_in_history_recycle_view.setLayoutManager(new LinearLayoutManager(this));
        cashInHistoryAdapter = new cash_in_adapter(this, cash_in_history_list);
        cash_in_history_recycle_view.setAdapter(cashInHistoryAdapter);

        cashInHistoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cash_in_history_list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    cash_in_data obj_cash_in_data = dataSnapshot.getValue(cash_in_data.class);
                    cash_in_history_list.add(obj_cash_in_data);
                }
                cashInHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        delete_cash_in_history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(cash_in_history.this);
                builder.setTitle("Delete cash in history");
                builder.setMessage("Are you sure you want to delete cash in history?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cashInHistoryReference.removeValue();
                        Toast.makeText(cash_in_history.this, "Cash In history deleted!", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                dialog = builder.create();
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

    public void _init_()
    {
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        back_btn = findViewById(R.id.back_btn);
        cash_in_history_recycle_view = findViewById(R.id.cash_in_history_recycle_view);
        delete_cash_in_history_btn = findViewById(R.id.delete_cash_in_history_btn);
    }
}