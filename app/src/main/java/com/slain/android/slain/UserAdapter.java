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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.slain.android.slain.app.AppController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> listUser;
    private Context mCtx;
    private static final String TAG = UserAdapter.class.getSimpleName();
    String tag_json_obj = "json_obj_req";
    public String email, nama, fungsi;

    public UserAdapter(Context mCtx, List<User> listUser) {
        this.mCtx = mCtx;
        this.listUser = listUser;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_user, null);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserAdapter.UserViewHolder holder, int position) {
        final User myList = listUser.get(position);
        nama = myList.getNama();
        fungsi = myList.getFungsi();
        email = myList.getEmail();
        int no = position+1;
        holder.uNama.setText(no+". "+myList.getNama());
        holder.uFungsi.setText("Fungsi "+myList.getFungsi());
        holder.uEmail.setText(myList.getEmail());
        final String id = String.valueOf(myList.getId());

        int status = myList.getState();
        if (status == 0){
            holder.uVerify.setVisibility(View.VISIBLE);
            holder.uDecline.setVisibility(View.VISIBLE);
            holder.uDelete.setVisibility(View.GONE);
            holder.uVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
                    alertDialogBuilder.setMessage("Verifikasi user ini?");
                    alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            accUser(id);
                            Intent i = new Intent(mCtx, AdminAkun.class);
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

            holder.uDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
                    alertDialogBuilder.setMessage("Tolak user ini?");
                    alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            deleteUser(id);
                            Intent i = new Intent(mCtx, AdminAkun.class);
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
        } else {
            holder.uVerify.setVisibility(View.GONE);
            holder.uDecline.setVisibility(View.GONE);
            holder.uDelete.setVisibility(View.VISIBLE);

            holder.uDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
                    alertDialogBuilder.setMessage("Hapus user ini?");
                    alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            deleteUser(id);
                            Intent i = new Intent(mCtx, AdminAkun.class);
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
    }

    private void deleteUser(final String ide){
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_DELETE_USER, new Response.Listener<String>() {

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

    private void accUser(final String ide){
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_ACC_USER, new Response.Listener<String>() {

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
        return listUser.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView uNama, uFungsi, uEmail;
        public Button uVerify, uDecline;
        public ImageButton uDelete;

        public UserViewHolder(View itemView) {
            super(itemView);

            uNama = (TextView) itemView.findViewById(R.id.uNama);
            uFungsi = (TextView) itemView.findViewById(R.id.uFungsi);
            uEmail = (TextView) itemView.findViewById(R.id.uEmail);
            uVerify = (Button) itemView.findViewById(R.id.uVerify);
            uDecline = (Button) itemView.findViewById(R.id.uDecline);
            uDelete = (ImageButton) itemView.findViewById(R.id.uDelete);

        }
    }

    class MyAsyncClass extends AsyncTask<Void, Void, Void> {
        //variable untuk email
        String no = UUID.randomUUID().toString();
        String code = no.substring(0, 4);
        final String emailSistem = MainActivityAdmin.emailSistem;
        final String emailPenerima = email;
        final String subject = "Verifikasi Akun JEMPOL.in #"+code;
        final String body = "Nama: "+nama
                +"\nFungsi: "+fungsi
                +"\nEmail: "+email
                +"\n\nAkun Anda dengan data seperti diatas berhasil diverifikasi oleh admin, silahkan Log In ke dalam applikasi JEMPOL.in";

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