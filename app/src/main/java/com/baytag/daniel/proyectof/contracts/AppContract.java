package com.baytag.daniel.proyectof.contracts;

import android.provider.BaseColumns;

/**
 * Created by Daniel on 25/11/2017.
 */

public final class AppContract {
    private AppContract() {
    }

    public static final String SQL_CREATE_USUARIOS =
            "CREATE TABLE " + UsuarioEntity.TABLE_NAME + " (" +
                    UsuarioEntity._ID + " INTEGER PRIMARY KEY," +
                    UsuarioEntity.COLUMN_NAME_NICK + " TEXT," +
                    UsuarioEntity.COLUMN_NAME_EMAIL + " TEXT," +
                    UsuarioEntity.COLUMN_NAME_TELEFONO + " TEXT," +
                    UsuarioEntity.COLUMN_NAME_NOMBRE + " TEXT," +
                    UsuarioEntity.COLUMN_NAME_PASS + " TEXT)";

    public static final String SQL_DELETE_USUARIOS =
            "DROP TABLE IF EXIST " + UsuarioEntity.TABLE_NAME;

    public static final String SQL_CREATE_CONTACTOS =
            "CREATE TABLE " + ContactoEntity.TABLE_NAME + " (" +
                    ContactoEntity._ID + "INTEGER PRIMARY KEY ," +
                    ContactoEntity.COLUMN_NAME_NOM_CON + " TEXT," +
                    ContactoEntity.COLUMN_NAME_P_APELLIDO + " TEXT," +
                    ContactoEntity.COLUMN_NAME_S_APELLIDO + " TEXT," +
                    ContactoEntity.COLUMN_NAME_TEL_CON + " TEXT," +
                    ContactoEntity.COLUMN_NAME_CORREO_CON + " TEXT," +
                    ContactoEntity.COLUMN_NAME_CIUDAD_CON + " TEXT," +
                    ContactoEntity.COLUMN_NAME_URL_IMG_CON + " TEXT,"+
                    ContactoEntity.COLUMN_NAME_ID_USUARIO + " TEXT," +
                    "FOREIGN KEY ( " + ContactoEntity.COLUMN_NAME_ID_USUARIO + ") " +
                    "REFERENCES " + UsuarioEntity.TABLE_NAME + "( " + BaseColumns._ID + ")"
                    + ");";

    public static final String SQL_DELETE_CONTACTOS =
            "DROP TABLE IF EXIST " + ContactoEntity.TABLE_NAME;

    public static class UsuarioEntity implements BaseColumns {
        public static final String TABLE_NAME = "usuario";
        public static final String COLUMN_NAME_NICK = "nick";
        public static final String COLUMN_NAME_PASS = "pass";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_TELEFONO = "telefono";
        public static final String COLUMN_NAME_NOMBRE = "nombreUsuario";
    }

    public static class ContactoEntity implements BaseColumns {
        public static final String TABLE_NAME = "contacto";
        public static final String COLUMN_NAME_NOM_CON = "nomCon",
                COLUMN_NAME_P_APELLIDO = "pApellido",
                COLUMN_NAME_S_APELLIDO = "sApellido",
                COLUMN_NAME_TEL_CON = "telCon",
                COLUMN_NAME_CORREO_CON = "correoCon",
                COLUMN_NAME_CIUDAD_CON = "ciudadCon",
                COLUMN_NAME_URL_IMG_CON = "urlImg",
                COLUMN_NAME_ID_USUARIO = "id_usu";
    }

}
