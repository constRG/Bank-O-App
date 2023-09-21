package com.example.bank_o_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class send_money extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private ImageView back_btn;
    private EditText enter_email, enter_amount;
    private Button send_money_btn;

    private String userID;
    private String email, str_amount;
    private double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        _init_();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        send_money_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = enter_email.getText().toString().trim();
                str_amount = enter_amount.getText().toString().trim();

                if(email.isEmpty()) {
                    enter_email.setError("Enter email!");
                    enter_email.requestFocus();
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    enter_email.setError("Enter valid email!");
                    enter_email.requestFocus();
                    return;
                }
                else if(str_amount.isEmpty()) {
                    enter_amount.setError("Enter amount!");
                    enter_amount.requestFocus();
                    return;
                }
                else {
                    amount = Double.parseDouble(str_amount);
                    Toast.makeText(send_money.this, "Money sent successfully!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.fetchSignInMethodsForEmail(enter_email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    boolean check = task.getResult().getSignInMethods().isEmpty();
                                    if(check) {
                                        enter_email.setError("User's email not exist!");
                                        enter_email.requestFocus();
                                        return;
                                    } else {
//                                        DatabaseReference usersReference = databaseReference.child("Users").child(userID);
//                                        Query queryByEmail = usersReference.orderByChild("email").equalTo(email);
//                                        queryByEmail.addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                DatabaseReference available_balance = databaseReference.child("Users").child(userID);
//                                                available_balance.child("available_balance").setValue(ServerValue.increment(amount));
//                                                Toast.makeText(send_money.this, "Money Sent Successfully!", Toast.LENGTH_SHORT).show();
//                                                onBackPressed();
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                            }
//                                        });
                                    }
                                }
                            });
                }
            }
        });
    }

    public void _init_() {
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        back_btn = findViewById(R.id.back_btn);
        enter_email = findViewById(R.id.enter_email);
        enter_amount = findViewById(R.id.enter_amount);
        send_money_btn = findViewById(R.id.send_money_btn);
    }

}