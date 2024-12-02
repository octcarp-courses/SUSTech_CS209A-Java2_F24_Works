package io.github.octcarp.sustech.cs209a.linkgame.client.net;

import io.github.octcarp.sustech.cs209a.linkgame.client.controller.LoginController;
import io.github.octcarp.sustech.cs209a.linkgame.client.controller.MainMenuController;
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Player;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Request;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.RequestType;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.SimpStatus;

public class LoginData {
    private static final LoginData instance = new LoginData();

    private Player currentPlayer;

    private LoginData() {
    }

    public static LoginData getInstance() {
        return instance;
    }

    public void playerRegister(Player player) {
        Request request = new Request(RequestType.REGISTER);
        request.setData(player);
        ClientService.getInstance().sendRequest(request);
    }

    public void rePlayerRegister(SimpStatus status) {
        LoginController controller = (LoginController)
                SceneSwitcher.getInstance().getController("login");
        controller.handleRegisterResult(status);
    }

    public void playerLogin(Player player) {
        Request request = new Request(RequestType.LOGIN, player);
        setCurrentPlayer(player);
        ClientService.getInstance().sendRequest(request);
    }

    public void rePlayerLogin(SimpStatus status) {
        if (status == SimpStatus.OK) {
            ClientService.getInstance().setMyId(currentPlayer.id());
        } else {
            setCurrentPlayer(null);
        }
        LoginController controller = (LoginController)
                SceneSwitcher.getInstance().getController("login");
        controller.handleLoginResult(status);
    }

    public void logout() {
        if (currentPlayer == null) {
            return;
        }

        Request request = new Request(RequestType.LOGOUT);
        request.setData(currentPlayer);
        ClientService.getInstance().sendRequest(request);
    }

    public void reLogout(SimpStatus status) {
        setCurrentPlayer(null);
        if (status == SimpStatus.OK) {
            setCurrentPlayer(null);
        }
        MainMenuController controller = (MainMenuController)
                SceneSwitcher.getInstance().getController("main-menu");
        controller.handleLogoutResult(status);
    }

    private void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
