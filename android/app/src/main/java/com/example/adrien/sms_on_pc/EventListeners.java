/**
 * Server events listeners
 */

package com.example.adrien.sms_on_pc;

import android.app.usage.UsageEvents;
import android.content.Context;

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
                    EventEmitters.sendContactList(mSocket, Contacts.getContacts());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
