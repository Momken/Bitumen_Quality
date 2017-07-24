package com.example.erfan.bitumen_quality.Db;

/**
 * Created by Erfan on 20.07.2017.
 */



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BitumenDBHelper extends SQLiteOpenHelper{

    private static final String LOG_TAG = BitumenDBHelper.class.getSimpleName();
    public static final String DB_NAME = "BitumenQuality_list.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_Bitumen_LIST = "Bitumen_list";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_Name = "name";
    public static final String COLUMN_Note = "note";

    public static final String SQL_CREATE = "CREATE TABLE " + TABLE_Bitumen_LIST +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_Name + " TEXT NOT NULL, " +
                    COLUMN_Note + " INTEGER NOT NULL);";


    public BitumenDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
