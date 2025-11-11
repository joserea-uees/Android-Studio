package com.example.proyecto;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText txtid;
    private EditText txtnombre;
    private EditText txtapellido;
    FeedReaderDbHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new FeedReaderDbHelper(this);
        txtid = findViewById(R.id.txtid);
        txtnombre = findViewById(R.id.txtnombre);
        txtapellido = findViewById(R.id.txtapellido);
    }

    public void Listar(View vista) {
        Intent listar = new Intent(this, Listado.class);
        startActivity(listar);
    }

    public void Guardar(View vista) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, txtnombre.getText().toString().trim());
        values.put(FeedReaderContract.FeedEntry.column2, txtapellido.getText().toString().trim());

        long newRowId = db.insert(FeedReaderContract.FeedEntry.nameTable, null, values);
        Toast.makeText(this, "Se guardó el registro con ID: " + newRowId, Toast.LENGTH_LONG).show();
        limpiarCampos();
        db.close();
    }

    public void Buscar(View vista) {
        if (txtid.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Ingrese un ID para buscar", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.column1,
                FeedReaderContract.FeedEntry.column2
        };
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { txtid.getText().toString().trim() };

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.nameTable,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column1));
            String apellido = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column2));
            txtnombre.setText(nombre);
            txtapellido.setText(apellido);
            Toast.makeText(this, "Registro encontrado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se encontró registro con ese ID", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        }
        cursor.close();
        db.close();
    }

    public void Actualizar(View vista) {
        if (txtid.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Ingrese el ID del registro a actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, txtnombre.getText().toString().trim());
        values.put(FeedReaderContract.FeedEntry.column2, txtapellido.getText().toString().trim());

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { txtid.getText().toString().trim() };

        int count = db.update(FeedReaderContract.FeedEntry.nameTable, values, selection, selectionArgs);
        if (count > 0) {
            Toast.makeText(this, "Registro actualizado correctamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No se encontró el registro para actualizar", Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    public void Eliminar(View vista) {
        if (txtid.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Ingrese el ID del registro a eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { txtid.getText().toString().trim() };

        int deletedRows = db.delete(FeedReaderContract.FeedEntry.nameTable, selection, selectionArgs);
        if (deletedRows > 0) {
            Toast.makeText(this, "Registro eliminado correctamente", Toast.LENGTH_LONG).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "No se encontró el registro para eliminar", Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    private void limpiarCampos() {
        txtid.setText("");
        txtnombre.setText("");
        txtapellido.setText("");
    }
}