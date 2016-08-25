package com.example.adrien.sms_on_pc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivityFragment extends Fragment {

    private TextView textDiscoveryServiceState;
    private Button buttonToggleDiscoveryServiceState;

    private Intent discoveryServiceIntent;

    private BroadcastReceiver updateUIReceiver; // Allow outside to force UI update

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        discoveryServiceIntent = new Intent(getContext(), DiscoveryService.class);

        textDiscoveryServiceState = (TextView)view.findViewById(R.id.textDiscoveryServiceState);
        buttonToggleDiscoveryServiceState = (Button)view.findViewById(R.id.buttonToggleDiscoveryServiceState);

        updateView();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.MAIN_ACTIVITY_FRAGMENT_UPDATE_UI_ACTION);
        updateUIReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
               updateView();
            }
        };
        getActivity().registerReceiver(updateUIReceiver,filter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(updateUIReceiver);
    }

    // Update button and text according to discovery service state
    public void updateView() {
        // If service is running
        if (Utility.isMyServiceRunning(getContext(), DiscoveryService.class)) {
            textDiscoveryServiceState.setTextColor(Color.GREEN);
            textDiscoveryServiceState.setText(R.string.C_on);
            buttonToggleDiscoveryServiceState.setText(R.string.FC_stop_discovery);
            buttonToggleDiscoveryServiceState.setOnClickListener(stopService);
        }

        else {
            textDiscoveryServiceState.setTextColor(Color.RED);
            textDiscoveryServiceState.setText(R.string.C_off);
            buttonToggleDiscoveryServiceState.setText(R.string.FC_start_discovery);
            buttonToggleDiscoveryServiceState.setOnClickListener(startService);
        }
    }

    private View.OnClickListener startService = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getContext().startService(discoveryServiceIntent);
            updateView();
        }
    };

    private View.OnClickListener stopService = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getContext().stopService(discoveryServiceIntent);
            updateView();
        }
    };
}
