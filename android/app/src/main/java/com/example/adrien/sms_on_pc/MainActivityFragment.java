package com.example.adrien.sms_on_pc;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivityFragment extends Fragment {

    private TextView textDiscoveryServiceState;
    private Button buttonToggleDiscoveryServiceState;

    private Intent discoveryServiceIntent;

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

        return view;
    }

    // Update button and text according to discovery service state
    private void updateView() {
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
