package com.slain.android.slain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RapatUser extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<Rapat> listRapat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapat_user);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewRapatUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listRapat = new ArrayList<>();

        loadRecyclerViewItem();


    }

    private void loadRecyclerViewItem() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GET_RAPATKU,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                listRapat.add(new Rapat(
                                        product.getInt("id"),
                                        product.getString("ruangan"),
                                        product.getString("start"),
                                        product.getString("end"),
                                        product.getString("subject"),
                                        product.getString("peserta"),
                                        product.getString("request"),
                                        product.getInt("status")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            RapatUserAdapter adapter = new RapatUserAdapter(RapatUser.this, listRapat);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", HomeActivity.id);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Rapatin.class);
        startActivity(i);
        finish();
    }
}
