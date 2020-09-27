package com.pan1.entity;

public class UserFile {

    private String id;

    private String email;

    private double size;

    private String filename;

    private int status;

    private String key;

    private String iv;

    public UserFile() {

    }

    public UserFile(String id, String email, double size, String filename, int status, String key, String iv) {
        this.id = id;
        this.email = email;
        this.size = size;
        this.filename = filename;
        this.status = status;
        this.key = key;
        this.iv = iv;
    }

    @Override
    public String toString() {
        return "UserFile{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", size=" + size +
                ", filename='" + filename + '\'' +
                ", status=" + status +
                ", key='" + key + '\'' +
                ", iv='" + iv + '\'' +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }



}
