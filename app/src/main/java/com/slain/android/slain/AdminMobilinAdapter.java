package com.slain.android.slain;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

public class AdminMobilinAdapter extends RecyclerView.Adapter<AdminMobilinAdapter.AdminMobilinViewHolder>{
    private List<Mobil> listMobil;
    private Context mCtx;
    private static final String TAG = AdminMobilinAdapter.class.getSimpleName();
    String tag_json_obj = "json_obj_req";

    public AdminMobilinAdapter(Context mCtx, List<Mobil> listMobil) {
        this.mCtx = mCtx;
        this.listMobil = listMobil;
    }

    @Override
    public AdminMobilinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_mobil_admin, null);
        return new AdminMobilinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdminMobilinAdapter.AdminMobilinViewHolder holder, int position) {
        final Mobil myList = listMobil.get(position);
        holder.mmMobil.setText(myList.getNama_mobil());
        holder.mmNamaFungsi.setText(myList.getNama()+" - "+ myList.getFungsi());
        holder.mmKeterangan.setText(myList.getKeterangan());
        final String id = String.valueOf(myList.getId());

        try {
            SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = dateFormatter1.parse(myList.getStart());
            Date date2 = dateFormatter1.parse(myList.getEnd());
            Date current_date = Calendar.getInstance().getTime();

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

            holder.mmStart.setText(stringFormatter1.format(date1) + " - "+ stringFormatter2.format(date1));
            holder.mmEnd.setText("s/d "+ stringFormatter1.format(date2) + " - "+ stringFormatter2.format(date2));

            if (current_date.compareTo(date2) > 0) {
                holder.textViewOver.setVisibility(View.VISIBLE);
            } else {
                holder.textViewOver.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.mmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mCtx, UpdateMobil.class);
                i.putExtra("myList", myList);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(i);
            }
        });

        holder.mmSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
                alertDialogBuilder.setMessage("Selesaikan peminjaman ini?");
                alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteMobil(id);
                        Intent i = new Intent(mCtx, AdminMobilin.class);
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

    private void deleteMobil(final String ide){
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_DELETE_MOBIL, new Response.Listener<String>() {
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ide);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public int getItemCount() {
        return listMobil.size();
    }

    public class AdminMobilinViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewOver, mmKeterangan, mmNamaFungsi, mmStart, mmEnd, mmMobil;
        public ImageButton mmEdit;
        public Button mmSelesai;

        public AdminMobilinViewHolder(View itemView) {
            super(itemView);

            mmKeterangan = (TextView) itemView.findViewById(R.id.mmKeterangan);
            mmNamaFungsi = (TextView) itemView.findViewById(R.id.mmNamaFungsi);
            mmStart = (TextView) itemView.findViewById(R.id.mmStart);
            mmEnd = (TextView) itemView.findViewById(R.id.mmEnd);
            mmMobil = (TextView) itemView.findViewById(R.id.mmMobil);
            textViewOver = (TextView) itemView.findViewById(R.id.textViewOver);
            mmEdit = (ImageButton) itemView.findViewById(R.id.mmEdit);
            mmSelesai = (Button) itemView.findViewById(R.id.mmSelesai);
        }
    }
}
