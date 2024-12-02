package io.github.octcarp.sustech.cs209a.linkgame.server.utils;

import io.github.octcarp.sustech.cs209a.linkgame.common.model.Match;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.MatchRecord;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Player;

import io.github.octcarp.sustech.cs209a.linkgame.server.model.MatchInfo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FileIO {

    private final static URL csvFileURL
            = FileIO.class.getResource(ServerConfig.getPlayerRecordCsvPath());

    private final static URL matchRecordCsvURL
            = FileIO.class.getResource(ServerConfig.getMatchRecordCsvPath());

    private static File getPlayerListFile() {
        return new File(csvFileURL.getFile());
    }

    private static File getMatchRecordFile() {
        return new File(matchRecordCsvURL.getFile());
    }

    public static List<Player> readPlayerList() {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().
                setHeader("Id", "Passwd").setSkipHeaderRecord(true).build();
        List<Player> playerList = new CopyOnWriteArrayList<>();

        try (FileReader reader = new FileReader(FileIO.getPlayerListFile())) {
            Iterable<CSVRecord> records = csvFormat.parse(reader);
            for (CSVRecord record : records) {
                String id = record.get("Id");
                String passwd = record.get("Passwd");
                playerList.add(new Player(id, passwd));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerList;
    }

    public static List<MatchRecord> readMatchRecordList() {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().
                setHeader("p1", "p2", "p1_score", "p2_score", "end_time", "result").setSkipHeaderRecord(true).build();
        List<MatchRecord> matchRecordList = new CopyOnWriteArrayList<>();
        try (FileReader reader = new FileReader(FileIO.getMatchRecordFile())) {
            Iterable<CSVRecord> records = csvFormat.parse(reader);
            for (CSVRecord record : records) {
                MatchRecord matchRecord = new MatchRecord(record.get("p1"), record.get("p2"),
                        record.get("p1_score"), record.get("p2_score"),
                        record.get("end_time"), record.get("result"));
                matchRecordList.add(matchRecord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matchRecordList;
    }

    public synchronized static boolean updatePlayerByList(List<Player> playerList) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader("ID", "Password").build();
        try (
                PrintWriter writer = new PrintWriter(FileIO.getPlayerListFile());
                CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)
        ) {
            for (Player player : playerList) {
                csvPrinter.printRecord(player.id(), player.password());
            }
            csvPrinter.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized static boolean updateMatchRecordByList(List<MatchRecord> matchRecordList) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().
                setHeader("p1", "p2", "p1_score", "p2_score", "end_time", "result").build();
        try (
                PrintWriter writer = new PrintWriter(FileIO.getMatchRecordFile());
                CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)
        ) {
            for (MatchRecord matchRecord : matchRecordList) {
                csvPrinter.printRecord(matchRecord.p1(), matchRecord.p2(),
                        matchRecord.p1Score(), matchRecord.p2Score(),
                        matchRecord.endTime(), matchRecord.result());
            }
            csvPrinter.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
