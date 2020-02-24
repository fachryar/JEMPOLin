package com.slain.android.slain;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
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

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminMobilinPendingAdapter extends RecyclerView.Adapter<AdminMobilinPendingAdapter.AdminMobilinPendingViewHolder>{
    private List<Mobil> listMobil;
    private Context mCtx;
    private static final String TAG = AdminMobilinPendingAdapter.class.getSimpleName();
    String tag_json_obj = "json_obj_req";

    public AdminMobilinPendingAdapter(Context mCtx, List<Mobil> listMobil) {
        this.mCtx = mCtx;
        this.listMobil = listMobil;
    }

    @Override
    public AdminMobilinPendingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_mobil_pending, null);
        return new AdminMobilinPendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdminMobilinPendingAdapter.AdminMobilinPendingViewHolder holder, int position) {
        final Mobil myList = listMobil.get(position);
        holder.amNama.setText(myList.getNama());
        holder.amFungsi.setText("Fungsi "+myList.getFungsi());
        holder.amKeterangan.setText(myList.getKeterangan());
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

            holder.amWaktu.setText("Jam peminjaman:\n   "+ stringFormatter1.format(date1) + " " + stringFormatter2.format(date1)
                    +"\n                    s/d\n   "+ stringFormatter1.format(date2) +" "+ stringFormatter2.format(date2));

            if (current_date.compareTo(date2) > 0) {
                holder.textViewExpired.setVisibility(View.VISIBLE);
                holder.amProses.setVisibility(View.GONE);
                holder.amDelete.setVisibility(View.GONE);
                deleteMobil(id);
            } else {
                holder.textViewExpired.setVisibility(View.GONE);
                holder.amProses.setVisibility(View.VISIBLE);
                holder.amDelete.setVisibility(View.VISIBLE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.amProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mCtx, AdminProsesMobil.class);
                i.putExtra("myList", myList);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(i);
            }
        });

        holder.amDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
                alertDialogBuilder.setMessage("Hapus form ini?");
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

    public class AdminMobilinPendingViewHolder extends RecyclerView.ViewHolder {
        public TextView amKeterangan, amNama, amFungsi, amWaktu, textViewExpired;
        public ImageButton amProses, amDelete;

        public AdminMobilinPendingViewHolder(View itemView) {
            super(itemView);

            amKeterangan = (TextView) itemView.findViewById(R.id.amKeterangan);
            amNama = (TextView) itemView.findViewById(R.id.amNama);
            amFungsi = (TextView) itemView.findViewById(R.id.amFungsi);
            amWaktu = (TextView) itemView.findViewById(R.id.amWaktu);
            amProses = (ImageButton) itemView.findViewById(R.id.amProses);
            amDelete = (ImageButton) itemView.findViewById(R.id.amDelete);
            textViewExpired = (TextView) itemView.findViewById(R.id.textViewExpired);
        }
    }
}

