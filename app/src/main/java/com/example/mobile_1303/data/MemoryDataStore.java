package com.example.mobile_1303.data;

import com.example.mobile_1303.model.Room;

import java.util.ArrayList;
import java.util.List;
public class MemoryDataStore {
    private static MemoryDataStore instance;
    private List<Room> roomList = new ArrayList<>();
    private MemoryDataStore() {
        // Dữ liệu mẫu
        roomList.add(new Room("P101", "Phòng 101", 2500000, false, "", ""));
        roomList.add(new Room("P102", "Phòng 102", 3000000, true, "Nguyễn Văn A", "0901234567"));
    }
    public static MemoryDataStore getInstance() {
        if (instance == null) instance = new MemoryDataStore();
        return instance;
    }
    public List<Room> getRoomList() { return roomList; }
}
