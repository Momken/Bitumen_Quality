package com.example.erfan.bitumen_quality.DB;



public class Sorte {

    private long id;
    private String bezeichnung;
    private String beschreibung;
    private int q1MinGreenQ;
    private int q1Min;
    private int q2MinGreenQ;
    private int q2Min;

    public int getQ1MinGreenQ() {
        return q1MinGreenQ;
    }

    public void setQ1MinGreenQ(int q1MinGreenQ) {
        this.q1MinGreenQ = q1MinGreenQ;
    }

    public int getQ1Min() {
        return q1Min;
    }

    public void setQ1Min(int q1Min) {
        this.q1Min = q1Min;
    }

    public int getQ2MinGreenQ() {
        return q2MinGreenQ;
    }

    public void setQ2MinGreenQ(int q2MinGreenQ) {
        this.q2MinGreenQ = q2MinGreenQ;
    }

    public int getQ2Min() {
        return q2Min;
    }

    public void setQ2Min(int q2Min) {
        this.q2Min = q2Min;
    }

    public Sorte(long id, String bezeichnung, String beschreibung, int q1max, int q1min, int q2max, int q2min) {
        this.id = id;
        this.bezeichnung = bezeichnung;
        this.beschreibung = beschreibung;
        this.q1MinGreenQ = q1max;
        this.q1Min =q1min;
        this.q2MinGreenQ =q2max;
        this.q2Min =q2min;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        return "Sorte{" +
                "id=" + id +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", beschreibung='" + beschreibung + '\'' +
                ", q1MinGreenQ=" + q1MinGreenQ +
                ", q1Min=" + q1Min +
                ", q2MinGreenQ=" + q2MinGreenQ +
                ", q2Min=" + q2Min +
                '}';
    }
}
