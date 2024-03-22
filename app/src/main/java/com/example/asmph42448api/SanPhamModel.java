package com.example.asmph42448api;

public class SanPhamModel {
    private String _id;
    private String ten;
    private double gia;
    private int soluong;
    private Boolean tonkho;


    public SanPhamModel(String tenSP, double gia, int soluong) {
    }

    public SanPhamModel(String ten, double gia, int soluong, Boolean tonkho) {
        this.ten = ten;
        this.gia = gia;
        this.soluong = soluong;
        this.tonkho = tonkho;
    }

    public SanPhamModel() {
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public Boolean getTonkho() {
        return tonkho;
    }

    public void setTonkho(Boolean tonkho) {
        this.tonkho = tonkho;
    }
}
