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
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.slain.android.slain.app.AppController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RapatUserAdapter extends RecyclerView.Adapter<RapatUserAdapter.RapatUserViewHolder> {
    private List<Rapat> listRapat;
    private Context mCtx;
    private static final String TAG = RapatUserAdapter.class.getSimpleName();
    String tag_json_obj = "json_obj_req";

    public RapatUserAdapter(Context mCtx, List<Rapat> listRapat) {
        this.mCtx = mCtx;
        this.listRapat = listRapat;
    }

    @Override
    public RapatUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_rapat_ku, null);
        return new RapatUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RapatUserAdapter.RapatUserViewHolder holder, int position) {
        final Rapat myList = listRapat.get(position);
        final String id = String.valueOf(myList.getId());
        holder.ttSubject.setText(myList.getSubject());
        holder.ttRuangan.setText("• "+myList.getRuangan());
        holder.ttPeserta.setText("• "+myList.getPeserta());

        if (myList.getRequest().equals("")){
            holder.ttRequest.setText("Keterangan Tambahan : \nTidak Ada");
        } else {
            holder.ttRequest.setText("Keterangan Tambahan : \n"+myList.getRequest());
        }

        int status = myList.getStatus();
        if (status == 0){
            holder.statusBoxRapat_user.setChecked(false);
            holder.ttPending.setVisibility(View.VISIBLE);
        }else if (status == 1){
            holder.statusBoxRapat_user.setChecked(true);
            holder.ttPending.setVisibility(View.GONE);
        }

        try {
            SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = dateFormatter1.parse(myList.getStart());
            Date date2 = dateFormatter1.parse(myList.getEnd());
            String formatTanggal = "EEEE, dd MMMM yyyy";
            SimpleDateFormat stringFormatter1 = new SimpleDateFormat(formatTanggal, Locale.US);
            String formatJam = "HH:mm";
            SimpleDateFormat stringFormatter2 = new SimpleDateFormat(formatJam, Locale.US);
            holder.ttJam.setText("• "+stringFormatter1.format(date1) + " \n   "
                    + stringFormatter2.format(date1) + " - " + stringFormatter2.format(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.OptionRapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                final PopupMenu popup = new PopupMenu(mCtx, holder.OptionRapat);
                //inflating menu from xml resource
                popup.inflate(R.menu.delete_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit_ic:
                                Intent i = new Intent(mCtx, UpdateRapatUser.class);
                                i.putExtra("myList", myList);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mCtx.startActivity(i);
                                break;

                            case R.id.delete_ic:
                                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(mCtx);
                                alertDialogBuilder2.setMessage("Hapus form ini?");
                                alertDialogBuilder2.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        deleteLaporan(id);
                                        Intent i = new Intent(mCtx, RapatUser.class);
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

    @Override
    public int getItemCount() {
        return listRapat.size();
    }

    public class RapatUserViewHolder extends RecyclerView.ViewHolder {
        public TextView ttJam, ttRuangan, ttSubject, ttPending, ttPeserta, ttRequest, OptionRapat;
        public CheckBox statusBoxRapat_user;


        public RapatUserViewHolder(View itemView) {
            super(itemView);

            ttJam = (TextView) itemView.findViewById(R.id.ttJam);
            ttRuangan = (TextView) itemView.findViewById(R.id.ttRuangan);
            ttSubject = (TextView) itemView.findViewById(R.id.ttSubject);
            ttPending = (TextView) itemView.findViewById(R.id.ttPending);
            ttPeserta = (TextView) itemView.findViewById(R.id.ttPeserta);
            ttRequest = (TextView) itemView.findViewById(R.id.ttRequest);
            OptionRapat = (TextView) itemView.findViewById(R.id.OptionRapat);
            statusBoxRapat_user = (CheckBox) itemView.findViewById(R.id.statusBoxRapat_user);
        }
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
}
