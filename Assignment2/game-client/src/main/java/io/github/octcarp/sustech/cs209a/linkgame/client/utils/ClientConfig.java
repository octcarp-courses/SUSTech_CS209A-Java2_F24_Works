package io.github.octcarp.sustech.cs209a.linkgame.client.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClientConfig {
    private static String serverAddress;
    private static int serverPort;

    static {
        try (InputStream input = ClientConfig.class.getClassLoader().getResourceAsStream("client_config.properties")) {
            if (input == null) {
                System.out.println("Sorry, can not find 'client_config.properties'");
                throw new IOException();
            }

            Properties prop = new Properties();
            prop.load(input);

            serverAddress = prop.getProperty("server.addr");
            serverPort = Integer.parseInt(prop.getProperty("server.port"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getServerAddress() {
        return serverAddress;
    }

    public static int getServerPort() {
        return serverPort;
    }
}
