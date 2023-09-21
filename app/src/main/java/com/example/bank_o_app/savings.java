package com.example.bank_o_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class savings extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ArrayList<saving_data> savings_list;
    private savings_adapter savingsAdapter;
    private FirebaseUser user;

    private ImageView back_btn, add_savings_btn;
    private RecyclerView savings_recycle_view;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings);

        _init_();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        DatabaseReference savingsReference = databaseReference.child("Users").child(userID).child("Savings");
        savings_list = new ArrayList<>();
        savings_recycle_view.setLayoutManager(new LinearLayoutManager(this));
        savingsAdapter = new savings_adapter(this, savings_list);
        savings_recycle_view.setAdapter(savingsAdapter);

        savingsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                savings_list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    saving_data obj_saving_data = dataSnapshot.getValue(saving_data.class);
                    savings_list.add(obj_saving_data);
                }
                savingsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        add_savings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(savings.this, add_saving.class);
                startActivity(intent);
            }
        });
    }

    public void _init_() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        back_btn = findViewById(R.id.back_btn);
        add_savings_btn = findViewById(R.id.add_savings_btn);
        savings_recycle_view = findViewById(R.id.savings_recycle_view);
    }
}