package com.slain.android.slain;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    EditText epNama, epEmail;
    TextView epFungsi;
    Spinner epSpinnerFungsi;
    Button epButton;
    public static String id;

    String [] daftarFungsi = {
            "Pilih fungsi baru. . .",
            "Asesmen Ekonomi & Surveilans",
            "Keuangan Inklusif",
            "Koordinasi & Komunikasi Kebijakan",
            "Pelaksanaan Pengembangan UMKM",
            "Perizinan & Pengawasan SP PUR",
            "Satuan Layanan Administrasi",
            "Unit Operasional SP",
            "Unit Pengelolaan Uang Rupiah",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        User user = SharedPrefManager.getInstance(this).getUser();
//        id = String.valueOf(user.getId());
//        name = user.getNama();
//        fungsi = user.getFungsi();
//        email = user.getEmail();

        epNama = (EditText) findViewById(R.id.epNama);
        epEmail = (EditText) findViewById(R.id.epEmail);
        epFungsi = (TextView) findViewById(R.id.epFungsi);
        epSpinnerFungsi = (Spinner) findViewById(R.id.epSpinnerFungsi);
        epButton = (Button) findViewById(R.id.epButton);

        epNama.setText(HomeActivity.name);
        epEmail.setText(HomeActivity.email);
        epFungsi.setText(HomeActivity.fungsi);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, daftarFungsi);
        epSpinnerFungsi.setAdapter(adapter);

        epSpinnerFungsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (epSpinnerFungsi.getSelectedItem().toString().equals("Asesmen Ekonomi & Surveilans")){
                    epFungsi.setText("Asesmen Ekonomi & Surveilans");
                } else if (epSpinnerFungsi.getSelectedItem().toString().equals("Keuangan Inklusif")){
                    epFungsi.setText("Keuangan Inklusif");
                } else if (epSpinnerFungsi.getSelectedItem().toString().equals("Koordinasi & Komunikasi Kebijakan")) {
                    epFungsi.setText("Koordinasi & Komunikasi Kebijakan");
                } else if (epSpinnerFungsi.getSelectedItem().toString().equals("Pelaksanaan Pengembangan UMKM")) {
                    epFungsi.setText("Pelaksanaan Pengembangan UMKM");
                } else if (epSpinnerFungsi.getSelectedItem().toString().equals("Perizinan & Pengawasan SP PUR")) {
                    epFungsi.setText("Perizinan & Pengawasan SP PUR");
                } else if (epSpinnerFungsi.getSelectedItem().toString().equals("Satuan Layanan Administrasi")) {
                    epFungsi.setText("Satuan Layanan Administrasi");
                } else if (epSpinnerFungsi.getSelectedItem().toString().equals("Unit Operasional SP")) {
                    epFungsi.setText("Unit Operasional SP");
                } else if (epSpinnerFungsi.getSelectedItem().toString().equals("Unit Pengelolaan Uang Rupiah")) {
                    epFungsi.setText("Unit Pengelolaan Uang Rupiah");
                } else if (epSpinnerFungsi.getSelectedItem().toString().equals("Pilih fungsi baru. . .")) {
                    epFungsi.setText(HomeActivity.fungsi);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                epFungsi.setText(HomeActivity.fungsi);
            }
        });

        epButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanUser();
                finish();
                Intent i = new Intent(Profile.this, HomeActivity.class);
                startActivity(i);
            }
        });
    }

    private void simpanUser(){
        final String nama = epNama.getText().toString().trim();
        final String fungsi = epFungsi.getText().toString().trim();
        final String email = epEmail.getText().toString().trim();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConfig.URL_EDIT_AKUN,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
//                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            Toast.makeText(Profile.this, "Data berhasil diubah, silahkan login ulang untuk melihat perubahan", Toast.LENGTH_LONG).show();
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
                params.put("nama", nama);
                params.put("fungsi", fungsi);
                params.put("email", email);
                params.put("id", HomeActivity.id);

                return params;
            }
        };
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
}
