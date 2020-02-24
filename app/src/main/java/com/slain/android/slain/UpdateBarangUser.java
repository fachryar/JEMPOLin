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

public class UpdateBarangUser extends AppCompatActivity {
    Calendar CalendarA, CalendarB;
    DatePickerDialog.OnDateSetListener date1, date2;
    TimePickerDialog.OnTimeSetListener time;
    Button btnSubmit5;
    TextView awalJB, akhirJB, awalHB, akhirHB, textAwalHB, textAkhirHB;
    EditText wiBarang, wiKeterangan;
    int stat = 0;
    public String ide;
    String jamMulai, jamSelesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_booking_barang);

        btnSubmit5 = (Button) findViewById(R.id.btnSubmit5);
        textAwalHB = (TextView) findViewById(R.id.textAwalHB);
        textAkhirHB = (TextView) findViewById(R.id.textAkhirHB);
        awalHB = (TextView) findViewById(R.id.awalHB);
        akhirHB = (TextView) findViewById(R.id.akhirHB);
        awalJB = (TextView) findViewById(R.id.awalJB);
        akhirJB = (TextView) findViewById(R.id.akhirJB);
        wiBarang = (EditText) findViewById(R.id.wiBarang);
        wiKeterangan = (EditText) findViewById(R.id.wiKeterangan);

        final Barang myList = (Barang) getIntent().getExtras().getSerializable("myList");
        wiKeterangan.setText(myList.getKeterangan());
        wiBarang.setText(myList.getNama_barang());
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

            awalHB.setText(stringFormatter3.format(date1));
            akhirHB.setText(stringFormatter3.format(date2));
            textAwalHB.setText(stringFormatter1.format(date1));
            textAkhirHB.setText(stringFormatter1.format(date2));
            awalJB.setText(stringFormatter2.format(date1));
            akhirJB.setText(stringFormatter2.format(date2));
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

        textAwalHB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateBarangUser.this, date1, CalendarA
                        .get(Calendar.YEAR), CalendarA.get(Calendar.MONTH),
                        CalendarA.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        textAkhirHB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateBarangUser.this, date2, CalendarB
                        .get(Calendar.YEAR), CalendarB.get(Calendar.MONTH),
                        CalendarB.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        awalJB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar jamMulai = Calendar.getInstance();
                int hour = jamMulai.get(Calendar.HOUR_OF_DAY);
                int minute = jamMulai.get(Calendar.MINUTE);
                TimePickerDialog aTimePicker;
                aTimePicker = new TimePickerDialog(UpdateBarangUser.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        awalJB.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                aTimePicker.setTitle("Jam Mulai Peminjaman");
                aTimePicker.show();
            }
        });

        akhirJB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar jamSelesai = Calendar.getInstance();
                int ahour = jamSelesai.get(Calendar.HOUR_OF_DAY);
                int aminute = jamSelesai.get(Calendar.MINUTE);
                TimePickerDialog bTimePicker;
                bTimePicker = new TimePickerDialog(UpdateBarangUser.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        akhirJB.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, ahour, aminute, true);//Yes 24 hour time
                bTimePicker.setTitle("Jam Selesai Peminjaman");
                bTimePicker.show();
            }
        });

        btnSubmit5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jamMulai = awalHB.getText().toString() + " " + awalJB.getText().toString();
                jamSelesai = akhirHB.getText().toString() + " " + akhirJB.getText().toString();
                if (wiBarang.getText().toString().trim().isEmpty()) {
                    wiBarang.setError("Isi nama barang yang ingin dipinjam!");
                    wiBarang.requestFocus();
                    return;
                }
                if (wiKeterangan.getText().toString().trim().isEmpty()) {
                    wiKeterangan.setError("Isi tujuan peminjaman!");
                    wiKeterangan.requestFocus();
                    return;
                }
                if (textAwalHB.getText().toString().trim().isEmpty() || awalJB.getText().toString().trim().isEmpty()
                        || textAkhirHB.getText().toString().trim().isEmpty() || akhirJB.getText().toString().trim().isEmpty()) {
                    Toast.makeText(UpdateBarangUser.this, "Lengkapi data waktu peminjaman!", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UpdateBarangUser.this);
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

//                Toast.makeText(FormBookingBarang.this, textAwalHB.getText().toString().trim() +" - "
//                        +awalHB.getText().toString().trim()+"\n"+textAkhirHB.getText().toString().trim()+" - "
//                        +akhirHB.getText().toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateHariAwal() {
        String formatDB = "yyyy-MM-dd";
        String formatText = "EEEE, dd-MMMM-yyyy";
        SimpleDateFormat for_text = new SimpleDateFormat(formatText, Locale.US);
        SimpleDateFormat for_db = new SimpleDateFormat(formatDB, Locale.US);
        textAwalHB.setText(for_text.format(CalendarA.getTime()));
        awalHB.setText(for_db.format(CalendarA.getTime()));
    }
    private void updateHariAkhir() {
        String formatDB = "yyyy-MM-dd";
        String formatText = "EEEE, dd-MMMM-yyyy";
        SimpleDateFormat for_text = new SimpleDateFormat(formatText, Locale.US);
        SimpleDateFormat for_db = new SimpleDateFormat(formatDB, Locale.US);
        textAkhirHB.setText(for_text.format(CalendarB.getTime()));
        akhirHB.setText(for_db.format(CalendarB.getTime()));
    }

    private void submitForm(){
        final String nama_barang = wiBarang.getText().toString().trim();
        final String keterangan = wiKeterangan.getText().toString().trim();
        final String start = jamMulai;
        final String end = jamSelesai;
        final String status = String.valueOf(stat);
        final String id = ide;

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConfig.URL_UPDATE_BARANG_USER,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            finish();
                            Intent i = new Intent(UpdateBarangUser.this, Pinjamin.class);
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
                params.put("nama_barang", nama_barang);
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
