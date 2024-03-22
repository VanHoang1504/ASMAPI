package com.example.asmph42448api;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {
    ListView lvMain;
    private ApiService apiService;
    List<SanPhamModel> sanphamModels;
    AdapterSanPham adapterSanpham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        lvMain = findViewById(R.id.lvDanhSach);
        FloatingActionButton fltadd = findViewById(R.id.floatAddDanhSach);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        Call<List<SanPhamModel>> call = apiService.getSanphams();



        call.enqueue(new Callback<List<SanPhamModel>>() {
            @Override
            public void onResponse(Call<List<SanPhamModel>> call, Response<List<SanPhamModel>> response) {
                if (response.isSuccessful()) {
                    sanphamModels = response.body();

                    adapterSanpham = new AdapterSanPham(Home.this, sanphamModels);

                    lvMain.setAdapter(adapterSanpham);
                    adapterSanpham.setOnDeleteClickListener(new AdapterSanPham.OnDeleteClickListener() {
                        @Override
                        public void onDeleteClick(int position) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                            builder.setTitle("Thông báo");
                            builder.setMessage("Bạn có muốn xóa sản phẩm không");
                            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    deleteSanPham(position);
                                }
                            });
                            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(Home.this, "Không xóa", Toast.LENGTH_SHORT).show();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<SanPhamModel>> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });

        fltadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendialog();
            }
        });
    }

    private void opendialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_add_update, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        EditText editt = view.findViewById(R.id.edtTenSP);
        EditText edgia = view.findViewById(R.id.edtGiaSP);
        EditText edsoluong = view.findViewById(R.id.edtSoLuongSP);

        Button btnadd = view.findViewById(R.id.btnUPDATEup);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenSP = editt.getText().toString();
                String giaStr = edgia.getText().toString();
                String soluongStr = edsoluong.getText().toString();

                if (tenSP.isEmpty() || giaStr.isEmpty() || soluongStr.isEmpty()) {
                    Toast.makeText(Home.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                double gia;
                int soluong;


                try {
                    gia = Double.parseDouble(giaStr);
                    soluong = Integer.parseInt(soluongStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(Home.this, "Giá và số lượng phải là số", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (gia <= 0 || soluong <= 0) {
                    Toast.makeText(Home.this, "Giá và số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                SanPhamModel newSanPham = new SanPhamModel(tenSP, gia, soluong, true);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiService.DOMAIN)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                apiService = retrofit.create(ApiService.class);
                Call<SanPhamModel> call = apiService.addSanpham(newSanPham);
                call.enqueue(new Callback<SanPhamModel>() {
                    @Override
                    public void onResponse(Call<SanPhamModel> call, Response<SanPhamModel> response) {
                        if (response.isSuccessful()) {
                            SanPhamModel addedSanPham = response.body();
                            sanphamModels.add(addedSanPham);
                            adapterSanpham.notifyDataSetChanged();
                            Toast.makeText(Home.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(Home.this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SanPhamModel> call, Throwable t) {
                        Log.e("MainActivity", "Error adding sản phẩm: " + t.getMessage());
                        Toast.makeText(Home.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.show();
    }

//    private void deleteSanPham(int position) {
//        String id = sanphamModels.get(position).get_id();
//        Call<Void> call = apiService.deleteSanpham(id);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    sanphamModels.remove(position);
//                    adapterSanpham.notifyDataSetChanged();
//                    Toast.makeText(Home.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(Home.this, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.e("Delete", t.getMessage());
//                Toast.makeText(Home.this, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//    public void openUpdateDialog(SanPhamModel sanpham) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
//        LayoutInflater inflater = LayoutInflater.from(Home.this);
//        View view = inflater.inflate(R.layout.item_add_update, null);
//        builder.setView(view);
//
//        EditText editt = view.findViewById(R.id.edtTenSP);
//        EditText edgia = view.findViewById(R.id.edtGiaSP);
//        EditText edsoluong = view.findViewById(R.id.edtSoLuongSP);
//        Button btnadd = view.findViewById(R.id.btnUPDATEup);
//
//        editt.setText(sanpham.getTen());
//        edgia.setText(String.valueOf(sanpham.getGia()));
//        edsoluong.setText(String.valueOf(sanpham.getSoluong()));
//
//        AlertDialog dialog = builder.create();
//
//        btnadd.setText("Cập nhật");
//        btnadd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String tenSP = editt.getText().toString();
//                String giaStr = edgia.getText().toString();
//                String soluongStr = edsoluong.getText().toString();
//
//                if (tenSP.isEmpty() || giaStr.isEmpty() || soluongStr.isEmpty()) {
//                    Toast.makeText(Home.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                double gia;
//                int soluong;
//
//                try {
//                    gia = Double.parseDouble(giaStr);
//                    soluong = Integer.parseInt(soluongStr);
//                } catch (NumberFormatException e) {
//                    Toast.makeText(Home.this, "Giá và số lượng phải là số", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (gia <= 0 || soluong <= 0) {
//                    Toast.makeText(Home.this, "Giá và số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                sanpham.setTen(tenSP);
//                sanpham.setGia(gia);
//                sanpham.setSoluong(soluong);
//
//                Call<SanPhamModel> call = apiService.updateSanpham(sanpham.get_id(), sanpham);
//                call.enqueue(new Callback<SanphamModel>() {
//                    @Override
//                    public void onResponse(Call<SanphamModel> call, Response<SanphamModel> response) {
//                        if (response.isSuccessful()) {
//                            adapterSanpham.notifyDataSetChanged();
//                            Toast.makeText(Home.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss(); // Dismiss the dialog after successful update
//                        } else {
//                            Toast.makeText(Home.this, "Cập nhật sản phẩm thất bại", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<SanPhamModel> call, Throwable t) {
//                        Log.e("Update", "Error updating sản phẩm: " + t.getMessage());
//                        Toast.makeText(Home.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//
//        dialog.show();
//    }
}
