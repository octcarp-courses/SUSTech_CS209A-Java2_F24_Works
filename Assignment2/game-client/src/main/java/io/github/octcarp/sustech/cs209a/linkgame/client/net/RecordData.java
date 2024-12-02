package io.github.octcarp.sustech.cs209a.linkgame.client.net;

import io.github.octcarp.sustech.cs209a.linkgame.client.controller.MatchRecordController;
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.MatchRecord;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Request;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.RequestType;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordData {
    private static RecordData instance = new RecordData();
    private List<String> playerList;
    private Map<String, List<MatchRecord>> playerMatchRecordMap;

    private RecordData() {
        playerList = new ArrayList<>();
        playerMatchRecordMap = new HashMap<>();
    }

    public static RecordData getInstance() {
        return instance;
    }

    public void getRecordRequest() {
        Request request = new Request(RequestType.GET_MATCH_RECORD);

        ClientService.getInstance().sendRequest(request);
    }

    public void reSyncRecord(List<MatchRecord> records) {
        playerMatchRecordMap.clear();
        playerList.clear();
        for (MatchRecord record : records) {
            String p1 = record.p1();
            String p2 = record.p2();
            if (!playerList.contains(p1)) {
                playerList.add(p1);
            }
            if (!playerList.contains(p2)) {
                playerList.add(p2);
            }
            if (!playerMatchRecordMap.containsKey(p1)) {
                playerMatchRecordMap.put(p1, new ArrayList<>(List.of(record)));
            } else {
                playerMatchRecordMap.get(p1).add(record);
            }
            if (!playerMatchRecordMap.containsKey(p2)) {
                playerMatchRecordMap.put(p2, new ArrayList<>(List.of(record)));
            } else {
                playerMatchRecordMap.get(p2).add(record);
            }
        }

        MatchRecordController controller =
                (MatchRecordController) SceneSwitcher.getInstance().getController("match-record");

        controller.updateWaitingPlayers(playerList);
    }

    public List<MatchRecord> getRecordByPlayer(String player) {
        return playerMatchRecordMap.get(player);
    }
}
