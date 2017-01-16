package com.example.melky.robotwifi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends Activity {

    Context ct;
    Vector<Integer> ids;
    Vector<Integer> ids_parp;
    Vector<Integer> ids_anim1;
    Vector<Integer> ids_anim2;
    Vector<Integer> ids_anim3;
    ImageView Cara;
    int anim;
    Integer anterior;
    int i;
    boolean animando;
    Integer siguiente;
    String nuevo;

    @Override
    protected void onNewIntent(Intent intent)
    {
        if(intent.getAction() == Intent.ACTION_SEND)
        {
            String[] datos = intent.getStringExtra(Constantes.datos_recibidos).split("@");
            if(datos.length > 0) {
                Log.d("datos0",datos[0]);
            }
            if(datos.length > 1) {
                if (datos[0].contentEquals(Constantes.animar)) {
                    Integer i = Character.getNumericValue(datos[1].charAt(0));
                    Log.d("datosI", String.valueOf(i));
                }
            }
            if(datos[0].contentEquals(Constantes.animar)){
                Integer i = Character.getNumericValue(datos[1].charAt(0));
                if(anterior == 0)
                {
                    siguiente = i;
                }
                else
                {
                    siguiente = i;
                    AnimOjoB();
                }
            }
            else if(datos[0].contentEquals(Constantes.parpadeo)){
                if(anterior != 0)
                {
                    siguiente = 0;
                    AnimOjoB();
                }
            }
            else if(datos[0].contentEquals(Constantes.tiempo)){
                ControladorDatos.Guardar(ct,Constantes.tiempo,intent.getIntExtra(Constantes.tiempo,300));
            }
        }
    }

    protected void InicializarIdsImagenes()
    {
        ids_parp = new Vector<Integer>();
        ids_parp.add(R.drawable.p1);
        ids_parp.add(R.drawable.p2);
        ids_parp.add(R.drawable.p3);
        ids_parp.add(R.drawable.p4);
        ids_parp.add(R.drawable.p5);
        ids_parp.add(R.drawable.p6);

        ids_anim1 = new Vector<Integer>();
        ids_anim1.add(R.drawable.a1);
        ids_anim1.add(R.drawable.a2);
        ids_anim1.add(R.drawable.a3);
        ids_anim1.add(R.drawable.a4);
        ids_anim1.add(R.drawable.a5);
        ids_anim1.add(R.drawable.a6);

        ids_anim2 = new Vector<Integer>();
        ids_anim2.add(R.drawable.b1);
        ids_anim2.add(R.drawable.b2);
        ids_anim2.add(R.drawable.b3);
        ids_anim2.add(R.drawable.b4);
        ids_anim2.add(R.drawable.b5);
        ids_anim2.add(R.drawable.b6);

        ids_anim3 = new Vector<Integer>();
        ids_anim3.add(R.drawable.c1);
        ids_anim3.add(R.drawable.c2);
        ids_anim3.add(R.drawable.c3);
        ids_anim3.add(R.drawable.c4);
        ids_anim3.add(R.drawable.c5);
        ids_anim3.add(R.drawable.c6);

    }

    protected PowerManager.WakeLock mWakeLock;
    @Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        ct = this;

        anterior = 0;
        anim = 0;

        //Cara = (ImageView) findViewById(R.id.ViewParpadeo);

        siguiente = 0;
        nuevo = "";

        NotificationCompat.Builder notiIP = new NotificationCompat.Builder(this)
                .setContentTitle("IP")
                .setContentText(getLocalIpAddress())
                .setSmallIcon(R.drawable.a1);

        NotificationManager NotM = (NotificationManager)getSystemService(this.NOTIFICATION_SERVICE);
        NotM.notify(10,notiIP.build());

        //NotificationCompat.Builder notiIP = new NotificationCompat.Builder(this)
        //        .setSmallIcon(R.mipmap.ic_launcher);


        //Flujo();

        /*Caras = new Vector<ImageView>();
        Caras.add((ImageView)findViewById(R.id.V1));
        Caras.add((ImageView)findViewById(R.id.V2));
        Caras.add((ImageView)findViewById(R.id.V3));
        Caras.add((ImageView)findViewById(R.id.V4));
        Caras.add((ImageView)findViewById(R.id.V5));
        Caras.add((ImageView)findViewById(R.id.V6));*/

        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();

        ControladorDatos.Guardar(ct, Constantes.ip_local, getLocalIpAddress());
        ControladorDatos.Guardar(ct,Constantes.puerto_local,getLocalPort());
        ControladorDatos.Guardar(ct, Constantes.ip_remota, getRemoteIpAddress());
        ControladorDatos.Guardar(ct, Constantes.puerto_remoto, getRemotePort());
        Iniciar_Recepcion();

        CerrarOjo();

    }
    @Override
    protected void onStart()
    {
        super.onStart();
    }
    AnimationDrawable frameAnimation;
    protected Integer ObtenerViewAnimA(Integer i) {
        Integer img = 0;

        switch (i){
            case 0:
                img = R.id.ViewCerrarOjo;
                break;
            case 1:
                img = R.id.ViewAnim1a;
                break;
            case 2:
                img = R.id.ViewAnim2a;
                break;
            case 3:
                img = R.id.ViewAnim3a;
                break;
            default:
                break;
        }
        return img;
    }
    protected Integer ObtenerViewAnimB(Integer i) {
        Integer img = 0;

        switch (i){
            case 0:
                img = R.id.ViewAbrirOjo;
                break;
            case 1:
                img = R.id.ViewAnim1b;
                break;
            case 2:
                img = R.id.ViewAnim2b;
                break;
            case 3:
                img = R.id.ViewAnim3b;
                break;
            default:
                break;
        }
        return img;
    }
    protected int getTotalDuration(AnimationDrawable f)
    {
        int dur = 0;
        for(int i = 0;i < f.getNumberOfFrames();i++)
        {
            dur += f.getDuration(i);
        }
        return dur;
    }
    protected void CerrarOjo()
    {
        ImageView img = (ImageView)findViewById(ObtenerViewAnimB(anterior));
        img.setVisibility(View.GONE);
        img = (ImageView)findViewById(R.id.ViewCerrarOjo);
        img.setVisibility(View.VISIBLE);
        frameAnimation = ((AnimationDrawable)img.getBackground());
        frameAnimation.start();
        anterior = 0;
        new CountDownTimer(getTotalDuration(frameAnimation),100){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                AbrirOjo();
            }
        }.start();
    }
    protected void AbrirOjo()
    {
        ImageView img = (ImageView)findViewById(R.id.ViewCerrarOjo);
        img.setVisibility(View.GONE);
        img = (ImageView)findViewById(R.id.ViewAbrirOjo);
        img.setVisibility(View.VISIBLE);
        frameAnimation = ((AnimationDrawable)img.getBackground());
        frameAnimation.start();

        new CountDownTimer(getTotalDuration(frameAnimation),100){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if(siguiente == 0)
                {
                    CerrarOjo();
                }
                else {
                    AnimOjoA();
                }
            }
        }.start();
    }

    protected void AnimOjoA()
    {
        ImageView img = (ImageView)findViewById(ObtenerViewAnimB(anterior));
        img.setVisibility(View.GONE);
        img = (ImageView)findViewById(ObtenerViewAnimA(siguiente));
        img.setVisibility(View.VISIBLE);
        frameAnimation = ((AnimationDrawable)img.getBackground());
        frameAnimation.start();
        anterior = siguiente;

        new CountDownTimer(getTotalDuration(frameAnimation),100){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if(siguiente != anterior)
                {
                    AnimOjoB();
                }
            }
        }.start();
    }

    protected void AnimOjoB()
    {
        ImageView img = (ImageView)findViewById(ObtenerViewAnimA(anterior));
        img.setVisibility(View.GONE);
        img = (ImageView)findViewById(ObtenerViewAnimB(anterior));
        img.setVisibility(View.VISIBLE);
        frameAnimation = ((AnimationDrawable)img.getBackground());
        frameAnimation.start();

        new CountDownTimer(getTotalDuration(frameAnimation),100){

            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                if(siguiente == 0)
                {
                    CerrarOjo();
                }
                else
                {
                    AnimOjoA();
                }

            }
        }.start();
    }
    public void MostrarMensaje(String titulo,String mensaje)
    {
        AlertDialog.Builder DialogoError  = new AlertDialog.Builder(ct);
        DialogoError.setTitle(titulo);
        DialogoError.setMessage(mensaje);
        DialogoError.show();
    }
    public void Enviar(String msg)
    {
        String ip_local = ControladorDatos.Leer(ct, Constantes.ip_local, "");
        String ip_rem = ControladorDatos.Leer(ct, Constantes.ip_remota, "");
        String puerto_loc = ControladorDatos.Leer(ct, Constantes.puerto_local, "");
        String puerto_rem = ControladorDatos.Leer(ct, Constantes.puerto_remoto, "");
        Enviar(msg, ip_local, puerto_loc, ip_rem, puerto_rem);
    }
    public void Enviar(String msg,String ip_local,String puerto_local,String ip_remota,String puerto_remoto)
    {
        if(ip_remota==null || ip_local == null || puerto_remoto == null ||
                ip_remota=="" || ip_local == "" || puerto_remoto == "") {
            MostrarMensaje("Error","Revisar datos de envío");
        }
        else {
            //MostrarMensaje(msg,ip_remota);
            new EnviarUDP(ip_local,puerto_local,ip_remota,puerto_remoto,msg).start();
        }
    }

    public void Iniciar_Recepcion() {
        if(!RecibirUDP.Activo) {
            String ip_local = ControladorDatos.Leer(ct,Constantes.ip_local,"");
            String puerto_local = ControladorDatos.Leer(ct,Constantes.puerto_local,"");
            RecibirUDP recepcion = new RecibirUDP(ip_local,puerto_local, this);
            Log.d("ip", ip_local);
            Log.d("puerto", puerto_local);
            recepcion.runUdpServer();
        }
    }

    public String getLocalIpAddress(){

        WifiManager wifiMan = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return ip;
    }
    public String getLocalPort(){
        //String ip = getLocalIpAddress();
        //String puerto = "8" + String.format("%3s",ip.substring(ip.lastIndexOf(".")+1)).replace(' ','0');
        String puerto = "8802";
        return puerto;
    }

    public String getRemoteIpAddress(){
        String ip = "192.168.1.8";
        return ip;
    }
    public String getRemotePort(){
        //String ip = getLocalIpAddress();
        //String puerto = "8" + String.format("%3s",ip.substring(ip.lastIndexOf(".")+1)).replace(' ','0');
        String puerto = "8802";
        return puerto;
    }
}
