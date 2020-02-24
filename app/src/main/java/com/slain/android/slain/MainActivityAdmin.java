package com.slain.android.slain;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityAdmin extends AppCompatActivity {
    ImageButton logout, laporin, rapatin, userin, mobilin, pinjamin, profileBtn;
    TextView nama;
    public static String name, fungsi;
    public static GMailSender sender;
    public static String emailSistem = "adm.jempolin@gmail.com";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        if(!isNetworkAvailable(this)) {
            Toast.makeText(this,"No Internet connection",Toast.LENGTH_LONG).show();
            finish(); //Calling this method to close this activity when internet is not available.
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        User user = SharedPrefManager.getInstance(this).getUser();
        fungsi = user.getFungsi();
        name = user.getNama();
        sender = new GMailSender("adm.jempolin@gmail.com", "xxx");


        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent i = new Intent(this, LoginUser.class);
            startActivity(i);
            finish();
        }
        if (SharedPrefManager.getInstance(this).isLoggedIn() && !user.getFungsi().equals("ADMIN")){
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
            finish();
        }

        laporin = (ImageButton) findViewById(R.id.laporinBtn);
        rapatin = (ImageButton) findViewById(R.id.rapatinBtn);
        logout = (ImageButton) findViewById(R.id.logoutBtn);
        userin = (ImageButton) findViewById(R.id.userinBtn);
        mobilin = (ImageButton) findViewById(R.id.mobilinBtn);
        pinjamin = (ImageButton) findViewById(R.id.pinjaminBtn);
        profileBtn = (ImageButton) findViewById(R.id.profileBtn);
        nama = (TextView) findViewById(R.id.namaAdmin);

        nama.setText(name);

        laporin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivityAdmin.this, AdminLaporin.class);
                startActivity(i);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(MainActivityAdmin.this).logout();
                Intent i = new Intent(MainActivityAdmin.this, LoginUser.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        rapatin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivityAdmin.this, AdminRapatin.class);
                startActivity(i);
                finish();
            }
        });

        mobilin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivityAdmin.this, AdminMobilin.class);
                startActivity(i);
            }
        });

        pinjamin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivityAdmin.this, AdminPinjamin.class);
                startActivity(i);
            }
        });

        userin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivityAdmin.this, AdminAkun.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }
}
