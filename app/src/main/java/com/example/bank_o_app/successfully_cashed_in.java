package com.example.bank_o_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

public class successfully_cashed_in extends AppCompatActivity {
    private DecimalFormat df;
    private TextView date_and_time_holder, amount_holder, cash_in_id_holder;
    private TextView download_btn;
    private Button continue_btn;

    private double amount;
    private String date_and_time, cash_in_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfully_cashed_in);

        _init_();

        amount = getIntent().getDoubleExtra("CASH_IN_AMOUNT",0);
        date_and_time = getIntent().getStringExtra("DATE_AND_TIME");
        cash_in_id = getIntent().getStringExtra("CASH_IN_ID");

        date_and_time_holder.setText(date_and_time);
        amount_holder.setText("₱"+ String.valueOf(df.format(amount)));
        cash_in_id_holder.setText("Ref id." + cash_in_id);

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(successfully_cashed_in.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                print_cashed_in_pdf();
            }
        });
    }

    public void _init_() {
        df = new DecimalFormat("###,##0.00");

        date_and_time_holder = findViewById(R.id.date_and_time_holder);
        amount_holder = findViewById(R.id.amount_holder);
        cash_in_id_holder = findViewById(R.id.cash_out_id_holder);
        continue_btn = findViewById(R.id.continue_btn);
        download_btn = findViewById(R.id.download_btn);

        ActivityCompat.requestPermissions(successfully_cashed_in.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);
    }

    public void print_cashed_in_pdf() {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(250, 350, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Paint paint = new Paint();
        Paint linePaint = new Paint();
        Canvas canvas = page.getCanvas();

        paint.setTextSize(20);
        paint.setColor(Color.rgb(12, 26, 69));

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("BANK-O RECEIPT", canvas.getWidth()/2, 70, paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(7);
        canvas.drawText("Ref id." + cash_in_id, canvas.getWidth()/2, 90, paint);

        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setPathEffect(new DashPathEffect(new float[]{5,5}, 0));
        linePaint.setStrokeWidth(1);

        canvas.drawLine(20, 110, 230, 110, linePaint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(8);
        canvas.drawText("Transaction method: cashed in", 20, 130, paint);

        canvas.drawLine(20, 145, 230, 145, linePaint);

        paint.setTextSize(10);
        canvas.drawText("Amount : " + "₱" +String.valueOf(df.format(amount)), 20, 175, paint);

        canvas.drawLine(20, 200, 230, 200, linePaint);

        paint.setTextSize(8);
        canvas.drawText(date_and_time, 20, 220, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(9);
        canvas.drawText("Thank you for using bank-o app.", canvas.getWidth()/2, 290, paint);

        pdfDocument.finishPage(page);

        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/bank-o-cashed-in-receipt.pdf";
        File file = new File(filePath);

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Downloaded!", Toast.LENGTH_SHORT).show();
        pdfDocument.close();
    }
}