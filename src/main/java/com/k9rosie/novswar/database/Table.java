package com.k9rosie.novswar.database;

import java.util.ArrayList;

public class Table {
    private String name;
    private ArrayList<Column> columns;

    public Table(String name) {
        this.name = name;
        columns = new ArrayList<Column>();
    }

}
