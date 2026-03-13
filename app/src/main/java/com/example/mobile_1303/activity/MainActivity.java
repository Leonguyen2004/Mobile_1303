package com.example.mobile_1303.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_1303.R;
import com.example.mobile_1303.adapter.RoomAdapter;
import com.example.mobile_1303.data.MemoryDataStore;
import com.example.mobile_1303.model.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvRooms;
    private RoomAdapter adapter;
    private List<Room> roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Lấy danh sách phòng từ MemoryDataStore
        roomList = MemoryDataStore.getInstance().getRoomList();

        // Khởi tạo RecyclerView
        rvRooms = findViewById(R.id.rv_rooms);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo Adapter với callback xóa
        adapter = new RoomAdapter(this, roomList, position -> {
            // Callback khi xóa thành công – có thể xử lý thêm logic ở đây
            Toast.makeText(MainActivity.this,
                    "Đã xóa phòng thành công!", Toast.LENGTH_SHORT).show();
        });

        rvRooms.setAdapter(adapter);
    }
}