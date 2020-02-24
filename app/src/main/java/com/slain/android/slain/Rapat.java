package com.slain.android.slain;

import java.io.Serializable;

public class Rapat implements Serializable{
    private int id, status, id_user;
    private String ruangan, start, end, request, peserta, subject, nama, fungsi, email;

    //Constructor
    public Rapat() {}
    public Rapat(int id, String ruangan, String start, String end, String subject, String peserta,
                 String request, int status, String nama, String fungsi){
        this.id = id;
        this.ruangan = ruangan;
        this.start = start;
        this.end = end;
        this.subject = subject;
        this.peserta = peserta;
        this.request = request;
        this.status = status;
        this.nama = nama;
        this.fungsi = fungsi;
    }

    public Rapat(int id, String ruangan, String start, String end, String subject, String peserta,
                 String request, int status, String nama, String fungsi, String email){
        this.id = id;
        this.ruangan = ruangan;
        this.start = start;
        this.end = end;
        this.subject = subject;
        this.peserta = peserta;
        this.request = request;
        this.status = status;
        this.nama = nama;
        this.fungsi = fungsi;
        this.email = email;
    }

    public Rapat(int id, String ruangan, String start, String end, String subject, String peserta,
                 String request, int status){
        this.id = id;
        this.ruangan = ruangan;
        this.start = start;
        this.end = end;
        this.subject = subject;
        this.peserta = peserta;
        this.request = request;
        this.status = status;
    }

    //Getters
    public int getId(){return id;}
    public int getStatus(){return status;}
    public int getId_user(){return id_user;}
    public String getRuangan(){return ruangan;}
    public String getStart(){return start;}
    public String getEnd(){return end;}
    public String getSubject() {return subject;}
    public String getRequest(){return request;}
    public String getPeserta(){return peserta;}
    public String getNama(){return nama;}
    public String getFungsi(){return fungsi;}
    public String getEmail(){return email;}

}
