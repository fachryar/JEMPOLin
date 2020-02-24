package com.slain.android.slain;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.slain.android.slain.app.AppController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AdminPinjaminAdapter extends RecyclerView.Adapter<AdminPinjaminAdapter.AdminPinjaminViewHolder> {
    private List<Barang> listBarang;
    private Context mCtx;
    private static final String TAG = AdminPinjaminAdapter.class.getSimpleName();
    String tag_json_obj = "json_obj_req";
    public String email, barang, jamAwal, jamAkhir;

    public AdminPinjaminAdapter(Context mCtx, List<Barang> listBarang) {
        this.mCtx = mCtx;
        this.listBarang = listBarang;
    }

    @Override
    public AdminPinjaminViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_barang_admin, null);
        return new AdminPinjaminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdminPinjaminAdapter.AdminPinjaminViewHolder holder, int position) {
        final Barang myList = listBarang.get(position);
        final String id = String.valueOf(myList.getId());
        email = myList.getEmail();
        barang = myList.getNama_barang();

        int status = myList.getStatus();
        if (status == 0){
            holder.baVerify.setVisibility(View.VISIBLE);
            holder.baDecline.setVisibility(View.VISIBLE);
            holder.baEdit.setVisibility(View.GONE);
            holder.baSelesai.setVisibility(View.GONE);
            holder.baOver.setVisibility(View.GONE);
        }else if (status == 1){
            holder.baVerify.setVisibility(View.GONE);
            holder.baDecline.setVisibility(View.GONE);
            holder.baEdit.setVisibility(View.VISIBLE);
            holder.baSelesai.setVisibility(View.GONE);
            holder.baOver.setVisibility(View.GONE);
        }

        holder.baKeterangan.setText(myList.getKeterangan());
        holder.baNama.setText(myList.getNama_barang());
        holder.baFungsi.setText(myList.getNama()+" - Fungsi "+myList.getFungsi());

        try {
            SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = dateFormatter1.parse(myList.getStart());
            Date date2 = dateFormatter1.parse(myList.getEnd());
            Date current_date = Calendar.getInstance().getTime();

            String formatTanggal = "EEEE, dd MMMM";
            SimpleDateFormat stringFormatter1 = new SimpleDateFormat(formatTanggal, Locale.US);
            String formatJam = "HH:mm";
            SimpleDateFormat stringFormatter2 = new SimpleDateFormat(formatJam, Locale.US);

            holder.baWaktu.setText(stringFormatter1.format(date1) + " " +stringFormatter2.format(date1)+" s/d\n"
                    +stringFormatter1.format(date2) + " " +stringFormatter2.format(date2));
            jamAwal = stringFormatter1.format(date1)+" "+stringFormatter2.format(date1);
            jamAkhir = stringFormatter1.format(date2)+" "+stringFormatter2.format(date2);

            if (myList.getStatus() == 1) {
                if (current_date.compareTo(date1) >= 0 && current_date.compareTo(date2) <= 0) {
                    holder.baSelesai.setVisibility(View.VISIBLE);
                    holder.baOver.setVisibility(View.GONE);
                } else if (current_date.compareTo(date2) > 0) {
                    holder.baSelesai.setVisibility(View.VISIBLE);
                    holder.baOver.setVisibility(View.VISIBLE);
                } else {
                    holder.baSelesai.setVisibility(View.GONE);
                    holder.baOver.setVisibility(View.GONE);
                }
            } else {
                holder.baSelesai.setVisibility(View.GONE);
                holder.baOver.setVisibility(View.GONE);
                if (current_date.compareTo(date2) > 0) {
                    holder.baOver.setVisibility(View.VISIBLE);
                    holder.baOver.setText("SUDAH MELEBIHI BATAS WAKTU, AKAN DIHAPUS OLEH SISTEM");
                    deleteLaporan(id);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.baSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mCtx);
                alertDialogBuilder2.setMessage("Selesaikan peminjaman Barang ini?");
                alertDialogBuilder2.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteLaporan(id);
                    }
                });
                alertDialogBuilder2.setNegativeButton("Tidak",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                AlertDialog alertDialog2 = alertDialogBuilder2.create();
                alertDialog2.show();
            }
        });

        holder.baDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mCtx);
                alertDialogBuilder2.setMessage("Tolak peminjaman Barang ini?");
                alertDialogBuilder2.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteLaporan(id);
                        Intent i = new Intent(mCtx, AdminPinjamin.class);
                        mCtx.startActivity(i);
                    }
                });
                alertDialogBuilder2.setNegativeButton("Tidak",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                AlertDialog alertDialog2 = alertDialogBuilder2.create();
                alertDialog2.show();
            }
        });

        holder.baVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mCtx);
                alertDialogBuilder2.setMessage("Setujui peminjaman Barang ini?");
                alertDialogBuilder2.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        verifyBarang(id);
                    }
                });
                alertDialogBuilder2.setNegativeButton("Tidak",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                AlertDialog alertDialog2 = alertDialogBuilder2.create();
                alertDialog2.show();
            }
        });

        holder.baEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mCtx, UpdateBarang.class);
                i.putExtra("myList", myList);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(i);
            }
        });
    }

    private void deleteLaporan(final String ide){
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_DELETE_BARANG, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                Intent i = new Intent(mCtx, AdminPinjamin.class);
                mCtx.startActivity(i);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ide);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void verifyBarang(final String ide){
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_ACC_BARANG, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                new MyAsyncClass().execute();
                Intent i = new Intent(mCtx, AdminPinjamin.class);
                mCtx.startActivity(i);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ide);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public int getItemCount() {
        return listBarang.size();
    }

    public class AdminPinjaminViewHolder extends RecyclerView.ViewHolder {
        public TextView baNama, baKeterangan, baWaktu, baOver, baFungsi;
        public Button baSelesai, baVerify, baDecline;
        public ImageButton baEdit;

        public AdminPinjaminViewHolder(View itemView) {
            super(itemView);

            baNama = (TextView) itemView.findViewById(R.id.baNama);
            baKeterangan = (TextView) itemView.findViewById(R.id.baKeterangan);
            baFungsi = (TextView) itemView.findViewById(R.id.baFungsi);
            baOver = (TextView)itemView.findViewById(R.id.baOver);
            baWaktu = (TextView)itemView.findViewById(R.id.baWaktu);
            baSelesai = (Button)itemView.findViewById(R.id.baSelesai);
            baVerify = (Button)itemView.findViewById(R.id.baVerify);
            baDecline = (Button)itemView.findViewById(R.id.baDecline);
            baEdit = (ImageButton)itemView.findViewById(R.id.baEdit);
        }
    }

    class MyAsyncClass extends AsyncTask<Void, Void, Void> {
        //variable untuk email
        String no = UUID.randomUUID().toString();
        String code = no.substring(0, 4);
        final String emailSistem = MainActivityAdmin.emailSistem;
        final String emailPenerima = email;
        final String subject = "Peminjaman Barang Kantor #"+code;
        final String body = "Form peminjaman barang kantor Anda telah disetujui oleh Admin"
                +"\n\nBarang: "+barang
                +"\nJam Mulai Peminjaman: "+jamAwal
                +"\nJam Selesai Peminjaman: "+jamAkhir;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                MainActivityAdmin.sender.sendMail(subject, body, emailSistem, emailPenerima);
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
}
