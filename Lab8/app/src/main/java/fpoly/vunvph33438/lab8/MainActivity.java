package fpoly.vunvph33438.lab8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import fpoly.vunvph33438.lab8.adapter.SanPhamAdapter;
import fpoly.vunvph33438.lab8.model.SanPhamModel;

public class MainActivity extends AppCompatActivity {
    private ArrayList<SanPhamModel> list = new ArrayList<>();
    private SanPhamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rcvSanPham = findViewById(R.id.rcvSanPham);

        list.add(new SanPhamModel("1", "Apple", "Công ty A", 8000, R.drawable.apple));
        list.add(new SanPhamModel("2","Banana", "Công ty A", 12000, R.drawable.bana));
        list.add(new SanPhamModel("3", "Orange", "Công ty A", 6000,  R.drawable.oranges));
        list.add(new SanPhamModel("4", "Grape", "Công ty A", 15000,  R.drawable.grapes));
        adapter = new SanPhamAdapter(this, list);
        rcvSanPham.setLayoutManager(new GridLayoutManager(this, 2));
        rcvSanPham.setAdapter(adapter);
    }
}