package fpoly.vunvph33438.assignment.services;

import java.util.List;

import fpoly.vunvph33438.assignment.model.SinhVienModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiServices {
    @GET("/")
    Call<List<SinhVienModel>> getSinhViens();

    @POST("add-sinhVien")
    Call<SinhVienModel> addSinhVien(@Body SinhVienModel sinhVienModel);

    @PUT("update-sinhVien-by-id/{id}")
    Call<SinhVienModel> updateSinhVien(@Path("id") String id, @Body SinhVienModel sinhVienModel);

    @DELETE("delete-sinhVien-by-id/{id}")
    Call<Void> deleteSinhVien(@Path("id") String id);
}
