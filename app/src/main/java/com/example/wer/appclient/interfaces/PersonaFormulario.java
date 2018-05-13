package com.example.wer.appclient.interfaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wer.appclient.R;
import com.example.wer.appclient.clases.Persona;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class PersonaFormulario extends AppCompatActivity {
    EditText cedula, nombre, emergencia;
    EditText coordenadas, adress;
    TextView tv1, tv2;
    Spinner spinner1;

    Persona persona;
    String id_persona;
    String dir; //Se almacena la direccion
    String coor; //Se almacenan las coordenadas


    //operacion: insertar, actualizar

    String operacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona_formulario);
        inicializar();

        Bundle bundle = getIntent().getExtras();
        this.operacion = bundle.getString("operacion");

        if (this.operacion.equals("actualizar")){
            this.id_persona = bundle.getString("id_persona");
            //obtener persona
            new ObtenerPersona().execute();
        } else {
            this.id_persona = "";
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }//finaliza onCreate

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void inicializar(){
        this.cedula = (EditText) findViewById(R.id.editTextCedula);
       // this.nombre = (EditText) findViewById(R.id.editTextNombre);
        //this.emergencia = (EditText) findViewById(R.id.editTextEmergencia);
      //  this.coordenadas = (EditText) findViewById(R.id.editTextCoordenadas);
      //  this.adress = (EditText) findViewById(R.id.editTextDireccion);
       // tv1 = findViewById(R.id.textViewCoor);
      //  tv2 = findViewById((R.id.textViewDir));
        spinner1 = findViewById(R.id.spinnerEmergencia);

        String [] opciones = {"Dolor de Estomago","Accidente de Transito","Maternidad","Incendio"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,opciones);
        spinner1.setAdapter(adapter);




    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
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

   //     tv1.setText("Localizacion agregada");
   //     tv2.setText("");
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






    public void btn_clickGuardarPersona(View view){
        persona = new Persona();
        persona.setDpi(cedula.getText().toString().trim());
     // persona.setEmergencia(emergencia.getText().toString().trim());
     //   persona.setNombre(nombre.getText().toString().trim());
      //  persona.setApellido(apellido.getText().toString().trim());

        if (this.operacion.equals("actualizar"))
            new ActualizarPersona().execute();
        if (this.operacion.equals("insertar"))
            new InsertarPersona().execute();
    }

    public void btn_clickEliminarPersona(View view){
        if (id_persona != ""){
            new EliminarPersona().execute();
        } else
            Toast.makeText(PersonaFormulario.this, "Esta opcion se encuentra disponible \n para personas registradas", Toast.LENGTH_LONG).show();
    }

    private class EliminarPersona extends AsyncTask<Void, Void, Boolean> {
        public Boolean doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();

            HttpDelete httpDelete = new HttpDelete("http://wzwer.pythonanywhere.com/rest/alert/"+id_persona+"/");
            httpDelete.setHeader("Content-Type", "application/json");

            try {
                httpClient.execute(httpDelete);
                return true;
            } catch (IOException e){
                e.printStackTrace();
                return false;
            }
        }

        public void onPostExecute(Boolean result){
            if(result){
                Toast.makeText(PersonaFormulario.this, "Eliminado Correctamente", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(PersonaFormulario.this, "Problema al Eliminar", Toast.LENGTH_LONG).show();
        }
    }


    //Insertar Persona
    private class InsertarPersona extends AsyncTask<Void, Void, Boolean> {
        public Boolean doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://wzwer.pythonanywhere.com/rest/alert/");
            httpPost.setHeader("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("dpi", persona.getDpi());
                jsonObject.put("coordenadas", coor);
                jsonObject.put("direccion", dir);

                //jsonObject.put("emergencia", persona.getEmergencia());

               // String [] opciones = {"Dolor de Estomago","Accidente de Transito","Maternidad","Incendio"};

                String seleccion = spinner1.getSelectedItem().toString();

                if(seleccion.equals("Dolor de Estomago")){
                    jsonObject.put("emergencia", "Dolor de Estomago");

                }
                else if(seleccion.equals("Accidente de Transito")){
                    jsonObject.put("emergencia", "Accidente de Transito");
                }
                else if(seleccion.equals("Maternidad")){
                    jsonObject.put("emergencia", "Maternidad");
                }
                else if(seleccion.equals("Incendio")){
                    jsonObject.put("emergencia", "Incendio");
                    }
                    else{

                    }







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
                Toast.makeText(PersonaFormulario.this, "Insertado Correctamente", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(PersonaFormulario.this, "Problema al Insertar", Toast.LENGTH_LONG).show();
        }
    }



    private class ActualizarPersona extends AsyncTask<Void, Void, Boolean>{
        public Boolean doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPut httpPut = new HttpPut("http://wzwer.pythonanywhere.com/rest/alert/" + id_persona + "/");
            httpPut.setHeader("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("dpi", persona.getDpi());
                jsonObject.put("nombre", persona.getNombre());
               // jsonObject.put("apellido", persona.getApellido());

                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPut.setEntity(stringEntity);
                httpClient.execute(httpPut);

                return true;
            } catch (JSONException e){
                e.printStackTrace();
                return false;
            } catch (java.io.UnsupportedEncodingException e){
                return false;
            } catch (java.io.IOException e){
                return false;
            }
        }

        public void onPostExecute(Boolean result){
            String msj;
            if (result) {
                msj = "Actualizado Correctamente";
            } else {
                msj = "Problemas al actualizar";
            }
                Toast.makeText(PersonaFormulario.this, msj, Toast.LENGTH_SHORT).show();
        }
    }

    private class ObtenerPersona extends AsyncTask<Void, Void, Persona>{
        public Persona doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://wzwer.pythonanywhere.com/rest/alert/" + id_persona + "/");
            httpGet.setHeader("Content-Type", "application/json");
            persona = new Persona();
            try {
                HttpResponse response = httpClient.execute(httpGet);
                String responString = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(responString);

                persona.setDpi(jsonObject.getString("dpi"));
                persona.setNombre(jsonObject.getString("nombre"));
         //       persona.setApellido(jsonObject.getString("apellido"));

                return persona;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(Persona persona){
            super.onPostExecute(persona);
            if (persona != null){
                cedula.setText(persona.getDpi());
                nombre.setText(persona.getNombre());
             //   adress.setText(persona.getApellido());
            } else
                Toast.makeText(PersonaFormulario.this, "Problemas al obtener el objeto", Toast.LENGTH_LONG).show();
        }

    }


    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        PersonaFormulario mainActivity;

        public PersonaFormulario getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(PersonaFormulario mainActivity) {
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
            // Este metodo se ejecuta cuando el GPS es desactivado
           // tv1.setText("GPS Desactivado");
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



}
