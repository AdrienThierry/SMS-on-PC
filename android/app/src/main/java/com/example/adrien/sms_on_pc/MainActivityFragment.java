package com.example.adrien.sms_on_pc;

import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    TextView ipAddress;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ipAddress = (TextView)view.findViewById(R.id.ipAddress);

        // Get WiFi IP address
        WifiManager wm = (WifiManager) getActivity().getSystemService(getContext().WIFI_SERVICE);
        int ip = wm.getConnectionInfo().getIpAddress();
        String ipString = String.format("%d.%d.%d.%d", (ip & 0xff),(ip >> 8 & 0xff),(ip >> 16 & 0xff),(ip >> 24 & 0xff));

        // Show IP address
        ipAddress.setText(ipString);

        // Broadcast service using mDNS
        try {
            Thread t = new Discovery((MainActivity)getActivity());
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }
}
