package com.slain.android.slain;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginUser extends AppCompatActivity {
    private static final String TAG = LoginUser.class.getSimpleName();
    private EditText inEmail, inPass;
    private Button btnLogin, btnRegist;
    private ProgressDialog pDialog;
    private ProgressBar progressBarLog;
    public static String fungsi, state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isNetworkAvailable(this)) {
            Toast.makeText(this,"No Internet connection",Toast.LENGTH_LONG).show();
            finish(); //Calling this method to close this activity when internet is not available.
        }

        //set view
        setContentView(R.layout.activity_login_user);
        inEmail = (EditText) findViewById(R.id.inputEmail);
        inPass = (EditText) findViewById(R.id.inputPass);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegist = (Button) findViewById(R.id.btn_register);
        progressBarLog = (ProgressBar) findViewById(R.id.progressBarLog);
        progressBarLog.setVisibility(View.INVISIBLE);

        User user = SharedPrefManager.getInstance(this).getUser();
        state = String.valueOf(user.getState());

        if (SharedPrefManager.getInstance(this).isLoggedIn() && user.getFungsi().equals("ADMIN")){
            Intent i = new Intent(this, MainActivityAdmin.class);
            startActivity(i);
            finish();
        } else if (SharedPrefManager.getInstance(this).isLoggedIn() && !user.getFungsi().equals("ADMIN") && state.equals("1")){
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginUser.this, SignUp.class);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });
    }

    private void userLogin(){
        //first getting the values
        final String email = inEmail.getText().toString().trim();
        final String password = inPass.getText().toString().trim();

        //validating inputs
        if (TextUtils.isEmpty(email)){
            inEmail.setError("Email harus diisi!");
            inEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            inPass.setError("Password harus diisi!");
            inPass.requestFocus();
            return;
        }
        progressBarLog.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBarLog.setVisibility(View.GONE);
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

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
                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginUser.this, "Login gagal, silahkan cek kembali email dan password Anda", Toast.LENGTH_SHORT).show();
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
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }
}
