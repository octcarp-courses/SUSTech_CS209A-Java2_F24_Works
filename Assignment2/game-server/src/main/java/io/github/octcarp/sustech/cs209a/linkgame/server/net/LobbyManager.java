package io.github.octcarp.sustech.cs209a.linkgame.server.net;

import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Response;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.ResponseType;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class LobbyManager {
    private static final LobbyManager instance = new LobbyManager();

    private final Map<String, ClientHandlerThread> lobbyPlayerThreads;
    private final Set<String> onlinePlayers;
    private final Set<String> waitingPlayers;
    private Map<String, String> exceptPlayers;

    private LobbyManager() {
        lobbyPlayerThreads = new ConcurrentHashMap<>();
        waitingPlayers = new ConcurrentSkipListSet<>();
        onlinePlayers = new ConcurrentSkipListSet<>();
        exceptPlayers = new ConcurrentHashMap<>();
    }

    public static LobbyManager getInstance() {
        return instance;
    }

    public ClientHandlerThread getClientThreadByPlayerId(String playerId) {
        return lobbyPlayerThreads.get(playerId);
    }

    public void enterLobby(String playerId, ClientHandlerThread clientHandlerThread) {
        lobbyPlayerThreads.put(playerId, clientHandlerThread);
        ClientHandlerThread client = clientHandlerThread;

        if (exceptPlayers.containsKey(playerId)) {
            String oppId = exceptPlayers.get(playerId);
            Response response = new Response(ResponseType.WAITING_OPPONENT);
            response.setData(oppId);
            client.sendResponse(response);
        }

        Response response = new Response(ResponseType.ALL_WAITING_PLAYERS);
        response.setData(waitingPlayers.stream().toList());
        client.sendResponse(response);
    }

    public void startWaiting(String playerId) {
        waitingPlayers.add(playerId);
        notifyAllLobbyPlayers();
    }

    public void stopWaiting(String playerId) {
        waitingPlayers.remove(playerId);
        notifyAllLobbyPlayers();
    }

    public void exitLobby(String playerId) {
        lobbyPlayerThreads.remove(playerId);
        if (waitingPlayers.contains(playerId)) {
            waitingPlayers.remove(playerId);
            notifyAllLobbyPlayers();
        }
    }

    public void notifyAllLobbyPlayers() {
        Response response = new Response(ResponseType.ALL_WAITING_PLAYERS, waitingPlayers.stream().toList());
        for (ClientHandlerThread clientHandlerThread : lobbyPlayerThreads.values()) {
            clientHandlerThread.sendResponse(response);
        }
    }

    public void joinPlayer(String playerId, String oppId) {
        ClientHandlerThread client = lobbyPlayerThreads.get(playerId);
        ClientHandlerThread oppClient = lobbyPlayerThreads.get(oppId);
        if (client == null || oppClient == null) {
            return;
        }
        stopWaiting(playerId);
        stopWaiting(oppId);
        MatchManager.getInstance().createMatch(playerId, oppId);
    }


    public void playerDisconnected(String playerId) {
        lobbyPlayerThreads.remove(playerId);
        if (waitingPlayers.contains(playerId)) {
            waitingPlayers.remove(playerId);
            notifyAllLobbyPlayers();
        }
    }

}
