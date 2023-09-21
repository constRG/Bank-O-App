package com.example.bank_o_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class registration extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private EditText enter_fullname, enter_email, enter_address, enter_mobile_no, enter_password, enter_confirm_password;
    private CheckBox terms_and_conditions_checkbox;
    private TextView terms_and_conditions_btn, login;
    private Button register_btn;

    private double available_balance;
    private String fullname, email, address, mobile_no, password, confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        _init_();

        terms_and_conditions_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registration.this, terms_and_conditions.class);
                startActivity(intent);
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                available_balance = 0;
                fullname = enter_fullname.getText().toString().trim();
                email = enter_email.getText().toString().trim();
                address = enter_address.getText().toString().trim();
                mobile_no = enter_mobile_no.getText().toString().trim();
                password = enter_password.getText().toString().trim();
                confirm_password = enter_confirm_password.getText().toString().trim();

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
                else if(address.isEmpty()) {
                    enter_address.setError("Enter address!");
                    enter_address.requestFocus();
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
                else if(password.isEmpty()) {
                    enter_password.setError("Enter password!");
                    enter_password.requestFocus();
                    return;
                }
                else if(password.length() < 6) {
                    enter_password.setError("Minimum password length should be 6!");
                    enter_password.requestFocus();
                    return;
                }
                else if(confirm_password.isEmpty()) {
                    enter_confirm_password.setError("Enter confirm password!");
                    enter_confirm_password.requestFocus();
                    return;
                }
                else if(!confirm_password.equals(password)) {
                    enter_confirm_password.setError("Password not match!");
                    enter_confirm_password.requestFocus();
                    return;
                }

                else if(!terms_and_conditions_checkbox.isChecked()) {
                    Toast.makeText(registration.this, "Please check the I Agree before register", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                   registration();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registration.this, login.class);
                startActivity(intent);
            }
        });
    }

    public void _init_() {
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        enter_fullname = findViewById(R.id.enter_fullname);
        enter_email = findViewById(R.id.enter_email);
        enter_address = findViewById(R.id.enter_address);
        enter_mobile_no = findViewById(R.id.enter_mobile_no);
        enter_password = findViewById(R.id.enter_password);
        enter_confirm_password = findViewById(R.id.enter_confirm_password);
        terms_and_conditions_checkbox = findViewById(R.id.terms_and_conditions_checkbox);
        terms_and_conditions_btn = findViewById(R.id.terms_and_conditions_btn);
        register_btn = findViewById(R.id.register_btn);
        login = findViewById(R.id.login);
    }

    public void registration() {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    user_data obj_user_data = new user_data(available_balance, fullname, email, address, mobile_no);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(obj_user_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(registration.this, successfully_registered.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(registration.this, "Failed to register!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(registration.this, "Failed to register! the email is already registered.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}