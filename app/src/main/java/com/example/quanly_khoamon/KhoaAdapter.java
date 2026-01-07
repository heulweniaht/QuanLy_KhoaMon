package com.example.quanly_khoamon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class KhoaAdapter extends RecyclerView.Adapter<KhoaAdapter.ViewHolder> {
    List<Khoa> list;
    Context context;
    OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Khoa item);
    }

    public KhoaAdapter(Context context, List<Khoa> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.item_khoa,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Khoa khoa = list.get(position);
        holder.tvTenKhoa.setText(khoa.getTenKhoa());
        holder.tvMaKhoa.setText("Mã : " + khoa.getMaKhoa());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(khoa));
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTenKhoa, tvMaKhoa;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenKhoa = itemView.findViewById(R.id.tvTenKhoa);
            tvMaKhoa = itemView.findViewById(R.id.tvMaKhoa);
        }
    }
}
