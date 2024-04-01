package fpoly.vunvph33438.lab8.model;

public class SanPhamModel {
    private String _id;
    private String tenSP;
    private String tenCongTy;
    private int gia;
    private int image;

    public SanPhamModel() {
    }

    public SanPhamModel(String _id, String tenSP, String tenCongTy, int gia, int image) {
        this._id = _id;
        this.tenSP = tenSP;
        this.tenCongTy = tenCongTy;
        this.gia = gia;
        this.image = image;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getTenCongTy() {
        return tenCongTy;
    }

    public void setTenCongTy(String tenCongTy) {
        this.tenCongTy = tenCongTy;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
