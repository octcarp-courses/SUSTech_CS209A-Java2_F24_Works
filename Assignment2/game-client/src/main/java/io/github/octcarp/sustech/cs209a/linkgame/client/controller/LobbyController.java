package io.github.octcarp.sustech.cs209a.linkgame.client.controller;

import io.github.octcarp.sustech.cs209a.linkgame.client.net.ClientService;
import io.github.octcarp.sustech.cs209a.linkgame.client.net.LobbyData;
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.util.List;

public class LobbyController {
    @FXML
    private ListView<String> lvWaitingPlayer;

    @FXML
    private Button btnWait;

    @FXML
    public void initialize() {
        lvWaitingPlayer.setCellFactory(_ -> new ListCell<>() {
            private Label lblId = new Label();
            private Button btnJoin = new Button("Join");
            private HBox hbox = new HBox(lblId, btnJoin);

            {
                hbox.setSpacing(100);
                hbox.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                String playerId = getItem();
                if (empty || playerId == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    lblId.setText(playerId);
                    if (playerId.equals(ClientService.getInstance().getMyId())) {
                        btnJoin.setDisable(true);
                    } else {
                        btnJoin.setDisable(false);
                        btnJoin.setOnAction(event -> {
                            System.out.println("Joining player: " + playerId);
                            LobbyData.getInstance().joinPlayer(playerId);
                        });
                    }
                    setGraphic(hbox);
                }
            }
        });
    }

//    public void requestWaitingPlayers() {
//
//    }

    public void updateWaitingPlayers(List<String> playerList) {
        Platform.runLater(() -> {
            String curId = ClientService.getInstance().getMyId();
            btnWait.setText(playerList.contains(curId) ? "Stop Waiting" : "Start Waiting");

            lvWaitingPlayer.getItems().clear();

            ObservableList<String> players = FXCollections.observableArrayList(playerList);
            lvWaitingPlayer.setItems(players);
        });
    }

    @FXML
    private void handleWaitAction(ActionEvent actionEvent) {
        boolean isWaiting = btnWait.getText().equals("Stop Waiting");
        if (isWaiting) {
            LobbyData.getInstance().stopWaiting();
        } else {
            LobbyData.getInstance().startWaiting();
        }
    }

    @FXML
    private void handleExitLobby(ActionEvent actionEvent) {
        LobbyData.getInstance().exitLobby();
        SceneSwitcher.getInstance().switchScene("main-menu");
    }

}
