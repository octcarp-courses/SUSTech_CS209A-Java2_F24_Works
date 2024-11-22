package io.github.octcarp.sustech.cs209a.practice.p9t2.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerHandler {
    private ChatClientController controller;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;

    Socket socket;

    public ServerHandler(ChatClientController controller, String clientName) {
        this.controller = controller;
        try {
            Socket socket = new Socket("localhost", 7890);
            this.socket = socket;

            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(new MessageReceiver(in, controller)).start();

            this.clientName = clientName;
            sendMessage(clientName);
        } catch (IOException e) {
            System.out.println("Disconnected from server");
        }
    }

    public void sendMessage(String message) {
        try {
            out.println(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeSocket() {
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


class MessageReceiver implements Runnable {
    private final BufferedReader in;
    private final ChatClientController controller;

    public MessageReceiver(BufferedReader in, ChatClientController controller) throws IOException {
        this.in = in;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                controller.addNewMessage(message);
            }
        } catch (IOException e) {
            System.out.println("Disconnected from server");
        }
    }
}