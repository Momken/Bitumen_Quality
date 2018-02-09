package com.example.erfan.bitumen_quality.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.erfan.bitumen_quality.DB.BitumenDBHelper;
import com.example.erfan.bitumen_quality.DB.Probe;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erfan on 05.11.2017.
 */

public class ProbeDAO {


    private static final String LOG_TAG = BitumenDAO.class.getSimpleName();

    private SQLiteDatabase database;
    private BitumenDBHelper dbHelper;

    private String[] columns = {
            BitumenDBHelper.Probe_ID,
            BitumenDBHelper.P_Lieferung_ID,
            BitumenDBHelper.Probe_Datum,
            BitumenDBHelper.Probe_Bezeichnung,
            BitumenDBHelper.Probe_Beschreibung
    };


    public ProbeDAO(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new BitumenDBHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public Probe createProbe(long lieferungId, Date date, String bezeichnung, String beschreibung) {
        ContentValues values = new ContentValues();

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


        values.put(BitumenDBHelper.P_Lieferung_ID, lieferungId);
        values.put(BitumenDBHelper.Probe_Datum, df.format(date));
        values.put(BitumenDBHelper.Probe_Bezeichnung, bezeichnung);
        values.put(BitumenDBHelper.Probe_Beschreibung, beschreibung);

        long insertId = database.insert(BitumenDBHelper.TABLE_Probe_LIST, null, values);

        Cursor cursor = database.query(BitumenDBHelper.TABLE_Probe_LIST,
                columns, BitumenDBHelper.Probe_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Probe temp = cursorToProbe(cursor);
        cursor.close();

        return temp;
    }


    public void deleteProbe(Probe temp) {
        long id = temp.getId();

        database.delete(BitumenDBHelper.TABLE_Probe_LIST,
                BitumenDBHelper.Probe_ID + "=" + id,
                null);

        Log.d(LOG_TAG, "Eintrag gelöscht! ID: " + id + " Inhalt: " + temp.toString());
    }

    private Probe cursorToProbe(Cursor cursor) {

        int idProbe = cursor.getColumnIndex(BitumenDBHelper.Probe_ID);
        int idLieferung = cursor.getColumnIndex(BitumenDBHelper.P_Lieferung_ID);
        int idDatum = cursor.getColumnIndex(BitumenDBHelper.Probe_Datum);
        int idBezeichnung = cursor.getColumnIndex(BitumenDBHelper.Probe_Bezeichnung);
        int idBeschreibung = cursor.getColumnIndex(BitumenDBHelper.Probe_Beschreibung);

        String bezeichnung = cursor.getString(idBeschreibung);
        String beschreibung = cursor.getString(idBezeichnung);
        String datum=cursor.getString(idDatum);
        long idLiefer = cursor.getLong(idLieferung);
        long id = cursor.getLong(idProbe);

        Probe temp = new Probe(id, idLiefer, datum, beschreibung, bezeichnung);

        return temp;
    }

    public List<Probe> getAllProbe() {
        List<Probe> list = new ArrayList<>();

        Cursor cursor = database.query(BitumenDBHelper.TABLE_Probe_LIST,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Probe temp;

        while(!cursor.isAfterLast()) {
            temp = cursorToProbe(cursor);
            list.add(temp);
            Log.d(LOG_TAG, "ID: " + temp.getId() + ", Inhalt: " + temp.toString());

            cursor.moveToNext();
        }

        cursor.close();

        return list;
    }

    public List<Probe> getAllProbe(String name) {
        List<Probe> list = new ArrayList<>();

        Cursor cursor = database.query(BitumenDBHelper.TABLE_Probe_LIST,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Probe temp;

        while(!cursor.isAfterLast()) {
            temp = cursorToProbe(cursor);
            if(temp.getBezeichnung().equals(name))
            list.add(temp);
            Log.d(LOG_TAG, "ID: " + temp.getId() + ", Inhalt: " + temp.toString());

            cursor.moveToNext();
        }

        cursor.close();

        return list;
    }
}
