package io.github.octcarp.sustech.cs209a.linkgame.client.net;

import io.github.octcarp.sustech.cs209a.linkgame.client.controller.MatchBoardController;
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Game;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.GridPos;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Match;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Player;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Request;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.RequestType;
import javafx.application.Platform;

import java.util.List;

public class MatchData {

    private static MatchData instance = new MatchData();

    Match match;

    private MatchData() {
    }

    public static MatchData getInstance() {
        return instance;
    }

    public void initBoard(GridPos size) {
        Request request = new Request(RequestType.SELECT_BOARD);
        request.setData(size);
        ClientService.getInstance().sendRequest(request);
    }

    public void shuffleBoard() {
        Request request = new Request(RequestType.SHUFFLE_BOARD);
        ClientService.getInstance().sendRequest(request);
    }

    public void reStartMatch(Match match) {
        SceneSwitcher.getInstance().switchScene("match-board");
        this.match = match;
        Platform.runLater(() -> {
            MatchBoardController controller = (MatchBoardController)
                    SceneSwitcher.getInstance().getController("match-board");
            controller.initMatch(match);
        });

    }

    public void reConnectToMatch(Match match) {
        SceneSwitcher.getInstance().switchScene("match-board");
        Platform.runLater(() -> {
                    reSyncMatch(match);
                }
        );

    }

    public void reSyncMatch(Match match) {
        this.match = match;
        MatchBoardController controller = (MatchBoardController)
                SceneSwitcher.getInstance().getController("match-board");
        controller.updateMatchByData(match);
    }

    public void reSyncBoard(Game game) {
        MatchBoardController controller = (MatchBoardController)
                SceneSwitcher.getInstance().getController("match-board");
        controller.updateBoard(game);
    }

    public void reMatchFinished(Match match) {
        this.match = match;
        MatchBoardController controller = (MatchBoardController)
                SceneSwitcher.getInstance().getController("match-board");
        controller.matchFinished(match);
    }

    public boolean exitMatch() {
        Player currentPlayer = LoginData.getInstance().getCurrentPlayer();
        if (currentPlayer == null) {
            return false;
        }
//        Request request = new Request(RequestType.);
        return true;
    }

    public void afterGameFinished() {
    }

    public void sendMove(List<GridPos> move) {
        if (move == null) {
            return;
        }
        Request request = new Request(RequestType.TURN_MOVE);
        request.setData(move);
        ClientService.getInstance().sendRequest(request);
    }

}
