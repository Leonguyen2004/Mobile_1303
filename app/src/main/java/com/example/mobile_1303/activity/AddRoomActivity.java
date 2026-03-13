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

        // 1. Kiểm tra mã phòng và tên phòng không trống
        if (roomId.isEmpty() || roomName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 2. Kiểm tra giá thuê hợp lệ (> 0)
        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá thuê không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (price <= 0) {
            Toast.makeText(this, "Giá thuê không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 3. Nếu "Đã thuê" thì tên & SĐT người thuê không được trống
        if (isOccupied) {
            String tenantName  = edtTenantName.getText().toString().trim();
            String tenantPhone = edtTenantPhone.getText().toString().trim();
            if (tenantName.isEmpty() || tenantPhone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
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
