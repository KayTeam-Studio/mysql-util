package org.kayteam.mysqlutil;

public class Update {

    private String table = "";
    private UpdateValue value = null;
    private String filter = "";

    public Update() {}
    /**
     * Get the SQL query
     * @param table the table to update
     * @param value the value to update
     * @param filter the filter to update
     */
    public Update(String table, UpdateValue value, String filter) {
        this.table = table;
        this.value = value;
        this.filter = filter;
    }

    /**
     * Set the table to update
     * @param table the table to update
     */
    public void setTable(String table) {
        this.table = table;
    }

    /**
     * Set the filter to update
     * @param filter the filter to update
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * Set the value to update
     * @param value the value to update
     */
    public void setValue(UpdateValue value) {
        this.value = value;
    }

    /**
     * Get the table to update
     * @return the table to update
     */
    public String getTable() {
        return table;
    }

    /**
     * Get the filter to update
     * @return the filter to update
     */
    public String getFilter() {
        return filter;
    }

    /**
     * Get the value to update
     * @return the value to update
     */
    public UpdateValue getValue() {
        return value;
    }

}