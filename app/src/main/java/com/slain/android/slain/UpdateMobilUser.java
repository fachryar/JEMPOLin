package com.slain.android.slain;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UpdateMobilUser extends AppCompatActivity {
    Calendar CalendarA, CalendarB;
    DatePickerDialog.OnDateSetListener date1, date2;
    TimePickerDialog.OnTimeSetListener time;
    Button btnSubmit2;
    TextView awalHariMobil, akhirHariMobil, awalJamMobil, akhirJamMobil, simpanHariAwal, simpanHariAkhir;
    EditText editKeterangan;
    int stat = 0;
    public String ide;
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

        final Mobil myList = (Mobil) getIntent().getExtras().getSerializable("myList");
        editKeterangan.setText(myList.getKeterangan());
        ide = String.valueOf(myList.getId());

        try {
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = formatter1.parse(myList.getStart());
            Date date2 = formatter1.parse(myList.getEnd());

            String formatTanggal = "EEEE, dd-MMM-yyyy";
            SimpleDateFormat stringFormatter1 = new SimpleDateFormat(formatTanggal, Locale.US);
            String formatJam = "HH:mm";
            SimpleDateFormat stringFormatter2 = new SimpleDateFormat(formatJam, Locale.US);
            String formatHari = "yyyy-MM-dd";
            SimpleDateFormat stringFormatter3 = new SimpleDateFormat(formatHari, Locale.US);

            simpanHariAwal.setText(stringFormatter3.format(date1));
            simpanHariAkhir.setText(stringFormatter3.format(date2));
            awalHariMobil.setText(stringFormatter1.format(date1));
            akhirHariMobil.setText(stringFormatter1.format(date2));
            awalJamMobil.setText(stringFormatter2.format(date1));
            akhirJamMobil.setText(stringFormatter2.format(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
                new DatePickerDialog(UpdateMobilUser.this, date1, CalendarA
                        .get(Calendar.YEAR), CalendarA.get(Calendar.MONTH),
                        CalendarA.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        akhirHariMobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateMobilUser.this, date2, CalendarB
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
                aTimePicker = new TimePickerDialog(UpdateMobilUser.this, new TimePickerDialog.OnTimeSetListener() {
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
                bTimePicker = new TimePickerDialog(UpdateMobilUser.this, new TimePickerDialog.OnTimeSetListener() {
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
                    Toast.makeText(UpdateMobilUser.this, "Lengkapi data waktu peminjaman!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editKeterangan.getText().toString().trim().isEmpty()) {
                    editKeterangan.setError("Isi tujuan peminjaman!");
                    editKeterangan.requestFocus();
                    return;
                }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UpdateMobilUser.this);
                alertDialogBuilder.setMessage("Ubah form ini? \nPerubahan data pada form ini akan mengubah status menjadi PENDING, dan harus menuggu persetujuan dari admin");
                alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        submitForm();
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
        final String keterangan = editKeterangan.getText().toString().trim();
        final String start = jamMulai;
        final String end = jamSelesai;
        final String status = String.valueOf(stat);
        final String id = ide;

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConfig.URL_UPDATE_MOBIL_USER,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            finish();
                            Intent i = new Intent(UpdateMobilUser.this, Mobilin.class);
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
                params.put("keterangan", keterangan);
                params.put("start", start);
                params.put("end", end);
                params.put("status", status);
                params.put("id", id);

                return params;
            }
        };
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
}
