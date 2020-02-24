package com.slain.android.slain;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class FormBookingMobil extends AppCompatActivity {
    Calendar CalendarA, CalendarB;
    DatePickerDialog.OnDateSetListener date1, date2;
    TimePickerDialog.OnTimeSetListener time;
    Button btnSubmit2;
    TextView awalHariMobil, akhirHariMobil, awalJamMobil, akhirJamMobil, simpanHariAwal, simpanHariAkhir;
    EditText editKeterangan;
    int stat = 0;
    String jamMulai, jamSelesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_booking_mobil);

        btnSubmit2 = (Button) findViewById(R.id.btnSubmit2);
        editKeterangan = (EditText) findViewById(R.id.editKeterangan);
        awalHariMobil = (TextView) findViewById(R.id.awalHariMobil);
        awalJamMobil = (TextView) findViewById(R.id.awalJamMobil);
        akhirHariMobil = (TextView) findViewById(R.id.akhirHariMobil);
        akhirJamMobil = (TextView) findViewById(R.id.akhirJamMobil);
        simpanHariAwal = (TextView) findViewById(R.id.simpanHariAwal);
        simpanHariAkhir = (TextView) findViewById(R.id.simpanHariAkhir);

        CalendarA = Calendar.getInstance();
        date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                CalendarA.set(Calendar.YEAR, year);
                CalendarA.set(Calendar.MONTH, monthOfYear);
                CalendarA.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateHariAwal();
            }
        };

        CalendarB = Calendar.getInstance();
        date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                CalendarB.set(Calendar.YEAR, year);
                CalendarB.set(Calendar.MONTH, monthOfYear);
                CalendarB.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateHariAkhir();
            }
        };

        awalHariMobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(FormBookingMobil.this, date1, CalendarA
                        .get(Calendar.YEAR), CalendarA.get(Calendar.MONTH),
                        CalendarA.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        akhirHariMobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(FormBookingMobil.this, date2, CalendarB
                        .get(Calendar.YEAR), CalendarB.get(Calendar.MONTH),
                        CalendarB.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        awalJamMobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar jamMulai = Calendar.getInstance();
                int hour = jamMulai.get(Calendar.HOUR_OF_DAY);
                int minute = jamMulai.get(Calendar.MINUTE);
                TimePickerDialog aTimePicker;
                aTimePicker = new TimePickerDialog(FormBookingMobil.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        awalJamMobil.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                aTimePicker.setTitle("Jam Mulai Peminjaman");
                aTimePicker.show();
            }
        });

        akhirJamMobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar jamSelesai = Calendar.getInstance();
                int ahour = jamSelesai.get(Calendar.HOUR_OF_DAY);
                int aminute = jamSelesai.get(Calendar.MINUTE);
                TimePickerDialog bTimePicker;
                bTimePicker = new TimePickerDialog(FormBookingMobil.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        akhirJamMobil.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, ahour, aminute, true);//Yes 24 hour time
                bTimePicker.setTitle("Jam Selesai Peminjaman");
                bTimePicker.show();
            }
        });

        btnSubmit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jamMulai = simpanHariAwal.getText().toString() + " " + awalJamMobil.getText().toString();
                jamSelesai = simpanHariAkhir.getText().toString() + " " + akhirJamMobil.getText().toString();
                if (awalHariMobil.getText().toString().trim().isEmpty() || awalJamMobil.getText().toString().trim().isEmpty()
                        || akhirHariMobil.getText().toString().trim().isEmpty() || akhirJamMobil.getText().toString().trim().isEmpty()) {
                    Toast.makeText(FormBookingMobil.this, "Lengkapi data waktu peminjaman!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editKeterangan.getText().toString().trim().isEmpty()) {
                    editKeterangan.setError("Isi tujuan peminjaman!");
                    editKeterangan.requestFocus();
                    return;
                }
                submitForm();

//                Toast.makeText(FormBookingMobil.this, awalHariMobil.getText().toString().trim() +" - "
//                        +simpanHariAwal.getText().toString().trim()+"\n"+akhirHariMobil.getText().toString().trim()+" - "
//                        +simpanHariAkhir.getText().toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateHariAwal() {
        String formatDB = "yyyy-MM-dd";
        String formatText = "EEEE, dd-MMMM-yyyy";
        SimpleDateFormat for_text = new SimpleDateFormat(formatText, Locale.US);
        SimpleDateFormat for_db = new SimpleDateFormat(formatDB, Locale.US);
        awalHariMobil.setText(for_text.format(CalendarA.getTime()));
        simpanHariAwal.setText(for_db.format(CalendarA.getTime()));
    }
    private void updateHariAkhir() {
        String formatDB = "yyyy-MM-dd";
        String formatText = "EEEE, dd-MMMM-yyyy";
        SimpleDateFormat for_text = new SimpleDateFormat(formatText, Locale.US);
        SimpleDateFormat for_db = new SimpleDateFormat(formatDB, Locale.US);
        akhirHariMobil.setText(for_text.format(CalendarB.getTime()));
        simpanHariAkhir.setText(for_db.format(CalendarB.getTime()));
    }

    private void submitForm(){
        final String nama_mobil = "Belum ada";
        final String keterangan = editKeterangan.getText().toString().trim();
        final String start = jamMulai;
        final String end = jamSelesai;
        final String status = String.valueOf(stat);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConfig.URL_SET_MOBIL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            new MyAsyncClass().execute();
                            finish();
                            Intent i = new Intent(FormBookingMobil.this, Mobilin.class);
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
                params.put("keterangan", keterangan);
                params.put("start", start);
                params.put("end", end);
                params.put("status", status);
                params.put("id_user", HomeActivity.id);

                return params;
            }
        };
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    class MyAsyncClass extends AsyncTask<Void, Void, Void> {
        //variable untuk email
        String no = UUID.randomUUID().toString();
        String code = no.substring(0, 4);
        final String emailSistem = HomeActivity.emailSistem;
        final String emailPenerima = HomeActivity.emailAdmin;
        final String subject = "Peminjaman Mobil Dinas #"+code;
        final String body = "Nama: "+HomeActivity.name+"\nFungsi: "+HomeActivity.fungsi
                +"\n\nUser diatas telah mengisi form peminjaman mobil melalui applikasi JEMPOL.in, " +
                "segera proses peminjaman mobil tersebut melalui applikasi JEMPOL.in";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                HomeActivity.sender.sendMail(subject, body, emailSistem, emailPenerima);
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
