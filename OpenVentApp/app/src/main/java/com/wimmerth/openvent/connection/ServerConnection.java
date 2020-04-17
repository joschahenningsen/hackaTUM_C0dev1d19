package com.wimmerth.openvent.connection;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class ServerConnection extends Thread {
    Socket s;
    static final String server = "openvent.joschas.page";
    static final int port = 5005;

    public ServerConnection(){

    }

    @Override
    public void run() {
        try {
            communicate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void communicate() throws IOException {
        try {
            s = new Socket(server, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader;
        PrintWriter writer;
        try {
            reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        writer.println("hallo fabian");
        writer.flush();
        Log.d("joscha", reader.readLine());
    }
}
