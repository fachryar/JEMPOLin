package com.slain.android.slain;

public class User {
    int id, state;
    String nama, fungsi, email;

    public User(){

    }

    public User(int id, String nama, String fungsi, String email, int state) {
        this.id = id;
        this.nama = nama;
        this.fungsi = fungsi;
        this.email = email;
        this.state = state;
    }

    public int getId() {return id;}
    public String getNama() {return nama;}
    public String getFungsi() {return fungsi;}
    public String getEmail() {return email;}
    public int getState() {return state;}
}
