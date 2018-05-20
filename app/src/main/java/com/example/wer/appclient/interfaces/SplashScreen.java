package com.example.wer.appclient.interfaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;



public class SplashScreen extends AppCompatActivity {


    String dato="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        leerFichero(); // buscar datos dentro del fichero
        //tv1.setText(dato); //Muestra el usuario
        if(dato.equals("")){
            Intent intent = new Intent(this, crearRegistro.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

            Toast.makeText(this,"Bienvendio "+dato, Toast.LENGTH_SHORT).show();
            // Intent o = new Intent(this,PersonaFormulario.class);
            // startActivity(o);
        }
       ;
    }


    private void leerFichero() {
        try {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("Nombre.txt")));

            String texto = fin.readLine();

            // tv1.setText(texto);
            this.dato = texto;
            fin.close();
        } catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");

        }
    }
}
