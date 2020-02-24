package com.slain.android.slain;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class AdminRapatAdapter extends RecyclerView.Adapter<AdminRapatAdapter.AdminRapatViewHolder>{
    private List<Rapat> listRapat;
    private Context mCtx;
    private static final String TAG = AdminRapatAdapter.class.getSimpleName();
    String tag_json_obj = "json_obj_req";
    public String email, ruangan, peserta, jam, tambahan;

    public AdminRapatAdapter(Context mCtx, List<Rapat> listRapat) {
        this.mCtx = mCtx;
        this.listRapat = listRapat;
    }

    @Override
    public AdminRapatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_rapat_admin, null);
        return new AdminRapatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdminRapatAdapter.AdminRapatViewHolder holder, int position) {
        final Rapat myList = listRapat.get(position);
        holder.aSubject.setText(myList.getSubject());
        holder.aRuangan.setText("• "+myList.getRuangan());
        holder.aPeserta.setText("• "+myList.getPeserta());
        holder.aNama.setText(myList.getNama()+" - "+myList.getFungsi());
        final String id = String.valueOf(myList.getId());

        int status = myList.getStatus();
        if (status == 0){
            holder.aAccept.setVisibility(View.VISIBLE);
            holder.aDelete.setVisibility(View.VISIBLE);
            holder.aEdit.setVisibility(View.GONE);
        } else {
            holder.aAccept.setVisibility(View.GONE);
            holder.aDelete.setVisibility(View.VISIBLE);
            holder.aEdit.setVisibility(View.VISIBLE);
        }

        if (myList.getRequest().equals("")){
            holder.aRequest.setText("Keterangan Tambahan : \nTidak Ada");
            tambahan = "Tidak Ada";
        } else {
            holder.aRequest.setText("Keterangan Tambahan : \n"+myList.getRequest());
            tambahan = myList.getRequest();
        }

        try {
            SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = dateFormatter1.parse(myList.getStart());
            Date date2 = dateFormatter1.parse(myList.getEnd());

            String formatHari = "EEEE";
            SimpleDateFormat stringFormatter3 = new SimpleDateFormat(formatHari, Locale.US);
            String formatTanggal = "EEEE, dd MMMM";
            SimpleDateFormat stringFormatter1 = new SimpleDateFormat(formatTanggal, Locale.US);
            String formatJam = "HH:mm";
            SimpleDateFormat stringFormatter2 = new SimpleDateFormat(formatJam, Locale.US);
            String formatCheck = "yyyy-MM-dd";
            SimpleDateFormat stringFormatter4 = new SimpleDateFormat(formatCheck, Locale.US);

            String today = stringFormatter4.format((Calendar.getInstance().getTime()));
            String data_db = stringFormatter4.format((date1));

            if (today.equals(data_db)){
                holder.aHari.setText("TODAY");
            } else {
                holder.aHari.setText(stringFormatter1.format(date1));
            }

            holder.aJam.setText("• "+ stringFormatter2.format(date1) + " - " + stringFormatter2.format(date2));
            jam = holder.aHari.getText().toString().trim()+" "+holder.aJam.getText().toString().trim();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        email = myList.getEmail();
        ruangan = myList.getRuangan();
        peserta = myList.getPeserta();


        holder.aEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mCtx, UpdateRapat.class);
                i.putExtra("myList", myList);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(i);
            }
        });

        holder.aDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
                alertDialogBuilder.setMessage("Hapus form ini?");
                alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteLaporan(id);
                        Intent i = new Intent(mCtx, AdminRapatin.class);
                        mCtx.startActivity(i);
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
            }
        });

        holder.aAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
                alertDialogBuilder.setMessage("Setujui jadwal rapat ini?");
                alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        accRapat(id);
                        Intent i = new Intent(mCtx, AdminRapatin.class);
                        mCtx.startActivity(i);
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
            }
        });

    }

    private void deleteLaporan(final String ide){
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_DELETE_RAPAT_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void accRapat(final String ide){
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_ACC_RAPAT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                new MyAsyncClass().execute();
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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public int getItemCount() {
        return listRapat.size();
    }

    public class AdminRapatViewHolder extends RecyclerView.ViewHolder {
        public TextView aSubject, aJam, aRuangan, aPeserta, aHari, aNama, aRequest;
        public ImageButton aEdit, aDelete;
        public Button aAccept;

        public AdminRapatViewHolder(View itemView) {
            super(itemView);

            aSubject = (TextView) itemView.findViewById(R.id.aSubject);
            aJam = (TextView) itemView.findViewById(R.id.aJam);
            aRuangan = (TextView) itemView.findViewById(R.id.aRuangan);
            aPeserta = (TextView) itemView.findViewById(R.id.aPeserta);
            aHari = (TextView) itemView.findViewById(R.id.aHari);
            aNama = (TextView) itemView.findViewById(R.id.aNama);
            aRequest = (TextView) itemView.findViewById(R.id.aRequest);
            aEdit = (ImageButton) itemView.findViewById(R.id.aEdit);
            aDelete = (ImageButton) itemView.findViewById(R.id.aDelete);
            aAccept = (Button) itemView.findViewById(R.id.aAccept);
        }
    }

    class MyAsyncClass extends AsyncTask<Void, Void, Void> {
        //variable untuk email
        String no = UUID.randomUUID().toString();
        String code = no.substring(0, 4);
        final String emailSistem = MainActivityAdmin.emailSistem;
        final String emailPenerima = email;
        final String subject = "Peminjaman Ruangan Rapat #"+code;
        final String body = "Form peminjaman ruangan rapat Anda telah disetujui oleh Admin"
                +"\n\nRuangan: "+ruangan
                +"\nPeserta: "+peserta
                +"\nPermintaan Tambahan: "+tambahan
                +"\nJam: "+jam;

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
