package com.example.mobile_1303.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_1303.R;
import com.example.mobile_1303.data.MemoryDataStore;
import com.example.mobile_1303.model.Room;

public class AddRoomActivity extends AppCompatActivity {

    // ── Khai báo view ──────────────────────────────────────────────────────────
    private EditText edtRoomId, edtRoomName, edtPrice, edtTenantName, edtTenantPhone;
    private RadioGroup rgStatus;
    private Button btnSave;

    // Label (để ẩn/hiện cùng EditText)
    private TextView lblTenantName, lblTenantPhone;

    // Trạng thái hiện tại (mặc định: Còn trống)
    private boolean isOccupied = false;

    // ── Lifecycle ──────────────────────────────────────────────────────────────
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        initViews();
        setupStatusListener();
        setupSaveButton();
    }

    // ── Khởi tạo view ─────────────────────────────────────────────────────────
    private void initViews() {
        edtRoomId      = findViewById(R.id.add_edt_room_id);
        edtRoomName    = findViewById(R.id.add_edt_room_name);
        edtPrice       = findViewById(R.id.add_edt_price);
        edtTenantName  = findViewById(R.id.add_edt_tenant_name);
        edtTenantPhone = findViewById(R.id.add_edt_tenant_phone);
        rgStatus       = findViewById(R.id.add_rg_status);
        btnSave        = findViewById(R.id.add_btn_save);
        lblTenantName  = findViewById(R.id.add_lbl_tenant_name);
        lblTenantPhone = findViewById(R.id.add_lbl_tenant_phone);

        // Mặc định "Còn trống" → ẩn các trường người thuê
        setTenantFieldsVisible(false);
    }

    // ── Lắng nghe RadioGroup tình trạng phòng ─────────────────────────────────
    private void setupStatusListener() {
        rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.add_rb_occupied) {
                isOccupied = true;
                setTenantFieldsVisible(true);
            } else {
                // add_rb_empty
                isOccupied = false;
                setTenantFieldsVisible(false);
            }
        });
    }

    /**
     * Ẩn / Hiện + enable / disable các trường thông tin người thuê.
     */
    private void setTenantFieldsVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        lblTenantName.setVisibility(visibility);
        lblTenantPhone.setVisibility(visibility);
        edtTenantName.setVisibility(visibility);
        edtTenantPhone.setVisibility(visibility);
        edtTenantName.setEnabled(visible);
        edtTenantPhone.setEnabled(visible);

        // Xóa nội dung khi ẩn để tránh dữ liệu rác
        if (!visible) {
            edtTenantName.setText("");
            edtTenantPhone.setText("");
        }
    }

    // ── Xử lý nút Lưu ─────────────────────────────────────────────────────────
    private void setupSaveButton() {
        btnSave.setOnClickListener(v -> {
            if (validate()) {
                saveRoom();
            }
        });
    }

    /**
     * Validate toàn bộ dữ liệu đầu vào.
     *
     * @return true nếu tất cả hợp lệ, false nếu có lỗi.
     */
    private boolean validate() {
        String roomId   = edtRoomId.getText().toString().trim();
        String roomName = edtRoomName.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();

        // 1. Kiểm tra mã phòng không trống
        if (roomId.isEmpty()) {
            edtRoomId.setError("Mã phòng không được để trống");
            edtRoomId.requestFocus();
            return false;
        }

        // 2. Kiểm tra mã phòng không bị trùng
        for (Room existingRoom : MemoryDataStore.getInstance().getRoomList()) {
            if (existingRoom.getRoomId().equalsIgnoreCase(roomId)) {
                edtRoomId.setError("Mã phòng \"" + roomId + "\" đã tồn tại!");
                edtRoomId.requestFocus();
                return false;
            }
        }

        // 3. Kiểm tra tên phòng không trống
        if (roomName.isEmpty()) {
            edtRoomName.setError("Tên phòng không được để trống");
            edtRoomName.requestFocus();
            return false;
        }

        // 3b. Kiểm tra tên phòng không bị trùng
        for (Room existingRoom : MemoryDataStore.getInstance().getRoomList()) {
            if (existingRoom.getRoomName().equalsIgnoreCase(roomName)) {
                edtRoomName.setError("Tên phòng \"" + roomName + "\" đã tồn tại!");
                edtRoomName.requestFocus();
                return false;
            }
        }

        if (priceStr.isEmpty()) {
            edtPrice.setError("Giá thuê không được để trống");
            edtPrice.requestFocus();
            return false;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            edtPrice.setError("Giá thuê không hợp lệ");
            edtPrice.requestFocus();
            return false;
        }

        if (price <= 0) {
            edtPrice.setError("Giá thuê phải lớn hơn 0");
            edtPrice.requestFocus();
            return false;
        }

        // 5. Nếu "Đã thuê" thì tên & SĐT người thuê không được trống
        if (isOccupied) {
            String tenantName  = edtTenantName.getText().toString().trim();
            String tenantPhone = edtTenantPhone.getText().toString().trim();
            if (tenantName.isEmpty()) {
                edtTenantName.setError("Tên người thuê không được để trống");
                edtTenantName.requestFocus();
                return false;
            }
            if (tenantPhone.isEmpty()) {
                edtTenantPhone.setError("SĐT người thuê không được để trống");
                edtTenantPhone.requestFocus();
                return false;
            }
        }

        return true;
    }

    /**
     * Tạo đối tượng Room và thêm vào MemoryDataStore, sau đó đóng màn hình.
     */
    private void saveRoom() {
        String roomId      = edtRoomId.getText().toString().trim();
        String roomName    = edtRoomName.getText().toString().trim();
        double price       = Double.parseDouble(edtPrice.getText().toString().trim());
        String tenantName  = isOccupied ? edtTenantName.getText().toString().trim() : "";
        String tenantPhone = isOccupied ? edtTenantPhone.getText().toString().trim() : "";

        Room newRoom = new Room(roomId, roomName, price, isOccupied, tenantName, tenantPhone);
        MemoryDataStore.getInstance().getRoomList().add(newRoom);

        Toast.makeText(this, "Thêm phòng thành công!", Toast.LENGTH_SHORT).show();
        finish(); // Quay về MainActivity
    }
}
