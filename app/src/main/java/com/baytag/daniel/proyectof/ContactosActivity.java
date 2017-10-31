package com.baytag.daniel.proyectof;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baytag.daniel.proyectof.adaptadores.ContactosAdapter;
import com.baytag.daniel.proyectof.apoyo.Conexion;
import com.baytag.daniel.proyectof.apoyo.Utilidades;
import com.baytag.daniel.proyectof.objetos.Contacto;
import com.baytag.daniel.proyectof.objetos.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import info.hoang8f.widget.FButton;

public class ContactosActivity extends AppCompatActivity {

    RecyclerView rcVwContactos;
    FButton btnInsertar;
    FButton btnBuscar;
    ImageButton btnMenu;
    Usuario usuario;
    ContactosAdapter adaptador;
    Contacto actual;
    Toolbar cstToolbar;

    private final int PERMISSIONS_REQUEST_CALL_PHONE = 0;
    private final int PREFERENCIAS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);


        usuario = (Usuario) getIntent().getExtras().get("usuario");

        rcVwContactos = (RecyclerView) findViewById(R.id.rcVw_rcVwCon_contactos);
        btnInsertar = (FButton) findViewById(R.id.btn_insetar_contactos);
        btnBuscar = (FButton) findViewById(R.id.btn_buscar_contactos);
        //btnMenu = (ImageButton) findViewById(R.id.btn_btnMenu_contactos);

        //registerForContextMenu(btnMenu);

        /*btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.showContextMenu();
            }
        });*/

        cstToolbar = (Toolbar) findViewById(R.id.tB_Cst_contactos);
        cstToolbar.setSubtitle(usuario.getEmail());
        cstToolbar.inflateMenu(R.menu.menu_configuracion);
        setSupportActionBar(cstToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(usuario.getNombre());
        }


        iniRcVw();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_configuracion, menu);
        return true;
    }

    private void iniRcVw() {
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        rcVwContactos.setLayoutManager(llm);
        registerForContextMenu(rcVwContactos);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rcVwContactos.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    ActionBar actionBar = getSupportActionBar();
                    if (oldScrollY >= scrollY) {
                        actionBar.show();
                    } else {
                        actionBar.hide();
                    }
                }
            });
        }
        new onBackRcVw(this).execute(usuario);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.toString()) {
            case "Preferencias":
                Intent i = new Intent(ContactosActivity.this, PreferenciasActivity.class);
                i.putExtra("usuario", usuario);
                startActivityForResult(i, PREFERENCIAS_REQUEST);
                break;
            case "salir":
                System.exit(1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        actual = adaptador.getItemSelected();

        Toast.makeText(getBaseContext(), actual.getNomCon() + " : " + item.getTitle(), Toast.LENGTH_SHORT).show();

        switch (item.getTitle().toString()) {
            case "Llamar":
                permisosLlamada();
                break;
            case "Escribir Email":
                enviarEmail();
                break;
            case "Editar":
                break;
            case "Eliminar":
                break;
            default:
                Log.wtf("defult", "algo muy defualt");
                break;
        }

        return super.onContextItemSelected(item);

    }

    private void permisosLlamada() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                    Toast.makeText(getBaseContext(),
                            "Necesita dar permiso a la aplicación desde el menu de configuracíones",
                            Toast.LENGTH_SHORT).show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSIONS_REQUEST_CALL_PHONE);
                }
            } else {
                llamarContacto();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PREFERENCIAS_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getBaseContext(), "Los cambios se han guardado", Toast.LENGTH_SHORT).show();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getBaseContext(), "Los cambios no fueron guardados", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    llamarContacto();
                } else {
                    Toast.makeText(getBaseContext(), "No se permitio la salida de llamadas", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private void llamarContacto() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + actual.getTelCon()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void enviarEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + actual.getCorreoCon()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private class onBackRcVw extends AsyncTask<Usuario, Boolean, ArrayList<Contacto>> {
        Activity activity;

        public onBackRcVw(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected ArrayList<Contacto> doInBackground(Usuario... usuarios) {

            ArrayList<Contacto> contactos = new ArrayList<>();

            Usuario usu = usuarios[0];
            HashMap parametros = new HashMap();
            parametros.put("numPeticion", "2");
            parametros.put("idUsu", String.valueOf(usu.getId()));

            try {

                Conexion con = new Conexion("http://192.168.100.5/WSPF/Peticiones.php");
                con.setParametros(parametros);
                con.executar(Conexion.metodoPeticion.POST);
                String respuesta = con.getRespuesta();

                HashMap hashContactos = Utilidades.datosJson(respuesta);
                Iterator<?> keys = hashContactos.entrySet().iterator();

                while (keys.hasNext()) {

                    Map.Entry entryCon = (Map.Entry) keys.next();
                    HashMap hashContacto = (HashMap) entryCon.getValue();
                    Contacto contacto = new Contacto(hashContacto);
                    contactos.add(contacto);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return contactos;
        }

        @Override
        protected void onPostExecute(ArrayList<Contacto> contactos) {
            super.onPostExecute(contactos);
            if (contactos.size() > 0) {
                adaptador = new ContactosAdapter(contactos);
                rcVwContactos.setAdapter(adaptador);
            }

        }
    }

}
