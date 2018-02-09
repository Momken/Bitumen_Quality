package com.example.erfan.bitumen_quality.DB;

/**
 * Created by Erfan on 20.07.2017.
 */



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BitumenDBHelper extends SQLiteOpenHelper{

    private static final String LOG_TAG = BitumenDBHelper.class.getSimpleName();

    /*Database SQL Table Strings*/
    public static final String DB_NAME = "BitumenQuality_D.db";
    public static final int DB_VERSION = 1;

    //Bitumen
    public static final String TABLE_Bitumen_LIST = "Bitumen_list";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_Name = "name";
    public static final String COLUMN_Note = "note";

    public static final String SQL_CREATE_Bitumen = "CREATE TABLE " + TABLE_Bitumen_LIST +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_Name + " TEXT NOT NULL, " +
                    COLUMN_Note + " INTEGER NOT NULL);";



    //Sorte
    public static final String TABLE_Sorte_LIST = "Sorte_list";
    public static final String Sorte_ID = "_id";
    public static final String Sorte_bezeichnung = "bezeichnung";
    public static final String Sorte_beschreibung = "beschreibung";
    public static final String Sorte_q1MinGreenQ = "Q1MinGreenQ";
    public static final String Sorte_q1Min = "Q1Min";
    public static final String Sorte_q2MinGreenQ = "Q2MinGreenQ";
    public static final String Sorte_q2Min = "Q2Min";


    public static final String SQL_CREATE_Sorte = "CREATE TABLE " + TABLE_Sorte_LIST +
            "(" +
            Sorte_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Sorte_bezeichnung + " TEXT NOT NULL, " +
            Sorte_beschreibung + " TEXT NOT NULL, " +
            Sorte_q1MinGreenQ + " TEXT NOT NULL, " +
            Sorte_q1Min + " TEXT NOT NULL, " +
            Sorte_q2MinGreenQ + " TEXT NOT NULL, " +
            Sorte_q2Min + " TEXT NOT NULL " +

            ");";


    //Hersteller
    public static final String TABLE_Hersteller_LIST = "Hersteller_list";

    public static final String Hersteller_ID = "_id";
    public static final String H_Sorten_ID = "_idSorte";
    public static final String Hersteller_Name = "name";
    public static final String Hersteller_beschreibung = "beschreibung";

    public static final String SQL_CREATE_Hersteller = "CREATE TABLE " + TABLE_Hersteller_LIST +
            "(" +
            Hersteller_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            H_Sorten_ID + " TEXT NOT NULL, " +
            Hersteller_Name + " TEXT NOT NULL, " +
            Hersteller_beschreibung + " TEXT NOT NULL " +
            ");";


    //Lieferung
    public static final String TABLE_Lieferung_LIST = "Lieferung_list";

    public static final String Lieferung_ID = "_id";
    public static final String L_Hersteller_ID = "_idHersteller";
    public static final String Lieferung_Datum = "datum";
    public static final String Lieferung_Bezeichnung = "bezeichnung";
    public static final String Lieferung_Beschreibung = "beschreibung";

    public static final String SQL_CREATE_Lieferung = "CREATE TABLE " + TABLE_Lieferung_LIST +
            "(" +
            Lieferung_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            L_Hersteller_ID + " TEXT NOT NULL, " +
            Lieferung_Datum + " TEXT NOT NULL, " +
            Lieferung_Bezeichnung + " TEXT NOT NULL, " +
            Lieferung_Beschreibung + " TEXT NOT NULL " +
            ");";


    //Probe
    public static final String TABLE_Probe_LIST = "Probe_list";
    public static final String Probe_ID = "_id";
    public static final String P_Lieferung_ID = "_idLieferung";
    public static final String Probe_Datum = "datum";
    public static final String Probe_Bezeichnung = "bezeichnung";
    public static final String Probe_Beschreibung = "beschreibung";


    public static final String SQL_CREATE_Probe = "CREATE TABLE " + TABLE_Probe_LIST +
            "(" +
            Probe_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            P_Lieferung_ID + " TEXT NOT NULL, " +
            Probe_Datum + " TEXT NOT NULL, " +
            Probe_Bezeichnung + " TEXT NOT NULL, " +
            Probe_Beschreibung + " TEXT NOT NULL " +
            ");";


    //Alterungszustand

    public static final String TABLE_Alterungszustand_LIST = "Alterungszustand_list";
    public static final String Alterungszustand_ID = "_id";
    public static final String A_Lieferung_ID = "_idLieferung";
    public static final String Alterungszustand_Datum = "datum";
    public static final String Alterungszustand_Bezeichnung = "bezeichnung";
    public static final String Alterungszustand_Messungsfaktoren = "messungsfaktoren";
    public static final String Alterungszustand_messung = "beschreibung";


    public static final String SQL_CREATE_Alterungszustand = "CREATE TABLE " + TABLE_Alterungszustand_LIST +
            "(" +
            Alterungszustand_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            A_Lieferung_ID + " TEXT NOT NULL, " +
            Alterungszustand_Datum + " TEXT NOT NULL, " +
            Alterungszustand_Bezeichnung + " TEXT NOT NULL, " +
            Alterungszustand_Messungsfaktoren + " TEXT NOT NULL, " +
            Alterungszustand_messung + " TEXT NOT NULL " +
            ");";



    public BitumenDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: Create" + " angelegt.");
            db.execSQL(SQL_CREATE_Bitumen);
            db.execSQL(SQL_CREATE_Probe);
            db.execSQL(SQL_CREATE_Sorte);
            db.execSQL(SQL_CREATE_Hersteller);
            db.execSQL(SQL_CREATE_Lieferung);
            db.execSQL(SQL_CREATE_Alterungszustand);

        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
