package org.kayteam.mysqlutil;

import java.util.ArrayList;
import java.util.List;

public class Result {

    private List<Row> rows = new ArrayList<>();

    public Result(){}
    public void addrow(Row row) {
        rows.add(row);
    }

    public List<Row> getRows() {
        return rows;
    }

}