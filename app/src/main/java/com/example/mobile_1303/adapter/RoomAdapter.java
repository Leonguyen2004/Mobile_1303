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
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_1303.R;
import com.example.mobile_1303.activity.EditRoomActivity;
import com.example.mobile_1303.model.Room;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

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
