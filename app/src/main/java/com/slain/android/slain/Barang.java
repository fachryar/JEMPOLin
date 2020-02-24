package com.slain.android.slain;

import java.io.Serializable;

public class Barang implements Serializable{
    private int id, id_user, status;
    private String nama_barang, keterangan, start, end, nama, fungsi, email;

    public Barang (){}
    public Barang (int id, String nama_barang, String keterangan, String start, String end, int status, int id_user){
        this.id = id;
        this.nama_barang = nama_barang;
        this.keterangan = keterangan;
        this.start = start;
        this.end = end;
        this.status = status;
        this.id_user = id_user;
    }
    public Barang (int id, String nama_barang, String keterangan, String start, String end, int status, int id_user, String nama, String fungsi){
        this.id = id;
        this.nama_barang = nama_barang;
        this.keterangan = keterangan;
        this.start = start;
        this.end = end;
        this.status = status;
        this.id_user = id_user;
        this.nama = nama;
        this.fungsi = fungsi;
    }

    public Barang (int id, String nama_barang, String keterangan, String start, String end, int status, int id_user,
                   String nama, String fungsi, String email){
        this.id = id;
        this.nama_barang = nama_barang;
        this.keterangan = keterangan;
        this.start = start;
        this.end = end;
        this.status = status;
        this.id_user = id_user;
        this.nama = nama;
        this.fungsi = fungsi;
        this.email = email;
    }

    public Barang (int id, String nama_barang, String keterangan, String start, String end, int status){
        this.id = id;
        this.nama_barang = nama_barang;
        this.keterangan = keterangan;
        this.start = start;
        this.end = end;
        this.status = status;
    }

    public int getId(){return id;}
    public int getId_user(){return id_user;}
    public int getStatus(){return status;}
    public String getNama_barang(){return nama_barang;}
    public String getNama(){return nama;}
    public String getKeterangan(){return keterangan;}
    public String getStart(){return start;}
    public String getEnd(){return end;}
    public String getFungsi(){return fungsi;}
    public String getEmail(){return email;}
}
