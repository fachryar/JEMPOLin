package com.slain.android.slain;

import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.slain.android.slain.app.AppController;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class DetailLaporan extends AppCompatActivity {
    TextView deskripsi, kategori, status, textuNote, isiNote;
    ImageView image;
    ImageButton delete;
    public static String id, stat;
    private static final String TAG = DetailLaporan.class.getSimpleName();
    String tag_json_obj = "json_obj_req";
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan);

//        final OkHttpClient client = new OkHttpClient.Builder()
//                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
//                .build();
//
//        final Picasso picasso = new Picasso.Builder(this)
//                .downloader(new OkHttp3Downloader(client))
//                .build();
//
//        Picasso.setSingletonInstance(picasso);
        Picasso.get().setLoggingEnabled(true);

        deskripsi = (TextView) findViewById (R.id.deskripsiLaporan);
        kategori = (TextView) findViewById (R.id.kategoriLaporan);
        status = (TextView) findViewById (R.id.statusLaporan);
        image = (ImageView) findViewById (R.id.gambarLaporan);
        delete = (ImageButton) findViewById(R.id.deleteLaporan);
        textuNote = (TextView) findViewById(R.id.textuNote);
        isiNote = (TextView) findViewById(R.id.isiNote);

        deskripsi.setMovementMethod(new ScrollingMovementMethod());
        isiNote.setMovementMethod(new ScrollingMovementMethod());

        final Laporan myList = (Laporan) getIntent().getExtras().getSerializable("myList");
        if(myList.getStatus() == 0){
            stat = "PENDING";
            delete.setVisibility(View.VISIBLE);
            textuNote.setVisibility(View.GONE);
            isiNote.setVisibility(View.GONE);
        } else if (myList.getStatus() == 1){
            stat = "ON PROCESS";
            delete.setVisibility(View.GONE);
            textuNote.setVisibility(View.GONE);
            isiNote.setVisibility(View.GONE);
        } else if (myList.getStatus() == 2){
            stat = "SELESAI";
            delete.setVisibility(View.GONE);
            textuNote.setVisibility(View.VISIBLE);
            isiNote.setVisibility(View.VISIBLE);
        }
        deskripsi.setText(myList.getDeskripsi());
        kategori.setText(myList.getKategori());
        isiNote.setText(myList.getNote());
        status.setText(stat);
        id = String.valueOf(myList.getId());

        Picasso.get().load(myList.getImage()).into(image, new Callback() {
            @Override
            public void onSuccess() {
//                Toast.makeText(DetailLaporan.this, "Sukses", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(DetailLaporan.this, "Image source not found", Toast.LENGTH_SHORT).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailLaporan.this);
                alertDialogBuilder.setMessage("Hapus laporan ini?");
                alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteLaporan();
                        Intent i = new Intent(DetailLaporan.this, Laporin.class);
                        startActivity(i);
                        finish();
                    }
                });
                alertDialogBuilder.setNegativeButton("Tidak",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailLaporan.this, FullScreenImage.class);
//                                Bundle b = new Bundle();
//                                b.putString("id", s);
                i.putExtra("myList", myList);
                DetailLaporan.this.startActivity(i);
            }
        });
    }

    private void deleteLaporan(){
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_DELETE_LAPORAN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

}


