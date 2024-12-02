package io.github.octcarp.sustech.cs209a.linkgame.server.net;

import io.github.octcarp.sustech.cs209a.linkgame.common.model.Match;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.MatchRecord;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Player;
import io.github.octcarp.sustech.cs209a.linkgame.server.model.MatchInfo;
import io.github.octcarp.sustech.cs209a.linkgame.server.utils.FileIO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RecordManager {
    private static final RecordManager instance = new RecordManager();

    private final List<Player> playerList;

    private final List<MatchRecord> matchRecordList;

    private RecordManager() {
        playerList = FileIO.readPlayerList();
        matchRecordList = FileIO.readMatchRecordList();
    }

    public static RecordManager getInstance() {
        return instance;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public List<MatchRecord> getMatchRecordList() {
        return matchRecordList;
    }

    boolean addMatchRecordByInfo(MatchInfo matchInfo) {
        Match match = matchInfo.getMatch();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String formattedDate = now.format(formatter);

        String p1 = matchInfo.getP1();
        String p2 = matchInfo.getP2();
        String p1Score = String.valueOf(match.getP1Score());
        String p2Score = String.valueOf(match.getP2Score());
        String endTime = formattedDate;
        String result = matchInfo.getResult();

        MatchRecord matchRecord = new MatchRecord(p1, p2, p1Score, p2Score, endTime, result);
        matchRecordList.add(matchRecord);
        return FileIO.updateMatchRecordByList(matchRecordList);
    }
}
