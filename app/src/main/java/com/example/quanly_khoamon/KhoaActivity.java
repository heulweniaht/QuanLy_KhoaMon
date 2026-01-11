package com.example.quanly_khoamon;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 ************************************************************
 *  Như 1 vở kịch buồn , anh diễn trọn cả nhiều vai           *
 *                                                          *
 *                                  Thái đẹp trai vcl       *
 ************************************************************
 */

public class KhoaActivity extends AppCompatActivity {
    RecyclerView rcvKhoa;
    KhoaAdapter adapter;
    List<Khoa> listKhoa = new ArrayList<>();
    FloatingActionButton fabAdd,fabBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khoa);
        rcvKhoa = findViewById(R.id.rcvKhoa);
        fabAdd = findViewById(R.id.fabAddKhoa);
        fabBack = findViewById(R.id.fabBackKhoa);

        rcvKhoa.setLayoutManager(new LinearLayoutManager(this));
        adapter = new KhoaAdapter(this, listKhoa, this::showDialog);
        rcvKhoa.setAdapter(adapter);

        loadData();
        fabAdd.setOnClickListener(v -> showDialog(null));
        fabBack.setOnClickListener(v -> finish());
    }

    private void loadData() {
        ApiClient.getService().getListKhoa().enqueue(new Callback<List<Khoa>>() {
            @Override
            public void onResponse(Call<List<Khoa>> call, Response<List<Khoa>> response) {
                if(response.isSuccessful() && response.body() != null){
                    listKhoa.clear();
                    listKhoa.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Khoa>> call, Throwable t) {
                Toast.makeText(KhoaActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Thay thế hàm showDialog trong KhoaActivity
    private void showDialog(Khoa khoa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_khoa, null);
        builder.setView(view);
        Dialog dialog = builder.create();

        EditText edtMa = view.findViewById(R.id.edtMaKhoa);
        EditText edtTen = view.findViewById(R.id.edtTenKhoa);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnDelete = view.findViewById(R.id.btnDelete);
        TextView tvTitle = view.findViewById(R.id.tvTitle);

        boolean isUpdate = (khoa != null);

        if (isUpdate) {
            tvTitle.setText("CẬP NHẬT KHOA");
            edtMa.setText(khoa.getMaKhoa());
            edtMa.setEnabled(false);
            edtTen.setText(khoa.getTenKhoa());
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setText("THÊM KHOA");
            edtMa.setEnabled(true);
            btnDelete.setVisibility(View.GONE);
        }

        // --- NÚT LƯU KHOA ---
        btnSave.setOnClickListener(v -> {
            String ma = edtMa.getText().toString().trim();
            String ten = edtTen.getText().toString().trim();

            if (ma.isEmpty() || ten.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Khoa newKhoa = new Khoa(ma, ten);

            if (isUpdate) {
                ApiClient.getService().updateKhoa(ma, newKhoa).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(KhoaActivity.this, "Cập nhật Khoa thành công!", Toast.LENGTH_SHORT).show();
                            loadData();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(KhoaActivity.this, "Lỗi Server: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(KhoaActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                ApiClient.getService().addKhoa(newKhoa).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(KhoaActivity.this, "Thêm Khoa thành công!", Toast.LENGTH_SHORT).show();
                            loadData();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(KhoaActivity.this, "Lỗi thêm Khoa: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(KhoaActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // --- NÚT XÓA KHOA ---
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cảnh báo")
                    .setMessage("Xóa Khoa " + khoa.getTenKhoa() + "?")
                    .setPositiveButton("Xóa", (d, w) -> {
                        ApiClient.getService().deleteKhoa(khoa.getMaKhoa()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(KhoaActivity.this, "Đã xóa Khoa", Toast.LENGTH_SHORT).show();
                                    loadData();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(KhoaActivity.this, "Lỗi xóa: " + response.code(), Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(KhoaActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        dialog.show();
    }
}