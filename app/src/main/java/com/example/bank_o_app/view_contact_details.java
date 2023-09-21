package com.example.bank_o_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class view_contact_details extends AppCompatActivity {
    private ImageView back_btn;
    private TextView fullname_holder, email_holder, mobile_no_holder, header_fullname_holder;

    private String fullname, email, mobile_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_details);

        _init_();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fullname = getIntent().getStringExtra("FULLNAME");
        email = getIntent().getStringExtra("EMAIL");
        mobile_no = getIntent().getStringExtra("MOBILE_NO");

        header_fullname_holder.setText(fullname);
        fullname_holder.setText(fullname);
        email_holder.setText(email);
        mobile_no_holder.setText(mobile_no);

    }

    public void _init_() {
        back_btn = findViewById(R.id.back_btn);
        header_fullname_holder = findViewById(R.id.header_fullname_holder);
        fullname_holder = findViewById(R.id.fullname_holder);
        email_holder = findViewById(R.id.email_holder);
        mobile_no_holder = findViewById(R.id.mobile_no_holder);
    }
}