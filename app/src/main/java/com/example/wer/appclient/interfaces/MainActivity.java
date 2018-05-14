package com.example.wer.appclient.interfaces;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wer.appclient.R;
import com.example.wer.appclient.clases.Archivo;
import com.example.wer.appclient.clases.crearRegistro;

import java.io.BufferedReader;
import java.io.InputStreamReader;



public class MainActivity extends AppCompatActivity {

    TextView tv1;
    private String dato="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tv1 = findViewById(R.id.txtUSer);




        leerFichero(); // buscar datos denttro del fichero
        tv1.setText(dato); //Muestra el usuario

        if(dato.equals("")){
            Toast.makeText(this,"No existe registro", Toast.LENGTH_SHORT).show();
            send();
           // Intent o = new Intent(this,Archivo.class);
           // startActivity(o);
        }
        else{
            Toast.makeText(this,"Bienvendio "+dato, Toast.LENGTH_SHORT).show();
           // Intent o = new Intent(this,PersonaFormulario.class);
           // startActivity(o);

        }

    }//Finish onCreate

    public void send() {
        Intent i = new Intent(this, crearRegistro.class);
        // i.putExtra("dato",et1.getText().toString());
        startActivity(i);
    }


    //para entrar al activity de buscar persona
    public void btn_buscarPersona(View view){
        Intent intent = new Intent(MainActivity.this, BuscarPersona.class);
        startActivity(intent);
    }

    public void btn_formularioPersona(View view){
        Intent intent = new Intent(MainActivity.this, PersonaFormulario.class);
        intent.putExtra("operacion", "insertar");
        startActivity(intent);
    }
    public void btn_formularioUsuario(View view){
        Intent intent = new Intent(MainActivity.this, usuario.class);
        //intent.putExtra("operacion", "insertar");
        startActivity(intent);
    }

    //Metodo para leer fichero

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
