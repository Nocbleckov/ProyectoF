package com.baytag.daniel.proyectof.objetos;

import com.baytag.daniel.proyectof.apoyo.Utilidades;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Daniel on 26/08/2017.
 */

public class Usuario implements Serializable {

    private int id;
    private String nombre;
    private String email;
    private String telefono;
    private String nombreUsuario;
    private boolean existe = false;

    public Usuario(String data) {
        try {
            HashMap respuesta = Utilidades.datosJson(data).get("usuario");
            this.id = Integer.parseInt((String) respuesta.get("idUsuario"));
            this.nombre = (String) respuesta.get("nomUsu");
            this.email = (String) respuesta.get("correoUsu");
            this.telefono = (String) respuesta.get("telUsu");
            existe = true;
        }catch (Exception e){
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
