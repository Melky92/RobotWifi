package com.example.melky.robotwifi;

/**
 * Created by MELKY on 08/04/2016.
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

public class RecibirUDP
{
    //String Dest;
    String ip;
    Context Cont;
    //aqui se declara el nuevo hilo
    private AsyncTask<Void, Void, Void> async;
    public static boolean Activo = false;
    int puerto;

    //public RecibirUDP(String local,String dest,Context ct)
    public RecibirUDP(String ip_local,String puerto_local,Context ct)
    {
        Cont = ct;
        ip = ip_local;
        puerto = Integer.parseInt(puerto_local);
        //Dest = dest;
        Activo = false;
    }

    @SuppressLint("NewApi")
    public void runUdpServer()
    {

        //MainActivity.MostrarMensaje(ip,String.valueOf(puerto));
        Activo = true;
        //aqui se define el nuevo hilo
        async = new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                byte[] buf = new byte[4096];
                DatagramPacket UDPPacket = new DatagramPacket(buf, buf.length);
                DatagramChannel UDPChannel = null;
                DatagramSocket UDPSocket = null;
                try
                {
                    UDPChannel = DatagramChannel.open();
                    UDPSocket = UDPChannel.socket();
                    UDPSocket.setReuseAddress(true);
                    UDPSocket.bind(new InetSocketAddress(ip, puerto));

                    while(Activo)
                    {
                        UDPSocket.receive(UDPPacket);

                        String datos = new String(UDPPacket.getData());
                        datos = datos.substring(0,UDPPacket.getLength());

                        //MainActivity.MostrarMensaje("mensaje",datos);

                        Log.d("Recibido",datos);
                        Intent IntentNuevoMensaje = new Intent(Cont, MainActivity.class);
                        IntentNuevoMensaje.setAction(Intent.ACTION_SEND);
                        IntentNuevoMensaje.putExtra(Constantes.datos_recibidos, datos);
                        Cont.startActivity(IntentNuevoMensaje);


                        /*Intent IntentNuevoMensaje = new Intent(Cont, ControladorDatos.class);
                        IntentNuevoMensaje.setAction(Intent.ACTION_SEND);
                        IntentNuevoMensaje.putExtra(Constantes.datos_recibidos, datos);
                        Cont.startActivity(IntentNuevoMensaje);*/

                        /*if(datos.startsWith(Constantes.Musica))
                        {
                            datos = datos.substring(6);

                            Intent IntentNuevoMensaje = new Intent(Cont, ControladorDatos.class);
                            IntentNuevoMensaje.setAction(Intent.ACTION_SEND);

                            IntentNuevoMensaje.putExtra(Constantes.accion, Constantes.Musica.toString());
                            IntentNuevoMensaje.putExtra("datos_cancion", datos);
                            Cont.startActivity(IntentNuevoMensaje);
                        }
                        else if(datos.startsWith(Constantes.Vincular))
                        {
                            ControladorDatos.Guardar(Cont,Constantes.vinculado,true);
                        }*/
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if (UDPSocket != null)
                    {
                        UDPSocket.close();
                    }
                }

                return null;
            }
        };

        //aqui se instancia el hilo
        if (Build.VERSION.SDK_INT >= 11) async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else async.execute();
    }

    public void stop_UDP_Server() {
        Activo = false;
    }
}