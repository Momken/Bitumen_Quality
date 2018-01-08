package com.example.erfan.bitumen_quality.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.erfan.bitumen_quality.DB.BitumenDBHelper;
import com.example.erfan.bitumen_quality.DB.Lieferung;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erfan on 05.11.2017.
 */

public class LieferungDAO {

    private static final String LOG_TAG = BitumenDAO.class.getSimpleName();

    private SQLiteDatabase database;
    private BitumenDBHelper dbHelper;

    private String[] columns = {
            BitumenDBHelper.Lieferung_ID,
            BitumenDBHelper.L_Hersteller_ID,
            BitumenDBHelper.Lieferung_Datum,
            BitumenDBHelper.Lieferung_Bezeichnung,
            BitumenDBHelper.Lieferung_Beschreibung
    };


    public LieferungDAO(Context context) {
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

    public Lieferung createLieferung( long herstllerId, Date date, String bezeichnung, String beschreibung) {
        ContentValues values = new ContentValues();
//        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        values.put(BitumenDBHelper.L_Hersteller_ID, herstllerId);
        values.put(BitumenDBHelper.Lieferung_Datum, df.format(date));
        values.put(BitumenDBHelper.Lieferung_Bezeichnung, bezeichnung);
        values.put(BitumenDBHelper.Lieferung_Beschreibung, beschreibung);

        long insertId = database.insert(BitumenDBHelper.TABLE_Lieferung_LIST, null, values);

        Cursor cursor = database.query(BitumenDBHelper.TABLE_Lieferung_LIST,
                columns, BitumenDBHelper.Lieferung_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Lieferung lieferung = cursorToLieferung(cursor);
        cursor.close();

        return lieferung;
    }


    public void deleteLieferung(Lieferung temp) {
        long id = temp.getId();

        database.delete(BitumenDBHelper.TABLE_Lieferung_LIST,
                BitumenDBHelper.Lieferung_ID + "=" + id,
                null);

        Log.d(LOG_TAG, "Eintrag gelöscht! ID: " + id + " Inhalt: " + temp.toString());
    }

    private Lieferung cursorToLieferung(Cursor cursor) {

        int idLieferung = cursor.getColumnIndex(BitumenDBHelper.Lieferung_ID);
        int idLHersteller = cursor.getColumnIndex(BitumenDBHelper.L_Hersteller_ID);
        int idDatum = cursor.getColumnIndex(BitumenDBHelper.Lieferung_Datum);
        int idBezeichnung = cursor.getColumnIndex(BitumenDBHelper.Lieferung_Bezeichnung);
        int idBeschreibung = cursor.getColumnIndex(BitumenDBHelper.Lieferung_Beschreibung);



        String bezeichnung = cursor.getString(idBeschreibung);
        String beschreibung = cursor.getString(idBezeichnung);
        String datum=cursor.getString(idDatum);
        long idHersteller = cursor.getLong(idLHersteller);
        long id = cursor.getLong(idLieferung);

        Lieferung temp = new Lieferung(id, idHersteller, datum, beschreibung, bezeichnung);

        return temp;
    }

    public List<Lieferung> getAllLieferung() {
        List<Lieferung> list = new ArrayList<>();

        Cursor cursor = database.query(BitumenDBHelper.TABLE_Lieferung_LIST,
                columns, null, null, null, null, null);
        cursor.moveToFirst();
        Lieferung temp;
        while(!cursor.isAfterLast()) {
            temp = cursorToLieferung(cursor);
            list.add(temp);
            Log.d(LOG_TAG, "ID: " + temp.getId() + ", Inhalt: " + temp.toString());
            cursor.moveToNext();
        }

        cursor.close();



        return list;
    }

    public List<Lieferung> getAllLieferung(String name) {
        List<Lieferung> list = new ArrayList<>();

        Cursor cursor = database.query(BitumenDBHelper.TABLE_Lieferung_LIST,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Lieferung temp;

        while(!cursor.isAfterLast()) {
            temp = cursorToLieferung(cursor);
            if(temp.getBezeichnung().equals(name))
            list.add(temp);

            cursor.moveToNext();
        }

        cursor.close();

        return list;
    }



}
