package com.example.melky.robotwifi;

/**
 * Created by MELKY on 18/03/2016.
 */
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.DatagramChannel;


public class EnviarUDP extends Thread
{
    String Msg;
    String ip_remota;
    String ip_local;
    int puerto_remoto;
    int puerto_local;
    //Context Cont;
    public EnviarUDP(String local,String puerto_l,String rem,String puerto_r,String str)
    {
        super(str);
        //Cont = ct;
        ip_local = local;
        ip_remota = rem;
        Msg = str;
        puerto_local = Integer.parseInt(puerto_l);
        puerto_remoto = Integer.parseInt(puerto_r);
    }
    public void run()
    {
        DatagramChannel UDPChannel;
        DatagramSocket UDPSocket;
        try
        {
            UDPChannel = DatagramChannel.open();
            UDPSocket = UDPChannel.socket();
            UDPSocket.setReuseAddress(true);
            byte[] buf = new byte[1024];
            buf = Msg.getBytes();
            //String ip = "192.168.0.115";
            //String ip = getLocalIpAddress();

            UDPSocket.bind(new InetSocketAddress(ip_local, puerto_local));
            InetAddress addr = InetAddress.getByName(ip_remota);//("192.168.0.108");
            DatagramPacket packet = new DatagramPacket(buf, buf.length, addr, puerto_remoto);
            UDPSocket.send(packet);
            UDPSocket.close();
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
