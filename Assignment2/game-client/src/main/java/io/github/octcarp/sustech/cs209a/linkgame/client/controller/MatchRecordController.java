package io.github.octcarp.sustech.cs209a.linkgame.client.controller;


import io.github.octcarp.sustech.cs209a.linkgame.client.net.RecordData;
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.MatchRecord;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.List;

public class MatchRecordController {
    @FXML
    private Label lblTitle;
    @FXML
    private ScrollPane spPlayerList;
    @FXML
    private ScrollPane spPlayerRecord;

    @FXML
    private ListView<String> lvPlayerList;
    @FXML
    private ListView<MatchRecord> lvPlayerRecord;


    @FXML
    public void initialize() {
        lvPlayerList.setCellFactory(_ -> new ListCell<>() {
            private Label lblId = new Label();
            private Button btnShowRecord = new Button("Show");
            private HBox hbox = new HBox(lblId, btnShowRecord);

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
                    btnShowRecord.setDisable(false);
                    btnShowRecord.setOnAction(event -> {
                        showPlayerRecord(playerId);
                    });
                    setGraphic(hbox);
                }
            }
        });

        spPlayerList.setManaged(false);
        spPlayerRecord.setManaged(false);
    }

    public void updateWaitingPlayers(List<String> playerList) {
        Platform.runLater(() -> {
            showPlayerListPane();
            ObservableList<String> players = FXCollections.observableArrayList(playerList);
            lvPlayerList.setItems(players);
        });
    }

    private void showPlayerRecord(String playerId) {
        Platform.runLater(() -> {
            lblTitle.setText("Player " + playerId + "'s Record");

            List<MatchRecord> records = RecordData.getInstance().getRecordByPlayer(playerId);
            ObservableList<MatchRecord> recordList = FXCollections.observableArrayList(records);
            lvPlayerRecord.setItems(recordList);

            showPlayerRecordPane();
        });
    }


    @FXML
    private void handleToMenu(ActionEvent actionEvent) {
        SceneSwitcher.getInstance().switchScene("main-menu");
    }

    @FXML
    private void handleToPlayerList(ActionEvent actionEvent) {
        lblTitle.setText("Recorded Players");
        showPlayerListPane();
    }

    @FXML
    private void handleRefreshRecord(ActionEvent actionEvent) {
        RecordData.getInstance().getRecordRequest();
    }

    private void showPlayerRecordPane() {
        spPlayerRecord.setManaged(true);
        spPlayerRecord.setVisible(true);
        spPlayerList.setManaged(false);
        spPlayerList.setVisible(false);
    }

    private void showPlayerListPane() {
        spPlayerRecord.setManaged(false);
        spPlayerRecord.setVisible(false);
        spPlayerList.setManaged(true);
        spPlayerList.setVisible(true);
    }
}

