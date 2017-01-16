package com.example.melky.robotwifi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by MELKY on 08/04/2016.
 */
public class ControladorDatos extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent detalles = getIntent();
        if (detalles.getAction() == Intent.ACTION_SEND) {
            String datos_recibidos = detalles.getStringExtra(Constantes.datos_recibidos);
            Log.d("dato:",datos_recibidos);

            //EditText et = (EditText) findViewById(R.id.editText);
            //et.append(datos_recibidos);

            /*
            String datos_separados[] = datos_recibidos.split(Constantes.separador);
            String orden = datos_separados[0];
            if (orden.startsWith(Constantes.Musica)) {
                orden = datos_separados[1];
                if(orden.startsWith(Constantes.Cancion)) {
                    String cancion = datos_separados[2];

                    Guardar(this, Constantes.cancion, cancion);
                }
                else if(orden.startsWith(Constantes.Grupo)) {
                    String grupo = datos_separados[2];

                    Guardar(this, Constantes.grupo, grupo);
                }
                else if(orden.startsWith(Constantes.Volumen))
                {
                    String volumen = datos_separados[2];

                    Guardar(this, Constantes.volumen, Integer.parseInt(volumen));
                }
            } else if (orden.startsWith(Constantes.Vincular)) {
                ControladorDatos.Guardar(this, Constantes.vinculado, true);
            }*/
        }
        finish();
    }

    public static void Guardar(Context ct, String nombre, String dato) {
        SharedPreferences settings = ct.getSharedPreferences("PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(nombre, dato);
        editor.commit();
    }

    public static void Guardar(Context ct, String nombre, boolean dato) {
        SharedPreferences settings = ct.getSharedPreferences("PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(nombre, dato);
        editor.commit();
    }

    public static void Guardar(Context ct, String nombre, int dato) {
        SharedPreferences settings = ct.getSharedPreferences("PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(nombre, dato);
        editor.commit();
    }

    public static String Leer(Context ct, String nombre, String def) {
        SharedPreferences settings = ct.getSharedPreferences("PREF", MODE_PRIVATE);
        return settings.getString(nombre, def);
    }

    public static boolean Leer(Context ct, String nombre, boolean def) {
        SharedPreferences settings = ct.getSharedPreferences("PREF", MODE_PRIVATE);
        return settings.getBoolean(nombre, def);
    }

    public static int Leer(Context ct, String nombre, int def) {
        SharedPreferences settings = ct.getSharedPreferences("PREF", MODE_PRIVATE);
        return settings.getInt(nombre, def);
    }

    public static void BorrarDatos(Context ct) {
        SharedPreferences settings = ct.getSharedPreferences("PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }
}
