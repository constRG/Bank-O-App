package com.example.bank_o_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class contacts extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ArrayList<contact_data> contact_list;
    private contacts_adapter contactsAdapter;
    private FirebaseUser user;

    private ImageView back_btn, delete_contact_btn;
    private RecyclerView contacts_recycle_view;
    private FloatingActionButton add_contact_btn;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        _init_();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        DatabaseReference contactsReference = databaseReference.child("Users").child(userID).child("Contacts");
        contact_list = new ArrayList<>();
        contacts_recycle_view.setLayoutManager(new LinearLayoutManager(this));
        contactsAdapter = new contacts_adapter(this, contact_list);
        contacts_recycle_view.setAdapter(contactsAdapter);

        contactsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contact_list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    contact_data obj_contact_data = dataSnapshot.getValue(contact_data.class);
                    contact_list.add(obj_contact_data);
                }
                contactsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        delete_contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contacts.this, delete_contact_data.class);
                startActivity(intent);
            }
        });

        add_contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contacts.this, add_contact.class);
                startActivity(intent);
            }
        });
    }

    public void _init_() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        back_btn = findViewById(R.id.back_btn);
        delete_contact_btn = findViewById(R.id.delete_contact_btn);
        contacts_recycle_view = findViewById(R.id.contacts_recycle_view);
        add_contact_btn = findViewById(R.id.add_contact_btn);
    }
}