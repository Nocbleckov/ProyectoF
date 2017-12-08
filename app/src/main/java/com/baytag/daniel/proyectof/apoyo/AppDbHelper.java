package com.baytag.daniel.proyectof.apoyo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.baytag.daniel.proyectof.contracts.AppContract;

/**
 * Created by Daniel on 25/11/2017.
 */

public class AppDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AppContactos.db";

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AppContract.SQL_CREATE_USUARIOS);
        db.execSQL(AppContract.SQL_CREATE_CONTACTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(AppContract.SQL_DELETE_USUARIOS);
        db.execSQL(AppContract.SQL_DELETE_CONTACTOS);
        onCreate(db);
    }
}
