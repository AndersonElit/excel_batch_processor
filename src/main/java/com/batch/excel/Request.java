package com.batch.excel;

public class Request {
    private int listSize;
    private int rowAccessWindows;
    private int bytes;

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

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "Request{" +
                "listSize=" + listSize +
                ", rowAccessWindows=" + rowAccessWindows +
                ", bytes=" + bytes +
                '}';
    }

}
