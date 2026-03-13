package com.example.mobile_1303.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_1303.R;
import com.example.mobile_1303.data.MemoryDataStore;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_1303.R;
import com.example.mobile_1303.activity.EditRoomActivity;
import com.example.mobile_1303.model.Room;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    // ===================== Interface callback xóa =====================
    public interface OnDeleteListener {
        void onDelete(int position);
    }

    // ===================== Fields =====================
    private final List<Room> roomList;
    private final Context context;
    private OnDeleteListener onDeleteListener;

    // ===================== Constructor =====================
    /**
     * Constructor nhận Context, danh sách phòng và callback xóa.
     */
    public RoomAdapter(Context context, List<Room> roomList, OnDeleteListener listener) {
        this.context = context;
        this.roomList = roomList;
        this.onDeleteListener = listener;
    }

    // Setter nếu muốn gán listener sau
    public void setOnDeleteListener(OnDeleteListener listener) {
        this.onDeleteListener = listener;
    }

    // ===================== ViewHolder =====================
    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvPrice, tvStatus, tvTenant;
        Button btnDelete;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.item_tv_room_name);
            tvPrice    = itemView.findViewById(R.id.item_tv_price);
            tvStatus   = itemView.findViewById(R.id.item_tv_status);
            tvTenant   = itemView.findViewById(R.id.item_tv_tenant);
            btnDelete  = itemView.findViewById(R.id.item_btn_delete);
        }
    }

    // ===================== Adapter overrides =====================
    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
    private final Context context;
    private final List<Room> roomList;

    public RoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);

        // --- Hiển thị thông tin phòng ---
        holder.tvRoomName.setText(room.getRoomName());

        // Format giá tiền VND
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.tvPrice.setText("Giá: " + nf.format(room.getPrice()) + " VND");

        // Trạng thái phòng
        if (room.isOccupied()) {
            holder.tvStatus.setText("Đã thuê");
            holder.tvStatus.setTextColor(0xFFF44336); // Đỏ
            // Hiện tên người thuê
            holder.tvTenant.setVisibility(View.VISIBLE);
            holder.tvTenant.setText("Người thuê: " + room.getTenantName()
                    + " - " + room.getTenantPhone());
        } else {
            holder.tvStatus.setText("Còn trống");
            holder.tvStatus.setTextColor(0xFF4CAF50); // Xanh
            holder.tvTenant.setVisibility(View.GONE);
        }

        // ===================== CHỨC NĂNG XÓA (TV4) =====================

        // Bật nút Xóa (item_btn_delete mặc định visibility="gone" trong XML)
        holder.btnDelete.setVisibility(View.VISIBLE);

        // --- Click nút Xóa → AlertDialog xác nhận ---
        holder.btnDelete.setOnClickListener(v -> {
            showDeleteDialog(holder, room);
        });

        // --- Long Click trên toàn bộ item → AlertDialog xóa tương tự ---
        holder.itemView.setOnLongClickListener(v -> {
            showDeleteDialog(holder, room);
            return true;
        // Hiển thị tên phòng
        holder.tvRoomName.setText(room.getRoomName());

        // Hiển thị giá thuê theo format "2,500,000 VND"
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        String formattedPrice = formatter.format((long) room.getPrice()) + " VND";
        holder.tvPrice.setText(formattedPrice);

        // Hiển thị trạng thái và tô màu
        if (room.isOccupied()) {
            holder.tvStatus.setText("Đã thuê");
            holder.tvStatus.setTextColor(Color.parseColor("#F44336")); // Đỏ
        } else {
            holder.tvStatus.setText("Còn trống");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Xanh lá
        }

        // Nút xóa ẩn tạm – thành viên 4 sẽ xử lý logic xóa
        holder.btnDelete.setVisibility(View.GONE);

        // Click item → mở EditRoomActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditRoomActivity.class);
            intent.putExtra("room_index", position);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return roomList == null ? 0 : roomList.size();
    }

    // ===================== Helper: Dialog xác nhận xóa =====================
    /**
     * Hiển thị AlertDialog xác nhận xóa phòng.
     * Sau khi xóa: cập nhật MemoryDataStore + notify Adapter + gọi callback.
     */
    private void showDeleteDialog(RoomViewHolder holder, Room room) {
        int pos = holder.getAdapterPosition();
        if (pos == RecyclerView.NO_POSITION) return; // tránh crash nếu item đã bị remove

        new AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa phòng \"" + room.getRoomName() + "\" không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // Xóa khỏi nguồn dữ liệu
                    MemoryDataStore.getInstance().getRoomList().remove(pos);
                    // Thông báo cho RecyclerView
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, getItemCount());
                    // Gọi callback (nếu có)
                    if (onDeleteListener != null) {
                        onDeleteListener.onDelete(pos);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName;
        TextView tvPrice;
        TextView tvStatus;
        Button btnDelete;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.item_tv_room_name);
            tvPrice    = itemView.findViewById(R.id.item_tv_price);
            tvStatus   = itemView.findViewById(R.id.item_tv_status);
            btnDelete  = itemView.findViewById(R.id.item_btn_delete);
        }
    }
}
