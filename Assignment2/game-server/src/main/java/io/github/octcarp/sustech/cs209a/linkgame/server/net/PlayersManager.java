package io.github.octcarp.sustech.cs209a.linkgame.server.net;

import io.github.octcarp.sustech.cs209a.linkgame.common.model.Player;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.SimpStatus;
import io.github.octcarp.sustech.cs209a.linkgame.server.utils.FileIO;
import io.github.octcarp.sustech.cs209a.linkgame.server.utils.ServerConfig;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayersManager {
    private static final PlayersManager instance = new PlayersManager();

    private final List<Player> playerList;

    private final Map<String, ClientHandlerThread> playerThreads;

    private PlayersManager() {
        playerList = RecordManager.getInstance().getPlayerList();
        playerThreads = new ConcurrentHashMap<>();
    }

    public static PlayersManager getInstance() {
        return instance;
    }

    public SimpStatus playerCanLogin(Player loginPlayer) {
        for (Player player : playerList) {
            if (player.id().equals(loginPlayer.id())) {
                return player.password().equals(loginPlayer.password()) ?
                        SimpStatus.OK : SimpStatus.UNAUTHORIZED;
            }
        }
        return SimpStatus.NOT_FOUND;
    }

    public SimpStatus playerLogout(String id) {
        if (playerThreads.containsKey(id)) {
            playerThreads.remove(id);
            removePlayerThread(id);
            return SimpStatus.OK;
        }
        return SimpStatus.NOT_FOUND;
    }

    public synchronized SimpStatus registerPlayer(Player newPlayer) {
        String id = newPlayer.id();
        if (ServerConfig.getReservedIds().contains(id)) {
            return SimpStatus.FORBIDDEN;
        }
        for (Player player : playerList) {
            if (player.id().equals(id)) {
                return SimpStatus.CONFLICT;
            }
        }
        playerList.add(newPlayer);
        if (!FileIO.updatePlayerByList(playerList)) {
            return SimpStatus.FAILURE;
        }
        return SimpStatus.OK;
    }

    public ClientHandlerThread getClientThreadByPlayerId(String id) {
        return playerThreads.get(id);
    }

    public void addPlayerThread(String id, ClientHandlerThread thread) {
        playerThreads.put(id, thread);
    }

    public void removePlayerThread(String id) {
        playerThreads.remove(id);
    }

    public void playerDisconnect(String id) {
        playerThreads.remove(id);
    }
}
