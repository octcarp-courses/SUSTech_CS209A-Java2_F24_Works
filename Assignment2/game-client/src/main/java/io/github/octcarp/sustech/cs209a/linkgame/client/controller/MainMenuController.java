package io.github.octcarp.sustech.cs209a.linkgame.client.controller;

import io.github.octcarp.sustech.cs209a.linkgame.client.net.LobbyData;
import io.github.octcarp.sustech.cs209a.linkgame.client.net.LoginData;
import io.github.octcarp.sustech.cs209a.linkgame.client.net.MatchData;
import io.github.octcarp.sustech.cs209a.linkgame.client.net.RecordData;
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.AlertPopper;
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Request;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.RequestType;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.SimpStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;


public class MainMenuController {
    @FXML
    private Label lblPlayerId;

    @FXML
    private void initialize() {
        lblPlayerId.setText(LoginData.getInstance().getCurrentPlayer().id());
    }

    @FXML
    private void handleStartMatchAction(ActionEvent actionEvent) {
        SceneSwitcher.getInstance().switchScene("lobby");
        LobbyData.getInstance().enterLobby();
    }

    @FXML
    private void handleGameHistoryAction(ActionEvent actionEvent) {
        SceneSwitcher.getInstance().switchScene("match-record");
    }

    @FXML
    private void handleLogoutAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("You will be redirected to the login page.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                LoginData.getInstance().logout();
            }
        });
    }

    public void handleLogoutResult(SimpStatus status) {
        if (status != SimpStatus.OK) {
            AlertPopper.popError("Logout",
                    "Logout failed", "Please try again.");
            return;
        }
        SceneSwitcher.getInstance().switchScene("login");
    }

    public void handleReconnectAction(ActionEvent actionEvent) {
        LobbyData.getInstance().reconnectMatch();
    }
}
