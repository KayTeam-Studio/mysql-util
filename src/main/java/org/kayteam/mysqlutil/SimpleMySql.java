package org.kayteam.mysqlutil;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.*;

public class SimpleMySql {

    private final String host;
    private final String port;
    private final String database;
    private final String user;
    private final String password;

    private Connection connection = null;

    public SimpleMySql(String host, String port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + user + "&password=" + password + "?autoReconnect=true";
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Connect to the MySQL database
     */
    public boolean connect() {
        try {
            MysqlDataSource mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setServerName(host);
            mysqlDataSource.setPort(Integer.parseInt(port));
            mysqlDataSource.setDatabaseName(database);
            mysqlDataSource.setUser(user);
            mysqlDataSource.setPassword(password);
            mysqlDataSource.setAutoReconnect(true);
            connection = mysqlDataSource.getConnection();
            if (!connection.isClosed()) System.out.println("Successfully connected to " + host + ":" + port + "/" + database);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cierra la conexión con la base de datos
     */
    public void disconnect() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ejecuta una sentencia SQL
     * Ejemplo: sql.tableInsert("myTable", "name, age", "Robert", "32");
     * @param table Nombre de la tabla
     * @param columns Columnas a insertar
     * @param data Datos a insertar
     * @return true si se ejecuta correctamente, false si no
     */
    public boolean tableInsert(String table, String columns, String... data) {
        StringBuilder sqlData = new StringBuilder();
        int i = 0;
        for (String d : data) {
            sqlData.append("'").append(d).append("'");
            i++;
            if(i != data.length) sqlData.append(", ");
        }
        String sql = "INSERT INTO " + table + " (" + columns + ") VALUES (" + sqlData + ");";
        Statement statement = null;
        try {
            if (connection.isClosed()) connect();
            statement = connection.createStatement();
            statement.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(statement != null) {
                try {
                    statement.close();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Ejecuta una sentencia SQL
     * Ejemplo: sql.tableInsert("myTable", "name, age", "Robert", "32");
     * @param builders Sentencias a ejecutar
     * @return true si se ejecuta correctamente, false si no
     */
    public boolean tableInsert(Insert... builders) {
        StringBuilder sql = new StringBuilder();
        for (Insert b : builders) {
            StringBuilder sqlData = new StringBuilder();
            int i = 0;
            for (String d : b.getData()) {
                sqlData.append("'").append(d).append("'");
                i++;
                if(i != b.getData().length) sqlData.append(", ");
            }
            sql.append("INSERT INTO ").append(b.getTable()).append(" (").append(b.getColumns()).append(") VALUES (").append(sqlData).append("); ");
        }
        Statement statement = null;
        try {
            if (connection.isClosed()) connect();
            statement = connection.createStatement();
            statement.execute(sql.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(statement != null) {
                try {
                    statement.close();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Ejecuta una consulta SQL
     * Ejemplo: sql.rowUpdate("myTable", new UpdateValue("age", "45"), "name = 'Robert'");
     * @param table Tabla a consultar
     * @param value Valor a buscar
     * @param filter Campo a filtrar
     * @return Resultado de la consulta
     */
    public boolean rowUpdate(String table, UpdateValue value, String filter) {
        StringBuilder change = new StringBuilder();
        int i = 0;
        for(String key : value.getKeys()) {
            change.append(key).append(" = '").append(value.get(key)).append("'");
            i++;
            if(i != value.getKeys().size()) change.append(", ");
        }
        String sql = "UPDATE " + table + " SET " + change + " WHERE " + filter + ";";
        Statement statement = null;
        try {
            if (connection.isClosed()) connect();
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Ejecuta una consulta SQL
     * Ejemplo: sql.rowUpdate(UpdateValue("age", "45"));
     * @param builders Sentencias a ejecutar
     * @return true si se ejecuta correctamente, false si no
     */
    public boolean rowUpdate(Update... builders) {
        StringBuilder sql = new StringBuilder();
        for (Update u : builders) {
            StringBuilder change = new StringBuilder();
            int i = 0;
            for(String key : u.getValue().getKeys()) {
                change.append(key).append(" = '").append(u.getValue().get(key)).append("'");
                i++;
                if(i != u.getValue().getKeys().size()) {
                    change.append(", ");
                }
            }
            sql.append("UPDATE ").append(database).append(".").append(u.getTable()).append(" SET ").append(change).append(" WHERE ").append(u.getFilter()).append("; ");
        }
        Statement statement = null;
        try {
            if (connection.isClosed()) connect();
            statement = connection.createStatement();
            statement.execute(sql.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Ejecuta una sentencia SQL
     * Ejemplo: sql.rowSelect("myTable", "*", "name = 'Robert'");
     * @param table Tabla a la que se va a conectar
     * @param columns Columnas a las que se va a conectar
     * @param filter Filtro a aplicar
     * @return Resultado de la sentencia
     */
    public Result rowSelect(String table, String columns, String filter) {
        if(columns == null || columns.equals("")) columns = "*";
        String sql = "SELECT " + columns + " FROM " + table;
        if(filter != null && !filter.equals("")) sql = sql + " WHERE " + filter;
        sql = sql + ";";

        Statement statement;
        ResultSet resultSet;
        try {
            if (connection.isClosed()) connect();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            Result result = new Result();
            while(resultSet.next()) {
                Row row = new Row();
                int i = 1;
                boolean bound = true;
                while (bound) {
                    try {
                        row.addColumn(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                    } catch (SQLException e) {
                        bound = false;
                    }
                    i++;
                }
                result.addrow(row);
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return new Result();
        }
    }

    /**
     * Ejecuta una sentencia SQL
     * Ejemplo: sql.rowSelect("myTable", "*", "name = 'Robert'");
     * @param select Sentencia a ejecutar
     * @return Resultado de la sentencia
     */
    public Result rowSelect(Select select) {
        String sql = "";
        String columns;
        String lsql;
        if(select.getColumns() == null || select.getColumns().equals("")) {
            columns = "*";
        } else {
            columns = select.getColumns();
        }
        lsql = "SELECT " + columns + " FROM " + select.getTable();
        if(select.getFilter() != null && !select.getFilter().equals("")) {
            lsql = lsql + " WHERE " + select.getFilter();
        }
        lsql = lsql + "; ";
        sql = sql + lsql;
        Statement statement;
        ResultSet resultSet;
        try {
            if (connection.isClosed()) connect();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            Result result = new Result();
            while(resultSet.next()) {
                Row row = new Row();
                int i = 1;
                boolean bound = true;
                while (bound) {
                    try {
                        row.addColumn(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                    } catch (SQLException e) {
                        bound = false;
                    }
                    i++;
                }
                result.addrow(row);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result();
        }
    }

    public boolean rowDelete(String table, String filter) {
        String sql = "DELETE FROM " + table;
        if(filter != null && !filter.equals("")) sql = sql + " WHERE " + filter;
        sql = sql + ";";
        Statement statement;
        try {
            if (connection.isClosed()) connect();
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Crear una tabla en la base de datos
     * @param table Nombre de la tabla
     * @param columns Columnas de la tabla
     * @return Verdadero si la tabla fue creada, falso en caso contrario
     */
    public boolean createTable(String table, String columns) {
        return custom("CREATE TABLE IF EXIST `" + database + "`.`" + table + "` ( " + columns + ") ENGINE = InnoDB;");
    }

    public boolean custom(String sql) {
        Statement statement = null;
        try {
            if (connection.isClosed()) connect();
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

}