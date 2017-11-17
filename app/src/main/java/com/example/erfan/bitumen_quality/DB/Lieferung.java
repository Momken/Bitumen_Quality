package com.example.erfan.bitumen_quality.DB;

import java.sql.Date;

/**
 * Created by Erfan on 05.11.2017.
 */

public class Lieferung {

    private long id;
    private long herstllerId;
    private Date date;
    private String bezeichnung;
    private String beschreibung;

    public Lieferung(long id, long herstllerId, Date date, String bezeichnung, String beschreibung) {
        this.id = id;
        this.herstllerId = herstllerId;
        this.date = date;
        this.bezeichnung = bezeichnung;
        this.beschreibung = beschreibung;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHerstllerId() {
        return herstllerId;
    }

    public void setHerstllerId(long herstllerId) {
        this.herstllerId = herstllerId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @Override
    public String toString() {
        return "Lieferung{" +
                "id=" + id +
                ", herstllerId=" + herstllerId +
                ", date=" + date +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", beschreibung='" + beschreibung + '\'' +
                '}';
    }
}
