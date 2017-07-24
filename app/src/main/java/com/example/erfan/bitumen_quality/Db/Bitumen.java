package com.example.erfan.bitumen_quality.Db;

/**
 * Created by Erfan on 17.07.2017.
 */

public class Bitumen {

    private long id;
    private String type;
    private String notiz;

    public Bitumen(long id, String type, String notiz) {
        this.id = id;
        this.type = type;
        this.notiz = notiz;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotiz() {
        return notiz;
    }

    public void setNotiz(String notiz) {
        this.notiz = notiz;
    }

    @Override
    public String toString() {
        return "Bitumen{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", notiz='" + notiz + '\'' +
                '}';
    }
}
