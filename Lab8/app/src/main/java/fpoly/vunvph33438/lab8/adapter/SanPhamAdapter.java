package fpoly.vunvph33438.lab8.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import fpoly.vunvph33438.lab8.LocationActivity;
import fpoly.vunvph33438.lab8.R;
import fpoly.vunvph33438.lab8.model.SanPhamModel;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder> {
    Context context;
    ArrayList<SanPhamModel> list;

    public SanPhamAdapter(Context context, ArrayList<SanPhamModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sanpham, parent, false);
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        SanPhamModel sanPhamModel = list.get(position);
        if (sanPhamModel != null) {
            holder.tvTenSanPham.setText(sanPhamModel.getTenSP());
            holder.tvTenCongTy.setText(sanPhamModel.getTenCongTy());
            Locale vietnamLocale = new Locale("vi", "VN");
            NumberFormat vietnamFormat = NumberFormat.getCurrencyInstance(vietnamLocale);
            String priceFormatted = vietnamFormat.format(sanPhamModel.getGia());
            holder.tvGia.setText(priceFormatted);
            Glide.with(context)
                    .load(sanPhamModel.getImage())
                    .into(holder.imgSanPham);
        }
        holder.imgBuy.setOnClickListener(v -> {
            Intent intent = new Intent(context, LocationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("_id", sanPhamModel.get_id());
            bundle.putString("tenSP", sanPhamModel.getTenSP());
            bundle.putString("tenCongTy", sanPhamModel.getTenCongTy());
            bundle.putInt("gia", sanPhamModel.getGia());
            bundle.putInt("image", sanPhamModel.getImage());
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SanPhamViewHolder extends RecyclerView.ViewHolder {

        TextView tvTenSanPham, tvTenCongTy, tvGia;
        ImageView imgSanPham, imgBuy;

        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
            tvTenCongTy = itemView.findViewById(R.id.tvTenCongTy);
            tvGia = itemView.findViewById(R.id.tvGia);
            imgBuy = itemView.findViewById(R.id.imgBuy);
        }
    }
}
