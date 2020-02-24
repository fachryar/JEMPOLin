package com.slain.android.slain;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

public class UpdateMobil extends AppCompatActivity {
    Calendar CalendarA, CalendarB;
    DatePickerDialog.OnDateSetListener date1, date2;
    TimePickerDialog.OnTimeSetListener time;
    Button btnSubmit3;
    TextView awalHariMobil2, akhirHariMobil2, awalJamMobil2, akhirJamMobil2,
            simpanHariAwal2, simpanHariAkhir2, namaFungsiMobil, editKeterangan2, textViewRental;
    Spinner spinnerPilihMobil2;
    EditText simpanMobil;
    int stat = 1;
    String ide, mobil, jamMulai, jamSelesai;;

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
                } else {
                    textViewRental.setVisibility(View.GONE);
                    simpanMobil.setVisibility(View.GONE);
                    mobil = spinnerPilihMobil2.getSelectedItem().toString().trim();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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

        awalHariMobil2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateMobil.this, date1, CalendarA
                        .get(Calendar.YEAR), CalendarA.get(Calendar.MONTH),
                        CalendarA.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        akhirHariMobil2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateMobil.this, date2, CalendarB
                        .get(Calendar.YEAR), CalendarB.get(Calendar.MONTH),
                        CalendarB.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        awalJamMobil2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar jamMulai = Calendar.getInstance();
                int hour = jamMulai.get(Calendar.HOUR_OF_DAY);
                int minute = jamMulai.get(Calendar.MINUTE);
                TimePickerDialog aTimePicker;
                aTimePicker = new TimePickerDialog(UpdateMobil.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        awalJamMobil2.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                aTimePicker.setTitle("Jam Mulai Peminjaman");
                aTimePicker.show();
            }
        });

        akhirJamMobil2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar jamSelesai = Calendar.getInstance();
                int ahour = jamSelesai.get(Calendar.HOUR_OF_DAY);
                int aminute = jamSelesai.get(Calendar.MINUTE);
                TimePickerDialog bTimePicker;
                bTimePicker = new TimePickerDialog(UpdateMobil.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        akhirJamMobil2.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, ahour, aminute, true);//Yes 24 hour time
                bTimePicker.setTitle("Jam Selesai Peminjaman");
                bTimePicker.show();
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

    private void updateHariAwal() {
        String formatDB = "yyyy-MM-dd";
        String formatText = "EEEE, dd-MMMM-yyyy";
        SimpleDateFormat for_text = new SimpleDateFormat(formatText, Locale.US);
        SimpleDateFormat for_db = new SimpleDateFormat(formatDB, Locale.US);
        awalHariMobil2.setText(for_text.format(CalendarA.getTime()));
        simpanHariAwal2.setText(for_db.format(CalendarA.getTime()));
    }
    private void updateHariAkhir() {
        String formatDB = "yyyy-MM-dd";
        String formatText = "EEEE, dd-MMMM-yyyy";
        SimpleDateFormat for_text = new SimpleDateFormat(formatText, Locale.US);
        SimpleDateFormat for_db = new SimpleDateFormat(formatDB, Locale.US);
        akhirHariMobil2.setText(for_text.format(CalendarB.getTime()));
        simpanHariAkhir2.setText(for_db.format(CalendarB.getTime()));
    }

    private void submitForm(){
        final String nama_mobil = mobil + simpanMobil.getText().toString().trim();
        final String start = jamMulai;
        final String end = jamSelesai;
        final String id = ide;

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConfig.URL_UPDATE_MOBIL_ADMIN,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            finish();
                            Intent i = new Intent(UpdateMobil.this, AdminMobilin.class);
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
                params.put("id", id);

                return params;
            }
        };
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
}
