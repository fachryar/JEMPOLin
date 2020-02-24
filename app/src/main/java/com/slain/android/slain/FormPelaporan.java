package com.slain.android.slain;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FormPelaporan extends AppCompatActivity{

    ImageView img;
    EditText editDeskripsi;
    TextView textKategori;
    CheckBox cek1, cek2, cek3, cek4, cek5, cek6;
    String isiKategori = ". . .";
    Button submit;
    public int stat = 0;
    GMailSender sender;
//    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pelaporan);

        img = (ImageView) findViewById(R.id.updatePict);
        editDeskripsi = (EditText) findViewById(R.id.editDeskripsi);
        cek1 = (CheckBox) findViewById(R.id.checkMobil);
        cek2 = (CheckBox) findViewById(R.id.checkPerangkat);
        cek3 = (CheckBox) findViewById(R.id.checkRumah);
        cek4 = (CheckBox) findViewById(R.id.checkCubicle);
        cek5 = (CheckBox) findViewById(R.id.checkTaman);
        cek6 = (CheckBox) findViewById(R.id.checkDll);
        submit = (Button) findViewById(R.id.btnSubmit);
        textKategori = (TextView) findViewById(R.id.kategori);

        sender = new GMailSender("adm.jempolin@gmail.com", "xxx");

        //checking the permission
        //if the permission is not given we will open setting to add permission
        //else app will not open
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }

        //Checkbutton kategori
        cek1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cek2.setChecked(false);
                cek3.setChecked(false);
                cek4.setChecked(false);
                cek5.setChecked(false);
                cek6.setChecked(false);
                isiKategori = "Mobil Dinas";
                textKategori.setText(isiKategori);
            }
        });
        cek2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cek1.setChecked(false);
                cek3.setChecked(false);
                cek4.setChecked(false);
                cek5.setChecked(false);
                cek6.setChecked(false);
                isiKategori = "Perangkat IT";
                textKategori.setText(isiKategori);
            }
        });
        cek3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cek1.setChecked(false);
                cek2.setChecked(false);
                cek4.setChecked(false);
                cek5.setChecked(false);
                cek6.setChecked(false);
                isiKategori = "Rujab/Rudin";
                textKategori.setText(isiKategori);
            }
        });
        cek4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cek1.setChecked(false);
                cek2.setChecked(false);
                cek3.setChecked(false);
                cek5.setChecked(false);
                cek6.setChecked(false);
                isiKategori = "Cubicle";
                textKategori.setText(isiKategori);
            }
        });
        cek5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cek1.setChecked(false);
                cek2.setChecked(false);
                cek3.setChecked(false);
                cek4.setChecked(false);
                cek6.setChecked(false);
                isiKategori = "Taman";
                textKategori.setText(isiKategori);
            }
        });
        cek6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cek1.setChecked(false);
                cek2.setChecked(false);
                cek3.setChecked(false);
                cek4.setChecked(false);
                cek5.setChecked(false);
                isiKategori = "Fasilitas Lain";
                textKategori.setText(isiKategori);
            }
        });

        findViewById(R.id.updatePict).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if the tags edittext is empty
                //we will throw input error
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editDeskripsi.getText().toString().trim().isEmpty()) {
                    editDeskripsi.setError("Isi deskripsi!");
                    editDeskripsi.requestFocus();
                    return;
                }
                if (!cek1.isChecked() && !cek2.isChecked() && !cek3.isChecked() && !cek4.isChecked()
                        && !cek5.isChecked() && !cek6.isChecked()){
                    Toast.makeText(FormPelaporan.this, "Pilih kategori", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();

            try {
                //getting bitmap object from uri
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                if (bitmap.getWidth() > bitmap.getHeight()) {
                    final Bitmap bMapRotate;
                    Matrix mat=new Matrix();
                    mat.postRotate(90);
                    bMapRotate = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(),bitmap.getHeight(), mat, true);
                    img.setImageBitmap(bMapRotate);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (editDeskripsi.getText().toString().trim().isEmpty()) {
                                editDeskripsi.setError("Isi deskripsi!");
                                editDeskripsi.requestFocus();
                                return;
                            }
                            if (!cek1.isChecked() && !cek2.isChecked() && !cek3.isChecked() && !cek4.isChecked()
                                    && !cek5.isChecked() && !cek6.isChecked()){
                                Toast.makeText(FormPelaporan.this, "Pilih kategori", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //calling the method uploadBitmap to upload image
                            uploadBitmap(bMapRotate);
                            finish();
                            Intent i = new Intent(FormPelaporan.this, Laporin.class);
                            startActivity(i);
                        }
                    });
                } else {
                    img.setImageBitmap(bitmap);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (editDeskripsi.getText().toString().trim().isEmpty()) {
                                editDeskripsi.setError("Isi deskripsi!");
                                editDeskripsi.requestFocus();
                                return;
                            }
                            if (!cek1.isChecked() && !cek2.isChecked() && !cek3.isChecked() && !cek4.isChecked()
                                    && !cek5.isChecked() && !cek6.isChecked()){
                                Toast.makeText(FormPelaporan.this, "Pilih kategori", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //calling the method uploadBitmap to upload image
                            uploadBitmap(bitmap);
                            finish();
                            Intent i = new Intent(FormPelaporan.this, Laporin.class);
                            startActivity(i);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using PNG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        //getting the tag from the edittext
        final String deskripsi = editDeskripsi.getText().toString().trim();
        final String kategori = isiKategori;
        final String status = String.valueOf(stat);
        final String note = "Tidak ada";
        final ProgressDialog pDialog;

//        pDialog = new ProgressDialog(FormPelaporan.this);
//        pDialog.setMessage("Processing. . .");
//        pDialog.show();


        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConfig.UPLOAD_URL,
                new Response.Listener<NetworkResponse>() {

                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            new MyAsyncClass().execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        pDialog.cancel();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                        pDialog.cancel();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kategori", kategori);
                params.put("deskripsi", deskripsi);
                params.put("status", status);
                params.put("id_user", HomeActivity.id);
                params.put("note", note);

                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    class MyAsyncClass extends AsyncTask<Void, Void, Void> {
        //variable untuk email
        String no = UUID.randomUUID().toString();
        String code = no.substring(0, 4);
        final String emailSistem = HomeActivity.emailSistem;
        final String emailPenerima = HomeActivity.emailAdmin;
        final String subject = "Laporan Kerusakan "+isiKategori+" #"+code;
        final String body = "Nama: "+HomeActivity.name+"\nFungsi: "+HomeActivity.fungsi+"\nKategori: "+isiKategori
                +"\n\nUser diatas telah melakukan pelaporan kerusakan melalui applikasi JEMPOL.in, " +
                "segera proses laporan tersebut melalui applikasi JEMPOL.in";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                // Add subject, Body, your mail Id, and receiver mail Id.
                sender.sendMail(subject, body, emailSistem, emailPenerima);
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
}

