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
import com.example.wer.appclient.clases.Persona;
import com.example.wer.appclient.interfaces.MainActivity;

import java.io.OutputStreamWriter;
import java.text.Normalizer;

public class crearRegistro extends AppCompatActivity {

    EditText et1; //recive Nombre
    EditText et2; //Recive Telefono
    EditText et3;//Recive DPI

    String telefono="";

    String nombre="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_registro);
        et1 = findViewById(R.id.input_name);
        et2 = findViewById(R.id.input_password);
        //et3 = findViewById(R.id.input_dpi);


    }

    public void crearFicheroNombre(){
        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            openFileOutput("Nombre.txt", Context.MODE_PRIVATE));
            //nombre=et1.getText().toString();
            quitarAcento(et1.getText().toString());

            fout.write(nombre);

            Toast.makeText(this, "Fichero creado correctamente",Toast.LENGTH_SHORT).show();

           // Toast.makeText(this, "Fichero creado correctamente",Toast.LENGTH_SHORT).show();
            fout.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
    }//finish crearFichero

    public void crearFicheroDPI(){

        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            openFileOutput("DPI.txt", Context.MODE_PRIVATE));


           fout.write(et3.getText().toString());// Escribe en el Fichero


            //Toast.makeText(this, "Fichero creado correctamente",Toast.LENGTH_SHORT).show();
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
        telefono = et3.getText().toString();

        if(telefono.equals("")) {
            Toast.makeText(this, "Debe ingresar un Numero de Telefono", Toast.LENGTH_SHORT).show();
        } else {
            crearFicheroNombre(); //Crea fichero nombre
            crearFicheroDPI(); //Crea fichero DPI
            crearFicheroTelefono();

            Toast.makeText(this, "Fichero creado correctamente", Toast.LENGTH_SHORT).show();
            Intent o = new Intent(this, MainActivity.class);
            startActivity(o);
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

