package com.slain.android.slain;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import org.w3c.dom.Text;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class FormBookingRapat extends AppCompatActivity {
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener time;
    TextView editHari, editMulai, editSelesai;
    EditText editSubject, editPeserta, editRequest;
    Button btnSubmit;
    Spinner sRuangan;
    public int stat = 0;
    String jamMulai, jamSelesai;
    TextView simpanHari;

    String [] daftarRuangan = {
            "Ruang Rapat Sawarna",
            "Ruang Rapat SP PUR",
            "Ruang Rapat Krakatau",
            "Ballroom Surosowan",
            "Ruang Rapat Moneter",
            "Ruang Rapat Kepala Perwakilan"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_booking_rapat);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        editHari = (TextView) findViewById(R.id.editHari);
        editMulai = (TextView) findViewById(R.id.editMulai);
        editSelesai = (TextView) findViewById(R.id.editSelesai);
        sRuangan = (Spinner) findViewById(R.id.spinnerRuangan);
        simpanHari = (TextView) findViewById(R.id.simpanHari);
        editSubject = (EditText) findViewById(R.id.editSubject);
        editPeserta = (EditText) findViewById(R.id.editPeserta);
        editRequest = (EditText) findViewById(R.id.editRequest);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, daftarRuangan);
        sRuangan.setAdapter(adapter);

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTime();
            }
        };

        editHari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FormBookingRapat.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar jamMulai = Calendar.getInstance();
                int hour = jamMulai.get(Calendar.HOUR_OF_DAY);
                int minute = jamMulai.get(Calendar.MINUTE);
                TimePickerDialog aTimePicker;
                aTimePicker = new TimePickerDialog(FormBookingRapat.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editMulai.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                aTimePicker.setTitle("Jam Mulai Rapat");
                aTimePicker.show();
            }
        });

        editSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar jamSelesai = Calendar.getInstance();
                int ahour = jamSelesai.get(Calendar.HOUR_OF_DAY);
                int aminute = jamSelesai.get(Calendar.MINUTE);
                TimePickerDialog bTimePicker;
                bTimePicker = new TimePickerDialog(FormBookingRapat.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editSelesai.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, ahour, aminute, true);//Yes 24 hour time
                bTimePicker.setTitle("Jam Selesai Rapat");
                bTimePicker.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jamMulai = simpanHari.getText().toString() + " " + editMulai.getText().toString();
                jamSelesai = simpanHari.getText().toString() + " " + editSelesai.getText().toString();
                if (editHari.getText().toString().trim().isEmpty() || editMulai.getText().toString().trim().isEmpty()
                        || editSelesai.getText().toString().trim().isEmpty()) {
                    Toast.makeText(FormBookingRapat.this, "Lengkapi data waktu rapat!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editSubject.getText().toString().trim().isEmpty()) {
                    editSubject.setError("Isi Subject!");
                    editSubject.requestFocus();
                    return;
                }
                if (editPeserta.getText().toString().trim().isEmpty()) {
                    editPeserta.setError("Isi Peserta!");
                    editPeserta.requestFocus();
                    return;
                }
                submitForm();
            }
        });
    }
    private void updateTime() {
        String formatHari = "yyyy-MM-dd";
        String formatB = "EEEE, dd-MMMM-yyyy";
        SimpleDateFormat fb = new SimpleDateFormat(formatB, Locale.US);
        SimpleDateFormat fh = new SimpleDateFormat(formatHari, Locale.US);
        editHari.setText(fb.format(myCalendar.getTime()));
        simpanHari.setText(fh.format(myCalendar.getTime()));
    }

    private void submitForm(){
        final String ruangan = sRuangan.getSelectedItem().toString().trim();
        final String start = jamMulai;
        final String end = jamSelesai;
        final String subject = editSubject.getText().toString().trim();
        final String peserta = editPeserta.getText().toString().trim();
        final String request = editRequest.getText().toString().trim();
        final String status = String.valueOf(stat);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConfig.URL_SET_RAPAT,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            new MyAsyncClass().execute();
                            finish();
                            Intent i = new Intent(FormBookingRapat.this, Rapatin.class);
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
                params.put("ruangan", ruangan);
                params.put("start", start);
                params.put("end", end);
                params.put("subject", subject);
                params.put("peserta", peserta);
                params.put("request", request);
                params.put("status", status);
                params.put("id_user", HomeActivity.id);

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
        final String subject = "Peminjaman Ruang Rapat #"+code;
        final String body = "Nama: "+HomeActivity.name+"\nFungsi: "+HomeActivity.fungsi+"\nRuangan: "+sRuangan.getSelectedItem().toString().trim()
                +"\n\nUser diatas telah mengisi form peminjaman ruang rapat melalui applikasi JEMPOL.in, " +
                "segera proses peminjaman rapat tersebut melalui applikasi JEMPOL.in";

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

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Rapatin.class);
        startActivity(i);
        finish();
    }
}
