package com.example.quanly_khoamon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MonHocAdapter extends RecyclerView.Adapter<MonHocAdapter.ViewHolder> {
    List<MonHoc> list;
    Context context;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MonHoc item);
    }

    public MonHocAdapter(Context context, List<MonHoc> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_monhoc,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MonHoc mh = list.get(position);
        holder.tvTenMon.setText(mh.getTenMon());
        holder.tvMaMon.setText("Mã: " + mh.getMaMH());
        holder.tvTinChi.setText(mh.getSoTinChi() + " Tín chỉ");

        holder.itemView.setOnClickListener(v -> listener.onItemClick(mh));
    }


    @Override
    public int getItemCount(){
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTenMon, tvMaMon, tvTinChi;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvMaMon = itemView.findViewById(R.id.tvMaMon);
            tvTinChi = itemView.findViewById(R.id.tvTinChi);
        }
    }
}
