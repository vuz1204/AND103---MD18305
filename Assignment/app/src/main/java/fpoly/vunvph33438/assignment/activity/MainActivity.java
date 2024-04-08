package fpoly.vunvph33438.assignment.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import fpoly.vunvph33438.assignment.R;
import fpoly.vunvph33438.assignment.adapter.SinhVienAdapter;
import fpoly.vunvph33438.assignment.model.SinhVienModel;
import fpoly.vunvph33438.assignment.services.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ListView listView;

    List<SinhVienModel> list;

    SinhVienAdapter sinhvienAdapter;
    EditText edTenSV, edMaSV, edDiemTB, edUrlAvt;
    String strTenSV, strMaSV, strDiemTB, strUrlAvt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.lvCustomListView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.101:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<SinhVienModel>> call = apiService.getSinhViens();

        call.enqueue(new Callback<List<SinhVienModel>>() {
            @Override
            public void onResponse(Call<List<SinhVienModel>> call, Response<List<SinhVienModel>> response) {
                if (response.isSuccessful()) {
                    list = response.body();

                    sinhvienAdapter = new SinhVienAdapter(MainActivity.this, list);
                    sinhvienAdapter.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void UpdateItem(int position) {
                            SinhVienModel sinhVienModel = list.get(position);
                            showAddOrUpdateDialog(MainActivity.this, 1, sinhVienModel, apiService);
                        }
                    });

                    listView.setAdapter(sinhvienAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<SinhVienModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.fabAdd).setOnClickListener(v -> {
            showAddOrUpdateDialog(MainActivity.this, 0, null, apiService);
        });
    }

    public void showAddOrUpdateDialog(Context context, int type, SinhVienModel sinhVienModel, ApiService apiService) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_or_edit_student, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        edTenSV = view.findViewById(R.id.edTenSV);
        edMaSV = view.findViewById(R.id.edMaSV);
        edDiemTB = view.findViewById(R.id.edDiemTB);
        edUrlAvt = view.findViewById(R.id.edUrlAvt);
        if (type != 0) {
            if (sinhVienModel != null) {
                edTenSV.setText(sinhVienModel.getTenSV());
                edMaSV.setText(sinhVienModel.getMaSV());
                edDiemTB.setText(String.valueOf(sinhVienModel.getDiemTB()));
                edUrlAvt.setText(sinhVienModel.getAvatar());
            }
        }
        view.findViewById(R.id.btnSave).setOnClickListener(v -> {
            strTenSV = edTenSV.getText().toString().trim();
            strMaSV = edMaSV.getText().toString().trim();
            strDiemTB = edDiemTB.getText().toString().trim();
            strUrlAvt = edUrlAvt.getText().toString().trim();
            if (validate(strTenSV, strMaSV, strDiemTB)) {
                if (type == 0) {
                    SinhVienModel sinhVienNew = new SinhVienModel();
                    sinhVienNew.setTenSV(strTenSV);
                    sinhVienNew.setMaSV(strMaSV);
                    sinhVienNew.setDiemTB(Float.parseFloat(strDiemTB));
                    sinhVienNew.setAvatar(strUrlAvt);
                    try {
                        Call<SinhVienModel> call = apiService.addSinhVien(sinhVienNew);
                        call.enqueue(new Callback<SinhVienModel>() {
                            @Override
                            public void onResponse(Call<SinhVienModel> call, Response<SinhVienModel> response) {
                                if (response.isSuccessful()) {
                                    loadSinhViensFromAPI(apiService);
                                    alertDialog.dismiss();
                                } else {
                                    Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();                                }
                            }

                            @Override
                            public void onFailure(Call<SinhVienModel> call, Throwable t) {
                                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    sinhVienModel.setTenSV(strTenSV);
                    sinhVienModel.setMaSV(strMaSV);
                    sinhVienModel.setDiemTB(Float.parseFloat(strDiemTB));
                    sinhVienModel.setAvatar(strUrlAvt);
                    try {
                        Call<SinhVienModel> call = apiService.updateSinhVien(sinhVienModel.get_id(), sinhVienModel);
                        call.enqueue(new Callback<SinhVienModel>() {
                            @Override
                            public void onResponse(Call<SinhVienModel> call, Response<SinhVienModel> response) {
                                if (response.isSuccessful()) {
                                    loadSinhViensFromAPI(apiService);
                                    alertDialog.dismiss();
                                } else {
                                    Toast.makeText(context, "Failed to update student", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<SinhVienModel> call, Throwable t) {
                                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(context, "Failed to update student", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        view.findViewById(R.id.btnCancel).setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private boolean validate(String tenSV, String maSV, String diemTB) {
        if (tenSV.isEmpty() || maSV.isEmpty() || diemTB.isEmpty()) {
            Toast.makeText(MainActivity.this, "Vui lòng không bỏ trống", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loadSinhViensFromAPI(ApiService apiService) {
        Call<List<SinhVienModel>> call = apiService.getSinhViens();
        call.enqueue(new Callback<List<SinhVienModel>>() {
            @Override
            public void onResponse(Call<List<SinhVienModel>> call, Response<List<SinhVienModel>> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    list.addAll(response.body());
                    sinhvienAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<SinhVienModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
