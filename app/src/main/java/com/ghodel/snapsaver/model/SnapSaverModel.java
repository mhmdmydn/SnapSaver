package com.ghodel.snapsaver.model;

public class SnapSaverModel {

    private String title;
    private String path;

    public SnapSaverModel(){

    }

    public SnapSaverModel(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "SnapSaverModel{" +
                "title='" + title + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
