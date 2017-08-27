package com.k9rosie.novswar.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Database {
    private DatabaseType type;
    private String prefix;
    private DatabaseConnection databaseConnection;

    public Database(DatabaseType type, String prefix) {
        this.type = type;
        this.prefix = prefix;
        databaseConnection = new DatabaseConnection();
    }

    public abstract void initialize();

    public DatabaseType getType() {
        return type;
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean exists(String table, String column, String search) {
        String query = "SELECT COUNT(1) FROM " + prefix+table + " WHERE " + column + " = '" + search +"';";
        ResultSet result = databaseConnection.scheduleQuery(query);
        int count = 0;
        try {
            if(result.next()){
                count = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void insert(String table, String[] columns, String[] values) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + prefix + table + " (");

        for (int index = 0; index < columns.length; index++) {
            sql.append(columns[index]);
            if (index != (columns.length - 1)) {
                sql.append(",");
                sql.append(" ");
            }
        }

        sql.append(") VALUES (");

        for (int index = 0; index < values.length; index++) {
            sql.append("'" + values[index] + "'");
            if (index != (values.length - 1)) {
                sql.append(",");
                sql.append(" ");
            }
        }

        sql.append(");");
        databaseConnection.scheduleUpdate(sql.toString());
    }

    public ResultSet select(String table, String column, String search) {
        String sql = "SELECT * FROM " + prefix + table + " WHERE " + column + " = '" + search + "';";
        return databaseConnection.scheduleQuery(sql);
    }

    public void add(String table, String update, String add, String column, String search) {
        String sql = "UPDATE " + prefix + table + " SET " + update + " = " + update + " + " + add + " WHERE " + column + " = '" + search + "';";
        databaseConnection.scheduleUpdate(sql);
    }

    public void subtract(String table, String update, String subtract, String column, String search) {
        String sql = "UPDATE " + prefix + table + " SET " + update + " = " + update + " - " + subtract + " WHERE " + column + " = '" + search + "';";
        databaseConnection.scheduleUpdate(sql);
    }

    public void increment(String table, String update, String column, String search) {
        String sql = "UPDATE " + prefix + table + " SET " + update + " = " + update + " + 1 WHERE " + column + " = '" + search + "';";
        databaseConnection.scheduleUpdate(sql);
    }

    public void decrement(String table, String update, String column, String search) {
        String sql = "UPDATE " + prefix + table + " SET " + update + " = " + update + " - 1 WHERE " + column + " = '" + search + "';";
        databaseConnection.scheduleUpdate(sql);
    }

    public void set(String table, String update, String set, String column, String search) {
        String sql = "UPDATE " + prefix + table + " SET " + update + " = " + set + " WHERE " + column + " = '" + search + "'";
        databaseConnection.scheduleUpdate(sql);
    }
}
