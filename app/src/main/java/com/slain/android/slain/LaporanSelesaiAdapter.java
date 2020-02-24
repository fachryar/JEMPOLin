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

public class LaporanSelesaiAdapter extends RecyclerView.Adapter<LaporanSelesaiAdapter.LaporanSelesaiViewHolder> {
    private List<Laporan> listLaporan;
    private Context mCtx;

    public LaporanSelesaiAdapter(Context mCtx, List<Laporan> listLaporan) {
        this.mCtx = mCtx;
        this.listLaporan = listLaporan;
    }

    @Override
    public LaporanSelesaiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_laporan_selesai, null);
        return new LaporanSelesaiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LaporanSelesaiAdapter.LaporanSelesaiViewHolder holder, int position) {
        final Laporan myList = listLaporan.get(position);
        int no = position;
        holder.selKategori.setText((no+1)+". Kategori "+myList.getKategori());
        final String s = String.valueOf(myList.getId());

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
                                Intent i = new Intent(mCtx, DetailLaporan.class);
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

    public class LaporanSelesaiViewHolder extends RecyclerView.ViewHolder {
        public TextView selKategori, buttonViewOption;

        public LaporanSelesaiViewHolder(View itemView) {
            super(itemView);

            selKategori = (TextView) itemView.findViewById(R.id.selKategori);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions2);
        }
    }
}
