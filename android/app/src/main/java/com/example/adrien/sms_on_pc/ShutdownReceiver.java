/**
 * Broadcast receiver that fires on shutdown
 * Stops discovery service
 */

package com.example.adrien.sms_on_pc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShutdownReceiver extends BroadcastReceiver {
    public ShutdownReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Stop discovery service
        Intent stopServiceIntent = new Intent(context, DiscoveryService.class);
        context.stopService(stopServiceIntent);
    }
}
