package com.example.adrien.sms_on_pc;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;

import org.json.JSONException;

import java.io.IOException;
import java.net.ServerSocket;

public class Discovery extends Thread {

    private MainActivity activity;
    private ServerSocket serverSocket;
    private String serviceName;
    private NsdServiceInfo nsdServiceInfo;
    private NsdManager nsdManager;
    private NsdManager.RegistrationListener registrationListener;

    public Discovery(MainActivity activity) throws IOException {
        this.activity = activity;
        this.serverSocket = new ServerSocket(0); // Use first available port
        try {
            this.serviceName = ConfigParser.getConfig(activity).getString("nsd_service_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        initializeRegistrationListener();
        initializeService(this.serverSocket.getLocalPort());
    }

    public void registerService() {
        nsdManager = (NsdManager)activity.getSystemService(Context.NSD_SERVICE);
        nsdManager.registerService(nsdServiceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);
    }

    public void unregisterService() {
        if (nsdManager != null && registrationListener != null) {
            nsdManager.unregisterService(registrationListener);
        }
    }

    private void initializeService(int port) {
        nsdServiceInfo  = new NsdServiceInfo();

        nsdServiceInfo.setServiceName(this.serviceName + "/" + getDeviceName());
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

    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

}
