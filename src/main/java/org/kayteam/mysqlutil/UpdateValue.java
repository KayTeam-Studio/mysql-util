package org.kayteam.mysqlutil;

import java.util.HashMap;
import java.util.Set;

public class UpdateValue {

    private final HashMap<String, String> data = new HashMap<>();

    public UpdateValue() {}
    public UpdateValue(String cellName,  String newValue) {
        data.put(cellName, newValue);
    }

    /**
     * Adds a new value to the update value.
     * @param cellName the new value
     * @param newValue the new value
     */
    public void add (String cellName, String newValue) {
        data.put(cellName, newValue);
    }

    public Set<String> getKeys() {
        return data.keySet();
    }

    public String get(String key) {
        return data.get(key);
    }

}