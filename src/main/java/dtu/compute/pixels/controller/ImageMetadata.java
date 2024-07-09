package dtu.compute.pixels.controller;

import java.util.Date;

public class ImageMetadata {
    private String name;
    private String path;
    private long size;
    private Date date;

// Getters
public String getName() {
    return name;
}

public String getPath() {
    return path;
}

public long getSize() {
    return size;
}

public Date getDate() {
    return date;
}

// Setters
public void setName(String name) {
    this.name = name;
}

public void setPath(String path) {
    this.path = path;
}

public void setSize(long size) {
    this.size = size;
}

public void setDate(Date date) {
    this.date = date;
    }   
}