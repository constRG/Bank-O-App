package com.example.bank_o_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private EditText enter_email, enter_password;
    private Button login_btn;
    private TextView register, forgot_password;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _init_();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = enter_email.getText().toString().trim();
                password = enter_password.getText().toString().trim();

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
                else {
                    login();
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, registration.class);
                startActivity(intent);
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, reset_password.class);
                startActivity(intent);
            }
        });
    }

    public void _init_() {
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        enter_email = findViewById(R.id.enter_email);
        enter_password = findViewById(R.id.enter_password);
        login_btn = findViewById(R.id.login_btn);
        register = findViewById(R.id.register);
        forgot_password = findViewById(R.id.forgot_password);
    }

    public void login() {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()) {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(login.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(login.this, "Successfully login!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        user.sendEmailVerification();
                        Toast.makeText(login.this, "Check your email to verify your account!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(login.this, "Failed to Login!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}