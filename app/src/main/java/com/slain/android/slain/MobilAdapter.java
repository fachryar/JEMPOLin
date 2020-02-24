package com.slain.android.slain;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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

public class MobilAdapter extends RecyclerView.Adapter<MobilAdapter.MobilViewHolder> {
    private List<Mobil> listMobil;
    private Context mCtx;
    private static final String TAG = MobilAdapter.class.getSimpleName();
    String tag_json_obj = "json_obj_req";

    public MobilAdapter(Context mCtx, List<Mobil> listMobil) {
        this.mCtx = mCtx;
        this.listMobil = listMobil;
    }

    @Override
    public MobilViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_mobil_user, null);
        return new MobilViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MobilAdapter.MobilViewHolder holder, int position) {
        final Mobil myList = listMobil.get(position);
        final String id = String.valueOf(myList.getId());

        int status = myList.getStatus();
        if (status == 0){
            holder.imageMobil.setChecked(false);
            holder.mobilMobil.setText("PENDING");
        }else if (status == 1){
            holder.imageMobil.setChecked(true);
            holder.mobilMobil.setText(myList.getNama_mobil());
        }

        holder.keteranganMobil.setText(myList.getKeterangan());
        holder.startMobil.setText(myList.getStart());
        holder.endMobil.setText(myList.getEnd());

        try {
            SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = dateFormatter1.parse(myList.getStart());
            Date date2 = dateFormatter1.parse(myList.getEnd());
            Date current_date = Calendar.getInstance().getTime();

            String formatTanggal = "EEEE, dd MMMM";
            SimpleDateFormat stringFormatter1 = new SimpleDateFormat(formatTanggal, Locale.US);
            String formatJam = "HH:mm";
            SimpleDateFormat stringFormatter2 = new SimpleDateFormat(formatJam, Locale.US);

            holder.startMobil.setText(stringFormatter1.format(date1) + " " +stringFormatter2.format(date1)+" s/d");
            holder.endMobil.setText(stringFormatter1.format(date2) + " " +stringFormatter2.format(date2));

            if (myList.getStatus() == 1) {
                if (current_date.compareTo(date1) >= 0 && current_date.compareTo(date2) <= 0) {
                    holder.selesaikan.setVisibility(View.VISIBLE);
                    holder.textViewBatas.setVisibility(View.GONE);
                } else if (current_date.compareTo(date2) > 0) {
                    holder.selesaikan.setVisibility(View.VISIBLE);
                    holder.textViewBatas.setVisibility(View.VISIBLE);
                } else {
                    holder.selesaikan.setVisibility(View.GONE);
                    holder.textViewBatas.setVisibility(View.GONE);
                }
            } else {
                holder.selesaikan.setVisibility(View.GONE);
                holder.textViewBatas.setVisibility(View.GONE);
                if (current_date.compareTo(date2) > 0) {
                    holder.textViewBatas.setVisibility(View.VISIBLE);
                    holder.textViewBatas.setText("SUDAH MELEBIHI BATAS WAKTU, AKAN DIHAPUS OLEH SISTEM");
                    deleteLaporan(id);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.selesaikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mCtx);
                alertDialogBuilder2.setMessage("Selesaikan peminjaman mobil ini?");
                alertDialogBuilder2.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteLaporan(id);
                        Intent i = new Intent(mCtx, Mobilin.class);
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

        holder.optionRapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                final PopupMenu popup = new PopupMenu(mCtx, holder.optionRapat);
                //inflating menu from xml resource
                popup.inflate(R.menu.delete_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit_ic:
                                try {
                                    SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date date1 = dateFormatter1.parse(myList.getStart());
                                    Date date2 = dateFormatter1.parse(myList.getEnd());
                                    Date current_date = Calendar.getInstance().getTime();

                                    if (current_date.compareTo(date2) > 0) {
                                        Toast.makeText(mCtx, "Tidak dapat diubah karena sudah melebihi batas waktu peminjaman", Toast.LENGTH_LONG).show();
                                    } else {
                                        Intent i = new Intent(mCtx, UpdateMobilUser.class);
                                        i.putExtra("myList", myList);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        mCtx.startActivity(i);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                break;

                            case R.id.delete_ic:
                                if(myList.getStatus() == 0){
                                    AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mCtx);
                                    alertDialogBuilder2.setMessage("Hapus form ini?");
                                    alertDialogBuilder2.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            deleteLaporan(id);
                                            Intent i = new Intent(mCtx, Mobilin.class);
                                            mCtx.startActivity(i);
                                            //COBA DELETE INTENTNYA NANTI!!!
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
                                } else {
                                    Toast.makeText(mCtx, "Mobil yang sudah disetujui hanya bisa dihapus oleh admin", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
    }

    private void deleteLaporan(final String ide){
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
        return listMobil.size();
    }

    public class MobilViewHolder extends RecyclerView.ViewHolder {
        public TextView mobilMobil, keteranganMobil, startMobil, endMobil, optionRapat, textViewBatas;
        public CheckBox imageMobil;
        public Button selesaikan;

        public MobilViewHolder(View itemView) {
            super(itemView);

            mobilMobil = (TextView) itemView.findViewById(R.id.mobilMobil);
            keteranganMobil = (TextView) itemView.findViewById(R.id.keteranganMobil);
            startMobil = (TextView) itemView.findViewById(R.id.startMobil);
            endMobil = (TextView) itemView.findViewById(R.id.endMobil);
            optionRapat = (TextView) itemView.findViewById(R.id.optionRapat);
            textViewBatas = (TextView) itemView.findViewById(R.id.textViewBatas);
            imageMobil = (CheckBox) itemView.findViewById(R.id.imageMobil);
            selesaikan = (Button) itemView.findViewById(R.id.selesaikan);
        }
    }
}