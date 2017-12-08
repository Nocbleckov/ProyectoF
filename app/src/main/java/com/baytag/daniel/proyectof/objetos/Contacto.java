package com.baytag.daniel.proyectof.objetos;

import android.database.Cursor;

import com.baytag.daniel.proyectof.contracts.AppContract;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Daniel on 26/08/2017.
 */

public class Contacto implements Serializable {

    public static final String KEY = "contacto";
    private String id, nomCon, pApellido, sApellido, telCon, correoCon, ciudadCon,urlImg;
    private boolean existe = false;

    public Contacto(String nomCon, String pApellido, String sApellido, String telCon, String correoCon, String ciudadCon, String urlImg) {
        this.nomCon = nomCon;
        this.pApellido = pApellido;
        this.sApellido = sApellido;
        this.telCon = telCon;
        this.correoCon = correoCon;
        this.ciudadCon = ciudadCon;
        existe = true;
    }

    public Contacto(Cursor c) {
        try {
            this.nomCon = c.getString(c.getColumnIndexOrThrow(AppContract.ContactoEntity.COLUMN_NAME_NOM_CON));
            this.pApellido = c.getString(c.getColumnIndexOrThrow(AppContract.ContactoEntity.COLUMN_NAME_P_APELLIDO));
            this.sApellido = c.getString(c.getColumnIndexOrThrow(AppContract.ContactoEntity.COLUMN_NAME_S_APELLIDO));
            this.telCon = c.getString(c.getColumnIndexOrThrow(AppContract.ContactoEntity.COLUMN_NAME_TEL_CON));
            this.correoCon = c.getString(c.getColumnIndexOrThrow(AppContract.ContactoEntity.COLUMN_NAME_CORREO_CON));
            this.ciudadCon = c.getString(c.getColumnIndexOrThrow(AppContract.ContactoEntity.COLUMN_NAME_CIUDAD_CON));
            this.urlImg = c.getString(c.getColumnIndexOrThrow(AppContract.ContactoEntity.COLUMN_NAME_URL_IMG_CON));
            existe = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Contacto(HashMap contacto) {

        try {
            this.id = (String) contacto.get("idContacto");
            this.nomCon = (String) contacto.get("nomCon");
            this.pApellido = (String) contacto.get("pApellido");
            this.sApellido = (String) contacto.get("sApellido");
            this.telCon = (String) contacto.get("telCon");
            this.correoCon = (String) contacto.get("correoCon");
            this.ciudadCon = (String) contacto.get("ciudadCon");
            this.existe = true;
        } catch (Exception e) {
            e.printStackTrace();
            this.existe = false;
        }
    }

    public String getId() {
        return id;
    }

    public String getNomCon() {
        return nomCon;
    }

    public String getpApellido() {
        return pApellido;
    }

    public String getsApellido() {
        return sApellido;
    }

    public String getTelCon() {
        return telCon;
    }

    public String getCorreoCon() {
        return correoCon;
    }

    public String getCiudadCon() {
        return ciudadCon;
    }

    public boolean existe() {
        return existe;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNomCon(String nomCon) {
        this.nomCon = nomCon;
    }

    public void setpApellido(String pApellido) {
        this.pApellido = pApellido;
    }

    public void setsApellido(String sApellido) {
        this.sApellido = sApellido;
    }

    public void setTelCon(String telCon) {
        this.telCon = telCon;
    }

    public void setCorreoCon(String correoCon) {
        this.correoCon = correoCon;
    }

    public void setCiudadCon(String ciudadCon) {
        this.ciudadCon = ciudadCon;
    }

    public void setExiste(boolean existe) {
        this.existe = existe;
    }
}
