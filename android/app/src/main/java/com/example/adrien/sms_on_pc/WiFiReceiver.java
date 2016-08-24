/**
 * Receives WiFi connectivity changes and toggles discovery service accordingly
 */

package com.example.adrien.sms_on_pc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class WiFiReceiver extends BroadcastReceiver {

    public WiFiReceiver() {
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent discoveryServiceIntent = new Intent(context, DiscoveryService.class);

        // Start discovery service if WiFI is connected and service is not started
        if (Utility.isWiFiConnected(context) && !Utility.isMyServiceRunning(context, DiscoveryService.class)) {
            context.startService(discoveryServiceIntent);
            Toast.makeText(context, "Wifi State change : start service", Toast.LENGTH_LONG).show();
        }
        // Stop discovery service if WiFi is disconnected
        else if (!Utility.isWiFiConnected(context) && Utility.isMyServiceRunning(context, DiscoveryService.class)){
            context.stopService(discoveryServiceIntent);
            Toast.makeText(context, "Wifi State change : stop service", Toast.LENGTH_LONG).show();
        }

        // Update UI
        Intent updateUI = new Intent();
        updateUI.setAction(Constants.MAIN_ACTIVITY_FRAGMENT_UPDATE_UI_ACTION);
        context.sendBroadcast(updateUI);
    }
}
