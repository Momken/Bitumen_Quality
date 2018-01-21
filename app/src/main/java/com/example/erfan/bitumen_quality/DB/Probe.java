package com.example.erfan.bitumen_quality.DB;

import java.sql.Date;

/**
 * Created by Erfan on 05.11.2017.
 */

public class Probe {

    private long id;
    private long LieferungId;
    private String date;
    private String bezeichnung;
    private String beschreibung;


    public Probe(long id, long lieferungId, String date, String bezeichnung, String beschreibung) {
        this.id = id;
        LieferungId = lieferungId;
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

    public long getLieferungId() {
        return LieferungId;
    }

    public void setLieferungId(long lieferungId) {
        LieferungId = lieferungId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
        return "Probe{" +
                "id=" + id +
                ", LieferungId=" + LieferungId +
                ", date=" + date +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", beschreibung='" + beschreibung + '\'' +
                '}';
    }
}
