package com.example.wer.appclient.interfaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;



public class SplashScreen extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences nombre = getSharedPreferences("datos", Context.MODE_PRIVATE);

        //tv1.setText(dato); //Muestra el usuario
        if(nombre.getString("name","").equals("")){
            Intent intent = new Intent(this, crearRegistro.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

            //Toast.makeText(this,"Bienvendio "+nombre.getString("name",""), Toast.LENGTH_SHORT).show();
            // Intent o = new Intent(this,PersonaFormulario.class);
            // startActivity(o);
        }
       ;
    }


}
