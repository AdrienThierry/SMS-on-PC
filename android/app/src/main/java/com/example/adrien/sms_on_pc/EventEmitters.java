/**
 * All events phone can emit to server
 */

package com.example.adrien.sms_on_pc;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import io.socket.client.Socket;

public class EventEmitters {

    private static Context mContext;
    private static int device_id;
    private static JSONObject config;

    // --------------------------------------------------
    // !! MUST BE CALLED BEFORE ANY OTHER FUNCTION OF
    // THIS CLASS !!
    // --------------------------------------------------
    public static void initialize(Context context) {
        mContext = context;

        SharedPreferences sharedPref = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        device_id = sharedPref.getInt(Constants.DEVICE_ID_KEY, -1);

        config = ConfigParser.getConfig(mContext);
    }

    // --------------------------------------------------
    // Send address list to server
    // --------------------------------------------------
    public static void sendAddressList(Socket socket, ArrayList<String> addresses) {
        JSONObject json = new JSONObject();

        JSONArray jsonAddresses = new JSONArray(addresses);

        try {
            json.put("device_id", device_id);
            json.put("addresses", jsonAddresses);
            socket.emit(config.getString("EVENT_send_address_list"), json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // --------------------------------------------------
    // Send contacts list to server
    // --------------------------------------------------
    public static void sendContactList(Socket socket, LinkedHashMap<String, String> contacts) {
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

    // --------------------------------------------------
    // Send SMS list to server
    // --------------------------------------------------
    public static void sendSMSList(Socket socket, String browserID, ArrayList<ArrayList<String>> sms) {
        ArrayList<String> smsTextArray = sms.get(0);

        JSONObject response = new JSONObject();
        ArrayList<JSONObject> SMS_list = new ArrayList<>();

        try {
            response.put("browser_id", browserID);

            for (int i = 0; i < smsTextArray.size(); i++) {
                SMS_list.add(new JSONObject());
                SMS_list.get(i).put("text", smsTextArray.get(i));
            }

            response.put("sms_list", SMS_list);

            socket.emit(config.getString("EVENT_send_SMS_list"), response);
        }
        catch (JSONException e) {

        }
    }

}
