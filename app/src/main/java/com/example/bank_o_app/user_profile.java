package com.example.bank_o_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user_profile extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference databaseReference;

    private TextView fullname_holder, email_holder, address_holder, mobile_no_holder;
    private TextView cash_in_history, cash_out_history, savings, contacts, reset_password;
    private Button logout_btn;

    private String userID;
    private String fullname, email, address, mobile_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        _init_();

        DatabaseReference userProfileReference = databaseReference.child("Users").child(userID);
        userProfileReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_data obj_user_data = snapshot.getValue(user_data.class);
                if(obj_user_data != null)
                {
                    fullname = obj_user_data.fullname;
                    email = obj_user_data.email;
                    address = obj_user_data.address;
                    mobile_no = obj_user_data.mobile_no;

                    fullname_holder.setText(fullname);
                    email_holder.setText(email);
                    address_holder.setText(address);
                    mobile_no_holder.setText(mobile_no);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // BOTTOM NAVIGATION //
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.user_profile_tab);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_tab:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
//                    case R.id.transaction_tab:
//                        startActivity(new Intent(getApplicationContext(), transaction.class));
//                        overridePendingTransition(0, 0);
                    case R.id.user_profile_tab:
                        return true;
                }
                return false;
            }
        });
        // BOTTOM NAVIGATION END //

        cash_in_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_profile.this, cash_in_history.class);
                startActivity(intent);
            }
        });

        cash_out_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_profile.this, cash_out_history.class);
                startActivity(intent);
            }
        });

        savings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_profile.this, savings.class);
                startActivity(intent);
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_profile.this, contacts.class);
                startActivity(intent);
            }
        });

        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_profile.this, reset_password.class);
                startActivity(intent);
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logout();
            }
        });
    }

    public void _init_() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        fullname_holder = findViewById(R.id.fullname_holder);
        email_holder = findViewById(R.id.email_holder);
        address_holder = findViewById(R.id.address_holder);
        mobile_no_holder = findViewById(R.id.mobile_no_holder);
        cash_in_history = findViewById(R.id.cash_in_history);
        cash_out_history = findViewById(R.id.cash_out_history);
        savings = findViewById(R.id.savings);
        contacts = findViewById(R.id.contacts);
        reset_password = findViewById(R.id.reset_password);
        logout_btn = findViewById(R.id.logout_btn);
    }

    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(user_profile.this);
        builder.setTitle("LOG OUT");
        builder.setMessage("Are you sure you want to log out?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(user_profile.this, login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(user_profile.this, "Successfully Logout!", Toast.LENGTH_SHORT).show();
                finish();
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
}