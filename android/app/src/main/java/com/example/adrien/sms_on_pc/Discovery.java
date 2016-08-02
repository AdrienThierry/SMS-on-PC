package com.example.adrien.sms_on_pc;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;

import java.io.IOException;
import java.net.ServerSocket;

public class Discovery extends Thread {

    private MainActivity activity;
    private ServerSocket serverSocket;
    private String serviceName;
    private NsdManager nsdManager;
    private NsdManager.RegistrationListener registrationListener;

    public Discovery(MainActivity activity) throws IOException {
        this.activity = activity;
        this.serverSocket = new ServerSocket(0); // Use first available port
        this.serviceName = "Android_SMS_on_PC";
    }

    public void run() {
        initializeRegistrationListener();
        registerService(this.serverSocket.getLocalPort());
    }

    private void registerService(int port) {
        NsdServiceInfo serviceInfo  = new NsdServiceInfo();

        serviceInfo.setServiceName(this.serviceName);
        serviceInfo.setServiceType("_http._tcp.");
        serviceInfo.setPort(port);

        nsdManager = (NsdManager)activity.getSystemService(Context.NSD_SERVICE);

        nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);
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
