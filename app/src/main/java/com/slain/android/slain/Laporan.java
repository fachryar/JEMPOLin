package com.slain.android.slain;

import java.io.Serializable;
import java.sql.Time;

public class Laporan implements Serializable{
    private int id, status, id_user;
    private String deskripsi, kategori, image, created_at, nama, fungsi, note, email;

    //constructor initializing values
    public Laporan(){}
    public Laporan(int id, String kategori, String deskripsi){
        this.id = id;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
    }
    public Laporan(int id, String kategori, String deskripsi, int status, int id_user){
        this.id = id;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.status = status;
        this.id_user = id_user;
    }
    public Laporan(int id, String deskripsi, String kategori, String created_at, int status, int id_user){
        this.id = id;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        this.created_at = created_at;
        this.status = status;
        this.id_user = id_user;
    }
    public Laporan(int id, String kategori, String deskripsi, String image, String created_at, int status, int id_user){
        this.id = id;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.image = image;
        this.created_at = created_at;
        this.status = status;
        this.id_user = id_user;
    }
    public Laporan(int id, String kategori, String deskripsi, String image, String created_at, int status, int id_user, String note){
        this.id = id;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.image = image;
        this.created_at = created_at;
        this.status = status;
        this.id_user = id_user;
        this.note = note;
    }
    public Laporan(int id, String kategori, String deskripsi, String image, String created_at, int status, String nama, String fungsi){
        this.id = id;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.image = image;
        this.created_at = created_at;
        this.status = status;
        this.nama = nama;
        this.fungsi = fungsi;
    }
    public Laporan(int id, String kategori, String deskripsi, String image, String created_at, int status, String note, String nama, String fungsi){
        this.id = id;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.image = image;
        this.created_at = created_at;
        this.status = status;
        this.note = note;
        this.nama = nama;
        this.fungsi = fungsi;
    }

    public Laporan(int id, String kategori, String deskripsi, String image, String created_at, int status, String note, String nama, String fungsi, String email){
        this.id = id;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.image = image;
        this.created_at = created_at;
        this.status = status;
        this.note = note;
        this.nama = nama;
        this.fungsi = fungsi;
        this.email = email;
    }

    //getters
    public int getId(){return id;}
    public String getDeskripsi(){return deskripsi;}
    public String getKategori(){return kategori;}
    public String getImage(){return image;}
    public String getCreated_at(){return created_at;}
    public int getStatus(){return status;}
    public int getId_user(){return  id_user;}
    public String getNote(){return note;}
    public String getNama(){return nama;}
    public String getFungsi(){return fungsi;}
    public String getEmail(){return email;}
}
