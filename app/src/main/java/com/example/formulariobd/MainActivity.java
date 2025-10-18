package com.example.formulariobd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText et1, et2, et3, et4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        et1 = findViewById(R.id.ncontrol);
        et2 = findViewById(R.id.nombre);
        et3 = findViewById(R.id.semestre);
        et4 = findViewById(R.id.carrera);
    }
    public void limpiar(View view){
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
    }
    public void altas(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String ncontrol = et1.getText().toString();
        String nombre = et2.getText().toString();
        String semestre = et3.getText().toString();
        String carrera = et4.getText().toString();

        ContentValues registro = new ContentValues();
        registro.put("ncontrol", ncontrol);
        registro.put("nombre", nombre);
        registro.put("semestre", semestre);
        registro.put("carrera", carrera);

        // los inserto en la base de datos
        bd.insert("usuario", null, registro);
        bd.close();

        // ponemos los campos a vacío para insertar el siguiente usuario
        et1.setText(""); et2.setText(""); et3.setText("");
        et4.setText("");
        Toast.makeText(this, "Datos del usuario cargados",
                Toast.LENGTH_SHORT).show();
    }
    // Hacemos búsqueda de usuario por DNI
    public void consulta(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String ncontrol = et1.getText().toString();
        Cursor fila = bd.rawQuery( "select nombre, semestre, carrera from usuario where ncontrol=" + ncontrol, null);
        if (fila.moveToFirst()) {
            et2.setText(fila.getString(0));
            et3.setText(fila.getString(1));
            et4.setText(fila.getString(2));
        } else
            Toast.makeText(this, "No existe ningún usuario con ese dni", Toast.LENGTH_SHORT).show(); bd.close();
    }
    public void baja(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String ncontrol = et1.getText().toString();
        // aquí borro la base de datos del usuario por el ncontrol
        int cant = bd.delete("usuario", "ncontrol=" + ncontrol, null);
        bd.close();
        et1.setText(""); et2.setText(""); et3.setText("");
        et4.setText("");
        if (cant == 1)
            Toast.makeText(this, "Usuario eliminado",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "No existe usuario",
                    Toast.LENGTH_SHORT).show();
    }
    public void modificacion(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String ncontrol = et1.getText().toString();
        String nombre = et2.getText().toString();
        String semestre = et3.getText().toString();
        String carrera = et4.getText().toString();
        ContentValues registro = new ContentValues();
        // actualizamos con los nuevos datos, la información cambiada
        registro.put("nombre", nombre);
        registro.put("semestre", semestre);
        registro.put("carrera", carrera);
        int cant = bd.update("usuario", registro, "ncontrol=" + ncontrol, null);
        bd.close();
        if (cant == 1)
            Toast.makeText(this, "Datos modificados con éxito", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "No existe usuario", Toast.LENGTH_SHORT).show();
    }
}
