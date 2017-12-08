package com.baytag.daniel.proyectof;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baytag.daniel.proyectof.apoyo.AppDbHelper;
import com.baytag.daniel.proyectof.apoyo.Conexion;
import com.baytag.daniel.proyectof.apoyo.PreferenciasManager;
import com.baytag.daniel.proyectof.contracts.AppContract;
import com.baytag.daniel.proyectof.objetos.Usuario;

import java.io.Serializable;
import java.util.HashMap;

import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_NUEVO_USUARIO = 1;

    ImageView imgAcceso;
    EditText edTxNomUsu, edTxPass;
    FButton btnIngresar, btnNvUsu;
    PreferenciasManager prefManager;
    String nomUsu, pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edTxNomUsu = (EditText) findViewById(R.id.edTx_NomUsu_ActMain);
        edTxPass = (EditText) findViewById(R.id.edTx_Pass_ActMain);
        imgAcceso = (ImageView) findViewById(R.id.imVw_ImgUsu_ActMain);
        btnIngresar = (FButton) findViewById(R.id.btn_Ing_ActMain);
        btnNvUsu = (FButton) findViewById(R.id.btn_NvoUsu_ActMain);

        prefManager = new PreferenciasManager(getBaseContext());

        btnIngresar.setOnClickListener(ingresar());
        btnNvUsu.setOnClickListener(nvUsu());

    }

    View.OnClickListener ingresar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomUsu = edTxNomUsu.getText().toString();
                pass = edTxPass.getText().toString();

                if (!nomUsu.equalsIgnoreCase("") || !pass.equalsIgnoreCase("")) {
                    //new onBackLoginServer(btnIngresar).execute(nomUsu, pass);
                    new onBackLoginInternal(btnIngresar).execute(nomUsu, pass);

                } else {
                    Toast.makeText(getBaseContext(), "Los campos de usuario y contraseña no pueden estar vacíos", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    View.OnClickListener nvUsu() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), NuevoUsuarioActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivityForResult(i, REQUEST_NUEVO_USUARIO);
            }
        };
    }


    private class onBackLoginInternal extends AsyncTask<String, Boolean, Usuario> {
        FButton btnIngreso;
        AppDbHelper appDbHelper;
        SQLiteDatabase db;
        Usuario usuario;

        String[] projection = {
                AppContract.UsuarioEntity._ID,
                AppContract.UsuarioEntity.COLUMN_NAME_NICK,
                AppContract.UsuarioEntity.COLUMN_NAME_TELEFONO,
                AppContract.UsuarioEntity.COLUMN_NAME_EMAIL,
                AppContract.UsuarioEntity.COLUMN_NAME_NOMBRE
        };

        public onBackLoginInternal(FButton btnIngreso) {
            this.btnIngreso = btnIngreso;
            appDbHelper = new AppDbHelper(getBaseContext());
            db = appDbHelper.getReadableDatabase();

        }

        @Override
        protected void onPreExecute() {
            btnIngreso.setEnabled(false);
            super.onPreExecute();
        }

        @Override
        protected Usuario doInBackground(String... params) {
            try {
                String selection = AppContract.UsuarioEntity.COLUMN_NAME_NICK + "= ?"
                        + "AND " +
                        AppContract.UsuarioEntity.COLUMN_NAME_PASS + "= ?";
                String[] selectionArgs = {params[0], params[1]};
                Cursor cursor = db.query(
                        AppContract.UsuarioEntity.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                while (cursor.moveToNext()) {
                    usuario = new Usuario(cursor);
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return usuario;
        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            super.onPostExecute(usuario);
            btnIngreso.setEnabled(true);
            try {
                if (usuario.existe()) {
                    Intent i = new Intent(MainActivity.this, ContactosActivity.class);
                    i.putExtra(Usuario.KEY, (Serializable) usuario);
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class onBackLoginServer extends AsyncTask<String, Boolean, Usuario> {

        FButton btnIngreso;

        public onBackLoginServer(FButton btnIngreso) {
            this.btnIngreso = btnIngreso;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnIngreso.setEnabled(false);
        }

        @Override
        protected Usuario doInBackground(String... params) {
            Usuario usuario = null;
            String email = params[0];
            String pass = params[1];

            HashMap<String, String> parametros = new HashMap<>();
            parametros.put("numPeticion", "1");
            parametros.put("email", email);
            parametros.put("pass", pass);

            try {
                Conexion con =
                        new Conexion(prefManager.leerPreferencias(
                                PreferenciasManager.URL_WEBSERVICES
                                , "http://192.168.100.5/WSPF/Peticiones.php"));
                con.setParametros(parametros);
                con.executar(Conexion.metodoPeticion.POST);
                String respuesta = con.getRespuesta();
                String mnsj = con.getMensaje();
                usuario = new Usuario(respuesta);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return usuario;
        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            super.onPostExecute(usuario);
            btnIngreso.setEnabled(true);
            try {
                if (usuario.existe()) {
                    Intent i = new Intent(MainActivity.this, ContactosActivity.class);
                    i.putExtra(Usuario.KEY, (Serializable) usuario);
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
