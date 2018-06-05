package com.example.wer.appclient.interfaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crear_registro);
        etNombre = findViewById(R.id.input_name);
        etTelefono = findViewById(R.id.input_telefono);
        //et3 = findViewById(R.id.input_dpi);



    }


    public void Guardar(View view) {
        SharedPreferences nombre = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences telefono = getSharedPreferences("datos", Context.MODE_PRIVATE);

        //Se crea un Editor para guardar y editar datos
        SharedPreferences.Editor edit1 = nombre.edit();
        SharedPreferences.Editor edit2 = telefono.edit();

        //Se asignan los datos de etNombre y etTelefono a su respectivo Editor
        edit1.putString("name", etNombre.getText().toString());
        edit2.putString("phone", etTelefono.getText().toString());

        edit1.commit();
        edit2.commit();

        Intent i =  new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }



}