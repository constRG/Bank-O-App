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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class cash_in extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private SimpleDateFormat simpleDateFormat;
    private Calendar calendar;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private ImageView back_btn;
    private EditText enter_amount;
    private Button cash_in_btn;
    private FloatingActionButton cash_in_history_btn;

    private String userID;
    private String cash_in_id, date_and_time, str_amount;
    private String get_date_and_time;
    private double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in);

        _init_();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cash_in_btn.setOnClickListener(new View.OnClickListener() {
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
                    cash_in();
                }
            }
        });

        cash_in_history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cash_in.this, cash_in_history.class);
                startActivity(intent);
            }
        });
    }

    public void _init_() {
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("EEEE, MMM-dd-yyyy, hh:mm a");
        get_date_and_time = simpleDateFormat.format(calendar.getTime());

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        back_btn = findViewById(R.id.back_btn);
        enter_amount = findViewById(R.id.enter_amount);
        cash_in_btn = findViewById(R.id.cash_in_btn);
        cash_in_history_btn = findViewById(R.id.cash_in_history_btn);
    }

    public void cash_in() {
        builder = new AlertDialog.Builder(cash_in.this);
        builder.setTitle("Cash In");
        builder.setMessage("Are you sure you want to cash in?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                amount = Double.parseDouble(str_amount);

                DatabaseReference cashInHistoryReference = databaseReference.child("Users").child(userID).child("Cash_in_history");
                cash_in_id = databaseReference.push().getKey();

                cash_in_data obj_cash_in_data = new cash_in_data(cash_in_id, date_and_time, amount);
                cashInHistoryReference.child(cash_in_id).setValue(obj_cash_in_data)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    DatabaseReference availableBalanceReference = databaseReference.child("Users").child(userID);
                                    availableBalanceReference.child("available_balance").setValue(ServerValue.increment(amount));
                                    Intent intent = new Intent(cash_in.this, successfully_cashed_in.class);
                                    intent.putExtra("DATE_AND_TIME", date_and_time);
                                    intent.putExtra("CASH_IN_AMOUNT", amount);
                                    intent.putExtra("CASH_IN_ID", cash_in_id);
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