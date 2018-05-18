package com.example.wer.appclient.interfaces;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wer.appclient.R;
import com.example.wer.appclient.clases.Persona;
import com.example.wer.appclient.interfaces.MainActivity;

import java.io.OutputStreamWriter;
import java.text.Normalizer;

public class crearRegistro extends AppCompatActivity {

    EditText etNombre; //recive Nombre
    EditText etTelefono; //Recive Telefono
    String telefono="";
    String nombre="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crear_registro);
        etNombre = findViewById(R.id.input_name);
        etTelefono = findViewById(R.id.input_telefono);
        //et3 = findViewById(R.id.input_dpi);
    }
    public void crearFicheroNombre(){
        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            openFileOutput("Nombre.txt", Context.MODE_PRIVATE));
            //nombre=et1.getText().toString();
            quitarAcento(etNombre.getText().toString());
            fout.write(nombre);
           // Toast.makeText(this, "Fichero creado correctamente",Toast.LENGTH_SHORT).show();
           // Toast.makeText(this, "Fichero creado correctamente",Toast.LENGTH_SHORT).show();
            fout.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
    }//finish crearFichero



    public void quitarAcento(String c){
        String origin = c;
      //  String original = "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýÿ";
        String cadenaNormalize = Normalizer.normalize(origin, Normalizer.Form.NFD);
        String cadenaSinAcentos = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "");
        nombre = cadenaSinAcentos;
    }

    public void crearFicheros(View view){
        telefono = etTelefono.getText().toString();
        if(telefono.equals("")) {
            Toast.makeText(this, "Debe ingresar un Numero de Telefono", Toast.LENGTH_SHORT).show();
        } else {
            crearFicheroNombre(); //Crea fichero nombre
            crearFicheroTelefono();

            Toast.makeText(this, "Datos ingresados Correcctamente", Toast.LENGTH_SHORT).show();
            Intent o = new Intent(this, MainActivity.class);
            startActivity(o);
            finish(); //Cierra activity
        }
    }

    public void crearFicheroTelefono(){
            try {
                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                openFileOutput("Telefono.txt", Context.MODE_PRIVATE));
                fout.write(telefono);
                fout.close();
            } catch (Exception ex) {
                Log.e("Ficheros", "Error al escribir fichero a memoria interna");
            }
    }//finish crearFichero
}