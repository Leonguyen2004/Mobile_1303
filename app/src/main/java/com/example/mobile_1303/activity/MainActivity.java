package com.example.mobile_1303.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_1303.R;
import com.example.mobile_1303.adapter.RoomAdapter;
import com.example.mobile_1303.data.MemoryDataStore;
import com.example.mobile_1303.model.Room;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvRooms;
    private FloatingActionButton fabAdd;
    private RoomAdapter adapter;
    private List<Room> roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        rvRooms = findViewById(R.id.main_rv_rooms);
        fabAdd  = findViewById(R.id.main_fab_add);

        // Lấy dữ liệu từ MemoryDataStore (Singleton)
        roomList = MemoryDataStore.getInstance().getRoomList();

        // Khởi tạo Adapter với callback xóa (Toast thông báo xóa thành công)
        adapter = new RoomAdapter(this, roomList, position -> {
            Toast.makeText(MainActivity.this,
                    "Đã xóa phòng thành công!", Toast.LENGTH_SHORT).show();
        });

        // Thiết lập RecyclerView
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);

        // Nút FAB Thêm phòng → mở AddRoomActivity
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, AddRoomActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật danh sách sau khi quay về từ AddRoomActivity / EditRoomActivity
        adapter.notifyDataSetChanged();
    }
}