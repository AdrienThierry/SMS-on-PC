package com.example.adrien.sms_on_pc;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

public class MainActivityFragment extends Fragment {

    Thread discoveryThread;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Broadcast service using mDNS for phone discovery by server
        try {
            discoveryThread = new Discovery((MainActivity)getActivity());
            discoveryThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((Discovery)discoveryThread).registerService();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((Discovery)discoveryThread).unregisterService();
    }
}
