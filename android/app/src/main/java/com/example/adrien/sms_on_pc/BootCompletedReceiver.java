/**
 * Broadcast receiver that fires on boot complete
 * Allows to start discovery service on boot
 */

package com.example.adrien.sms_on_pc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {
    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Start discovery service if WiFI is connected
        if (Utility.isWiFiConnected(context)) {
            Intent startServiceIntent = new Intent(context, DiscoveryService.class);
            context.startService(startServiceIntent);
        }
    }
}
