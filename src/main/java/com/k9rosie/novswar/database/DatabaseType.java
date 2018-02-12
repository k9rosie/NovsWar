package com.k9rosie.novswar.database;

public enum DatabaseType {
    MariaDB,
    SQLite;

    public static DatabaseType matchType(String str) {
        for(DatabaseType type : values()) {
            if(type.toString().equalsIgnoreCase(str)){
                return type;
            }
        }

        return null;
    }
}
