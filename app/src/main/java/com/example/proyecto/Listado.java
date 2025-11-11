package com.example.proyecto;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Listado extends AppCompatActivity {

    private FeedReaderDbHelper dbHelper;
    private TableLayout tableLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        dbHelper = new FeedReaderDbHelper(this);
        tableLayout = findViewById(R.id.tblistado);

        Button btnRegresar = findViewById(R.id.btnregresar);
        btnRegresar.setOnClickListener(v -> finish());

        cargarDatos();
    }

    private void cargarDatos() {
        tableLayout.removeAllViews();


        TableRow header = new TableRow(this);
        header.setBackgroundColor(0xFF644FD3);
        header.setPadding(16, 32, 16, 32);

        String[] titulos = {"ID", "NOMBRE", "APELLIDO"};
        for (String titulo : titulos) {
            TextView tv = new TextView(this);
            tv.setText(titulo);
            tv.setTextColor(0xFFFFFFFF);
            tv.setPadding(32, 16, 32, 16);
            tv.setTextSize(16);
            tv.setBackgroundColor(0xFF644FD3);
            header.addView(tv);
        }
        tableLayout.addView(header);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.column1,
                FeedReaderContract.FeedEntry.column2
        };

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.nameTable,
                projection,
                null, null, null, null,
                BaseColumns._ID + " ASC"
        );

        boolean hayDatos = false;
        while (cursor.moveToNext()) {
            hayDatos = true;
            TableRow row = new TableRow(this);
            row.setPadding(16, 24, 16, 24);
            row.setBackgroundColor(cursor.getPosition() % 2 == 0 ? 0xFFF5F5F5 : 0xFFE8E8E8);

            TextView tvId = new TextView(this);
            tvId.setText(cursor.getString(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
            tvId.setPadding(32, 16, 32, 16);

            TextView tvNombre = new TextView(this);
            tvNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column1)));
            tvNombre.setPadding(32, 16, 32, 16);

            TextView tvApellido = new TextView(this);
            tvApellido.setText(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column2)));
            tvApellido.setPadding(32, 16, 32, 16);

            row.addView(tvId);
            row.addView(tvNombre);
            row.addView(tvApellido);
            tableLayout.addView(row);
        }

        if (!hayDatos) {
            TableRow row = new TableRow(this);
            TextView tv = new TextView(this);
            tv.setText("No hay registros");
            tv.setPadding(32, 40, 32, 40);
            tv.setTextSize(18);
            row.addView(tv);
            tableLayout.addView(row);
        }

        cursor.close();
        db.close();
    }
}