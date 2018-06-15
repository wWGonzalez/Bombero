package app.keepthink.user.KeepThink.interfaces;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wer.KeepThink.R;
//import com.example.wer.appclient.clases.Archivo;
import app.keepthink.user.KeepThink.clases.Persona;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button show;
    Dialog MyDialog;
    Dialog MyDialog1;
    Button btn_signup1;
    Button hello;
    ImageView image;
    Button close;
    TextView tv1;
    private String dato="";

    //LinerLayout de cada tipo de emergencia
    LinearLayout linearLayout0;
    LinearLayout linearLayout1;
    LinearLayout linearLayout2;
    LinearLayout linearLayout3;
    LinearLayout linearLayout4;
    LinearLayout linearLayout5;
    LinearLayout mainGrid;
    Menu menu;
    //Variables para Post
    public String emergencia;
    Persona persona;
    String id_persona;
    String dir; //Se almacena la direccion
    String coor; //Se almacenan las coordenadas

    private Locale locale;
    private Configuration config = new Configuration();
    //Variables tuto
    private ViewPager slideViewPager;
    private LinearLayout dotLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainGrid = (LinearLayout) findViewById(R.id.mainGrid);
        linearLayout0 = findViewById(R.id.linear_0);
        linearLayout1 = findViewById(R.id.linear_1);
        linearLayout2 = findViewById(R.id.linear_2);
        linearLayout3 = findViewById(R.id.linear_3);
        //Set Event
       // setSingleEvent(mainGrid);
        setLiner0(linearLayout0);
        setLiner1(linearLayout1);
        setLiner2(linearLayout2);
        setLiner3(linearLayout3);
        //setToggleEvent(mainGrid);
        Bundle bundle = getIntent().getExtras();
        //tv1 = findViewById(R.id.txtUSer);
        //tv1.setText(dato); //Muestra el usuario
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
        SharedPreferences nombre = getSharedPreferences("datos", Context.MODE_PRIVATE);
        Toast.makeText(this,"Bienvendio "+nombre.getString("name",""), Toast.LENGTH_SHORT).show();
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
                    case 2:
                        locale = new Locale("mam");
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
                Uri uri = Uri.parse("http://bomberossanpedro.pythonanywhere.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            case R.id.action_desarrolladores:
                //crear nuevo aviso
                abrir_desarrolladores(null);
                return true;
            case R.id.action_cambiar_idioma:
                //crear nuevo aviso
                showDialog();
                return true;
            case R.id.action_tutorial:
                abrir_tutorial(null);
                return true;
            case R.id.action_salir:
                finish();
                return true;
            default:
                return false;
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
            HttpPost httpPost = new HttpPost("http://bomberossanpedro.pythonanywhere.com/rest/alert/");
            httpPost.setHeader("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            try {
                SharedPreferences nombre = getSharedPreferences("datos",Context.MODE_PRIVATE);
                SharedPreferences telefono = getSharedPreferences("datos",Context.MODE_PRIVATE);
            //  jsonObject.put("dpi", dpi);
                jsonObject.put("nombre", nombre.getString("name",""));
                jsonObject.put("telefono", telefono.getString("phone",""));
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
            String Text = "" + loc.getLatitude() + "," + loc.getLongitude();
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

    //Establece evento para Liner 0
    private  void setLiner0(LinearLayout liner){
       // LinearLayout menu_photos = (LinearLayout )findViewById(R.id.linear_semestre1);
        liner.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                MyCustomAlertDialog("Maternidad");
            }
        });
    }

    //Establece evento para Liner 1
    private  void setLiner1(LinearLayout liner){
        // LinearLayout menu_photos = (LinearLayout )findViewById(R.id.linear_semestre1);
        liner.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                MyCustomAlertDialog("Accidente");
            }
        });
    }

    //Establece evento para Liner 2
    private  void setLiner2(LinearLayout liner){
        // LinearLayout menu_photos = (LinearLayout )findViewById(R.id.linear_semestre1);
        liner.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                MyCustomAlertDialog("Incendio");
            }
        });
    }

    //Establece evento para Liner 3
    private  void setLiner3(LinearLayout liner){
        // LinearLayout menu_photos = (LinearLayout )findViewById(R.id.linear_semestre1);
        liner.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                MyCustomAlertDialog("Primeros Auxilios");
            }
        });
    }

    private void setSingleEvent(LinearLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            LinearLayout card = (LinearLayout) mainGrid.getChildAt(i);
            final int finalI = i;
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch(finalI) {
                        case 0:
                           // hola();
                          //  MyCustomAlertDialog("Maternidad");
                            //Toast.makeText(this, "Maternidad", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                         //   MyCustomAlertDialog("Accidente");
                           // sentenciaN;
                            break;
                        case 2:
                          //  MyCustomAlertDialog("Incendio");
                           // sentenciaN;
                            break;
                        case 3:
                          //  MyCustomAlertDialog("Primeros Auxilios");
                            // sentenciaN;
                            break;
                    }
                }
            });
        }
    }

    //Llamar menu emergente
    public void MyCustomAlertDialog(String m){
        TextView alerta;

        final String emerg =m; //recive tipo de emergencia

        MyDialog = new Dialog(MainActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.activity_menu_emergente);

      //  MyDialog.setTitle("My Custom Dialog");

        hello = (Button) MyDialog.findViewById(R.id.hello);
        close = (Button) MyDialog.findViewById(R.id.close);
        image = MyDialog.findViewById(R.id.imagenView);
        alerta = MyDialog.findViewById(R.id.txtAlert);

        if (emerg == "Maternidad"){
            Drawable myDrawable = getResources().getDrawable(R.drawable.p_maternidad);
            image.setImageDrawable(myDrawable);
        }
        else if(emerg == "Accidente"){
            Drawable myDrawable = getResources().getDrawable(R.drawable.p_accidentes);
            image.setImageDrawable(myDrawable);
        }
        else if(emerg == "Incendio"){
            Drawable myDrawable = getResources().getDrawable(R.drawable.p_incendios);
            image.setImageDrawable(myDrawable);
        }
        else if(emerg == "Primeros Auxilios"){
            Drawable myDrawable = getResources().getDrawable(R.drawable.p_primeros_auxilios);
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
    }//Finaliza menu emergente

    //para entrar al activity de buscar persona
    public void btn_buscarPersona(View view){
        Intent intent = new Intent(MainActivity.this, BuscarPersona.class);
        startActivity(intent);
    }

    public void abrir_desarrolladores(View view){
        Intent intent = new Intent(MainActivity.this, desarrolladores.class);
        startActivity(intent);
    }

    public void abrir_tutorial(View view){
        Intent intent = new Intent(MainActivity.this, tuto.class);
        startActivity(intent);
    }

    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            onNetworkChange(ni);
        }
    };

    @Override
    protected void onPause() {
        unregisterReceiver(networkStateReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    int val=1;

    //Clase para mostrar si esta conectado a la red
    private void onNetworkChange(NetworkInfo networkInfo) {
        isOnlineNet();

        if (networkInfo != null) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                Log.d("MenuActivity", "CONNECTED");
               if (val != 1){
                  //  Toast.makeText(this, "Conectado a internet", Toast.LENGTH_SHORT).show();
                }
                else{
                   Toast.makeText(this, "No tiene acceso a internet", Toast.LENGTH_SHORT).show();
               }

                //Toast.makeText(this,"Conectado a internet",Toast.LENGTH_SHORT).show();
            } else {
                Log.d("MenuActivity", "DISCONNECTED");

              //  Toast.makeText(this,"No Conectado a Internet",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this,"Problemas al conectarse a la Red",Toast.LENGTH_SHORT).show();
            // finish();
        }
    }

    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

             val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}