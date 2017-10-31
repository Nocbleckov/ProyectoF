package com.baytag.daniel.proyectof;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baytag.daniel.proyectof.apoyo.Conexion;
import com.baytag.daniel.proyectof.objetos.Usuario;

import java.io.Serializable;
import java.util.HashMap;

import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity {

    ImageView imgAcceso;
    EditText edTxNomUsu, edTxPass;
    FButton btnIngresar;

    String nomUsu, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edTxNomUsu = (EditText) findViewById(R.id.edTx_NomUsu_ActMain);
        edTxPass = (EditText) findViewById(R.id.edTx_Pass_ActMain);
        imgAcceso = (ImageView) findViewById(R.id.imVw_ImgUsu_ActMain);
        btnIngresar = (FButton) findViewById(R.id.btn_Ing_ActMain);

        btnIngresar.setOnClickListener(ingresar());

    }

    View.OnClickListener ingresar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomUsu = edTxNomUsu.getText().toString();
                pass = edTxPass.getText().toString();

                if (!nomUsu.equalsIgnoreCase("") || !pass.equalsIgnoreCase("")) {
                    new onBackLogin(btnIngresar).execute(nomUsu, pass);
                } else {
                    Toast.makeText(getBaseContext(), "Los campos de usuario y contraseña no pueden estar vacíos", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private class onBackLogin extends AsyncTask<String, Boolean, Usuario> {

        FButton btnIngreso;

        public onBackLogin(FButton btnIngreso) {
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
                Conexion con = new Conexion("http://192.168.100.5/WSPF/Peticiones.php");
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
                    i.putExtra("usuario", (Serializable) usuario);
                    startActivity(i);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


}
