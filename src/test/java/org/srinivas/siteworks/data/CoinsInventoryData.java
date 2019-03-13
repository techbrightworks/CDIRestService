package org.srinivas.siteworks.data;

import java.util.Hashtable;
import java.util.Map;

public class CoinsInventoryData {

    public static Map<Integer, Integer> getInventoryData() {
        Map<Integer, Integer> inventory = new Hashtable<Integer, Integer>();
        inventory.put(100, 11);
        inventory.put(50, 24);
        inventory.put(20, 0);
        inventory.put(10, 99);
        inventory.put(5, 200);
        inventory.put(2, 11);
        inventory.put(1, 23);
        return inventory;
    }

}
