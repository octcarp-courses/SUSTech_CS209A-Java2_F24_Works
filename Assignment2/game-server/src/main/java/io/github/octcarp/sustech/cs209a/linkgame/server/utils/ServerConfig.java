package io.github.octcarp.sustech.cs209a.linkgame.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ServerConfig {
    private static int serverPort;
    private static List<String> reservedIds = new ArrayList<>();
    private static String playerRecordCsvPath;
    private static String matchRecordCsvPath;
    private static int maxClientNum = 10;


    static {
        try (InputStream input = ServerConfig.class.getClassLoader().getResourceAsStream("server_config.properties")) {
            if (input == null) {
                System.out.println("Sorry, can not find 'server_config.properties'");
                throw new IOException();
            }

            Properties prop = new Properties();
            prop.load(input);

            serverPort = Integer.parseInt(prop.getProperty("server.port"));
            playerRecordCsvPath = prop.getProperty("player_record_csv_path");
            matchRecordCsvPath = prop.getProperty("match_record_csv_path");
            maxClientNum = Integer.parseInt(prop.getProperty("max_client_num"));

            String reservedIdsStr = prop.getProperty("reserved.ids");
            if (reservedIdsStr != null && !reservedIdsStr.isEmpty()) {
                String[] ids = reservedIdsStr.split(",");
                for (String id : ids) {
                    reservedIds.add(id.trim());
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static List<String> getReservedIds() {
        return reservedIds;
    }

    public static String getPlayerRecordCsvPath() {
        return playerRecordCsvPath;
    }

    public static int getMaxClientNum() {
        return maxClientNum;
    }

    public static String getMatchRecordCsvPath() {
        return matchRecordCsvPath;
    }
}