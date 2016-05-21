package com.k9rosie.novswar.database;

public class Column {
    private String name;
    private String type;
    private String defaultValue;
    private Table table;
    private boolean autoIncrement;
    private boolean primary;

    public Column(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue == null ? "" : defaultValue;
    }

    public String getType() {
        return type;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
        this.autoIncrement = primary;
    }

    public void setTable(Table table) {
        if (this.table == null) {
            this.table = table;
        }
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean shouldAutoIncrement() {
        return autoIncrement;
    }

}
