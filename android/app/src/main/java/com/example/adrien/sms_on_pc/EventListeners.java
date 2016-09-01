/**
 * Server events listeners
 */

package com.example.adrien.sms_on_pc;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class EventListeners {

    private static JSONObject config;
    private static Context mContext;

    public static void register(Context context, Socket socket) {
        mContext = context;
        config = ConfigParser.getConfig(mContext);
        register_ask_contact_list(socket);
        register_ask_sms_list(socket);
    }

    // --------------------------------------------------
    // On contacts list asked by server
    // --------------------------------------------------
    private static void register_ask_contact_list(Socket socket) {
        final Socket mSocket = socket;

        try {
            socket.on(config.getString("EVENT_ask_contact_list"), new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    EventEmitters.sendContactList(mSocket, Contacts.getContactsWithMessages());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // --------------------------------------------------
    // On SMS asked by server
    // --------------------------------------------------
    private static void register_ask_sms_list(Socket socket) {
        final Socket mSocket = socket;

        try {
            socket.on(config.getString("EVENT_ask_SMS_list"), new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject)args[0];

                    try {
                        int contactID = Integer.parseInt(data.getString("contact_id"));
                        String browserID = data.getString("browser_id");
                        EventEmitters.sendSMSList(mSocket, browserID, SMS.getAllSmsFromContact(contactID));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
