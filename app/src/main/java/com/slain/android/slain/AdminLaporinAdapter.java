package com.slain.android.slain;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

public class AdminLaporinAdapter extends RecyclerView.Adapter<AdminLaporinAdapter.AdminLaporanViewHolder> {
    private List<Laporan> listLaporan;
    private Context mCtx;

    public AdminLaporinAdapter(Context mCtx, List<Laporan> listLaporan) {
        this.mCtx = mCtx;
        this.listLaporan = listLaporan;
    }

    @Override
    public AdminLaporanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_laporan_admin, null);
        return new AdminLaporanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdminLaporinAdapter.AdminLaporanViewHolder holder, int position) {
        final Laporan myList = listLaporan.get(position);
        holder.textKategori.setText(myList.getKategori());
        holder.textNama.setText(myList.getNama());
        final String s = String.valueOf(myList.getId());
        int status = myList.getStatus();
        if (status == 0){
            holder.statusBox.setChecked(false);
        } else if (status == 1){
            holder.statusBox.setChecked(true);
        } else {
            holder.statusBox.setVisibility(View.INVISIBLE);
            holder.statusBox2.setVisibility(View.VISIBLE);
        }
//        final String tes = String.valueOf(myList.getId());

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                final PopupMenu popup = new PopupMenu(mCtx, holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.details:
                                //handle lihat detail
                                Intent i = new Intent(mCtx, AdminLaporinDetail.class);
//                                Bundle b = new Bundle();
//                                b.putString("id", s);
                                i.putExtra("myList", myList);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mCtx.startActivity(i);
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
        return listLaporan.size();
    }

    public class AdminLaporanViewHolder extends RecyclerView.ViewHolder {
        public TextView textKategori, textDeskripsi, buttonViewOption, textNama;
        public CheckBox statusBox, statusBox2;

        public AdminLaporanViewHolder(View itemView) {
            super(itemView);

            textKategori = (TextView) itemView.findViewById(R.id.textKategori);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
            statusBox = (CheckBox) itemView.findViewById(R.id.statusBox);
            statusBox2 = (CheckBox) itemView.findViewById(R.id.statusBox2);
            textNama = (TextView) itemView.findViewById(R.id.textNama);
        }
    }
}
