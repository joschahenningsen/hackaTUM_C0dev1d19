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
    private final Caller c;
    Socket s;
    static final String server = "openvent.joschas.page";
    static final int port = 5005;
    final int id;

    public ServerConnection(Caller c, int id) {
        this.c = c;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            communicate();
        } catch (IOException e) {
            return;
        }
    }

    private void communicate() throws IOException {
        s = new Socket(server, port);

        BufferedReader reader;
        PrintWriter writer;
        reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

        writer.print(id);
        writer.flush();

        String line;
        while ((line = reader.readLine()) != null) {
            c.onResponse(line);
        }
    }
}
