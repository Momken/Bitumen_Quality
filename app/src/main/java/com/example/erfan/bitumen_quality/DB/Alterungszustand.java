package com.example.erfan.bitumen_quality.DB;

import java.sql.Date;

/**
 * Created by Erfan on 05.11.2017.
 */

public class Alterungszustand {

    private long id;
    private long probenId;
    private Date date;
    private String bezeichnung;
    private String messungsfaktoren;
    private String messung;

    public Alterungszustand(long id, long probenId, Date date, String bezeichnung, String messungsfaktoren, String messung) {
        this.id = id;
        this.probenId = probenId;
        this.date = date;
        this.bezeichnung = bezeichnung;
        this.messungsfaktoren = messungsfaktoren;
        this.messung = messung;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProbenId() {
        return probenId;
    }

    public void setProbenId(long probenId) {
        this.probenId = probenId;
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

    public String getMessungsfaktoren() {
        return messungsfaktoren;
    }

    public void setMessungsfaktoren(String messungsfaktoren) {
        this.messungsfaktoren = messungsfaktoren;
    }

    public String getMessung() {
        return messung;
    }

    public void setMessung(String messung) {
        this.messung = messung;
    }

    @Override
    public String toString() {
        return "Alterungszustand{" +
                "id=" + id +
                ", probenId=" + probenId +
                ", date=" + date +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", messungsfaktoren='" + messungsfaktoren + '\'' +
                ", messung='" + messung + '\'' +
                '}';
    }
}
