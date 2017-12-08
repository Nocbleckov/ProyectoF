package com.baytag.daniel.proyectof.controllers;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.baytag.daniel.proyectof.apoyo.AppDbHelper;
import com.baytag.daniel.proyectof.apoyo.Conexion;
import com.baytag.daniel.proyectof.apoyo.PreferenciasManager;
import com.baytag.daniel.proyectof.contracts.AppContract;
import com.baytag.daniel.proyectof.objetos.Contacto;
import com.baytag.daniel.proyectof.objetos.Usuario;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by Daniel on 04/11/2017.
 */

public class ContactoController {


    public static final int NUEVO_CONTACTO = 3;
    public static final int EDITAR_CONTACTO = 4;

    private int request;
    private Contacto contacto;
    private Button actButton;
    private PreferenciasManager prefManager;
    private Context context;
    private Usuario usuario;
    private Activity activity;

    public ContactoController(Contacto contacto, Usuario usuario, Activity activity, Context context, int request) {
        this.context = context;
        this.contacto = contacto;
        this.activity = activity;
        this.usuario = usuario;
        this.prefManager = new PreferenciasManager(context);
        this.request = request;
    }

    public void disableItems(Button actButton) {
        this.actButton = actButton;
    }

    public void actionContacto() {
        //new OnBackActionContacto().execute(contacto);
        new OnBackActionContactoInternal().execute(contacto);
    }

    private class OnBackActionContactoInternal extends AsyncTask<Contacto, Void, Boolean> {

        AppDbHelper appDbHelper = new AppDbHelper(context);
        SQLiteDatabase db = appDbHelper.getWritableDatabase();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actButton.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Contacto... contactos) {
            Contacto contacto = contactos[0];

            ContentValues values = new ContentValues();
            values.put(AppContract.ContactoEntity.COLUMN_NAME_NOM_CON, contacto.getNomCon());
            values.put(AppContract.ContactoEntity.COLUMN_NAME_P_APELLIDO, contacto.getpApellido());
            values.put(AppContract.ContactoEntity.COLUMN_NAME_S_APELLIDO, contacto.getsApellido());
            values.put(AppContract.ContactoEntity.COLUMN_NAME_TEL_CON, contacto.getTelCon());
            values.put(AppContract.ContactoEntity.COLUMN_NAME_CORREO_CON, contacto.getCorreoCon());
            values.put(AppContract.ContactoEntity.COLUMN_NAME_CIUDAD_CON, contacto.getCiudadCon());
            values.put(AppContract.ContactoEntity.COLUMN_NAME_ID_USUARIO, usuario.getId());

            if (request == NUEVO_CONTACTO) {
                Long i = db.insert(AppContract.ContactoEntity.TABLE_NAME, null, values);
            } else if (request == EDITAR_CONTACTO) {
                String selection = AppContract.ContactoEntity.COLUMN_NAME_TEL_CON + " = ?";
                String[] selectionArgs = {contacto.getTelCon()};
                int count = db.update(
                        AppContract.ContactoEntity.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                Log.wtf("num", "" + count);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean respuesta) {
            super.onPostExecute(respuesta);
            if (respuesta == true) {
                actButton.setEnabled(true);
                Intent resultIntent = new Intent();
                activity.setResult(Activity.RESULT_OK, resultIntent);

            } else {
                Toast.makeText(context, "La accion no se pudo realizar", Toast.LENGTH_SHORT).show();
                actButton.setEnabled(true);
                Intent resultIntent = new Intent();
                activity.setResult(Activity.RESULT_CANCELED, resultIntent);
            }
            activity.finish();
        }

    }


    private class OnBackActionContactoServer extends AsyncTask<Contacto, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actButton.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Contacto... params) {
            Contacto contacto = params[0];
            Gson gson = new Gson();
            String contactoJSON = gson.toJson(contacto);


            HashMap<String, String> parametros = new HashMap<>();

            parametros.put("numPeticion", "" + request);
            parametros.put("contacto", contactoJSON);
            parametros.put("idUsu", "" + usuario.getId());

            try {
                Conexion conexion = new Conexion(prefManager.leerPreferencias(
                        PreferenciasManager.URL_WEBSERVICES
                        , "http://192.168.100.5/WSPF/Peticiones.php"));
                conexion.setParametros(parametros);
                conexion.executar(Conexion.metodoPeticion.POST);
                String respuesta = conexion.getRespuesta();
                String mnsj = conexion.getMensaje();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean respuesta) {
            super.onPostExecute(respuesta);
            if (respuesta == true) {
                actButton.setEnabled(true);
                Intent resultIntent = new Intent();
                activity.setResult(Activity.RESULT_OK, resultIntent);

            } else {
                Toast.makeText(context, "La accion no se pudo realizar", Toast.LENGTH_SHORT).show();
                actButton.setEnabled(true);
                Intent resultIntent = new Intent();
                activity.setResult(Activity.RESULT_CANCELED, resultIntent);
            }
            activity.finish();
        }
    }

}
