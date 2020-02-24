package com.slain.android.slain;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AdminProsesMobil extends AppCompatActivity {
    TimePickerDialog.OnTimeSetListener time;
    Button btnSubmit3;
    TextView awalHariMobil2, akhirHariMobil2, awalJamMobil2, akhirJamMobil2,
            simpanHariAwal2, simpanHariAkhir2, namaFungsiMobil, editKeterangan2, textViewRental;
    Spinner spinnerPilihMobil2;
    EditText simpanMobil;
    int stat = 1;
    String ide, mobil, jamMulai, jamSelesai;;

    public String eEmail, eMobil, eJamMulai, eJamAkhir;

    String [] daftarMobil = {
            "KIJANG INNOVA A 1120",
            "KIJANG INNOVA A 763",
            "PAJERO SPORT A 1023 AZ",
            "CAMRY A 1022 AZ",
            "ALTIS A 761",
            "KIJANG LGX A 1291",
            "VIOS B 1115 PQB",
            "HILLUX B 8360",
            "RENTAL"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_proses_mobil);

        final Mobil myList = (Mobil) getIntent().getExtras().getSerializable("myList");
        eEmail = myList.getEmail();

        btnSubmit3 = (Button) findViewById(R.id.btnSubmit3);
        editKeterangan2 = (TextView) findViewById(R.id.editKeterangan2);
        awalHariMobil2 = (TextView) findViewById(R.id.awalHariMobil2);
        awalJamMobil2 = (TextView) findViewById(R.id.awalJamMobil2);
        akhirHariMobil2 = (TextView) findViewById(R.id.akhirHariMobil2);
        akhirJamMobil2 = (TextView) findViewById(R.id.akhirJamMobil2);
        simpanHariAwal2 = (TextView) findViewById(R.id.simpanHariAwal2);
        simpanHariAkhir2 = (TextView) findViewById(R.id.simpanHariAkhir2);
        textViewRental = (TextView) findViewById(R.id.textViewRental);
        simpanMobil = (EditText) findViewById(R.id.simpanMobil);
        namaFungsiMobil = (TextView) findViewById(R.id.namaFungsiMobil);
        spinnerPilihMobil2 = (Spinner) findViewById(R.id.spinnerPilihMobil2);

        editKeterangan2.setText(myList.getKeterangan());
        namaFungsiMobil.setText(myList.getNama()+"\n"+myList.getFungsi());
        ide = String.valueOf(myList.getId());

        try {
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = formatter1.parse(myList.getStart());
            Date date2 = formatter1.parse(myList.getEnd());

            String formatTanggal = "EEEE, dd-MMM-yyyy";
            SimpleDateFormat stringFormatter1 = new SimpleDateFormat(formatTanggal, Locale.US);
            String formatTanggal2 = "EEEE, dd MMMM";
            SimpleDateFormat stringFormatter4 = new SimpleDateFormat(formatTanggal2, Locale.US);
            String formatJam = "HH:mm";
            SimpleDateFormat stringFormatter2 = new SimpleDateFormat(formatJam, Locale.US);
            String formatHari = "yyyy-MM-dd";
            SimpleDateFormat stringFormatter3 = new SimpleDateFormat(formatHari, Locale.US);

            simpanHariAwal2.setText(stringFormatter3.format(date1));
            simpanHariAkhir2.setText(stringFormatter3.format(date2));
            awalHariMobil2.setText(stringFormatter1.format(date1));
            akhirHariMobil2.setText(stringFormatter1.format(date2));
            awalJamMobil2.setText(stringFormatter2.format(date1));
            akhirJamMobil2.setText(stringFormatter2.format(date2));
            eJamMulai = stringFormatter4.format(date1)+" "+stringFormatter2.format(date1);
            eJamAkhir = stringFormatter4.format(date2)+" "+stringFormatter2.format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, daftarMobil);
        spinnerPilihMobil2.setAdapter(adapter);

        spinnerPilihMobil2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerPilihMobil2.getSelectedItem().toString().equals("RENTAL")){
                    textViewRental.setVisibility(View.VISIBLE);
                    simpanMobil.setVisibility(View.VISIBLE);
                    mobil = "RENTAL - ";
                    eMobil = mobil + simpanMobil.getText().toString().trim();
                } else {
                    textViewRental.setVisibility(View.GONE);
                    simpanMobil.setVisibility(View.GONE);
                    mobil = spinnerPilihMobil2.getSelectedItem().toString().trim();
                    eMobil = mobil;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnSubmit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jamMulai = simpanHariAwal2.getText().toString() + " " + awalJamMobil2.getText().toString();
                jamSelesai = simpanHariAkhir2.getText().toString() + " " + akhirJamMobil2.getText().toString();
                submitForm();
            }
        });
    }

    private void submitForm(){
        final String nama_mobil = mobil + simpanMobil.getText().toString().trim();
        final String start = jamMulai;
        final String end = jamSelesai;
        final String status = String.valueOf(stat);
        final String id = ide;

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConfig.URL_PROSES_MOBIL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            new MyAsyncClass().execute();
                            finish();
                            Intent i = new Intent(AdminProsesMobil.this, AdminMobilin.class);
                            startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_mobil", nama_mobil);
                params.put("start", start);
                params.put("end", end);
                params.put("status", status);
                params.put("id", id);

                return params;
            }
        };
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    class MyAsyncClass extends AsyncTask<Void, Void, Void> {
        //variable untuk email
        String no = UUID.randomUUID().toString();
        String code = no.substring(0, 4);
        final String emailSistem = MainActivityAdmin.emailSistem;
        final String emailPenerima = eEmail;
        final String subject = "Peminjaman Mobil Dinas #"+code;
        final String body = "Form peminjaman Mobil Dinas Anda telah disetujui oleh Admin"
                +"\n\nMobil: "+eMobil+simpanMobil.getText().toString().trim()
                +"\nJam Mulai Peminjaman: "+eJamMulai
                +"\nJam Selesai Peminjaman: "+eJamAkhir;

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
}
