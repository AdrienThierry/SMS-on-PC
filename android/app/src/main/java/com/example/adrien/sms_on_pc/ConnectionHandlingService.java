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

public class ConnectionHandlingService extends Service {
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
            Log.e("Yolo", "Yolo " + bundle.getString("server_info"));
        }

        return START_REDELIVER_INTENT;
    }
}
