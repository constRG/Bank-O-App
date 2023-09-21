package com.example.bank_o_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class cash_out extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private ImageView back_btn;
    private EditText enter_amount;
    private Button cash_out_btn;
    private FloatingActionButton cash_out_history_btn;

    private String userID;
    private String cash_out_id, date_and_time, str_amount;
    private String get_date_and_time;
    private double available_balance_value;
    private double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_out);

        _init_();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cash_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date_and_time = get_date_and_time;
                str_amount = enter_amount.getText().toString().trim();

                if(str_amount.isEmpty())
                {
                    enter_amount.setError("Enter amount");
                    enter_amount.requestFocus();
                    return;
                }
                else {
                    amount = Double.parseDouble(str_amount);

                    cash_out();
                }
            }
        });

        cash_out_history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cash_out.this, cash_out_history.class);
                startActivity(intent);
            }
        });
    }

    public void _init_() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM-dd-yyyy, hh:mm a");
        get_date_and_time = simpleDateFormat.format(calendar.getTime());

        back_btn = findViewById(R.id.back_btn);
        enter_amount = findViewById(R.id.enter_amount);
        cash_out_btn = findViewById(R.id.cash_out_btn);
        cash_out_history_btn = findViewById(R.id.cash_out_history_btn);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void cash_out() {
        DatabaseReference userProfileReference = databaseReference.child("Users");
        userProfileReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_data obj_user_data = snapshot.getValue(user_data.class);
                available_balance_value = obj_user_data.available_balance;

                if(available_balance_value == 0)
                {
                    Toast.makeText(cash_out.this, "Your available balance is 0", Toast.LENGTH_SHORT).show();
                }
                else if(available_balance_value <= 100)
                {
                    Toast.makeText(cash_out.this, "Your available balance is 100 or less", Toast.LENGTH_SHORT).show();
                }
                else if(amount > 10000)
                {
                    Toast.makeText(cash_out.this, "You cannot cash out amount greater than 10000", Toast.LENGTH_SHORT).show();
                }
                else if(amount == 0)
                {
                    Toast.makeText(cash_out.this, "You entered 0 amount!", Toast.LENGTH_SHORT).show();
                }
                else if(available_balance_value < amount)
                {
                    Toast.makeText(cash_out.this, "Your available balance is not enough!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    builder = new AlertDialog.Builder(cash_out.this);
                    builder.setTitle("Cash Out");
                    builder.setMessage("Are you sure you want to cash out?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference cashOutHistoryReference = databaseReference.child("Users").child(userID).child("Cash_out_history");
                            cash_out_id = databaseReference.push().getKey();

                            cash_out_data obj_cash_out_data = new cash_out_data(cash_out_id, date_and_time, amount);

                            cashOutHistoryReference.child(cash_out_id).setValue(obj_cash_out_data)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                DatabaseReference availableBalanceReference = databaseReference.child("Users").child(userID);
                                                availableBalanceReference.child("available_balance").setValue(ServerValue.increment(-amount));
                                                Intent intent = new Intent(cash_out.this, successfully_cashed_out.class);
                                                intent.putExtra("DATE_AND_TIME", date_and_time);
                                                intent.putExtra("CASH_OUT_AMOUNT", amount);
                                                intent.putExtra("CASH_OUT_ID", cash_out_id);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}