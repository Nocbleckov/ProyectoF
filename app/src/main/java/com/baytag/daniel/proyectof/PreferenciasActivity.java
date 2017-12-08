package com.baytag.daniel.proyectof;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.baytag.daniel.proyectof.apoyo.PreferenciasManager;
import com.baytag.daniel.proyectof.objetos.Usuario;

public class PreferenciasActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edTxUrlWS, edTxEmailUsu, edTxTelefonoUsu;
    RadioButton rdoAfrica, rdoAmerica, rdoAsia, rdoEuropa, rdoOceania;
    ImageButton imgBtnGuardar;
    PreferenciasManager prefManager;
    Usuario usuario;

    private String region = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);
        this.usuario = (Usuario) getIntent().getExtras().get("usuario");
        prefManager = new PreferenciasManager(getBaseContext());

        iniWdg();
        iniEdTxs();
        iniRdoGroup();
        iniImgBtn();

    }

    private void iniWdg() {
        edTxUrlWS = (EditText) findViewById(R.id.edTx_urlWS_preferencias);
        edTxEmailUsu = (EditText) findViewById(R.id.edTx_emailUsu_preferencias);
        edTxTelefonoUsu = (EditText) findViewById(R.id.edTx_telefonoUsu_preferencias);

        rdoAfrica = (RadioButton) findViewById(R.id.rdo_africa_preferencias);
        rdoAmerica = (RadioButton) findViewById(R.id.rdo_america_preferencias);
        rdoAsia = (RadioButton) findViewById(R.id.rdo_asia_preferencias);
        rdoEuropa = (RadioButton) findViewById(R.id.rdo_europa_preferencias);
        rdoOceania = (RadioButton) findViewById(R.id.rdo_oceania_preferencias);

        imgBtnGuardar = (ImageButton) findViewById(R.id.imgBtn_guardar_preferencias);

        rdoAfrica.setOnClickListener(this);
        rdoAmerica.setOnClickListener(this);
        rdoAsia.setOnClickListener(this);
        rdoEuropa.setOnClickListener(this);
        rdoOceania.setOnClickListener(this);

    }

    public void iniEdTxs() {
        edTxUrlWS.setText(prefManager.leerPreferencias(PreferenciasManager.URL_WEBSERVICES, "http://192.168.100.5/WSPF/Peticiones.php"));
        edTxEmailUsu.setText(prefManager.leerPreferencias(PreferenciasManager.EMAIL_USU, usuario.getEmail()));
        edTxTelefonoUsu.setText(prefManager.leerPreferencias(PreferenciasManager.TELEFONO_USU, usuario.getTelefono()));
    }

    public void iniImgBtn() {
        imgBtnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String urlWS = edTxUrlWS.getText().toString();
                    String emailUsu = edTxEmailUsu.getText().toString();
                    String telefonoUsu = edTxTelefonoUsu.getText().toString();
                    prefManager.escribriPreferencia(PreferenciasManager.URL_WEBSERVICES, urlWS);
                    prefManager.escribriPreferencia(PreferenciasManager.EMAIL_USU, emailUsu);
                    prefManager.escribriPreferencia(PreferenciasManager.TELEFONO_USU, telefonoUsu);
                    prefManager.escribriPreferencia(PreferenciasManager.REGION, region);

                    Intent returnInt = new Intent();
                    setResult(Activity.RESULT_OK, returnInt);

                } catch (Exception e) {
                    Intent returnInt = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnInt);
                }
                finish();
            }
        });
    }

    public void iniRdoGroup() {
        String actual = prefManager.leerPreferencias(PreferenciasManager.REGION, "americas");
        switch (actual) {
            case "africa":
                rdoAfrica.setChecked(true);
                region = "africa";
                break;
            case "americas":
                rdoAmerica.setChecked(true);
                region = "americas";
                break;
            case "asia":
                rdoAsia.setChecked(true);
                region = "asia";
                break;
            case "europe":
                rdoEuropa.setChecked(true);
                region = "europe";
                break;
            case "oceania":
                rdoOceania.setChecked(true);
                region = "oceania";
                break;
        }
    }


    @Override
    public void onClick(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.rdo_africa_preferencias:
                if (checked) {
                    region = "africa";
                }
                break;
            case R.id.rdo_america_preferencias:
                if (checked) {
                    region = "americas";
                }
                break;
            case R.id.rdo_asia_preferencias:
                if (checked) {
                    region = "asia";
                }
                break;
            case R.id.rdo_europa_preferencias:
                if (checked) {
                    region = "europe";
                }
                break;
            case R.id.rdo_oceania_preferencias:
                if (checked) {
                    region = "oceania";
                }
                break;
        }
    }
}
