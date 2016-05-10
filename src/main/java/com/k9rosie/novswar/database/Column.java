package com.k9rosie.novswar.database;

public class Column {
    private String name;
    private String type;
    private String defaultValue;
    private boolean autoIncrement;
    private boolean primary;

    public Column(String name, String type) {
        this.name = name;
        this.type = type;
        defaultValue = "";
        autoIncrement = false;
        primary = false;
    }




}
