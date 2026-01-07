package com.example.quanly_khoamon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnMonHoc = findViewById(R.id.btnMonHoc);
        Button btnKhoa = findViewById(R.id.btnKhoa);
        btnMonHoc.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MonHocActivity.class));
        });
        btnKhoa.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, KhoaActivity.class));
        });
    }
}