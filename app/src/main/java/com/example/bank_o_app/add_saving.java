package com.example.bank_o_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class add_saving extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private ImageView back_btn;
    private EditText enter_saving_title, enter_saving_amount;
    private Button add_saving_btn;

    private String userID;
    private String saving_id;
    private String saving_title, str_saving_amount;
    private double available_balance_value;
    private double saving_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_saving);

        _init_();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        add_saving_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saving_title = enter_saving_title.getText().toString().trim();
                str_saving_amount = enter_saving_amount.getText().toString().trim();

                if(saving_title.isEmpty())
                {
                    enter_saving_title.setError("Enter saving title!");
                    enter_saving_title.requestFocus();
                    return;
                }
                else if(enter_saving_amount.length() == 0)
                {
                    enter_saving_amount.setError("Enter saving amount!");
                    enter_saving_amount.requestFocus();
                    return;
                }
                else
                {
                    add_saving();
                }
            }
        });
    }

    public void _init_() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        back_btn = findViewById(R.id.back_btn);
        enter_saving_title = findViewById(R.id.enter_saving_title);
        enter_saving_amount = findViewById(R.id.enter_saving_amount);
        add_saving_btn = findViewById(R.id.add_saving_btn);
    }

    public void add_saving() {
        saving_amount = Double.parseDouble(str_saving_amount);
        DatabaseReference userProfileReference = databaseReference.child("Users");
        userProfileReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_data obj_user_data = snapshot.getValue(user_data.class);
                available_balance_value = obj_user_data.available_balance;

                if(available_balance_value == 0)
                {
                    Toast.makeText(add_saving.this, "Your available balance is 0", Toast.LENGTH_SHORT).show();
                }
                else if(available_balance_value <= 100)
                {
                    Toast.makeText(add_saving.this, "Your available balance is 100 or less", Toast.LENGTH_SHORT).show();
                }
                else if(saving_amount == 0)
                {
                    Toast.makeText(add_saving.this, "You entered 0 amount!", Toast.LENGTH_SHORT).show();
                }
                else if(available_balance_value < saving_amount)
                {
                    Toast.makeText(add_saving.this, "Your available balance is not enough!", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(add_saving.this);
                    builder.setTitle("Add Saving");
                    builder.setMessage("Are you sure you want to add this saving?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference savingsReference = databaseReference.child("Users").child(userID).child("Savings");
                            saving_id = databaseReference.push().getKey();

                            saving_data obj_saving_data = new saving_data(saving_id, saving_title, saving_amount);

                            savingsReference.child(saving_id).setValue(obj_saving_data)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                DatabaseReference available_balance = databaseReference.child("Users").child(userID);
                                                available_balance.child("available_balance").setValue(ServerValue.increment(-saving_amount));
                                                Toast.makeText(add_saving.this, "Saving added!", Toast.LENGTH_SHORT).show();
                                                onBackPressed();
                                            }
                                        }
                                    });
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}