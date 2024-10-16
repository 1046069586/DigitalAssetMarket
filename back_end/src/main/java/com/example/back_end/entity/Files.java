package com.example.back_end.entity;

public class Files {

    private String url;
    private String type;
    private long size;

    public Files(String url, String type, long size) {
        this.url = url;
        this.type = type;
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
