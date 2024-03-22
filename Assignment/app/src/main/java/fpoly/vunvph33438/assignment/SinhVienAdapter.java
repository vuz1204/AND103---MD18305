package fpoly.vunvph33438.assignment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SinhVienAdapter extends BaseAdapter {
    public ItemClickListener itemClickListener;
    Activity context;
    List<SinhVienModel> mList = new ArrayList<>();

    public SinhVienAdapter(Activity context, List<SinhVienModel> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.sv_list_item, parent, false);

        TextView tvID = (TextView) rowView.findViewById(R.id.tvId);
        ImageView imgAvatar = (ImageView) rowView.findViewById(R.id.imgAvatar);
        TextView tvTenSV = (TextView) rowView.findViewById(R.id.tvTenSV);
        TextView tvMaSV = (TextView) rowView.findViewById(R.id.tvMaSV);
        TextView tvDiemTB = (TextView) rowView.findViewById(R.id.tvDiemTB);
        ImageView imgDelete = (ImageView) rowView.findViewById(R.id.imgDelete);

        SinhVienModel sinhVienModel = mList.get(position);

        tvID.setText(String.valueOf(sinhVienModel.get_id()));
        tvTenSV.setText(sinhVienModel.getTenSV());
        tvMaSV.setText(sinhVienModel.getMaSV());
        tvDiemTB.setText(String.valueOf(sinhVienModel.getDiemTB()));

        String avatarUrl = sinhVienModel.getAvatar();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Picasso.get().load(avatarUrl).into(imgAvatar);
        } else {
            imgAvatar.setImageResource(R.drawable.ic_launcher_background);
        }

        imgDelete.setOnClickListener(v -> {
            showDeleteAlert(position);
//            try {
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://10.24.20.241:3000/")
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//
//                ApiService apiService = retrofit.create(ApiService.class);
//
//                Call<Void> call = apiService.deleteSinhVien(sinhVienModel.get_id());
//                call.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        if (response.isSuccessful()) {
//                            mList.remove(position);
//                            notifyDataSetChanged();
//                        } else {
//                            if (response.code() == 404) {
//                                Toast.makeText(context, "Không tìm thấy sinh viên", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } catch (Exception e) {
//                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
//            }
        });

        rowView.setOnLongClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.UpdateItem(position);
            }
            return false;
        });

        return rowView;
    }

    public void showDeleteAlert(int position) {
        SinhVienModel sinhVienModel = mList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.warning_icon);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có chắc chắn muốn xóa thành viên " + sinhVienModel.getTenSV() + " không?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://10.24.20.241:3000/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    ApiService apiService = retrofit.create(ApiService.class);

                    Call<Void> call = apiService.deleteSinhVien(sinhVienModel.get_id());
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                mList.remove(position);
                                notifyDataSetChanged();
                            } else {
                                if (response.code() == 404) {
                                    Toast.makeText(context, "Không tìm thấy sinh viên", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
