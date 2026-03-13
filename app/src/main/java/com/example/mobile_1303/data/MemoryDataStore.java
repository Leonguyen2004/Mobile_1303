package com.example.mobile_1303.data;

import java.util.ArrayList;
import java.util.List;

public class MemoryDataStore {
    private static MemoryDataStore instance;
    // Sẽ đổi Object thành Model thực tế khi có đề (VD: SinhVien, Product...)
    private List<Object> itemList;

    private MemoryDataStore() {
        itemList = new ArrayList<>();
    }

    public static synchronized MemoryDataStore getInstance() {
        if (instance == null) {
            instance = new MemoryDataStore();
        }
        return instance;
    }

    public List<Object> getAllItems() { return itemList; }

    public void addItem(Object item) { itemList.add(item); }

    public void updateItem(int index, Object updatedItem) {
        if (index >= 0 && index < itemList.size()) {
            itemList.set(index, updatedItem);
        }
    }

    public void deleteItem(int index) {
        if (index >= 0 && index < itemList.size()) {
            itemList.remove(index);
        }
    }
}
