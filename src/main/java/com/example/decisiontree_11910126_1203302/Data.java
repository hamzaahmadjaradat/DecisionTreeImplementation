package com.example.decisiontree_11910126_1203302;

import java.util.ArrayList;

public class Data {

    Feature[] columns; //features
    ArrayList<String[]> rows;

    public Data(Feature[] columns, ArrayList<String[]> rows) {
        this.columns = columns;
        this.rows = rows;
    }

    void addRow(String[] row) {
        rows.add(row);
    }

    public ArrayList<String[]> getRows() {
        return rows;
    }

    public Feature[] getColumns() {
        return columns;
    }

    public ArrayList<String> getColumnData(String name) {
        ArrayList<String> column = new ArrayList<>();
        int index = -1;
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].getName().equals(name)) {
                index = i;
                break;
            }
        }
        for (String[] row : rows) {
            column.add(row[index]);
        }
        return column;
    }

    public int getColumnIndex(String name) {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }


}
