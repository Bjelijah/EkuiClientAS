package com.howell.ekuiclient.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import static android.text.format.Formatter.formatIpAddress;

/**
 * Created by howell on 2016/11/4.
 */

public class PhoneConfig {
    public static String getPhoneIP(Context context){
        String ip = null;
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
           return "0.0.0.0";
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        ip = formatIpAddress(ipAddress);
        return  ip;
    }



}
