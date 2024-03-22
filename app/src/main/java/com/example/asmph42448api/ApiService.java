package com.example.asmph42448api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    String DOMAIN = "http://10.24.31.168:3000";

    @GET("/api/list")
    Call<List<SanPhamModel>> getSanphams();

    @POST("/add_sp")
    Call<SanPhamModel> addSanpham(SanPhamModel newSanPham);
    @DELETE("/xoa/{id}")
    Call<Void> deleteProduct(@Path("id") String id);
}
