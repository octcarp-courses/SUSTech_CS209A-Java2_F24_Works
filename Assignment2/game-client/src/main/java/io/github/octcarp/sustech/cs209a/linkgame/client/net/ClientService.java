package io.github.octcarp.sustech.cs209a.linkgame.client.net;

import io.github.octcarp.sustech.cs209a.linkgame.client.utils.AlertPopper;
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.ClientConfig;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Request;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.RequestType;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientService {
    private final Socket socket;
    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;
    private final ServerHandlerThread listener;

    private String myId = null;

    private static ClientService instance = null;


    private ClientService(String serverAddress, int port) {
        try {
            socket = new Socket(InetAddress.getByName(serverAddress), port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            listener = new ServerHandlerThread(socket, ois);
            new Thread(listener).start();
        } catch (IOException e) {
            if (e instanceof ConnectException) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Network Initialization Error");
                alert.setHeaderText("Can't connect to server");
                alert.setContentText("Please check your network, or server is down");
                alert.showAndWait();
            }
            throw new RuntimeException(e);
        }
    }

    public static ClientService getInstance() {
        if (instance == null) {
            String serverAddress = ClientConfig.getServerAddress();
            int serverPort = ClientConfig.getServerPort();
            instance = new ClientService(serverAddress, serverPort);
        }
        return instance;
    }

    public void disconnect(){
        try {
            oos.writeObject(new Request(RequestType.DISCONNECT));
            ois.close();
            oos.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            AlertPopper.popNetErrAndExit();
        }
    }

    public void sendRequest(Request request) {
        try {
            if (request.getSender() == null) {
                request.setSender(myId == null ? "default" : myId);
            }
            oos.writeObject(request);
            oos.flush();
        } catch (IOException e) {
            AlertPopper.popNetErrAndExit();
            throw new RuntimeException(e);
        }
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getMyId() {
        return myId;
    }
}