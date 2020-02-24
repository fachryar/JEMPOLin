package com.slain.android.slain;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RapatAdapter extends RecyclerView.Adapter<RapatAdapter.RapatViewHolder> {
    private List<Rapat> listRapat;
    private Context mCtx;

    public RapatAdapter(Context mCtx, List<Rapat> listRapat) {
        this.mCtx = mCtx;
        this.listRapat = listRapat;
    }

    @Override
    public RapatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_rapat, null);
        return new RapatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RapatAdapter.RapatViewHolder holder, int position) {
        final Rapat myList = listRapat.get(position);
        holder.tSubject.setText(myList.getSubject());
        holder.tPeserta.setText(myList.getPeserta());
        holder.tRuangan.setText(myList.getRuangan());
        holder.tNama.setText(myList.getNama());

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
                holder.tHari.setText("TODAY");
            } else {
                holder.tHari.setText(stringFormatter1.format(date1));
            }

            holder.tJam.setText(stringFormatter2.format(date1) + " - " + stringFormatter2.format(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listRapat.size();
    }

    public class RapatViewHolder extends RecyclerView.ViewHolder {
        public TextView tSubject, tJam, tRuangan, tPeserta, tHari, tNama;

        public RapatViewHolder(View itemView) {
            super(itemView);

            tSubject = (TextView) itemView.findViewById(R.id.tSubject);
            tJam = (TextView) itemView.findViewById(R.id.tJam);
            tRuangan = (TextView) itemView.findViewById(R.id.tRuangan);
            tPeserta = (TextView) itemView.findViewById(R.id.tPeserta);
            tHari = (TextView) itemView.findViewById(R.id.tHari);
            tNama = (TextView) itemView.findViewById(R.id.tNama);
        }
    }
}
