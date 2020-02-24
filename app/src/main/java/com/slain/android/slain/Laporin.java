package com.slain.android.slain;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class Laporin extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout refreshLayout;
    private ImageButton mImageButton, selLapBtn;
    //recyclerview objects
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    //model object for our list data
    private List<Laporan> listLaporan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporin);

        mImageButton = (ImageButton) findViewById(R.id.bookBtn);
        selLapBtn = (ImageButton) findViewById(R.id.selLapBtn);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewLaporin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listLaporan = new ArrayList<>();
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
                Intent i = new Intent(Laporin.this, Laporin.class);
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

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formLaporan();
            }
        });
        selLapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Laporin.this, LaporanSelesai.class);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });
    }
    private void loadRecyclerViewItem() {
        /*
         * Creating a String Request a
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_LIST_LAPORAN,
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
                                listLaporan.add(new Laporan(
                                        product.getInt("id"),
                                        product.getString("kategori"),
                                        product.getString("deskripsi"),
                                        product.getString("image"),
                                        product.getString("created_at"),
                                        product.getInt("status"),
                                        product.getInt("id_user")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            LaporanAdapter adapter = new LaporanAdapter(Laporin.this, listLaporan);
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

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void formLaporan(){
        Intent i = new Intent(Laporin.this, FormPelaporan.class);
        startActivity(i);
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