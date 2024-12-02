package io.github.octcarp.sustech.cs209a.linkgame.server;


import io.github.octcarp.sustech.cs209a.linkgame.server.net.ClientHandlerThread;
import io.github.octcarp.sustech.cs209a.linkgame.server.utils.ServerConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    public static void main(String[] args) {
        Server server = new Server();

        server.startServer();
    }
}


class Server {

    private final int port;

    ExecutorService threadPool;

    public Server() {
        this.port = ServerConfig.getServerPort();
    }

    public Server(int port) {
        this.port = port;
    }

    public void initServer() {
        threadPool = Executors.newCachedThreadPool();
    }

    public void startServer() {
        initServer();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client Connect:" + socket.getRemoteSocketAddress());
                threadPool.execute(new ClientHandlerThread(socket));
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}