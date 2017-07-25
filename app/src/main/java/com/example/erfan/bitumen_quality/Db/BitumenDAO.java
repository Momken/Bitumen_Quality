package com.example.erfan.bitumen_quality.Db;

/**
 * Created by Erfan on 20.07.2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;



public class BitumenDAO {

    private static final String LOG_TAG = BitumenDAO.class.getSimpleName();

    private SQLiteDatabase database;
    private BitumenDBHelper dbHelper;

    private String[] columns = {
            BitumenDBHelper.COLUMN_ID,
            BitumenDBHelper.COLUMN_Name,
            BitumenDBHelper.COLUMN_Note
    };


    public BitumenDAO(Context context) {
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

    public Bitumen createBitumen(String name, int note) {
        ContentValues values = new ContentValues();

        values.put(BitumenDBHelper.COLUMN_Name, name);
        values.put(BitumenDBHelper.COLUMN_Note, note);
        long insertId = database.insert(BitumenDBHelper.TABLE_Bitumen_LIST, null, values);

        Cursor cursor = database.query(BitumenDBHelper.TABLE_Bitumen_LIST,
                columns, BitumenDBHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Bitumen Bitumen = cursorToBitumen(cursor);
        cursor.close();

        return Bitumen;
    }

    private Bitumen cursorToBitumen(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(BitumenDBHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(BitumenDBHelper.COLUMN_Name);
        int idNote = cursor.getColumnIndex(BitumenDBHelper.COLUMN_Note);

        String name = cursor.getString(idName);
        String note = cursor.getString(idNote);
        long id = cursor.getLong(idIndex);

        Bitumen Bitumen = new Bitumen(id, name, note);

        return Bitumen;
    }

    public List<Bitumen> getAllBitumens() {
        List<Bitumen> BitumenList = new ArrayList<>();

        Cursor cursor = database.query(BitumenDBHelper.TABLE_Bitumen_LIST,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Bitumen Bitumen;

        while(!cursor.isAfterLast()) {
            Bitumen = cursorToBitumen(cursor);
            BitumenList.add(Bitumen);
            Log.d(LOG_TAG, "ID: " + Bitumen.getId() + ", Inhalt: " + Bitumen.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return BitumenList;
    }

}