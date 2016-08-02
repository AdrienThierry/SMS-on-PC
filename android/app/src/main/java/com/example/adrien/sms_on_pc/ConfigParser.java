/**
 * Parsing of shared config file
 */

package com.example.adrien.sms_on_pc;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class ConfigParser {

    private static JSONObject config = null;

    private static String loadJSONFromAsset(Activity activity) {
        String json = null;
        try {
            InputStream is = activity.getResources().openRawResource(R.raw.conf);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject getConfig(Activity activity) {
        if (config == null) {
            try {
                config = new JSONObject(loadJSONFromAsset(activity));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return config;
    }
}
