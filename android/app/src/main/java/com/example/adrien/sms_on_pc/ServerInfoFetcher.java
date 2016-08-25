/**
 * Fetches info sent by server after user has chosen a phone
 */

package com.example.adrien.sms_on_pc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerInfoFetcher extends Thread {

    ServerSocket serverSocket;
    Context context;

    public ServerInfoFetcher(ServerSocket serverSocket, Context context) {
        this.serverSocket = serverSocket;
        this.context = context;
    }

    @Override
    public void run() {
        while(true) {
            try {

                // Fetch server info
                Socket server = this.serverSocket.accept();
                DataInputStream in = new DataInputStream(server.getInputStream());
                final BufferedReader inBuf = new BufferedReader(new InputStreamReader(in));
                final String data = inBuf.readLine();
                final String browserID = data.split("-")[0];
                final String serverInfo = data.split("-")[1];
                final String ipAddress = serverInfo.split("\\://")[1].split("\\:")[0];
                server.close();

                // Show notification
                String title = context.getString(R.string.app_name);
                String smallText = context.getString(R.string.FC_authorize) + " " + ipAddress + " ?";
                String bigText = ipAddress + " " + context.getString(R.string.wants_connect);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.ic_dialog_email)
                        .setContentTitle(title)
                        .setContentText(smallText);

                mBuilder.setPriority(MAX_PRIORITY);
                mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

                // Start ConnectionHandlingService on OK button click
                Intent grantIntent = new Intent(context, ConnectionHandlingService.class);
                grantIntent.putExtra("grant", true);
                grantIntent.putExtra("server_info", serverInfo);
                grantIntent.putExtra("browser_id", browserID);
                PendingIntent grantPendingIntent = PendingIntent.getService(context, 0, grantIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                Intent declineIntent = new Intent(context, ConnectionHandlingService.class);
                declineIntent.putExtra("grant", false);
                PendingIntent declinePendingIntent = PendingIntent.getService(context, 1, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder.setContentIntent(declinePendingIntent);

                mBuilder.setStyle((new NotificationCompat.BigTextStyle()
                        .bigText(bigText)))
                        .addAction(R.drawable.ic_check_black_24dp,
                                context.getString(R.string.FC_grant), grantPendingIntent)
                        .addAction(R.drawable.ic_close_black_24dp,
                                context.getString(R.string.FC_deny), declinePendingIntent);

                // Sets an ID for the notification
                int mNotificationId = Constants.AUTHORIZE_NOTIFICATION_ID;
                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
