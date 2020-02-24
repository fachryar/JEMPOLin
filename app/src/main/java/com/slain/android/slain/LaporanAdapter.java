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

public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.LaporanViewHolder> {
    private List<Laporan> listLaporan;
    private Context mCtx;

    public LaporanAdapter(Context mCtx, List<Laporan> listLaporan) {
        this.mCtx = mCtx;
        this.listLaporan = listLaporan;
    }

    @Override
    public LaporanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_laporan, null);
        return new LaporanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LaporanAdapter.LaporanViewHolder holder, int position) {
        final Laporan myList = listLaporan.get(position);
        holder.textKategori.setText(myList.getKategori());
        holder.textDeskripsi.setText(myList.getDeskripsi());
        final String s = String.valueOf(myList.getId());
        int status = myList.getStatus();
        if (status == 0){
            holder.statusBox.setChecked(false);
        }else if (status == 1){
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
//                                loadDetailLaporan(mCtx, tes);
                                Intent i = new Intent(mCtx, DetailLaporan.class);
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

    public class LaporanViewHolder extends RecyclerView.ViewHolder {
        public TextView textKategori;
        public TextView textDeskripsi;
        public TextView buttonViewOption;
        public CheckBox statusBox, statusBox2;

        public LaporanViewHolder(View itemView) {
            super(itemView);

            textKategori = (TextView) itemView.findViewById(R.id.textKategori);
            textDeskripsi = (TextView) itemView.findViewById(R.id.textDeskripsi);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
            statusBox = (CheckBox) itemView.findViewById(R.id.statusBox);
            statusBox2 = (CheckBox) itemView.findViewById(R.id.statusBox2);
        }
    }
}
