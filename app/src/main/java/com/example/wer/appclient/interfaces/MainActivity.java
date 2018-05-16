package com.example.wer.appclient.interfaces;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.content.pm.PackageManager;
//import android.preference.PreferenceManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wer.appclient.R;
//import com.example.wer.appclient.clases.Archivo;
import com.example.wer.appclient.clases.Persona;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button show;
    Dialog MyDialog;
    Button hello;
    ImageView image;
    Button close;

    TextView tv1;
    private String dato="";

    GridLayout mainGrid;

    Menu menu;
    //Variables para Post
    public String emergencia;
    Persona persona;
    String nombre="";
    String telefono="";
    String id_persona;
    String dir; //Se almacena la direccion
    String coor; //Se almacenan las coordenadas

    private Locale locale;
    private Configuration config = new Configuration();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

        //Set Event
        setSingleEvent(mainGrid);
        //setToggleEvent(mainGrid);
        Bundle bundle = getIntent().getExtras();

        tv1 = findViewById(R.id.txtUSer);
        leerFichero(); // buscar datos denttro del fichero
        tv1.setText(dato); //Muestra el usuario

        if(dato.equals("")){
            Toast.makeText(this,"No existe registro", Toast.LENGTH_SHORT).show();
            send();
        }
        else{
            Toast.makeText(this,"Bienvendio "+dato, Toast.LENGTH_SHORT).show();
           // Intent o = new Intent(this,PersonaFormulario.class);
           // startActivity(o);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

        leerFicheroTelefono();
        leerFicheroNombre();

    }//Finish onCreate

    private void showDialog(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getResources().getString(R.string.idioma));
        //obtiene los idiomas del array de string.xml
        String[] types = getResources().getStringArray(R.array.languages);
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch(which){
                    case 0:
                        locale = new Locale("en");
                        config.locale =locale;
                        break;
                    case 1:
                        locale = new Locale("es");
                        config.locale =locale;
                        break;
                }
                getResources().updateConfiguration(config, null);
                Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refresh);
                finish();
            }

        });

        b.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_alertas:
                //crear nuevo aviso
                btn_buscarPersona(null);
                return true;
            case R.id.action_pagina_web:
                //crear nuevo aviso
                Uri uri = Uri.parse("http://www.google.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            case R.id.action_historia:
                //crear nuevo aviso
                abrir_hitoria(null);
                return true;
            case R.id.action_quienes_somos:
                //crear nuevo aviso
                abrir_quienes_somos(null);
                return true;
            case R.id.action_desarrolladores:
                //crear nuevo aviso
                abrir_desarrolladores(null);
                return true;
            case R.id.action_cambiar_idioma:
                //crear nuevo aviso
                showDialog();
                return true;
            case R.id.action_salir:
                finish();
                return true;
            default:
                return false;
        }
    }

    //Leer Ficheros
    private void leerFicheroNombre() {
        try {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("Nombre.txt")));
            String texto = fin.readLine();
            nombre = texto;
            //Toast.makeText(this, "Nombre: " +nombre,Toast.LENGTH_SHORT).show();
            //this.nombre = texto;
            fin.close();
        } catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
    }

    private void leerFicheroTelefono() {
        try {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("Telefono.txt")));
            String texto = fin.readLine();
            telefono = texto;
            // tv1.setText(texto);
            //  this.nombre = texto;
            fin.close();
        } catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MainActivity.Localizacion Local = new MainActivity.Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
    }//Finaliza locationStart

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }//Finaliza onRequest

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    dir = (DirCalle.getAddressLine(0));
                    //    tv2.setText(dir);
                    //    nombre.setText(direccion);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//Finaliza setLocation

    //Insertar Persona
    private class InsertarPersona extends AsyncTask<Void, Void, Boolean> {
        public Boolean doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://wzwer.pythonanywhere.com/rest/alert/");
            httpPost.setHeader("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            try {
                // jsonObject.put("dpi", persona.getDpi());
                jsonObject.put("nombre", nombre);
                jsonObject.put("telefono", telefono);
                jsonObject.put("coordenadas", coor);
                jsonObject.put("direccion", dir);
                jsonObject.put("emergencia", emergencia);
                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                httpClient.execute(httpPost);
                return true;
            } catch (org.json.JSONException e) {
                return false;
            } catch (java.io.UnsupportedEncodingException e) {
                return false;
            } catch (org.apache.http.client.ClientProtocolException e) {
                return false;
            } catch (java.io.IOException e) {
                return false;
            }
        }

        public void onPostExecute(Boolean result){
            if(result){
                Toast.makeText(MainActivity.this, "Alerta Enviada", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(MainActivity.this, "Problema al Enviar Alerta", Toast.LENGTH_LONG).show();
        }
    }//Finaliza Insertar

    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        MainActivity mainActivity;

        public MainActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }
        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();

            String Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();
            //tv1.setText(Text);
            //String Text =loc.getLatitude()+ loc.getLongitude()+"";
            coor= Text;
            //tv1.setText(Text);
            this.mainActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            //Este metodo se ejecuta cuando el GPS es desactivado
            //tv1.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            // tv1.setText("GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch(finalI) {
                        case 0:
                            MyCustomAlertDialog("Maternidad");
                            break;
                        case 1:
                            MyCustomAlertDialog("Accidente");
                           // sentenciaN;

                            break;
                        case 2:
                            MyCustomAlertDialog("Incendio");
                           // sentenciaN;
                            break;
                        case 3:
                            MyCustomAlertDialog("Primeros Auxilios");
                           // sentenciaN;
                            break;
                    }
                }
            });
        }
    }

    //Llamar menu emergente
    public void MyCustomAlertDialog(String m){
        final String emerg =m;

        MyDialog = new Dialog(MainActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.activity_menu_emergente);

        MyDialog.setTitle("My Custom Dialog");

        hello = (Button) MyDialog.findViewById(R.id.hello);
        close = (Button) MyDialog.findViewById(R.id.close);
        image = MyDialog.findViewById(R.id.imagenView);

        if (emerg == "Maternidad"){
            Drawable myDrawable = getResources().getDrawable(R.drawable.me_time);
            image.setImageDrawable(myDrawable);
        }
        else if(emerg == "Accidente"){
            Drawable myDrawable = getResources().getDrawable(R.drawable.family_time);
            image.setImageDrawable(myDrawable);
        }
        else if(emerg == "Incendio"){
            Drawable myDrawable = getResources().getDrawable(R.drawable.lovely_time);
            image.setImageDrawable(myDrawable);
        }
        else if(emerg == "Primeros Auxilios"){
            Drawable myDrawable = getResources().getDrawable(R.drawable.team_time);
            image.setImageDrawable(myDrawable);
        }

        hello.setEnabled(true);
        close.setEnabled(true);

        hello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Emergencia = "+   emerg, Toast.LENGTH_LONG).show();
                emergencia = emerg;
                new MainActivity.InsertarPersona().execute();
                MyDialog.cancel();

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.show();
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

    public void abrir_desarrolladores(View view){
        Intent intent = new Intent(MainActivity.this, desarrolladores.class);
        startActivity(intent);
    }

    public void abrir_quienes_somos(View view){
        Intent intent = new Intent(MainActivity.this, quienes_somos.class);
        startActivity(intent);
    }

    public void abrir_hitoria(View view){
        Intent intent = new Intent(MainActivity.this, Historia.class);
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
