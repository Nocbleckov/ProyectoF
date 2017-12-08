package com.baytag.daniel.proyectof.objetos;

import android.database.Cursor;

import com.baytag.daniel.proyectof.apoyo.Utilidades;
import com.baytag.daniel.proyectof.contracts.AppContract;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Daniel on 26/08/2017.
 */


public class Usuario implements Serializable {

    public static final String KEY = "usuario";


    private int id;


    private String nombre;
    private String email;
    private String telefono;
    private String nombreUsuario;
    private boolean existe = false;

    /*public Usuario(int id, String nombre, String email, String telefono, String nombreUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.nombreUsuario = nombreUsuario;
        existe = true;
    }*/

    public Usuario(Cursor c) {
        try {
            this.id = (int) c.getLong(c.getColumnIndexOrThrow(AppContract.UsuarioEntity._ID));
            this.nombreUsuario = c.getString(c.getColumnIndexOrThrow(AppContract.UsuarioEntity.COLUMN_NAME_NOMBRE));
            this.nombre = c.getString(c.getColumnIndexOrThrow(AppContract.UsuarioEntity.COLUMN_NAME_NICK));
            this.email = c.getString(c.getColumnIndexOrThrow(AppContract.UsuarioEntity.COLUMN_NAME_EMAIL));
            this.telefono = c.getString(c.getColumnIndexOrThrow(AppContract.UsuarioEntity.COLUMN_NAME_TELEFONO));

            this.existe = true;
        } catch (Exception e) {
            e.printStackTrace();
            this.existe = false;
        }
    }

    public Usuario(String data) {
        try {
            HashMap respuesta = Utilidades.datosJson(data).get("usuario");
            this.id = Integer.parseInt((String) respuesta.get("idUsuario"));
            this.nombre = (String) respuesta.get("nomUsu");
            this.email = (String) respuesta.get("correoUsu");
            this.telefono = (String) respuesta.get("telUsu");
            existe = true;
        } catch (Exception e) {
            e.printStackTrace();
            existe = false;
        }
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public boolean existe() {
        return existe;
    }
}
