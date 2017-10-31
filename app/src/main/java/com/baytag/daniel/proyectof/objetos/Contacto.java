package com.baytag.daniel.proyectof.objetos;

import java.util.HashMap;

/**
 * Created by Daniel on 26/08/2017.
 */

public class Contacto {

    private String id, nomCon, pApellido, sApellido, telCon, correoCon, ciudadCon;
    private boolean existe = false;

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
}
