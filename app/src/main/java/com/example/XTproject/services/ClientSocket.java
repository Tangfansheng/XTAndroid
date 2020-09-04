package com.example.XTproject.services;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

public class ClientSocket {
    private String serverHost;
    private int port;
    private Socket socket;
    private OutputStream outputStream;

    public ClientSocket(String host, int port){
        this.serverHost = host;
        this.port = port;
    }

    public void connectServer() throws IOException {
        this.socket = new Socket(serverHost, port);
        this.outputStream = socket.getOutputStream();
    }

    public void sendSinge(String message) throws IOException {
        try {
            this.outputStream.write(message.getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        this.outputStream.close();
        this.socket.close();
    }

}
