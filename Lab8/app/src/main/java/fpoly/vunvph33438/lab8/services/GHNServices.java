package fpoly.vunvph33438.lab8.services;

import java.util.ArrayList;

import fpoly.vunvph33438.lab8.model.District;
import fpoly.vunvph33438.lab8.model.DistrictRequest;
import fpoly.vunvph33438.lab8.model.Province;
import fpoly.vunvph33438.lab8.model.ResponseGHN;
import fpoly.vunvph33438.lab8.model.Ward;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GHNServices {
    public static String GHN_URL = "https://dev-online-gateway.ghn.vn/";

    @GET("shiip/public-api/master-data/province")
    Call<ResponseGHN<ArrayList<Province>>> getListProvince();

    @GET("shiip/public-api/master-data/district")
    Call<ResponseGHN<ArrayList<District>>> getListDistrict(@Query("province_id") int provinceId);

    @GET("shiip/public-api/master-data/ward")
    Call<ResponseGHN<ArrayList<Ward>>> getListWard(@Query("district_id") int district_id);
}
