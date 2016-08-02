package com.example.adrien.sms_on_pc;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Broadcast service using mDNS for phone discovery by server
        try {
            Thread t = new Discovery((MainActivity)getActivity());
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }
}
