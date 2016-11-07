package com.howell.ekuiclient.action;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.utils.IConst;
import com.howell.ekuiclient.fragment.DeviceListFragment;
import com.howell.ekuiclient.server.SearchReceiveService;
import com.howell.ekuiclient.utils.JsonUtil;
import com.howell.ekuiclient.utils.PhoneConfig;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by howell on 2016/11/4.
 */

public class SearchAction implements IConst{
    private static SearchAction mInstance = null;
    public static SearchAction getInstance(){
        if (mInstance==null){
            mInstance = new SearchAction();
        }
        return mInstance;
    }
    private SearchAction(){}
    private MulticastSocket ms=null;
    Context mContext;
    MySearchReceive mReceive;
    Handler mHandler;
    public SearchAction setHandler(Handler h){
        this.mHandler = h;
        return  this;
    }
    public SearchAction setContext(Context c){
        this.mContext = c;
        return this;
    }
    public void searchMyDevice(){

        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendBrocast();
        startReceiveService();
    }

    private void init() throws IOException {

        mReceive = new MySearchReceive();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CLIENT_RECEIVE_ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mContext.registerReceiver(mReceive, filter);
    }

    private void startReceiveService(){
        Intent intent = new Intent(mContext,SearchReceiveService.class);
        mContext.startService(intent);
    }

    private void sendBrocast(){
        Log.i("123","sendBrocast");
        new Thread(){
            @Override
            public void run() {
                super.run();
                DatagramPacket dataPacket = null;
                byte [] data = JsonUtil.createIPJstr(PhoneConfig.getPhoneIP(mContext)).getBytes();
                try {
                    ms = new MulticastSocket();
                    ms.setTimeToLive(4);
                    InetAddress address = InetAddress.getByName("224.0.0.1");
                    dataPacket = new DatagramPacket(data,data.length,address,CLIENT_SEARCH_SEND_PORT);
                    Log.i("123","sendBrocast:"+new String(data,0,data.length));
                    ms.send(dataPacket);
                    ms.close();

                    Log.i("123","sendend");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public class MySearchReceive extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            String sIP = intent.getStringExtra("ServerIP");
            if (mHandler!=null){
                Message msg = new Message();
                msg.what = DeviceListFragment.MSG_RECEIVE_SIP;
                msg.obj = sIP;
                mHandler.sendMessage(msg);
            }
        }
    }
}
