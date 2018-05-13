package com.example.wer.appclient.clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.wer.appclient.R;
import java.util.ArrayList;
/**
 * Created by wer on 29/03/2018.
 */

public class PersonaAdapter extends BaseAdapter {
    Context context;
    ArrayList<Persona> personaArrayList;

    public PersonaAdapter(Context context, ArrayList<Persona> personaArrayList) {
        this.context = context;
        this.personaArrayList = personaArrayList;
    }

    @Override
    public int getCount() {
        return this.personaArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.personaArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertViw, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_persona, parent, false);

        TextView DPI = (TextView) view.findViewById(R.id.textView5);
        TextView Coordenadas = (TextView) view.findViewById(R.id.textView6);
        TextView Direccion = (TextView) view.findViewById(R.id.textView7);
        TextView Emergencia = (TextView) view.findViewById(R.id.textView8);

      //  TextView Apellido = (TextView) view.findViewById(R.id.textView7);

        Persona persona = this.personaArrayList.get(position);
        if(persona != null){
            DPI.setText(String.format("DPI: %s", persona.getDpi()));
            Coordenadas.setText(String.format("Coordenadas: %s", persona.getCoordenadas()));
            Direccion.setText(String.format("Direccion: %s", persona.getDireccion()));
            Emergencia.setText(String.format("Emergencia: %s", persona.getEmergencia()));


            //Nombre.setText(String.format("Coordenadas: %s", persona.getNombre()));

        }
        return view;
    }

}
