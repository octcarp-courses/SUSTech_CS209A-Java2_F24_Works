package io.github.octcarp.sustech.cs209a.linkgame.server.model;

import io.github.octcarp.sustech.cs209a.linkgame.common.model.GridPos;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Match;
import io.github.octcarp.sustech.cs209a.linkgame.server.net.ClientHandlerThread;

import java.io.Serializable;
import java.util.List;

public class MatchInfo implements Serializable {
    private Match match;

    private final String p1;
    private final String p2;

    private ClientHandlerThread p1Thread;
    private ClientHandlerThread p2Thread;

    private String result;

    public MatchInfo(Match match, String p1, String p2, ClientHandlerThread p1Thread, ClientHandlerThread p2Thread) {
        this.match = match;
        this.p1 = p1;
        this.p2 = p2;
        this.p1Thread = p1Thread;
        this.p2Thread = p2Thread;
    }

//    public void syncMatch() {
//        Response response = new Response(ResponseType.SYNC_MATCH);
//        response.setData( getMatch());
//        player1Thread.sendResponse(response);
//        player2Thread.sendResponse(response);
//    }

    public void judgeMoveAndUpdate(String playerId, GridPos start, GridPos end) {
        Match match = getMatch();
        List<GridPos> path = match.getGame().judge(start.row(), start.col(), end.row(), end.col());
        if (path == null) {
            path = List.of();
        } else {
            path.addFirst(start);
            path.addLast(end);
            if (getP1().equals(playerId)) {
                match.incP1Score(1);
            } else {
                match.incP2Score(1);
            }
        }
        match.switchTurn();
        match.setLastPath(path);
    }

    public Match getMatch() {
        return match;
    }

    public String getP1() {
        return p1;
    }

    public String getP2() {
        return p2;
    }

    public ClientHandlerThread getP1Thread() {
        return p1Thread;
    }

    public ClientHandlerThread getP2Thread() {
        return p2Thread;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setP2Thread(ClientHandlerThread p2Thread) {
        this.p2Thread = p2Thread;
    }

    public void setP1Thread(ClientHandlerThread p1Thread) {
        this.p1Thread = p1Thread;
    }
}
