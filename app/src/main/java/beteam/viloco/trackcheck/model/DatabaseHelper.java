package beteam.viloco.trackcheck.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import beteam.viloco.trackcheck.dto.Constantes;

public class DatabaseHelper extends SQLiteOpenHelper {

    //TODO No borrar
    public static final String Autenticado = "Autenticado";

    //TODO No borrar
    public static final String CREATE_TABLE_AUTENTICADO = "CREATE TABLE IF NOT EXISTS Autenticado (" +
            "Id INTEGER PRIMARY KEY," +
            "IdUserType INT NOT NULL," +
            "UserName TEXT NOT NULL," +
            "FirstName TEXT NOT NULL," +
            "LastName TEXT NOT NULL," +
            "MiddleName TEXT NOT NULL," +
            "Password TEXT NOT NULL)";

    public static final String BusinessType = "BusinessType";

    public static final String CREATE_TABLE_BUSINESSTYPE = "CREATE TABLE IF NOT EXISTS BusinessType (" +
            "Id INTEGER PRIMARY KEY," + //TODO Recordar siempre quitar AUTOINCREMENT por ser catalogo
            "Name TEXT NOT NULL," +
            "Description TEXT NOT NULL)";

    public static final String Data = "Data";

    public static final String CREATE_TABLE_DATA = "CREATE TABLE IF NOT EXISTS Data (" +
            "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "IdUser INT NOT NULL," +
            "IdTerritory INT NOT NULL," +
            "IdZone INT NOT NULL," +
            "IdBusinessType INT NOT NULL," +
            "SerialNumber TEXT NOT NULL," +
            "BusinessName TEXT NOT NULL," +
            "Latitude REAL NOT NULL," +
            "Longitude REAL NOT NULL," +
            "Street TEXT NOT NULL," +
            "Number TEXT NOT NULL," +
            "Colony TEXT NOT NULL," +
            "DelegationTown TEXT NOT NULL," +
            "City TEXT NOT NULL," +
            "State TEXT NOT NULL," +
            "PostalCode TEXT NOT NULL," +
            "Poster INT NOT NULL," +
            "ThermoRoshpack60x40 INT NOT NULL," +
            "ThermoTi2260x40 INT NOT NULL," +
            "ElectroGota INT NOT NULL," +
            "ElectroImagen INT NOT NULL," +
            "Banderola INT NOT NULL," +
            "Calcomanis INT NOT NULL," +
            "CaballeteCambioAceite INT NOT NULL," +
            "CaballeteVentaAceite INT NOT NULL," +
            "Lona2x1Roshpack INT NOT NULL," +
            "Lona2x1ImagenMecanico INT NOT NULL," +
            "Date INT NOT NULL," +
            "IdServer INT NULL)"; //TODO Recordar no borrar

    public static final String DataPhoto = "DataPhoto";

    public static final String CREATE_TABLE_DATAPHOTO = "CREATE TABLE IF NOT EXISTS DataPhoto (" +
            "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "IdData INT NOT NULL," +
            "Photo TEXT NOT NULL," +
            "IdServer INT NULL)"; //TODO Recordar no borrar

    public static final String LogError = "LogError";

    public static final String CREATE_TABLE_LOGERROR = "CREATE TABLE IF NOT EXISTS LogError (" +
            "IDLogError INTEGER PRIMARY KEY AUTOINCREMENT," +
            "Modulo TEXT NOT NULL," +
            "ErrorResumido TEXT NOT NULL," +
            "ErrorInterno TEXT NOT NULL," +
            "Fecha INT NOT NULL)";

    public static final String Territory = "Territory";

    public static final String CREATE_TABLE_TERRITORY = "CREATE TABLE IF NOT EXISTS Territory (" +
            "Id INTEGER PRIMARY KEY," + //TODO Recordar siempre quitar AUTOINCREMENT por ser catalogo
            "Name TEXT NOT NULL," +
            "Description TEXT NOT NULL)";

    public static final String User = "User";

    public static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS User (" +
            "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "IdUserType INT NOT NULL," +
            "UserName TEXT NOT NULL," +
            "FirstName TEXT NOT NULL," +
            "LastName TEXT NOT NULL," +
            "MiddleName TEXT NOT NULL," +
            "Password TEXT NOT NULL)";

    public static final String UserType = "UserType";

    public static final String CREATE_TABLE_USERTYPE = "CREATE TABLE IF NOT EXISTS UserType (" +
            "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "Name TEXT NOT NULL," +
            "Description TEXT NOT NULL)";

    public static final String Zone = "Zone";

    public static final String CREATE_TABLE_ZONE = "CREATE TABLE IF NOT EXISTS Zone (" +
            "Id INTEGER PRIMARY KEY," + //TODO Recordar siempre quitar AUTOINCREMENT por ser catalogo
            "Name TEXT NOT NULL," +
            "Description TEXT NOT NULL)";

    public DatabaseHelper(Context context) {
        super(context, Constantes.DATABASE_NAME, null, Constantes.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_AUTENTICADO);
        db.execSQL(CREATE_TABLE_BUSINESSTYPE);
        db.execSQL(CREATE_TABLE_DATA);
        db.execSQL(CREATE_TABLE_DATAPHOTO);
        db.execSQL(CREATE_TABLE_LOGERROR);
        db.execSQL(CREATE_TABLE_TERRITORY);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_USERTYPE);
        db.execSQL(CREATE_TABLE_ZONE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        BackUpDB("AUTENTICADO", db);
        BackUpDB("BUSINESSTYPE", db);
        BackUpDB("DATA", db);
        BackUpDB("DATAPHOTO", db);
        BackUpDB("LOGERROR", db);
        BackUpDB("TERRITORY", db);
        BackUpDB("USER", db);
        BackUpDB("USERTYPE", db);
        BackUpDB("ZONE", db);
        onCreate(db);
    }

    private void BackUpDB(String TableName, SQLiteDatabase db) {
        switch (TableName) {
            case "BUSINESSTYPE":
                break;
            case "DATA":
                break;
            case "DATAPHOTO":
                break;
            case "LOGERROR":
                break;
            case "TERRITORY":
                break;
            case "USER":
                break;
            case "USERTYPE":
                break;
            case "ZONE":
                break;
            default:
                break;
        }
    }
}
