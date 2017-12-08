package com.baytag.daniel.proyectof.apoyo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Daniel on 29/10/2017.
 */

public class PreferenciasManager {

    private static final String FILE_PREFERENCIAS = "com.baytaG.contactos.PREFERENCIAS_FILE_KEY";

    public static final String URL_WEBSERVICES = "URL_WEBSERVICES";
    public static final String REGION = "REGION";
    public static final String EMAIL_USU = "EMAIL_USU";
    public static final String TELEFONO_USU = "TELEFONO_USU";

    private Context context;
    private SharedPreferences sharedPreferences;

    public PreferenciasManager(Context context) {
        sharedPreferences = context.getSharedPreferences(FILE_PREFERENCIAS, context.MODE_PRIVATE);
    }

    public void escribriPreferencia(String stringKey, String info) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(stringKey, info);
        editor.commit();
    }

    public String leerPreferencias(String stringKey, String def) {
        return sharedPreferences.getString(stringKey, def);
    }

    public Context getContext() {
        return context;
    }
}
