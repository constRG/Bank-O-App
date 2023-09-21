package com.example.bank_o_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class reset_password extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private ImageView back_btn;
    private EditText enter_email;
    private Button reset_password_btn;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        _init_();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        reset_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }
    private void resetPassword() {
        email = enter_email.getText().toString().trim();

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
        else {
           reset_password();
        }
    }

    public void _init_() {
        mAuth = FirebaseAuth.getInstance();

        back_btn = findViewById(R.id.back_btn);
        enter_email = findViewById(R.id.enter_email);
        reset_password_btn = findViewById(R.id.reset_password_btn);
        progressBar = findViewById(R.id.progressBar);
    }

    public void reset_password() {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(reset_password.this, reset_password_mail_sent.class);
                    startActivity(intent);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(reset_password.this, "Try again! something wrong happened!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}