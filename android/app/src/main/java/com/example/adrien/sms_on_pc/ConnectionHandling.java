/**
 * Handles connection with server after user has chosen a phone
 */

package com.example.adrien.sms_on_pc;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandling extends Thread {

    ServerSocket serverSocket;

    public ConnectionHandling(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {

            Socket server = this.serverSocket.accept();
            DataInputStream in = new DataInputStream(server.getInputStream());
            final BufferedReader inBuf = new BufferedReader(new InputStreamReader(in));

            final String text = inBuf.readLine();

            Log.e("Yolo", text);

            server.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
