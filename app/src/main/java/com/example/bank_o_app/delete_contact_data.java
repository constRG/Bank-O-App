package com.example.bank_o_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.firebase.database.ValueEventListener;

public class delete_contact_data extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private ImageView back_btn;
    private EditText enter_email;
    private Button delete_data_btn;

    private String userID;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_contact_data);

        _init_();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        delete_data_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                else
                {
                    delete_contact_data();
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
        delete_data_btn = findViewById(R.id.delete_data_btn);
    }

    public void delete_contact_data() {
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
                            builder = new AlertDialog.Builder(delete_contact_data.this);
                            builder.setTitle("Delete Contact data");
                            builder.setMessage("Are you sure you want to delete this contact?");

                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DatabaseReference contactsReference = databaseReference.child("Users").child(userID)
                                            .child("Contacts");
                                    Query queryByEmail = contactsReference.orderByChild("email").equalTo(email);

                                    queryByEmail.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot dataSnapshot: snapshot.getChildren())
                                            {
                                                dataSnapshot.getRef().removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Toast.makeText(delete_contact_data.this, "Contact data deleted!", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
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
                    }
                });
    }
}