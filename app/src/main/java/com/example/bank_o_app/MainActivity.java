package com.example.bank_o_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private DecimalFormat df;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private TextView fullname_holder, available_balance_holder;
    private Button cash_in, cash_out, send_money, contacts, messaging, savings;
    private Button cash_in_plus_btn;
    private BottomNavigationView bottomNavigationView;

    private String userID;
    private String fullname;
    private double available_balance_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _init_();

        cash_in_plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, cash_in.class);
                startActivity(intent);
            }
        });

        DatabaseReference userProfileReference = databaseReference.child("Users");
        userProfileReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_data obj_user_data = snapshot.getValue(user_data.class);
                if(obj_user_data != null)
                {
                    fullname = obj_user_data.fullname;
                    fullname_holder.setText(fullname);
                    available_balance_value = obj_user_data.available_balance;
                    available_balance_holder.setText("₱ " + String.valueOf(df.format(available_balance_value)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // BOTTOM NAVIGATION //
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home_tab);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_tab:
                        return true;
//                    case R.id.transaction_tab:
//                        startActivity(new Intent(getApplicationContext(), transaction.class));
//                        overridePendingTransition(0, 0);
//                        return true;
                    case R.id.user_profile_tab:
                        startActivity(new Intent(getApplicationContext(), user_profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        // BOTTOM NAVIGATION END //

        cash_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, cash_in.class);
                startActivity(intent);
            }
        });

        cash_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, cash_out.class);
                startActivity(intent);
            }
        });

        savings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, savings.class);
                startActivity(intent);
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, contacts.class);
                startActivity(intent);
            }
        });

//        send_money.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, send_money.class);
//                startActivity(intent);
//            }
//        });
//
//        messaging.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, messaging.class);
//                startActivity(intent);
//            }
//        });

    }

    public void _init_() {
        df = new DecimalFormat("###,##0.00");

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        fullname_holder = findViewById(R.id.fullname_holder);
        cash_in_plus_btn = findViewById(R.id.cash_in_plus_btn);
        available_balance_holder = findViewById(R.id.available_balance_holder);
        cash_in = findViewById(R.id.cash_in);
        cash_out = findViewById(R.id.cash_out);
        savings = findViewById(R.id.savings);
        contacts = findViewById(R.id.contacts);
//        send_money = findViewById(R.id.send_money);
//        messaging = findViewById(R.id.messaging);
    }
}
