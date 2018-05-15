package com.example.wer.appclient.interfaces;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wer.appclient.R;
import com.example.wer.appclient.interfaces.MainActivity;

import java.io.OutputStreamWriter;

public class crearRegistro extends AppCompatActivity {

    EditText et1;
    EditText et2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_registro);
        et1 = findViewById(R.id.input_name);
        et2 = findViewById(R.id.input_password);
    }

    public void crearFicheroNombre(){
        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            openFileOutput("Nombre.txt", Context.MODE_PRIVATE));

            fout.write(et1.getText().toString());
            Toast.makeText(this, "Fichero creado correctamente",Toast.LENGTH_SHORT).show();
            fout.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
    }//finish crearFichero

    public void crearFicheroTelefono(View view){
        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            openFileOutput("DPI.txt", Context.MODE_PRIVATE));
            fout.write(et2.getText().toString());
            Toast.makeText(this, "Fichero creado correctamente",Toast.LENGTH_SHORT).show();
            fout.close();
            crearFicheroNombre();
            Intent o = new Intent(this,MainActivity.class);
            startActivity(o);
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
    }//finish crearFichero
}
