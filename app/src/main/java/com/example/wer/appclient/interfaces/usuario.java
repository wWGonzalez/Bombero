package com.example.wer.appclient.interfaces;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wer.appclient.R;

public class usuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(usuario.this);

        SharedPreferences.Editor myEditor = myPreferences.edit();

        myEditor.putString("NAME", "Alice");
        myEditor.putInt("AGE", 25);
        myEditor.putBoolean("SINGLE?", true);

    }
}
