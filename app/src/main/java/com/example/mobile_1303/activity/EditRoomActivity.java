package com.example.mobile_1303.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_1303.R;
import com.example.mobile_1303.data.MemoryDataStore;
import com.example.mobile_1303.model.Room;

import java.util.List;

public class EditRoomActivity extends AppCompatActivity {

    private EditText edtRoomId, edtRoomName, edtPrice, edtTenantName, edtTenantPhone;
    private RadioGroup rgStatus;
    private RadioButton rbEmpty, rbOccupied;
    private LinearLayout layoutTenant;
    private Button btnUpdate;

    private int roomIndex = -1;
    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);

        // Ánh xạ view
        edtRoomId = findViewById(R.id.edit_edt_room_id);
        edtRoomName = findViewById(R.id.edit_edt_room_name);
        edtPrice = findViewById(R.id.edit_edt_price);
        rgStatus = findViewById(R.id.edit_rg_status);
        rbEmpty = findViewById(R.id.edit_rb_empty);
        rbOccupied = findViewById(R.id.edit_rb_occupied);
        edtTenantName = findViewById(R.id.edit_edt_tenant_name);
        edtTenantPhone = findViewById(R.id.edit_edt_tenant_phone);
        layoutTenant = findViewById(R.id.edit_layout_tenant);
        btnUpdate = findViewById(R.id.edit_btn_update);

        // Nhận room_index từ Intent
        roomIndex = getIntent().getIntExtra("room_index", -1);

        // Kiểm tra index hợp lệ
        List<Room> roomList = MemoryDataStore.getInstance().getRoomList();
        if (roomIndex < 0 || roomIndex >= roomList.size()) {
            Toast.makeText(this, "Lỗi: Không tìm thấy phòng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        room = roomList.get(roomIndex);

        // Điền sẵn dữ liệu (prefill)
        prefillData();

        // Xử lý ẩn/hiện tenant theo RadioGroup
        rgStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.edit_rb_occupied) {
                    layoutTenant.setVisibility(View.VISIBLE);
                } else {
                    layoutTenant.setVisibility(View.GONE);
                }
            }
        });

        // Xử lý nút Cập nhật
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRoom();
            }
        });
    }

    /**
     * Điền sẵn dữ liệu của phòng vào các EditText
     */
    private void prefillData() {
        edtRoomId.setText(room.getRoomId());
        edtRoomName.setText(room.getRoomName());
        edtPrice.setText(String.valueOf(room.getPrice()));

        if (room.isOccupied()) {
            rbOccupied.setChecked(true);
            layoutTenant.setVisibility(View.VISIBLE);
            edtTenantName.setText(room.getTenantName());
            edtTenantPhone.setText(room.getTenantPhone());
        } else {
            rbEmpty.setChecked(true);
            layoutTenant.setVisibility(View.GONE);
        }
    }

    /**
     * Validate dữ liệu và cập nhật Room
     */
    private void updateRoom() {
        String roomName = edtRoomName.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        boolean isOccupied = rbOccupied.isChecked();
        String tenantName = edtTenantName.getText().toString().trim();
        String tenantPhone = edtTenantPhone.getText().toString().trim();

        // Validate: Tên phòng không được trống
        if (roomName.isEmpty()) {
            edtRoomName.setError("Tên phòng không được để trống");
            edtRoomName.requestFocus();
            return;
        }

        // Validate: Giá phải > 0
        if (priceStr.isEmpty()) {
            edtPrice.setError("Giá thuê không được để trống");
            edtPrice.requestFocus();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            edtPrice.setError("Giá thuê không hợp lệ");
            edtPrice.requestFocus();
            return;
        }

        if (price <= 0) {
            edtPrice.setError("Giá thuê phải lớn hơn 0");
            edtPrice.requestFocus();
            return;
        }

        // Validate: Nếu đã thuê thì tenant không được trống
        if (isOccupied) {
            if (tenantName.isEmpty()) {
                edtTenantName.setError("Tên người thuê không được để trống");
                edtTenantName.requestFocus();
                return;
            }
            if (tenantPhone.isEmpty()) {
                edtTenantPhone.setError("SĐT người thuê không được để trống");
                edtTenantPhone.requestFocus();
                return;
            }
        }

        // Cập nhật object Room hiện có (KHÔNG tạo mới)
        room.setRoomName(roomName);
        room.setPrice(price);
        room.setOccupied(isOccupied);

        if (isOccupied) {
            room.setTenantName(tenantName);
            room.setTenantPhone(tenantPhone);
        } else {
            room.setTenantName("");
            room.setTenantPhone("");
        }

        Toast.makeText(this, "Cập nhật phòng thành công!", Toast.LENGTH_SHORT).show();
        finish(); // Quay về, MainActivity.onResume() sẽ gọi notifyDataSetChanged
    }
}
