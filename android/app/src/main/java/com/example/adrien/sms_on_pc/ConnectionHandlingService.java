/**
 * Handles connection with server
 */

package com.example.adrien.sms_on_pc;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;


public class ConnectionHandlingService extends Service {

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

        Bundle bundle = intent.getExtras();

        // Access granted by user -> establish socket.io connection
        if (bundle.getBoolean("grant") == true) {
            // Connect to server
            try {
                socket = IO.socket(bundle.getString("server_info"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            socket.connect();
        }

        return START_REDELIVER_INTENT;
    }
}
