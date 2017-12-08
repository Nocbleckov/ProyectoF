package com.baytag.daniel.proyectof;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.baytag.daniel.proyectof.apoyo.AppDbHelper;
import com.baytag.daniel.proyectof.apoyo.Conexion;
import com.baytag.daniel.proyectof.apoyo.PreferenciasManager;
import com.baytag.daniel.proyectof.apoyo.Utilidades;
import com.baytag.daniel.proyectof.contracts.AppContract;
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
    Toolbar cstToolbar;
    Contacto actual;
    PreferenciasManager prefManager;
    AppDbHelper appDbHelper;
    SQLiteDatabase db;

    public static final int PERMISSIONS_REQUEST_CALL_PHONE = 0;
    public static final int PREFERENCIAS_REQUEST = 1;
    public static final int REQUEST_EDITAR_CONTACTO = 2;
    public static final int REQUEST_NUEVO_CONTACTO = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);


        usuario = (Usuario) getIntent().getExtras().get(Usuario.KEY);
        prefManager = new PreferenciasManager(getBaseContext());

        rcVwContactos = (RecyclerView) findViewById(R.id.rcVw_rcVwCon_contactos);
        btnInsertar = (FButton) findViewById(R.id.btn_insetar_contactos);
        btnBuscar = (FButton) findViewById(R.id.btn_buscar_contactos);

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevoContacto(null);
            }
        });

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
        //new onBackRcVw(this).execute(usuario);
        new OnBackRcVwInternal(this).execute(usuario);
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

        Contacto actual = adaptador.getItemSelected();
        this.actual = actual;

        Toast.makeText(getBaseContext(), actual.getNomCon() + " : " + item.getTitle(), Toast.LENGTH_SHORT).show();

        switch (item.getTitle().toString()) {
            case "Llamar":
                permisosLlamada(actual);
                break;
            case "Escribir Email":
                enviarEmail(actual);
                break;
            case "Editar":
                editarContacto(actual);
                break;
            case "Eliminar":
                eliminarContacto(actual);
                break;
            default:
                Log.wtf("defult", "algo muy defualt");
                break;
        }

        return super.onContextItemSelected(item);

    }

    private void permisosLlamada(Contacto actual) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                    /*Toast.makeText(getBaseContext(),
                            "Necesita dar permiso a la aplicación desde el menu de configuracíones",
                            Toast.LENGTH_SHORT).show();*/
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSIONS_REQUEST_CALL_PHONE);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSIONS_REQUEST_CALL_PHONE);
                }
            } else {
                llamarContacto(actual);
            }
        }else{
            llamarContacto(actual);
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
            case REQUEST_EDITAR_CONTACTO:
                if (resultCode == Activity.RESULT_OK) {
                    //new onBackRcVw(this).execute(usuario);
                    new OnBackRcVwInternal(this).execute(usuario);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getBaseContext(), "No se realizo ningun cambio", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_NUEVO_CONTACTO:
                if (resultCode == Activity.RESULT_OK) {
                    //new onBackRcVw(this).execute(usuario);
                    new OnBackRcVwInternal(this).execute(usuario);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getBaseContext(), "No se realizo ningun cambio", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    llamarContacto(this.actual);
                } else {
                    Toast.makeText(getBaseContext(), "No se permitio la salida de llamadas", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @SuppressLint("MissingPermission")
    private void llamarContacto(Contacto actual) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + actual.getTelCon()));
        startActivity(intent);
    }

    private void enviarEmail(Contacto actual) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + actual.getCorreoCon()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void editarContacto(Contacto actual) {
        Intent i = new Intent(ContactosActivity.this, InfoContactoActivity.class);
        i.putExtra(Contacto.KEY, actual);
        i.putExtra(Usuario.KEY, usuario);
        i.putExtra("requestCode", REQUEST_EDITAR_CONTACTO);
        startActivityForResult(i, REQUEST_EDITAR_CONTACTO);
    }

    private void nuevoContacto(Contacto contacto) {
        Intent i = new Intent(ContactosActivity.this, InfoContactoActivity.class);
        i.putExtra(Contacto.KEY, contacto);
        i.putExtra(Usuario.KEY, usuario);
        i.putExtra("requestCode", REQUEST_NUEVO_CONTACTO);
        startActivityForResult(i, REQUEST_NUEVO_CONTACTO);
    }

    private void eliminarContacto(Contacto contacto) {
        appDbHelper = new AppDbHelper(getBaseContext());
        db = appDbHelper.getReadableDatabase();
        String selection = AppContract.ContactoEntity.COLUMN_NAME_TEL_CON + " = ?";
        String[] selectionArgs = {contacto.getTelCon()};
        db.delete(AppContract.ContactoEntity.TABLE_NAME, selection, selectionArgs);
        new OnBackRcVwInternal(this).execute(usuario);
    }

    private class OnBackRcVwInternal extends AsyncTask<Usuario, Boolean, ArrayList<Contacto>> {


        String[] projection = {
                AppContract.ContactoEntity.COLUMN_NAME_NOM_CON,
                AppContract.ContactoEntity.COLUMN_NAME_P_APELLIDO,
                AppContract.ContactoEntity.COLUMN_NAME_S_APELLIDO,
                AppContract.ContactoEntity.COLUMN_NAME_TEL_CON,
                AppContract.ContactoEntity.COLUMN_NAME_CIUDAD_CON,
                AppContract.ContactoEntity.COLUMN_NAME_CORREO_CON
        };

        Activity activity;

        public OnBackRcVwInternal(Activity activity) {
            this.activity = activity;
            appDbHelper = new AppDbHelper(getBaseContext());
            db = appDbHelper.getReadableDatabase();
        }

        @Override
        protected ArrayList<Contacto> doInBackground(Usuario... usuarios) {

            ArrayList<Contacto> contactos = new ArrayList<>();

            try {
                Usuario usu = usuarios[0];
                String selection = AppContract.ContactoEntity.COLUMN_NAME_ID_USUARIO + " = ?";
                String[] selectionArgs = {String.valueOf(usu.getId())};
                //String sortOrder = AppContract.ContactoEntity._ID + " ASC";

                Cursor cursor = db.query(
                        AppContract.ContactoEntity.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );

                while (cursor.moveToNext()) {
                    Contacto temp = new Contacto(cursor);
                    contactos.add(temp);
                }
                cursor.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return contactos;
        }

        @Override
        protected void onPostExecute(ArrayList<Contacto> contactos) {
            super.onPostExecute(contactos);
            if (contactos != null) {
                adaptador = new ContactosAdapter(contactos);
                rcVwContactos.setAdapter(adaptador);
            }
        }
    }

    private class onBackRcVwServer extends AsyncTask<Usuario, Boolean, ArrayList<Contacto>> {
        Activity activity;

        public onBackRcVwServer(Activity activity) {
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

                Conexion con = new Conexion(
                        prefManager.leerPreferencias(
                                PreferenciasManager.URL_WEBSERVICES
                                , "http://192.168.100.5/WSPF/Peticiones.php"
                        ));
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
