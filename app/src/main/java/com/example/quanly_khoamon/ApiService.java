package com.example.quanly_khoamon;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    // --- MÔN HỌC (Sửa theo hình Swagger) ---
    @GET("api/MonHoc")
    Call<List<MonHoc>> getListMonHoc();

    @POST("api/MonHoc")
    Call<Void> addMonHoc(@Body MonHoc monHoc);

    // QUAN TRỌNG: Thêm /{id} vào URL và dùng @Path
    @PUT("api/MonHoc/{id}")
    Call<Void> updateMonHoc(@Path("id") String id, @Body MonHoc monHoc);

    // QUAN TRỌNG: Thêm /{id} vào URL và dùng @Path
    @DELETE("api/MonHoc/{id}")
    Call<Void> deleteMonHoc(@Path("id") String id);

    // --- KHOA (Sửa theo hình Swagger) ---
    @GET("api/Khoa")
    Call<List<Khoa>> getListKhoa();

    @POST("api/Khoa")
    Call<Void> addKhoa(@Body Khoa khoa);

    @PUT("api/Khoa/{id}")
    Call<Void> updateKhoa(@Path("id") String id, @Body Khoa khoa);

    @DELETE("api/Khoa/{id}")
    Call<Void> deleteKhoa(@Path("id") String id);
}