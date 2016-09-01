/**
 * Establishes socket.io connection with server
 */

package com.example.adrien.sms_on_pc;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.Ack;
import io.socket.client.IO;

public class ConnectionHandlingService extends Service {

    private JSONObject config;
    private io.socket.client.Socket socket;

    public ConnectionHandlingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Close notification
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(Constants.AUTHORIZE_NOTIFICATION_ID);

        final Bundle bundle = intent.getExtras();

        // Access granted by user -> establish socket.io connection
        if (bundle.getBoolean("grant") == true) {
            // Connect to server
            try {
                socket = IO.socket(bundle.getString("server_info"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            socket.connect();

            Contacts.initialize(getBaseContext());
            SMS.initialize(getBaseContext());
            EventEmitters.initialize(getBaseContext());
            EventListeners.register(getBaseContext(), socket);

            // Get shared config
            config = ConfigParser.getConfig(getBaseContext());

            // --------------------------------------------------
            // Send device ID to server
            // --------------------------------------------------
            // Check if device ID exists in shared preferences
            final SharedPreferences sharedPref = getBaseContext().getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPref.edit();
            int deviceID = sharedPref.getInt(Constants.DEVICE_ID_KEY, -1);

            if (deviceID == -1) { // No device ID => ask device ID from server
                try {
                    socket.emit(config.getString("EVENT_ask_new_device_id"), "", new Ack() {
                        @Override
                        public void call(Object... args) {

                            int response = Integer.parseInt(args[0].toString());
                            int newID = (int) (2 * Math.pow(10, Math.floor(Math.log10(response)) + 1) + response);
                            editor.putInt(Constants.DEVICE_ID_KEY, newID);
                            editor.commit();

                            try {
                                JSONObject phoneResponse = new JSONObject();
                                phoneResponse.put("device_id", String.valueOf(sharedPref.getInt(Constants.DEVICE_ID_KEY, -1)));
                                phoneResponse.put("second_party_id", bundle.getString("browser_id"));
                                socket.emit(config.getString("EVENT_device_id"), phoneResponse);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            else {
                try {
                    JSONObject phoneResponse = new JSONObject();
                    phoneResponse.put("device_id", String.valueOf(sharedPref.getInt(Constants.DEVICE_ID_KEY, -1)));
                    phoneResponse.put("second_party_id", bundle.getString("browser_id"));
                    socket.emit(config.getString("EVENT_device_id"), phoneResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        return START_REDELIVER_INTENT;
    }
}
