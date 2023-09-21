package com.example.bank_o_app;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_contact extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private ImageView back_btn;
    private EditText enter_fullname, enter_email, enter_mobile_no;
    private Button add_contact_btn;

    private String userID;
    private String contact_id;
    private String fullname, email, mobile_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        _init_();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        add_contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname = enter_fullname.getText().toString().trim();
                email = enter_email.getText().toString().trim();
                mobile_no = enter_mobile_no.getText().toString().trim();

                if(fullname.isEmpty()) {
                    enter_fullname.setError("Enter fullname!");
                    enter_fullname.requestFocus();
                    return;
                }
                else if(email.isEmpty()) {
                    enter_email.setError("Enter email!");
                    enter_email.requestFocus();
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    enter_email.setError("Enter valid email!");
                    enter_email.requestFocus();
                    return;
                }
                else if(mobile_no.isEmpty()) {
                    enter_mobile_no.setError("Enter mobile number!");
                    enter_mobile_no.requestFocus();
                    return;
                }
                else if(mobile_no.length() != 11)
                {
                    enter_mobile_no.setError("Enter 11 numbers!");
                    enter_mobile_no.requestFocus();
                    return;
                }
                else {
                    add_contact();
                }
            }
        });
    }
    public void _init_() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        mAuth = FirebaseAuth.getInstance();

        back_btn = findViewById(R.id.back_btn);
        enter_fullname = findViewById(R.id.enter_fullname);
        enter_email = findViewById(R.id.enter_email);
        enter_mobile_no = findViewById(R.id.enter_mobile_no);
        add_contact_btn = findViewById(R.id.add_contact_btn);
    }

    public void add_contact() {
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
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference contactsReference = databaseReference.child("Users").child(userID).child("Contacts");
                            contact_id = contactsReference.push().getKey();
                            contact_data obj_contact_data = new contact_data(contact_id, fullname, email, mobile_no);

                            contactsReference.child(contact_id).setValue(obj_contact_data)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(add_contact.this, "Contact data added!", Toast.LENGTH_SHORT).show();
                                                onBackPressed();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
