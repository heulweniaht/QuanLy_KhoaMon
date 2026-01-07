package com.example.quanly_khoamon;

public class MonHoc {
    String maMH;
    String tenMon;
    int soTinChi;

    public MonHoc(String maMH, String tenMon, int soTinChi) {
        this.maMH = maMH;
        this.soTinChi = soTinChi;
        this.tenMon = tenMon;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public int getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(int soTinChi) {
        this.soTinChi = soTinChi;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }
}
