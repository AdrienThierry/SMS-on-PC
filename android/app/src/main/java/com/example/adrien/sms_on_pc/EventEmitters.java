/**
 * All events phone can emit to server
 */

package com.example.adrien.sms_on_pc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import io.socket.client.Socket;

public class EventEmitters {

    private static Context mContext;
    private static int device_id;

    public static void initialize(Context context) {
        mContext = context;

        SharedPreferences sharedPref = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        device_id = sharedPref.getInt(Constants.DEVICE_ID_KEY, -1);
    }

    public static void sendContactList(Socket socket, LinkedHashMap<String, String> contacts) {
        JSONObject config = ConfigParser.getConfig(mContext);
        JSONObject jsonContacts = new JSONObject(contacts);

        JSONObject msg = new JSONObject();

        try {
            msg.put("device_id", device_id);
            msg.put("contacts", jsonContacts);
            socket.emit(config.getString("EVENT_send_contact_list"), msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
