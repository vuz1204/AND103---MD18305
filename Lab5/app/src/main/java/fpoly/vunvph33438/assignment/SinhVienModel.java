package fpoly.vunvph33438.assignment;

public class SinhVienModel {

    private String _id;
    private String tenSV;
    private String maSV;
    private float diemTB;
    private String avatar;

    public SinhVienModel() {
    }

    public SinhVienModel(String _id, String tenSV, String maSV, float diemTB, String avatar) {
        this._id = _id;
        this.tenSV = tenSV;
        this.maSV = maSV;
        this.diemTB = diemTB;
        this.avatar = avatar;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTenSV() {
        return tenSV;
    }

    public void setTenSV(String tenSV) {
        this.tenSV = tenSV;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public float getDiemTB() {
        return diemTB;
    }

    public void setDiemTB(float diemTB) {
        this.diemTB = diemTB;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
