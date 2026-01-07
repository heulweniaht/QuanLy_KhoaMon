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

public class MonHocActivity extends AppCompatActivity {
    RecyclerView rcvMonHoc;
    MonHocAdapter adapter;
    List<MonHoc> listMonHoc = new ArrayList<>();
    FloatingActionButton fabAdd,fabBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_hoc);
        rcvMonHoc = findViewById(R.id.rcvMonHoc);
        fabAdd = findViewById(R.id.fabAdd);
        fabBack = findViewById(R.id.fabBack);
        rcvMonHoc.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MonHocAdapter(this, listMonHoc, this::showDialog);
        rcvMonHoc.setAdapter(adapter);

        loadData();

        fabAdd.setOnClickListener(v -> showDialog(null));
        fabBack.setOnClickListener(v -> finish());
    }

    private void loadData() {
        ApiClient.getService().getListMonHoc().enqueue(new Callback<List<MonHoc>>() {
            @Override
            public void onResponse(Call<List<MonHoc>> call, Response<List<MonHoc>> response) {
                if(response.isSuccessful() && response.body() != null){
                    listMonHoc.clear();
                    listMonHoc.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<MonHoc>> call, Throwable t) {
                Toast.makeText(MonHocActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Thay thế hàm showDialog cũ bằng hàm này:
    private void showDialog(MonHoc monHoc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_monhoc, null);
        builder.setView(view);
        Dialog dialog = builder.create();

        EditText edtMa = view.findViewById(R.id.edtMaMon);
        EditText edtTen = view.findViewById(R.id.edtTenMon);
        EditText edtTC = view.findViewById(R.id.edtTinChi);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnDelete = view.findViewById(R.id.btnDelete);
        TextView tvTitle = view.findViewById(R.id.tvTitle);

        boolean isUpdate = (monHoc != null);

        if (isUpdate) {
            tvTitle.setText("CẬP NHẬT MÔN HỌC");
            edtMa.setText(monHoc.getMaMH());
            edtMa.setEnabled(false); // Khóa mã
            edtTen.setText(monHoc.getTenMon());
            edtTC.setText(String.valueOf(monHoc.getSoTinChi()));
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setText("THÊM MÔN HỌC");
            edtMa.setEnabled(true);
            btnDelete.setVisibility(View.GONE);
        }

        // --- SỬA NÚT LƯU ---
        btnSave.setOnClickListener(v -> {
            String ma = edtMa.getText().toString().trim();
            String ten = edtTen.getText().toString().trim();
            String tcStr = edtTC.getText().toString().trim();

            if (ma.isEmpty() || ten.isEmpty() || tcStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            int tinChi = 0;
            try {
                tinChi = Integer.parseInt(tcStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số tín chỉ phải là số!", Toast.LENGTH_SHORT).show();
                return;
            }

            MonHoc newMH = new MonHoc(ma, ten, tinChi);

            // Debug: Báo đang xử lý
            Toast.makeText(this, "Đang gửi dữ liệu...", Toast.LENGTH_SHORT).show();

            if (isUpdate) {
                // CẬP NHẬT
                ApiClient.getService().updateMonHoc(ma, newMH).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(MonHocActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            loadData();
                            dialog.dismiss();
                        } else {
                            // HIỆN LỖI TỪ SERVER (QUAN TRỌNG)
                            Toast.makeText(MonHocActivity.this, "Lỗi cập nhật: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(MonHocActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                // THÊM MỚI
                ApiClient.getService().addMonHoc(newMH).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(MonHocActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                            loadData();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(MonHocActivity.this, "Lỗi thêm: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(MonHocActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // --- SỬA NÚT XÓA ---
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cảnh báo")
                    .setMessage("Bạn chắc chắn muốn xóa môn: " + monHoc.getTenMon() + "?")
                    .setPositiveButton("Xóa", (d, w) -> {
                        // Gọi API Xóa
                        ApiClient.getService().deleteMonHoc(monHoc.getMaMH()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(MonHocActivity.this, "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
                                    loadData();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(MonHocActivity.this, "Không thể xóa. Lỗi: " + response.code(), Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(MonHocActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        dialog.show();


    }
}