package com.example.erfan.bitumen_quality.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.erfan.bitumen_quality.DB.BitumenDBHelper;
import com.example.erfan.bitumen_quality.DB.Sorte;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erfan on 05.11.2017.
 */

public class SorteDAO {

    private static final String LOG_TAG = BitumenDAO.class.getSimpleName();

    private SQLiteDatabase database;
    private BitumenDBHelper dbHelper;

    private String[] columns = {
            BitumenDBHelper.Sorte_ID,
            BitumenDBHelper.Sorte_bezeichnung,
            BitumenDBHelper.Sorte_beschreibung,
            BitumenDBHelper.Sorte_q1MinGreenQ,
            BitumenDBHelper.Sorte_q1Min,
            BitumenDBHelper.Sorte_q2MinGreenQ,
            BitumenDBHelper.Sorte_q2Min

    };


    public SorteDAO(Context context) {
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

    public Sorte createSorte(String bezeichnung, String beschreibung,int q1max, int q1min, int q2max, int q2min) {
        ContentValues values = new ContentValues();

        values.put(BitumenDBHelper.Sorte_bezeichnung, bezeichnung);
        values.put(BitumenDBHelper.Sorte_beschreibung, beschreibung);
        values.put(BitumenDBHelper.Sorte_q1MinGreenQ, q1max);
        values.put(BitumenDBHelper.Sorte_q1Min, q1min);
        values.put(BitumenDBHelper.Sorte_q2MinGreenQ, q2max);
        values.put(BitumenDBHelper.Sorte_q2Min, q2min);



        Log.d(LOG_TAG,values.toString());
        long insertId = database.insert(BitumenDBHelper.TABLE_Sorte_LIST, null, values);


        Cursor cursor = database.query(BitumenDBHelper.TABLE_Sorte_LIST,
                columns, BitumenDBHelper.Sorte_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Sorte sorte = cursorToSorte(cursor);
        cursor.close();
        return sorte;
    }


    public void deleteSorte(Sorte temp) {
        long id = temp.getId();

        database.delete(BitumenDBHelper.TABLE_Sorte_LIST,
                BitumenDBHelper.Sorte_ID + "=" + id,
                null);

        Log.d(LOG_TAG, "Eintrag gelöscht! ID: " + id + " Inhalt: " + temp.toString());
    }

    private Sorte cursorToSorte(Cursor cursor) {

        int idSorte = cursor.getColumnIndex(BitumenDBHelper.Sorte_ID);
        int idBezeichnung = cursor.getColumnIndex(BitumenDBHelper.Sorte_bezeichnung);
        int idBescheibung = cursor.getColumnIndex(BitumenDBHelper.Sorte_beschreibung);
        int idq1MinGreenQ = cursor.getColumnIndex(BitumenDBHelper.Sorte_q1MinGreenQ);
        int idq1m = cursor.getColumnIndex(BitumenDBHelper.Sorte_q1Min);
        int idq2MinGreenQ = cursor.getColumnIndex(BitumenDBHelper.Sorte_q2MinGreenQ);
        int idq2m = cursor.getColumnIndex(BitumenDBHelper.Sorte_q2Min);


        String beschreibung = cursor.getString(idBescheibung);
        String bezeichnung = cursor.getString(idBezeichnung);
        int q1MinGreenQ = cursor.getInt(idq1MinGreenQ);
        int q1Min = cursor.getInt(idq1m);
        int q2MinGreenQ = cursor.getInt(idq2MinGreenQ);
        int q2Min = cursor.getInt(idq2m);

        long id = cursor.getLong(idSorte);

        Sorte sorte = new Sorte(id, bezeichnung, beschreibung, q1MinGreenQ, q1Min, q2MinGreenQ, q2Min);

        return sorte;
    }

    public List<Sorte> getAllSorte() {
        List<Sorte> list = new ArrayList<>();

        Cursor cursor = database.query(BitumenDBHelper.TABLE_Sorte_LIST,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Sorte sorte;

        while(!cursor.isAfterLast()) {
            sorte = cursorToSorte(cursor);
            list.add(sorte);
            Log.d(LOG_TAG, "ID: " + sorte.getId() + ", Inhalt: " + sorte.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return list;
    }

    public List<Sorte> getAllSorte(String name) {
        List<Sorte> list = new ArrayList<>();

        Cursor cursor = database.query(BitumenDBHelper.TABLE_Sorte_LIST,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Sorte temp;

        while(!cursor.isAfterLast()) {

            temp = cursorToSorte(cursor);


            if(temp.getBezeichnung().equals(name))
                list.add(temp);

            cursor.moveToNext();
        }

        cursor.close();

        return list;
    }




}
