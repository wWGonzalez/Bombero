package com.example.wer.appclient.interfaces;

import android.content.Intent;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wer.appclient.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
