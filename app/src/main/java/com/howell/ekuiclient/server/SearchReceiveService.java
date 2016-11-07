package com.howell.ekuiclient.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.utils.IConst;
import com.howell.ekuiclient.utils.JsonUtil;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

/**
 * Created by howell on 2016/11/4.
 */

public class SearchReceiveService extends Service implements IConst {

    SearchReceiveThread mThread = null;
    private MulticastSocket ds;
    String multicastHost="224.0.0.1";
    InetAddress receiveAddress;

    private DatagramSocket mSocket = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startSearchReceiveThread();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startSearchReceiveThread(){


        try {
            mSocket = new DatagramSocket(CLIENT_SEARCH_RECEIVE_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }


        if (mThread==null){
            mThread = new SearchReceiveThread();
            mThread.start();
        }
    }

    private void sendBrocast(String serverIP){
        //只有动态注册过接受器的广播才能监听到
        Intent myIntent = new Intent(CLIENT_RECEIVE_ACTION);//
        myIntent.putExtra("ServerIP", serverIP);
        sendBroadcast(myIntent);
    }

    class SearchReceiveThread extends Thread{

        @Override
        public void run() {
            super.run();
            byte buf[] = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buf, 1024);
            while (true){
                try {
                    mSocket.receive(dp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String str = new String(buf,0,dp.getLength());
                Log.i("123","SearchReceiveThread str="+str);
                String sIp = JsonUtil.getIPFromJstr(str);
                sendBrocast(sIp);
            }



        }
    }



}
