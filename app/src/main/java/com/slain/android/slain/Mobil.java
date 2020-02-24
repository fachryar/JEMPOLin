package com.slain.android.slain;

import java.io.Serializable;

public class Mobil implements Serializable {
    private int id, id_user, status;
    private String nama_mobil, keterangan, start, end, nama, fungsi, email;

    //Constructor
    public Mobil(){}
    public Mobil(int id, String nama_mobil, String keterangan, String start, String end, int status, int id_user){
        this.id = id;
        this.nama_mobil = nama_mobil;
        this.keterangan = keterangan;
        this.start = start;
        this.end = end;
        this.status = status;
        this.id_user = id_user;
    }
    public Mobil(int id, String nama_mobil, String keterangan, String start, String end, int status, int id_user, String nama, String fungsi){
        this.id = id;
        this.nama_mobil = nama_mobil;
        this.keterangan = keterangan;
        this.start = start;
        this.end = end;
        this.status = status;
        this.id_user = id_user;
        this.nama = nama;
        this.fungsi = fungsi;
    }

    public Mobil(int id, String nama_mobil, String keterangan, String start, String end, int status, int id_user,
                 String nama, String fungsi, String email){
        this.id = id;
        this.nama_mobil = nama_mobil;
        this.keterangan = keterangan;
        this.start = start;
        this.end = end;
        this.status = status;
        this.id_user = id_user;
        this.nama = nama;
        this.fungsi = fungsi;
        this.email = email;
    }

    //Getter
    public int getId(){return id;}
    public int getId_user(){return id_user;}
    public int getStatus(){return status;}
    public String getNama_mobil(){return nama_mobil;}
    public String getNama(){return nama;}
    public String getKeterangan(){return keterangan;}
    public String getStart(){return start;}
    public String getEnd(){return end;}
    public String getFungsi(){return fungsi;}
    public String getEmail(){return email;}
}
