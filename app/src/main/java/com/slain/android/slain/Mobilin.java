package com.slain.android.slain;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

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

public class Mobilin extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<Mobil> listMobil;
    private SwipeRefreshLayout refreshLayout;
    private ImageButton formMobilin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobilin);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMobilin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listMobil = new ArrayList<>();

        formMobilin = (ImageButton) findViewById(R.id.formMobilin);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        refreshLayout.setOnRefreshListener(this);

        loadRecyclerViewItem();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
            void refreshItems() {
                // Load complete
                refreshLayout.setRefreshing(true);
                Intent i = new Intent(Mobilin.this, Mobilin.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
                onItemsLoadComplete();
            }

            void onItemsLoadComplete() {
                // Stop refresh animation
                refreshLayout.setRefreshing(false);
            }
        });

        formMobilin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Mobilin.this, FormBookingMobil.class);
                startActivity(i);
            }
        });
    }

    private void loadRecyclerViewItem() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GET_MOBIL,
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
                                listMobil.add(new Mobil(
                                        product.getInt("id"),
                                        product.getString("nama_mobil"),
                                        product.getString("keterangan"),
                                        product.getString("start"),
                                        product.getString("end"),
                                        product.getInt("status"),
                                        product.getInt("id_user")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            MobilAdapter adapter = new MobilAdapter(Mobilin.this, listMobil);
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
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        overridePendingTransition(0, 0);
        finish();
    }

    public void onRefresh() {
        refreshLayout.setRefreshing(true);
    }
}
