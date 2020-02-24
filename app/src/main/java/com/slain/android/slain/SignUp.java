package com.slain.android.slain;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class SignUp extends AppCompatActivity {
    private static final String TAG = SignUp.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private EditText mNama, mEmail, mPassword;
    private Button daftar;
    Spinner mFungsi;
//    private ProgressDialog pDialog;
    GMailSender sender;

    String [] daftarFungsi = {
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
        setContentView(R.layout.activity_sign_up);

        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        daftar = (Button) findViewById(R.id.button_daftar);
        mNama = (EditText) findViewById(R.id.inputNama);
        mFungsi = (Spinner) findViewById(R.id.spinner_fungsi);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, daftarFungsi);
        mFungsi.setAdapter(adapter);

        sender = new GMailSender("adm.jempolin@gmail.com", "xxx");

        // Progress dialog
//        pDialog = new ProgressDialog(this);
//        pDialog.setCancelable(false);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser(){
        final String nama = mNama.getText().toString().trim();
        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        final String fungsi = mFungsi.getSelectedItem().toString().trim();

        if (TextUtils.isEmpty(nama)) {
            mNama.setError("Masukkan Nama Anda!");
            mNama.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Masukkan Email Anda!");
            mEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Email Tidak Valid");
            mEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Masukkan Password!");
            mPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            mPassword.setError("Password minimal 6 kata!");
            mPassword.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Method.POST, AppConfig.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("nama"),
                                userJson.getString("fungsi"),
                                userJson.getString("email"),
                                userJson.getInt("state")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        new MyAsyncClass().execute();

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), LoginUser.class));
                        overridePendingTransition(0, 0);
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
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
                params.put("password", password);
                return params;
        }

    };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    class MyAsyncClass extends AsyncTask<Void, Void, Void> {
        String no = UUID.randomUUID().toString();
        String code = no.substring(0, 4);
        final String emailSistem = "adm.jempolin@gmail.com";
        final String emailPenerima = "far.tester888@gmail.com";
        final String subject = "Verifikasi User Baru #"+code;
        final String body = "Nama: "+mNama.getText().toString().trim()
                +"\nFungsi: "+mFungsi.getSelectedItem().toString().trim()
                +"\nEmail: "+ mEmail.getText().toString().trim()
                +"\n\nUser diatas telah mendaftar di Applikasi JEMPOL.in dan menunggu verifikasi dari admin, " +
                "segera verifikasi akun user tersebut melalui applikasi JEMPOL.in";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... mApi) {
            try {
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, LoginUser.class);
        startActivity(i);
        overridePendingTransition(0, 0);
        finish();
    }
}
