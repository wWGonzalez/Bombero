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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.wer.appclient.R;
import com.example.wer.appclient.clases.HttpRequest;
import com.example.wer.appclient.clases.Persona;
import com.example.wer.appclient.clases.PersonaAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BuscarPersona extends AppCompatActivity {
    Spinner spinnerParametro;
    EditText dato;
    ListView listViewPersona;
    TextView tv1,tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_persona);
        inicializar();
    }
    public void inicializar(){
        this.spinnerParametro = (Spinner) findViewById(R.id.spinnerPersonParametros);
       // this.dato = (EditText) findViewById(R.id.editTextDato);
        this.listViewPersona = (ListView) findViewById(R.id.listViewPersonas);
        tv1 = findViewById(R.id.textViewG1);
        tv2 = findViewById(R.id.textViewG2);

        new getPersonas().execute("http://wzwer.pythonanywhere.com/rest/alert/");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
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

        tv1.setText("Localizacion agregada");
        tv2.setText("");
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
                    tv2.setText("Mi direccion es: \n"
                            + DirCalle.getAddressLine(0));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//Finaliza setLocation

    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        BuscarPersona mainActivity;

        public BuscarPersona getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(BuscarPersona mainActivity) {
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
            tv1.setText(Text);
            this.mainActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            tv1.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            tv1.setText("GPS Activado");
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

    //get personas
    public class getPersonas extends AsyncTask<String, Void, String>{
        public String doInBackground(String... params) {
            try {
                return HttpRequest.get(params[0]).accept("application/json").body();
            } catch (Exception e) {
                return "";
            }
        }

        public void onPostExecute(String result){
            if(result.isEmpty()){
                Toast.makeText(BuscarPersona.this,"No se generaron resultados",Toast.LENGTH_LONG).show();
            }else{
                ArrayList<Persona> personas = Persona.obtenerPersonas(result);
                ArrayList<Persona> personas_aux = new ArrayList();

                if(spinnerParametro.getSelectedItem().equals(("Listar Todo"))){
                    personas_aux = personas;
                }else{
                    for (int i = 0; i < personas.size(); i++){
                        switch (spinnerParametro.getSelectedItem().toString()){
                            case "DPI":
                                if(personas.get(i).getDpi().equals(dato.getText().toString().trim())){
                                    personas_aux.add(personas.get(i));
                                }
                                break;
                            case "Coordenadas":
                                if(personas.get(i).getCoordenadas().equals(dato.getText().toString().trim())){
                                    personas_aux.add(personas.get(i));
                                }
                                break;
                            case "Direccion":
                                if(personas.get(i).getDireccion().equals(dato.getText().toString().trim())){
                                    personas_aux.add(personas.get(i));
                                }
                                break;
                        }
                    }
                }
                if(personas_aux.size() != 0){
                    PersonaAdapter adapter = new PersonaAdapter(BuscarPersona.this, personas_aux);
                    listViewPersona.setAdapter(adapter);
                    listViewPersona.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent i = new Intent(BuscarPersona.this, PersonaFormulario.class);
                            i.putExtra("operacion", "actualizar");
                            i.putExtra("id_persona", ((Persona) parent.getAdapter().getItem(position)).getDpi());
                            startActivity(i);
                        }
                    });
                }
            }
        }
    }
}