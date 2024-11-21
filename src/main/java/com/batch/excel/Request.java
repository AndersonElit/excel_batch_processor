package com.batch.excel;

public class Request {
    private int listSize;
    private int rowAccessWindows;

    public int getListSize() {
        return listSize;
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public int getRowAccessWindows() {
        return rowAccessWindows;
    }

    public void setRowAccessWindows(int rowAccessWindows) {
        this.rowAccessWindows = rowAccessWindows;
    }

    @Override
    public String toString() {
        return "Request{" +
                "listSize=" + listSize +
                ", rowAccessWindows=" + rowAccessWindows +
                '}';
    }

}
