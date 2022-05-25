package org.kayteam.mysqlutil;

import java.util.HashMap;
import java.util.Set;

public class Row {

    private final HashMap<String, Object> content = new HashMap<>();

    public void addColumn(String name, Object content) {
        this.content.put(name, content);
    }

    public HashMap<String, Object> getColumns() {
        return content;
    }

    public Object get(String key) {
        return content.get(key);
    }

    public Set<String> getKeys() {
        return content.keySet();
    }

}