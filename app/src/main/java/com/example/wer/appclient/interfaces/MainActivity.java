package com.example.wer.appclient.interfaces;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridLayout;
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
    GridLayout mainGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

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

    private void setToggleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));
                        Toast.makeText(MainActivity.this, "State : True", Toast.LENGTH_SHORT).show();

                    } else {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        Toast.makeText(MainActivity.this, "State : False", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


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
