package com.baytag.daniel.proyectof;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baytag.daniel.proyectof.apoyo.AppDbHelper;
import com.baytag.daniel.proyectof.contracts.AppContract;

public class NuevoUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    private AppDbHelper dbHelper;
    private SQLiteDatabase db;
    private EditText edTxNick, edTxPass, edTxTel, edTxCorreo, edTxNom;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);

        dbHelper = new AppDbHelper(getBaseContext());
        db = dbHelper.getWritableDatabase();

        edTxNick = (EditText) findViewById(R.id.edTx_NicUsu_NvUsu);
        edTxPass = (EditText) findViewById(R.id.edTx_PassUsu_NvUsu);
        edTxTel = (EditText) findViewById(R.id.edTx_TelUsu_NvUsu);
        edTxCorreo = (EditText) findViewById(R.id.edTx_CorreoUsu_NvUsu);
        edTxNom = (EditText) findViewById(R.id.edTx_NomUsu_NvUsu);

        btnGuardar = (Button) findViewById(R.id.btn_Guardar_NvUsu);

        btnGuardar.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (!edTxNick.getText().toString().equals("")
                && !edTxPass.getText().toString().equals("")
                && !edTxTel.getText().toString().equals("")
                && !edTxCorreo.getText().toString().equals("")
                && !edTxNom.getText().toString().equals("")) {
            ContentValues values = new ContentValues();

            values.put(AppContract.UsuarioEntity.COLUMN_NAME_NICK, edTxNick.getText().toString());
            values.put(AppContract.UsuarioEntity.COLUMN_NAME_PASS, edTxPass.getText().toString());
            values.put(AppContract.UsuarioEntity.COLUMN_NAME_TELEFONO, edTxTel.getText().toString());
            values.put(AppContract.UsuarioEntity.COLUMN_NAME_EMAIL, edTxCorreo.getText().toString());
            values.put(AppContract.UsuarioEntity.COLUMN_NAME_NOMBRE, edTxNom.getText().toString());

            long newRodId = db.insert(AppContract.UsuarioEntity.TABLE_NAME, null, values);
            Log.wtf("WoW", "" + newRodId);
            setResult(RESULT_OK);
            finish();

        } else {
            Toast.makeText(getBaseContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
        }
    }
}
