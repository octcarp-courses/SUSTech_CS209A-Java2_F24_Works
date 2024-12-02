package io.github.octcarp.sustech.cs209a.linkgame.client.net;

import io.github.octcarp.sustech.cs209a.linkgame.client.controller.LobbyController;
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.AlertPopper;
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Request;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.RequestType;
import javafx.application.Platform;

import java.util.List;

public class LobbyData {
    private static final LobbyData instance = new LobbyData();

    private List<String> waitingPlayers;
    private String lastOpponent;

    private boolean isWaiting = false;

    private LobbyData() {
    }

    public static LobbyData getInstance() {
        return instance;
    }

    public void enterLobby() {
        Request request = new Request(RequestType.ENTER_LOBBY);
        ClientService.getInstance().sendRequest(request);
    }

    public void exitLobby() {
        Request request = new Request(RequestType.EXIT_LOBBY);
        ClientService.getInstance().sendRequest(request);
    }

    public void startWaiting() {
        Request request = new Request(RequestType.START_WAITING);
        ClientService.getInstance().sendRequest(request);
    }

    public void stopWaiting() {
        Request request = new Request(RequestType.STOP_WAITING);
        ClientService.getInstance().sendRequest(request);
    }

    public void reconnectMatch() {
        Request request = new Request(RequestType.RECONNECT_MATCH);
        ClientService.getInstance().sendRequest(request);
    }

    public void reNoReconnect() {
        Platform.runLater(() -> {
            AlertPopper.popError("Reconnect", "Reconnect failed",
                    "No match to reconnect");
        });
    }

    public void reAllWaitingPlayers(List<String> players) {
        Platform.runLater(() -> {
            LobbyController controller = (LobbyController)
                    SceneSwitcher.getInstance().getController("lobby");
            controller.updateWaitingPlayers(players);
        });

    }

    public void joinPlayer(String oppId) {
        Request request = new Request(RequestType.JOIN_PLAYER);
        request.setData(oppId);
        ClientService.getInstance().sendRequest(request);
    }
}
