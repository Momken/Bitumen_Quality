package com.example.erfan.bitumen_quality.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.erfan.bitumen_quality.DB.Alterungszustand;
import com.example.erfan.bitumen_quality.DB.BitumenDBHelper;

import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erfan on 05.11.2017.
 */

public class AlterungszustandDAO {

    private static final String LOG_TAG = BitumenDAO.class.getSimpleName();

    private SQLiteDatabase database;
    private BitumenDBHelper dbHelper;

    private String[] columns = {
            BitumenDBHelper.Alterungszustand_ID,
            BitumenDBHelper.A_Lieferung_ID,
            BitumenDBHelper.Alterungszustand_Datum,
            BitumenDBHelper.Alterungszustand_Bezeichnung,
            BitumenDBHelper.Alterungszustand_Messungsfaktoren,
            BitumenDBHelper.Alterungszustand_messung

    };


    public AlterungszustandDAO(Context context) {
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

    public Alterungszustand createAlterungszustand
            (int idLieferung, Date datum, String bezeichnung, String messungsfaktoren, String messung) {
        ContentValues values = new ContentValues();

        values.put(BitumenDBHelper.A_Lieferung_ID, idLieferung);
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

        values.put(BitumenDBHelper.Alterungszustand_Datum, df.format(datum));
        values.put(BitumenDBHelper.Alterungszustand_Bezeichnung, bezeichnung);
        values.put(BitumenDBHelper.Alterungszustand_Messungsfaktoren, messungsfaktoren);
        values.put(BitumenDBHelper.Alterungszustand_messung, messung);


        long insertId = database.insert(BitumenDBHelper.TABLE_Alterungszustand_LIST, null, values);


        Cursor cursor = database.query(BitumenDBHelper.TABLE_Alterungszustand_LIST,
                columns, BitumenDBHelper.Alterungszustand_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Alterungszustand alterung = cursorToAlterungszustand(cursor);
        cursor.close();

        return alterung;
    }

    private Alterungszustand cursorToAlterungszustand(Cursor cursor) {


        int idIndex = cursor.getColumnIndex(BitumenDBHelper.Alterungszustand_ID);
        int idLId = cursor.getColumnIndex(BitumenDBHelper.A_Lieferung_ID);
        int idDatum = cursor.getColumnIndex(BitumenDBHelper.Alterungszustand_Datum);
        int idBezeichnung = cursor.getColumnIndex(BitumenDBHelper.Alterungszustand_Bezeichnung);
        int idMessungsFakt = cursor.getColumnIndex(BitumenDBHelper.Alterungszustand_Messungsfaktoren);
        int idMessung = cursor.getColumnIndex(BitumenDBHelper.Alterungszustand_messung);


        String messung = cursor.getString(idMessung);
        String messungsFakt = cursor.getString(idMessungsFakt);
        String bezeichnung = cursor.getString(idBezeichnung);

        //Todo check for the seystem

        String datum=cursor.getString(idDatum);

        long lId = cursor.getLong(idLId);
        long id = cursor.getLong(idIndex);

        Alterungszustand alterung = new Alterungszustand(id, lId, datum, bezeichnung, messungsFakt, messung);
        return alterung;



    }

    public List<Alterungszustand> getAllAlterungzustand() {
        List<Alterungszustand> list = new ArrayList<>();

        Cursor cursor = database.query(BitumenDBHelper.TABLE_Alterungszustand_LIST,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Alterungszustand alterung;

        while(!cursor.isAfterLast()) {
            alterung = cursorToAlterungszustand(cursor);
            list.add(alterung);
            cursor.moveToNext();
        }

        cursor.close();

        return list;
    }

}
