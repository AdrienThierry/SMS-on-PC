/**
 * Discovery service
 * Starts on boot
 * Broadcasts phone ip and other info on the network so that the server can fetch it
 */

package com.example.adrien.sms_on_pc;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.IBinder;

import org.json.JSONException;

import java.io.IOException;
import java.net.ServerSocket;

public class DiscoveryService extends Service {
    private ServerSocket serverSocket;
    private String serviceName;
    private NsdServiceInfo nsdServiceInfo;
    private NsdManager nsdManager;
    private NsdManager.RegistrationListener registrationListener;

    public DiscoveryService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            serverSocket = new ServerSocket(0); // Use first available port
            ServerInfoFetcher serverInfoFetcher = new ServerInfoFetcher(serverSocket, getApplicationContext());
            serverInfoFetcher.start(); // Start java socket thread to handle connection with server
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set service name
        try {
            serviceName = ConfigParser.getConfig(getApplicationContext()).getString("nsd_service_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initializeRegistrationListener();
        initializeService(serverSocket.getLocalPort());
        registerService();

        // Let it continue running until it is stopped.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterService();
    }

    public void registerService() {
        nsdManager = (NsdManager)getApplicationContext().getSystemService(Context.NSD_SERVICE);
        nsdManager.registerService(nsdServiceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);
    }

    public void unregisterService() {
        if (nsdManager != null && registrationListener != null) {
            nsdManager.unregisterService(registrationListener);
        }
    }

    private void initializeService(int port) {
        nsdServiceInfo  = new NsdServiceInfo();

        nsdServiceInfo.setServiceName(this.serviceName + "/" + Utility.getDeviceName());
        nsdServiceInfo.setServiceType("_http._tcp.");
        nsdServiceInfo.setPort(port);
    }

    private void initializeRegistrationListener() {
        registrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                // Save the service name.  Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
                serviceName = NsdServiceInfo.getServiceName();
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Registration failed!  Put debugging code here to determine why.
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                // Service has been unregistered.  This only happens when you call
                // NsdManager.unregisterService() and pass in this listener.
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Unregistration failed.  Put debugging code here to determine why.
            }
        };
    }
}
