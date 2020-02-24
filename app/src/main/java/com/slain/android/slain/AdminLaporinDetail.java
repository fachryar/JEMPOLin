package com.slain.android.slain;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.media.ExifInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.slain.android.slain.app.AppController;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit2.http.Url;

public class AdminLaporinDetail extends AppCompatActivity {

    TextView deskripsi;
    TextView kategori;
    TextView status;
    TextView nama;
    TextView fungsi;
    TextView textNote;
    TextView noteLaporanText;
    TextView noteLaporan;
    ImageView image, image2;
    ImageButton selesai, proses, delete, close;
    public static String stat, id;
    private static final String TAG = AdminLaporinDetail.class.getSimpleName();
    String tag_json_obj = "json_obj_req";
    public String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_laporin_detail);

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
        nama = (TextView) findViewById (R.id.namaLaporan);
        fungsi = (TextView) findViewById (R.id.fungsiLaporan);
        image = (ImageView) findViewById (R.id.gambarLaporan);
        selesai = (ImageButton) findViewById(R.id.selesaiLaporan);
        proses = (ImageButton) findViewById(R.id.prosesLaporan);
        delete = (ImageButton) findViewById(R.id.deleteLaporan);
        noteLaporan = (TextView) findViewById(R.id.noteLaporan);
        textNote = (TextView) findViewById(R.id.textNote);
        noteLaporanText = (TextView) findViewById(R.id.noteLaporanText);
        deskripsi.setMovementMethod(new ScrollingMovementMethod());
        noteLaporanText.setMovementMethod(new ScrollingMovementMethod());


        final Laporan myList = (Laporan) getIntent().getExtras().getSerializable("myList");
        if(myList.getStatus() == 0){
            stat = "PENDING";
            proses.setVisibility(View.VISIBLE);
            selesai.setVisibility(View.GONE);
            textNote.setVisibility(View.GONE);
//            noteLaporan.setVisibility(View.GONE);
            noteLaporanText.setVisibility(View.GONE);
        } else if (myList.getStatus() == 1){
            stat = "ON PROCESS";
            selesai.setVisibility(View.VISIBLE);
            proses.setVisibility(View.GONE);
            textNote.setVisibility(View.GONE);
//            noteLaporan.setVisibility(View.VISIBLE);
            noteLaporanText.setVisibility(View.GONE);
        } else if (myList.getStatus() == 2){
            stat = "SELESAI";
            selesai.setVisibility(View.GONE);
            proses.setVisibility(View.GONE);
            textNote.setVisibility(View.VISIBLE);
//            noteLaporan.setVisibility(View.GONE);
            noteLaporanText.setVisibility(View.VISIBLE);
        }

        status.setText(stat);
        deskripsi.setText(myList.getDeskripsi());
        kategori.setText(myList.getKategori());
        nama.setText(myList.getNama());
        fungsi.setText("Fungsi "+myList.getFungsi());
        noteLaporanText.setText(myList.getNote());
        id = String.valueOf(myList.getId());
        email = myList.getEmail();

//        Picasso.get().load(getRightAngleImage(myList.getImage())).into(image, new Callback() {
//            @Override
//            public void onSuccess() {
////                Toast.makeText(AdminLaporinDetail.this, "Sukses", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Toast.makeText(AdminLaporinDetail.this, "Image source not found", Toast.LENGTH_SHORT).show();
//            }
//        });

        Picasso.get().load(Uri.parse(getRightAngleImage(myList.getImage()))).into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminLaporinDetail.this, FullScreenImage.class);
                i.putExtra("myList", myList);
                AdminLaporinDetail.this.startActivity(i);
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminLaporinDetail.this);
                alertDialogBuilder.setMessage("Hapus laporan ini?");
                alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteLaporan();
                        finish();
                        Intent i = new Intent(AdminLaporinDetail.this, AdminLaporin.class);
                        startActivity(i);
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

        proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminLaporinDetail.this);
                alertDialogBuilder.setMessage("Proses laporan ini?");
                alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prosesLaporan();
                        finish();
                        Intent i = new Intent(AdminLaporinDetail.this, AdminLaporin.class);
                        startActivity(i);
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

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminLaporinDetail.this);
                final EditText inputNote = new EditText(AdminLaporinDetail.this);
                alertDialogBuilder.setTitle("Masukkan pesan untuk pelapor");
                inputNote.setSingleLine(false);
                inputNote.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                inputNote.setLines(3);
                inputNote.setMaxLines(10);
                inputNote.setGravity(0);
                inputNote.setTextSize(12);
                alertDialogBuilder.setView(inputNote);

                alertDialogBuilder.setPositiveButton("Selesai", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String note = inputNote.getText().toString().trim();
                        noteLaporan.setText(note);
                        if (TextUtils.isEmpty(inputNote.getText().toString().trim())) {
                            Toast.makeText(AdminLaporinDetail.this, "Note admin untuk pelapor wajib diisi!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        selesaiLaporan();
                        finish();
                        Intent i = new Intent(AdminLaporinDetail.this, AdminLaporin.class);
                        startActivity(i);
                    }
                });
                alertDialogBuilder.setNegativeButton("Batal",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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

    private void prosesLaporan(){
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_PROSES_LAPORAN, new Response.Listener<String>() {

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

    private void selesaiLaporan(){
        final String note = noteLaporan.getText().toString().trim();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_SELESAI_LAPORAN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                new MyAsyncClass().execute();
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
                params.put("note", note);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private String getRightAngleImage(String photoPath) {

        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degree = 0;

            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
                default:
                    degree = 90;
            }

            return rotateImage(degree,photoPath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return photoPath;
    }

    class MyAsyncClass extends AsyncTask<Void, Void, Void> {
        //variable untuk email
        String no = UUID.randomUUID().toString();
        String code = no.substring(0, 4);
        final String emailSistem = MainActivityAdmin.emailSistem;
        final String emailPenerima = email;
        final String subject = "Laporan Kerusakan "+kategori.getText().toString().trim()+" #"+code;
        final String body = "Laporan kerusakan Anda telah selesai diproses, berikut adalah catatan dari administrator terkait dengan laporan Anda"
                +"\n\n'"+noteLaporan.getText().toString().trim()+"'"
                +"\n\nTerima kasih atas partisipasi Anda atas pelaporan kerusakan fasilitas di Kantor";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                MainActivityAdmin.sender.sendMail(subject, body, emailSistem, emailPenerima);
            }
            catch (Exception ex) {
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private String rotateImage(int degree, String imagePath){

        if(degree<=0){
            return imagePath;
        }
        try{
            Bitmap b= BitmapFactory.decodeFile(imagePath);

            Matrix matrix = new Matrix();
            if(b.getWidth()>b.getHeight()){
                matrix.setRotate(degree);
                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
                        matrix, true);
            }

            FileOutputStream fOut = new FileOutputStream(imagePath);
            String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            String imageType = imageName.substring(imageName.lastIndexOf(".") + 1);

            FileOutputStream out = new FileOutputStream(imagePath);
            if (imageType.equalsIgnoreCase("png")) {
                b.compress(Bitmap.CompressFormat.PNG, 100, out);
            }else if (imageType.equalsIgnoreCase("jpeg")|| imageType.equalsIgnoreCase("jpg")) {
                b.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
            fOut.flush();
            fOut.close();

            b.recycle();
        }catch (Exception e){
            e.printStackTrace();
        }
        return imagePath;
    }
}
