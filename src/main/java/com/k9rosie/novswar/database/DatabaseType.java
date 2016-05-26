package com.k9rosie.novswar.database;

public enum DatabaseType {
    MySQL ("mysql.jar"),
    SQLite ("sqlite.jar");

    private String driver;

    DatabaseType(String driver) {
        this.driver = driver;
    }

    public String getDriver() {
        return driver;
    }

    public static DatabaseType matchType(String str) {
        for(DatabaseType type : values()) {
            if(type.toString().equalsIgnoreCase(str)){
                return type;
            }
        }

        return null;
    }
}
