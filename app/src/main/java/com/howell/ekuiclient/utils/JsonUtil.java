package com.howell.ekuiclient.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by howell on 2016/11/4.
 */

public class JsonUtil {

    public static String createIPJstr(String ip){
        JSONObject o = new JSONObject();
        try {
            o.put("ClientIP",ip);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return o.toString();
    }

    public static String getIPFromJstr(String jsonStr){
        String str = null;
        JSONObject o = null;
        try {
            o = new JSONObject(jsonStr);
            str = o.getString("ServerIP");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }
}
