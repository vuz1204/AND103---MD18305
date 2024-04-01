package fpoly.vunvph33438.lab8;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fpoly.vunvph33438.lab8.adapter.Adapter_Item_District_Select_GHN;
import fpoly.vunvph33438.lab8.adapter.Adapter_Item_Province_Select_GHN;
import fpoly.vunvph33438.lab8.adapter.Adapter_Item_Ward_Select_GHN;
import fpoly.vunvph33438.lab8.model.District;
import fpoly.vunvph33438.lab8.model.DistrictRequest;
import fpoly.vunvph33438.lab8.model.Province;
import fpoly.vunvph33438.lab8.model.ResponseGHN;
import fpoly.vunvph33438.lab8.model.Ward;
import fpoly.vunvph33438.lab8.services.GHNRequest;
import fpoly.vunvph33438.lab8.services.GHNServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends AppCompatActivity {
    private GHNRequest request;
    private GHNServices ghnServices;
    private String _id, tenSP, tenCongTy, description, WardCode;
    private int image, DistrictID, ProvinceID, gia;
    private Adapter_Item_Province_Select_GHN adapter_item_province_select_ghn;
    private Adapter_Item_District_Select_GHN adapter_item_district_select_ghn;
    Callback<ResponseGHN<ArrayList<District>>> responseDistrict = new Callback<ResponseGHN<ArrayList<District>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<District>>> call, Response<ResponseGHN<ArrayList<District>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getCode() == 200) {
                    ArrayList<District> ds = new ArrayList<>(response.body().getData());
                    SetDataSpinDistrict(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<District>>> call, Throwable t) {
            Toast.makeText(LocationActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
        }
    };

    private Adapter_Item_Ward_Select_GHN adapter_item_ward_select_ghn;
    Callback<ResponseGHN<ArrayList<Ward>>> responseWard = new Callback<ResponseGHN<ArrayList<Ward>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<Ward>>> call, Response<ResponseGHN<ArrayList<Ward>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getCode() == 200) {
                    ArrayList<Ward> ds = new ArrayList<>(response.body().getData());
                    if (response.body().getData() == null) return;
                    ds.addAll(response.body().getData());
                    SetDataSpinWard(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<Ward>>> call, Throwable t) {
            Toast.makeText(LocationActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
        }
    };
    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId() == R.id.sp_province) {
                ProvinceID = ((Province) parent.getAdapter().getItem(position)).getProvinceID();
                request.callAPI().getListDistrict(ProvinceID).enqueue(responseDistrict);
            } else if (parent.getId() == R.id.sp_district) {
                DistrictID = ((District) parent.getAdapter().getItem(position)).getDistrictID();
                request.callAPI().getListWard(DistrictID).enqueue(responseWard);
            } else if (parent.getId() == R.id.sp_ward) {
                WardCode = ((Ward) parent.getAdapter().getItem(position)).getWardCode();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private Spinner spProvince, spDistrict, spWard;
    Callback<ResponseGHN<ArrayList<Province>>> responseProvince = new Callback<ResponseGHN<ArrayList<Province>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<Province>>> call, Response<ResponseGHN<ArrayList<Province>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getCode() == 200) {
                    ArrayList<Province> ds = new ArrayList<>(response.body().getData());
                    SetDataSpinProvince(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<Province>>> call, Throwable t) {
            Toast.makeText(LocationActivity.this, "Lấy dữ liệu bị lỗi", Toast.LENGTH_SHORT).show();
        }
    };
    private Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        request = new GHNRequest();

        spProvince = findViewById(R.id.sp_province);
        spDistrict = findViewById(R.id.sp_district);
        spWard = findViewById(R.id.sp_ward);
        btnOrder = findViewById(R.id.btn_order);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            _id = bundle.getString("_id");
            tenSP = bundle.getString("tenSP");
            tenCongTy = bundle.getString("tenCongTy");
            gia = bundle.getInt("gia");
            image = bundle.getInt("image");
        }

        request.callAPI().getListProvince().enqueue(responseProvince);
        spProvince.setOnItemSelectedListener(onItemSelectedListener);
        spDistrict.setOnItemSelectedListener(onItemSelectedListener);
        spWard.setOnItemSelectedListener(onItemSelectedListener);

        spProvince.setSelection(0);
        spDistrict.setSelection(0);
        spWard.setSelection(0);
    }

    private void SetDataSpinProvince(ArrayList<Province> ds) {
        adapter_item_province_select_ghn = new Adapter_Item_Province_Select_GHN(this, ds);
        spProvince.setAdapter(adapter_item_province_select_ghn);
    }

    private void SetDataSpinDistrict(ArrayList<District> ds) {
        adapter_item_district_select_ghn = new Adapter_Item_District_Select_GHN(this, ds);
        spDistrict.setAdapter(adapter_item_district_select_ghn);
    }

    private void SetDataSpinWard(ArrayList<Ward> ds) {
        adapter_item_ward_select_ghn = new Adapter_Item_Ward_Select_GHN(this, ds);
        spWard.setAdapter(adapter_item_ward_select_ghn);
    }
}
