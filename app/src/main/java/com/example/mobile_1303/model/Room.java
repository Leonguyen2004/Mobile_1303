package com.example.mobile_1303.model;

public class Room {
    private String roomId;       // Mã phòng – VD: "P101"
    private String roomName;     // Tên phòng – VD: "Phòng 101"
    private double price;        // Giá thuê (VND/tháng)
    private boolean isOccupied;  // false = Còn trống | true = Đã thuê
    private String tenantName;   // Tên người thuê (rỗng nếu còn trống)
    private String tenantPhone;  // Số điện thoại người thuê (rỗng nếu còn trống)

    // Constructor đầy đủ
    public Room(String roomId, String roomName, double price,
                boolean isOccupied, String tenantName, String tenantPhone) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.price = price;
        this.isOccupied = isOccupied;
        this.tenantName = tenantName;
        this.tenantPhone = tenantPhone;
    }

    // Getters & Setters (tự sinh bằng IDE)
    public String getRoomId()           { return roomId; }
    public void setRoomId(String v)     { this.roomId = v; }
    public String getRoomName()         { return roomName; }
    public void setRoomName(String v)   { this.roomName = v; }
    public double getPrice()            { return price; }
    public void setPrice(double v)      { this.price = v; }
    public boolean isOccupied()         { return isOccupied; }
    public void setOccupied(boolean v)  { this.isOccupied = v; }
    public String getTenantName()       { return tenantName; }
    public void setTenantName(String v) { this.tenantName = v; }
    public String getTenantPhone()      { return tenantPhone; }
    public void setTenantPhone(String v){ this.tenantPhone = v; }
}

