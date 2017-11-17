package com.example.erfan.bitumen_quality.DB;

/**
 * Created by Erfan on 05.11.2017.
 */

public class Hersteller {

    private long id;
    private long sortenId;
    private String name;
    private String beschreibung;


    public Hersteller(long id, long sortenId, String name, String beschreibung) {
        this.id = id;
        this.sortenId = sortenId;
        this.name = name;
        this.beschreibung = beschreibung;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSortenId() {
        return sortenId;
    }

    public void setSortenId(long sortenId) {
        this.sortenId = sortenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @Override
    public String toString() {
        return "Hersteller{" +
                "id=" + id +
                ", sortenId=" + sortenId +
                ", name='" + name + '\'' +
                ", beschreibung='" + beschreibung + '\'' +
                '}';
    }
}
