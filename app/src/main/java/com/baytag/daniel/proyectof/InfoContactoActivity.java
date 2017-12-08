package com.baytag.daniel.proyectof;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.baytag.daniel.proyectof.apoyo.Conexion;
import com.baytag.daniel.proyectof.apoyo.PreferenciasManager;
import com.baytag.daniel.proyectof.apoyo.Utilidades;
import com.baytag.daniel.proyectof.controllers.ContactoController;
import com.baytag.daniel.proyectof.objetos.Contacto;
import com.baytag.daniel.proyectof.objetos.Usuario;

import java.util.ArrayList;

public class InfoContactoActivity extends AppCompatActivity implements View.OnClickListener {

    int requestCode = 0;

    Contacto contacto;
    EditText edTxNomCon, edTxPApellidoCon, edTxSApellidoCon, edTxTelefonoCon, edTxEmailCon;
    Spinner spCiudad;
    Button btnGuardar;
    Usuario usuario;
    PreferenciasManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_contacto);

        edTxNomCon = (EditText) findViewById(R.id.edTx_nomCon_infoContacto);
        edTxTelefonoCon = (EditText) findViewById(R.id.edTx_telefonoCon_infoContacto);
        edTxEmailCon = (EditText) findViewById(R.id.edTx_emailCon_infoContacto);
        edTxPApellidoCon = (EditText) findViewById(R.id.edTx_pApellidoCon_infoContacto);
        edTxSApellidoCon = (EditText) findViewById(R.id.edTx_sApellidoCon_infoContacto);

        spCiudad = (Spinner) findViewById(R.id.sp_ciudadCon_infoContacto);

        btnGuardar = (Button) findViewById(R.id.btn_cambiarCont_infoContacto);
        requestCode = getIntent().getIntExtra("requestCode", 0);
        usuario = (Usuario) getIntent().getExtras().get(Usuario.KEY);
        prefManager = new PreferenciasManager(this);

        if (requestCode == ContactosActivity.REQUEST_EDITAR_CONTACTO) {
            contacto = (Contacto) getIntent().getExtras().get(Contacto.KEY);
        }
        iniWg();

    }

    private void iniWg() {
        if (requestCode == ContactosActivity.REQUEST_EDITAR_CONTACTO) {
            edTxNomCon.setText(contacto.getNomCon());
            edTxPApellidoCon.setText(contacto.getpApellido());
            edTxSApellidoCon.setText(contacto.getsApellido());
            edTxTelefonoCon.setText(contacto.getTelCon());
            edTxEmailCon.setText(contacto.getCorreoCon());
            btnGuardar.setText("Editar Contacto");
            btnGuardar.setOnClickListener(this);
        } else if (requestCode == ContactosActivity.REQUEST_NUEVO_CONTACTO) {
            btnGuardar.setText("Guardar Contacto");
            btnGuardar.setOnClickListener(this);
        }
        new OnBackFillSpinner().execute();
    }

    public void syncInfoContacto() {
        if (requestCode == ContactosActivity.REQUEST_NUEVO_CONTACTO) {
            String nomCon = edTxNomCon.getText().toString();
            String pApellido = edTxPApellidoCon.getText().toString();
            String sApellido = edTxSApellidoCon.getText().toString();
            String telefonoCon = edTxTelefonoCon.getText().toString();
            String emailCon = edTxEmailCon.getText().toString();
            String ciudad = spCiudad.getSelectedItem().toString();
            String urlImg = "http://www.jrtstudio.com/sites/default/files/ico_android.png";
            contacto = new Contacto(nomCon, pApellido, sApellido, telefonoCon, emailCon, ciudad,urlImg);
        } else if (requestCode == ContactosActivity.REQUEST_EDITAR_CONTACTO) {
            contacto.setNomCon(edTxNomCon.getText().toString());
            contacto.setpApellido(edTxPApellidoCon.getText().toString());
            contacto.setsApellido(edTxSApellidoCon.getText().toString());
            contacto.setTelCon(edTxTelefonoCon.getText().toString());
            contacto.setCorreoCon(edTxEmailCon.getText().toString());
            contacto.setCiudadCon(spCiudad.getSelectedItem().toString());
        }
    }

    @Override
    public void onClick(View v) {
        ContactoController cc = null;
        syncInfoContacto();
        if (requestCode == ContactosActivity.REQUEST_EDITAR_CONTACTO) {
            cc = new ContactoController(contacto, usuario, this, getBaseContext(), ContactoController.EDITAR_CONTACTO);
        } else if (requestCode == ContactosActivity.REQUEST_NUEVO_CONTACTO) {
            cc = new ContactoController(contacto, usuario, this, getBaseContext(), ContactoController.NUEVO_CONTACTO);
        }
        cc.disableItems(btnGuardar);
        cc.actionContacto();

    }

    private class OnBackFillSpinner extends AsyncTask<Void, Void, ArrayList<String>> {

        ArrayList<String> some;

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            try {
                String region = prefManager.leerPreferencias(PreferenciasManager.REGION, "americas");
                Conexion conexion = new Conexion("https://restcountries.eu/rest/v2/region/" + region);
                conexion.executar(Conexion.metodoPeticion.GET);
                String respuesta = conexion.getRespuesta();
                String msj = conexion.getRespuesta();
                some = Utilidades.stringToCities(respuesta);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return some;
        }

        @Override
        protected void onPostExecute(ArrayList<String> objects) {
            super.onPostExecute(objects);
            if (objects != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getBaseContext(),
                        android.R.layout.simple_spinner_item,
                        objects);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCiudad.setAdapter(adapter);
                if (contacto != null) {
                    spCiudad.setSelection(adapter.getPosition(contacto.getCiudadCon()));
                }
            }
        }
    }

}
