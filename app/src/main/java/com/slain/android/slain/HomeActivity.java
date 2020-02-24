package com.slain.android.slain;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static String emailSistem = "adm.jempolin@gmail.com";
    public static String emailAdmin = "far.tester888@gmail.com";
    public static GMailSender sender;

    ProgressDialog pDialog;
    TextView text_nama, text_fungsi, text_email;
    public static String name, fungsi, id, state, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(!isNetworkAvailable(this)) {
            Toast.makeText(this,"No Internet connection",Toast.LENGTH_LONG).show();
            finish(); //Calling this method to close this activity when internet is not available.
        }


        // The request code used in ActivityCompat.requestPermissions()
        // and returned in the Activity's onRequestPermissionsResult()
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_SMS,
                android.Manifest.permission.CAMERA
        };
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();
        id = String.valueOf(user.getId());
        name = user.getNama();
        fungsi = user.getFungsi();
        state = String.valueOf(user.getState());
        email = user.getEmail();

        sender = new GMailSender("adm.jempolin@gmail.com", "xxx");

        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(this).isLoggedIn() || state.equals("0")) {
            Toast.makeText(this, "Akun anda belum terverifikasi", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, LoginUser.class);
            startActivity(i);
            finish();
        }
        if (SharedPrefManager.getInstance(this).isLoggedIn() && user.getFungsi().equals("ADMIN")){
            Intent i = new Intent(this, MainActivityAdmin.class);
            startActivity(i);
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        text_nama = header.findViewById(R.id.textView_1);
        text_fungsi = header.findViewById(R.id.textView_2);
        text_email = header.findViewById(R.id.textView_3);

        //setting the values to the textviews
        text_nama.setText(user.getNama());
        text_fungsi.setText("Fungsi " + user.getFungsi());
        text_email.setText(user.getEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_layout_laporin) {
            Intent intent = new Intent(HomeActivity.this, Laporin.class);
            startActivity(intent);
        } else if (id == R.id.nav_layout_rapatin){
            Intent intent = new Intent(HomeActivity.this, Rapatin.class);
            startActivity(intent);
        } else if (id == R.id.nav_layout_mobilin){
            Intent i = new Intent(HomeActivity.this, Mobilin.class);
            startActivity(i);
        } else if (id == R.id.nav_layout_pinjamin){
            Intent i = new Intent(HomeActivity.this, Pinjamin.class);
            startActivity(i);
        } else if (id == R.id.nav_layout_profil){
            Intent i = new Intent(HomeActivity.this, Profile.class);
            startActivity(i);
        } else if (id == R.id.nav_layout_logout) {
            SharedPrefManager.getInstance(HomeActivity.this).logout();
            Intent i = new Intent(HomeActivity.this, LoginUser.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }
}
