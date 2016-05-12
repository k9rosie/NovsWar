package com.k9rosie.novswar.database;

public class ASyncUpdateSQL implements Runnable {
    private DatabaseConnection connection;
    private String query;

    public ASyncUpdateSQL(DatabaseConnection connection, String query) {
        this.connection = connection;
        this.query = query;
    }

    public void run() {
        connection.executeUpdate(query);
    }
}
