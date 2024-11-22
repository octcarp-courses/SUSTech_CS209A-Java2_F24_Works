package io.github.octcarp.sustech.cs209a.practice.p9t2.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class ChatClientController {
    @FXML
    private TextField inputField;

    @FXML
    private TextArea displayArea;

    private String userName;


    public void setUserName(String userName) {
        this.userName = userName;
        displayArea.appendText("Welcome, " + userName + "!\n");

    }

    @FXML
    public void initialize() {
        // this method is automatically invoked
        // once everything is loaded

        // display message when user presses Enter key
        inputField.setOnAction(e -> {
            String userInput = inputField.getText();
            if (!userInput.isEmpty()) {
                ChatClientApp.getServerHandler().sendMessage(userInput);
                inputField.clear();
            }
        });
    }

    void addNewMessage(String message) {
        Platform.runLater(() -> {
                    displayArea.appendText(message + '\n');
                }
        );
    }

}