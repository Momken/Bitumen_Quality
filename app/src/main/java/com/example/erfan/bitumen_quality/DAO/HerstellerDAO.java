package com.example.erfan.bitumen_quality.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.erfan.bitumen_quality.DB.BitumenDBHelper;
import com.example.erfan.bitumen_quality.DB.Hersteller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erfan on 05.11.2017.
 */

public class HerstellerDAO {


    private static final String LOG_TAG = BitumenDAO.class.getSimpleName();

    private SQLiteDatabase database;
    private BitumenDBHelper dbHelper;

    private String[] columns = {
            BitumenDBHelper.Hersteller_ID,
            BitumenDBHelper.H_Sorten_ID,
            BitumenDBHelper.Hersteller_Name,
            BitumenDBHelper.Hersteller_beschreibung

    };


    public HerstellerDAO(Context context) {
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

    public Hersteller createHersteller( long sortenId, String name, String beschreibung) {
        ContentValues values = new ContentValues();

        values.put(BitumenDBHelper.H_Sorten_ID, sortenId);
        values.put(BitumenDBHelper.Hersteller_Name, name);
        values.put(BitumenDBHelper.Hersteller_beschreibung, beschreibung);

        long insertId = database.insert(BitumenDBHelper.TABLE_Hersteller_LIST, null, values);

        Cursor cursor = database.query(BitumenDBHelper.TABLE_Hersteller_LIST,
                columns, BitumenDBHelper.Hersteller_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Hersteller hersteller = cursorToHersteller(cursor);
        cursor.close();

        return hersteller;
    }


    public void deleteHersteller(Hersteller temp) {
        long id = temp.getId();

        database.delete(BitumenDBHelper.TABLE_Hersteller_LIST,
                BitumenDBHelper.Hersteller_ID + "=" + id,
                null);

        Log.d(LOG_TAG, "Eintrag gelöscht! ID: " + id + " Inhalt: " + temp.toString());
    }

    private Hersteller cursorToHersteller(Cursor cursor) {


        int idIndex = cursor.getColumnIndex(BitumenDBHelper.Hersteller_ID);
        int idSorte = cursor.getColumnIndex(BitumenDBHelper.H_Sorten_ID);
        int idName = cursor.getColumnIndex(BitumenDBHelper.Hersteller_Name);
        int idBescheibung = cursor.getColumnIndex(BitumenDBHelper.Hersteller_beschreibung);


        String beschreibung = cursor.getString(idBescheibung);
        String name = cursor.getString(idName);
        long IdSorte = cursor.getLong(idSorte);
        long id = cursor.getLong(idIndex);

        Hersteller hersteller = new Hersteller(id, IdSorte, name, beschreibung);

        return hersteller;
    }

    public List<Hersteller> getAllHersteller() {
        List<Hersteller> list = new ArrayList<>();

        Cursor cursor = database.query(BitumenDBHelper.TABLE_Hersteller_LIST,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Hersteller hersteller;

        while(!cursor.isAfterLast()) {
            hersteller = cursorToHersteller(cursor);
            list.add(hersteller);
            Log.d(LOG_TAG, "ID: " + hersteller.getId() + ", Inhalt: " + hersteller.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return list;
    }

    public List<Hersteller> getAllHersteller(String name) {
        List<Hersteller> list = new ArrayList<>();

        Cursor cursor = database.query(BitumenDBHelper.TABLE_Hersteller_LIST,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Hersteller temp;

        while(!cursor.isAfterLast()) {
            temp = cursorToHersteller(cursor);
            if(temp.getName().equals(name))
                list.add(temp);
            cursor.moveToNext();
        }

        cursor.close();

        return list;
    }


}
