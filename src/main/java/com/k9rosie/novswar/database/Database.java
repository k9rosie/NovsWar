package com.k9rosie.novswar.database;

import java.util.ArrayList;

public class Database {
    private String name;
    private String prefix;
    private ArrayList<Table> tables;

    public Database(String name) {
        this.name = name;
        tables = new ArrayList<Table>();
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }


}
